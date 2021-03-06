import be.larp.mylarpmanager.WardenApplication;
import be.larp.mylarpmanager.controllers.UserController;
import be.larp.mylarpmanager.models.Role;
import be.larp.mylarpmanager.models.TranslatedItem;
import be.larp.mylarpmanager.models.uuid.JoinNationDemand;
import be.larp.mylarpmanager.requests.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = WardenApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class IntegrationTest {

    public static final String SEKWET = "sekwet";
    public static final String LOGIN = "thelegend27";
    private static String token;
    private static String userUuid;
    private static String nationUuid;
    private static String characterUuid;
    private static String skillTreeUuid;
    private static String skillUuid;
    private final Gson gson = new Gson();
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserController userController;

    @Test
    public void step_01_login_default_account_test() throws Exception {
        login("admin", "changeme");
        String response;
        MvcResult mvcResult;
        mvcResult = getMvcResult("/api/v1/auth/whoAmI");
        response = mvcResult.getResponse().getContentAsString();
        assertTrue(response.contains("admin"));
    }

    @Test
    public void step_02_renew_token() throws Exception {
        TimeUnit.SECONDS.sleep(1);
        String oldToken = token;
        MvcResult mvcResult = getMvcResult("/api/v1/auth/renew");
        extractToken(mvcResult);
        assertNotEquals(oldToken, token);
        mvcResult = getMvcResult("/api/v1/auth/whoAmI");
        assertTrue(mvcResult.getResponse().getContentAsString().contains("admin"));
        mvc.perform(get("/api/v1/auth/whoAmI")
                        .header("Authorization", "Bearer " + oldToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void step_03_admin_creates_user() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest()
                .setFirstName("Bobby")
                .setLastName("Von Bobbleson")
                .setPassword(SEKWET)
                .setPasswordConfirmation(SEKWET)
                .setUsername(LOGIN)
                .setEmail("thelegend.27@yopmail.com");
        MvcResult mvcResult = getMvcResult("/api/v1/user/create", createUserRequest);
        String response = mvcResult.getResponse().getContentAsString();
        ChangeUserDetailsRequest changeUserDetailsRequest = gson.fromJson(response, ChangeUserDetailsRequest.class);
        assertEquals(changeUserDetailsRequest.getUsername(), LOGIN);
        assertNotNull(changeUserDetailsRequest.getUuid());
        assertFalse(response.contains(SEKWET));
        userUuid = changeUserDetailsRequest.getUuid();
    }

    @Test
    public void step_04_admin_creates_nation() throws Exception {
        String nationNameFr = "Les chaussettes";
        String fullDescription = "C'est l'histoire racont??e par des chaussettes. C'est tr??s marrant. Ce texte fait un petit peu plus que 255 caract??res. Quelle belle cueillette nous f??mes : quelques hym??nomyc??tes, des hypholomes, des helvelles, des polypores versicoles, des g??asters fimbri??s, de superbes coulemelles et un hydne imbriqu?? ma foi fort app??tissant.";
        CreateNationRequest createNationRequest = new CreateNationRequest()
                .setContributionMandatory(false)
                .setContributionInCents(9000)
                .setFamilyFriendly(false)
                .setName(new TranslatedItem().setFr(nationNameFr))
                .setFullDescription(new TranslatedItem().setFr(fullDescription))
                .setIntroText(new TranslatedItem().setFr("C'est l'histoire racont??e par des chaussettes. C'est tr??s marrant."))
                .setInternationalFriendly(true);
        MvcResult mvcResult = getMvcResult("/api/v1/nation/create", createNationRequest);
        String response = mvcResult.getResponse().getContentAsString();
        ChangeNationDetailsRequest changeNationDetailsRequest = gson.fromJson(response, ChangeNationDetailsRequest.class);
        assertEquals(nationNameFr, changeNationDetailsRequest.getName().getFr());
        nationUuid = changeNationDetailsRequest.getUuid();
    }

    @Test
    public void step_05_admin_forces_new_user_join_new_nation() throws Exception {
        ForceJoinNationRequest forceJoinNationRequest = new ForceJoinNationRequest()
                .setNationUuid(nationUuid)
                .setPlayerUuid(userUuid);
        getMvcResult("/api/v1/nation/forceJoinNation", forceJoinNationRequest);
    }

    @Test
    public void step_06_admin_design_new_user_nation_admin() throws Exception {
        SetRoleRequest setRoleRequest = new SetRoleRequest()
                .setRole(Role.NATION_ADMIN.name())
                .setUserUuid(userUuid);
        getMvcResult("/api/v1/user/setRole", setRoleRequest);
    }

    @Test
    public void step_07_admin_logout() throws Exception {
        getMvcResult("/api/v1/auth/logout");
        mvc.perform(get("/api/v1/auth/whoAmI")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void step_08_nation_admin_login() throws Exception {
        login(LOGIN, SEKWET);
    }

    @Test
    public void step_09_nation_admin_updates_nation() throws Exception {
        MvcResult mvcResult = getMvcResult("/api/v1/nation/getMyNation");
        ChangeNationDetailsRequest changeNationDetailsRequest = gson.fromJson(mvcResult.getResponse().getContentAsString(), ChangeNationDetailsRequest.class);
        assertFalse(changeNationDetailsRequest.isFamilyFriendly());
        changeNationDetailsRequest.setFamilyFriendly(true);
        getMvcResult("/api/v1/nation/changeDetails", changeNationDetailsRequest);
        mvcResult = getMvcResult("/api/v1/nation/getMyNation");
        changeNationDetailsRequest = gson.fromJson(mvcResult.getResponse().getContentAsString(), ChangeNationDetailsRequest.class);
        assertTrue(changeNationDetailsRequest.isFamilyFriendly());

    }

    @Test
    public void step_10_nation_admin_leave_nation() throws Exception {
        MvcResult mvcResult = getMvcResult("/api/v1/auth/whoAmI");
        assertTrue(mvcResult.getResponse().getContentAsString().contains(Role.NATION_ADMIN.name()));
        getRequest("/api/v1/nation/leaveNation", userUuid);
        mvc.perform(get("/api/v1/nation/getMyNation")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
        mvcResult = getMvcResult("/api/v1/auth/whoAmI");
        assertFalse(mvcResult.getResponse().getContentAsString().contains(Role.NATION_ADMIN.name()));
        assertTrue(mvcResult.getResponse().getContentAsString().contains(Role.PLAYER.name()));
    }

    @Test
    public void step_11_admin_promotes_other_admin() throws Exception {
        login("admin", "changeme");
        SetRoleRequest setRoleRequest = new SetRoleRequest()
                .setRole(String.valueOf(Role.ADMIN))
                .setUserUuid(userUuid);
        getMvcResult("/api/v1/user/setRole", setRoleRequest);
        login(LOGIN, SEKWET);
        MvcResult mvcResult = getMvcResult("/api/v1/auth/whoAmI");
        assertTrue(mvcResult.getResponse().getContentAsString().contains(Role.ADMIN.name()));
        assertFalse(mvcResult.getResponse().getContentAsString().contains(Role.NATION_ADMIN.name()));
    }

    @Test
    public void step_12_admin_change_his_role_to_error() throws Exception {
        SetRoleRequest setRoleRequest = new SetRoleRequest()
                .setRole("POTATO_ADMIN")
                .setUserUuid(userUuid);
        postError("/api/v1/user/setRole", setRoleRequest);
        MvcResult mvcResult = getMvcResult("/api/v1/auth/whoAmI");
        assertTrue(mvcResult.getResponse().getContentAsString().contains(Role.ADMIN.name()));
        assertFalse(mvcResult.getResponse().getContentAsString().contains(Role.NATION_ADMIN.name()));
    }

    @Test
    public void step_13_admin_creates_character() throws Exception {
        CreateCharacterRequest createCharacterRequest = new CreateCharacterRequest()
                .setAge(300)
                .setName("Jean-Pierre")
                .setBackground("Jean-Pierre Delavergemolle est un grand artiste.")
                .setRace("Humain")
                .setPictureURL("https://cornhub.website/");
        ChangeCharacterDetailsRequest changeCharacterDetailsRequest = gson.fromJson(getMvcResult("/api/v1/character/create", createCharacterRequest).getResponse().getContentAsString(), ChangeCharacterDetailsRequest.class);
        assertEquals(changeCharacterDetailsRequest.getName(), "Jean-Pierre");
        assertNotNull(changeCharacterDetailsRequest.getUuid());
        characterUuid = changeCharacterDetailsRequest.getUuid();
    }


    @Test
    public void step_14_admin_creates_skillTree_and_skill() throws Exception {
        CreateSkillTreeRequest createSkillTreeRequest = new CreateSkillTreeRequest()
                .setBlessing(new TranslatedItem().setFr("B??n??diction"))
                .setName(new TranslatedItem().setFr("Nom bidon"))
                .setDescription(new TranslatedItem().setFr("Une chouette description"));
        ChangeSkillTreeDetailsRequest changeSkillTreeDetailsRequest = gson.fromJson(getMvcResult("/api/v1/skillTree/create", createSkillTreeRequest).getResponse().getContentAsString(), ChangeSkillTreeDetailsRequest.class);
        assertNotNull(changeSkillTreeDetailsRequest.getUuid());
        skillTreeUuid = changeSkillTreeDetailsRequest.getUuid();
        CreateSkillRequest createSkillRequest = new CreateSkillRequest()
                .setDescription(new TranslatedItem().setFr("Desc"))
                .setName(new TranslatedItem().setFr("Nice skill name"))
                .setSkillTreeUuid(skillTreeUuid)
                .setCost(5)
                .setAllowMultiple(true)
                .setHidden(false)
                .setLevel(1);
        ChangeSkillDetailsRequest changeSkillDetailsRequest = gson.fromJson(getMvcResult("/api/v1/skill/create", createSkillRequest).getResponse().getContentAsString(), ChangeSkillDetailsRequest.class);
        assertNotNull(changeSkillDetailsRequest.getUuid());
        skillUuid = changeSkillDetailsRequest.getUuid();
    }


    @Test
    public void step_15_admin_adds_skill_to_character() throws Exception {
        AddCharacterSkillRequest addCharacterSkillRequest = new AddCharacterSkillRequest();
        addCharacterSkillRequest.setSkillUuid(skillUuid);
        addCharacterSkillRequest.setCharacterUuid(characterUuid);
        assertEquals(10, Integer.parseInt(getRequest("/api/v1/character/getPointsAvailable", characterUuid).getResponse().getContentAsString()));
        getMvcResult("/api/v1/character/addSkill", addCharacterSkillRequest);

        assertEquals(5, Integer.parseInt(getRequest("/api/v1/character/getPointsAvailable", characterUuid).getResponse().getContentAsString()));
        getMvcResult("/api/v1/character/addSkill", addCharacterSkillRequest);

        assertEquals(0, Integer.parseInt(getRequest("/api/v1/character/getPointsAvailable", characterUuid).getResponse().getContentAsString()));
        postError("/api/v1/character/addSkill", addCharacterSkillRequest);
        assertEquals(0, Integer.parseInt(getRequest("/api/v1/character/getPointsAvailable", characterUuid).getResponse().getContentAsString()));
        getMvcResult("/api/v1/character/removeSkill", addCharacterSkillRequest);
        assertEquals(5, Integer.parseInt(getRequest("/api/v1/character/getPointsAvailable", characterUuid).getResponse().getContentAsString()));
        getMvcResult("/api/v1/character/addSkill", addCharacterSkillRequest);
        assertEquals(0, Integer.parseInt(getRequest("/api/v1/character/getPointsAvailable", characterUuid).getResponse().getContentAsString()));
    }

    @Test
    public void step_16_admin_deletes_skill_already_assigned_to_character() throws Exception {
        deleteRequest("/api/v1/skill", skillUuid);
        assertEquals(10, Integer.parseInt(getRequest("/api/v1/character/getPointsAvailable", characterUuid).getResponse().getContentAsString()));
        CreateSkillRequest createSkillRequest = new CreateSkillRequest()
                .setDescription(new TranslatedItem().setFr("Desc"))
                .setName(new TranslatedItem().setFr("Nice skill name"))
                .setSkillTreeUuid(skillTreeUuid)
                .setCost(5)
                .setAllowMultiple(false)
                .setHidden(false)
                .setLevel(1);
        ChangeSkillDetailsRequest changeSkillDetailsRequest = gson.fromJson(getMvcResult("/api/v1/skill/create", createSkillRequest).getResponse().getContentAsString(), ChangeSkillDetailsRequest.class);
        assertNotNull(changeSkillDetailsRequest.getUuid());
        skillUuid = changeSkillDetailsRequest.getUuid();
        AddCharacterSkillRequest addCharacterSkillRequest = new AddCharacterSkillRequest();
        addCharacterSkillRequest.setSkillUuid(skillUuid);
        addCharacterSkillRequest.setCharacterUuid(characterUuid);
        getMvcResult("/api/v1/character/addSkill", addCharacterSkillRequest);
        assertEquals(5, Integer.parseInt(getRequest("/api/v1/character/getPointsAvailable", characterUuid).getResponse().getContentAsString()));
    }

    @Test
    public void step_17_admin_deletes_character_having_a_skill() throws Exception {
        deleteRequest("/api/v1/character", characterUuid);
        getRequestError4xx("/api/v1/character/getPointsAvailable", characterUuid);
        CreateCharacterRequest createCharacterRequest = new CreateCharacterRequest()
                .setAge(300)
                .setName("Jean-Pierre")
                .setBackground("Jean-Pierre Delavergemolle est un grand artiste.")
                .setRace("Humain")
                .setPictureURL("https://cornhub.website/");
        ChangeCharacterDetailsRequest changeCharacterDetailsRequest = gson.fromJson(getMvcResult("/api/v1/character/create", createCharacterRequest).getResponse().getContentAsString(), ChangeCharacterDetailsRequest.class);
        assertEquals(changeCharacterDetailsRequest.getName(), "Jean-Pierre");
        assertNotNull(changeCharacterDetailsRequest.getUuid());
        characterUuid = changeCharacterDetailsRequest.getUuid();
        AddCharacterSkillRequest addCharacterSkillRequest = new AddCharacterSkillRequest();
        addCharacterSkillRequest.setSkillUuid(skillUuid);
        addCharacterSkillRequest.setCharacterUuid(characterUuid);
        getMvcResult("/api/v1/character/addSkill", addCharacterSkillRequest);
        assertEquals(5, Integer.parseInt(getRequest("/api/v1/character/getPointsAvailable", characterUuid).getResponse().getContentAsString()));
    }

    @Test
    public void step_18_error_on_second_time_skill_not_allowed_multiple_times() throws Exception{
        AddCharacterSkillRequest addCharacterSkillRequest = new AddCharacterSkillRequest();
        addCharacterSkillRequest.setSkillUuid(skillUuid);
        addCharacterSkillRequest.setCharacterUuid(characterUuid);
        postError("/api/v1/character/addSkill", addCharacterSkillRequest);
        assertEquals(5, Integer.parseInt(getRequest("/api/v1/character/getPointsAvailable", characterUuid).getResponse().getContentAsString()));
    }

    @Test
    public void step_19_delete_nation_having_player() throws Exception{
        deleteRequest("/api/v1/nation", nationUuid);
        CreateNationRequest createNationRequest = new CreateNationRequest()
                .setContributionMandatory(false)
                .setContributionInCents(9000)
                .setFamilyFriendly(false)
                .setName(new TranslatedItem().setFr("nationNameFr"))
                .setFullDescription(new TranslatedItem().setFr("fullDescription"))
                .setIntroText(new TranslatedItem().setFr("C'est l'histoire racont??e par des chaussettes. C'est tr??s marrant."))
                .setInternationalFriendly(true);
        MvcResult mvcResult = getMvcResult("/api/v1/nation/create", createNationRequest);
        String response = mvcResult.getResponse().getContentAsString();
        ChangeNationDetailsRequest changeNationDetailsRequest = gson.fromJson(response, ChangeNationDetailsRequest.class);
        assertEquals("nationNameFr", changeNationDetailsRequest.getName().getFr());
        nationUuid = changeNationDetailsRequest.getUuid();
        mvcResult = getRequestError4xx("/api/v1/nation/getMyNation");
        assertTrue(mvcResult.getResponse().getContentAsString().contains("The user does not belong to a nation."));
    }

    @Test
    public void step_20_demand_nation_join() throws Exception{
        JoinNationRequest joinNationRequest = (JoinNationRequest) new JoinNationRequest().setPlayerUuid(userUuid).setNationUuid(nationUuid);
        getMvcResult("/api/v1/nation/joinNation", joinNationRequest);
        getMvcResult("/api/v1/nation/joinNation", joinNationRequest);
    }

    @Test
    public void step_21_spam_renew() throws Exception{
        for(int i=0;i<100;i++) {
            MvcResult mvcResult = getMvcResult("/api/v1/auth/renew");
            extractToken(mvcResult);
        }
    }

    //################################################################################################################################################################


    private MvcResult deleteRequest(String url, String uuid) throws Exception {
        return mvc.perform(delete(url + "/" + uuid).accept(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)).andExpect(status().isOk()).andReturn();
    }

    private MvcResult getRequest(String url, String uuid) throws Exception {
        return mvc.perform(get(url + "/" + uuid).accept(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)).andExpect(status().isOk()).andReturn();
    }

    private MvcResult getRequestError4xx(String url, String uuid) throws Exception {
        return mvc.perform(get(url + "/" + uuid).accept(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)).andExpect(status().is4xxClientError()).andReturn();
    }

    private MvcResult getRequestError4xx(String url) throws Exception {
        return mvc.perform(get(url ).accept(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)).andExpect(status().is4xxClientError()).andReturn();
    }
    private MvcResult getMvcResult(String url) throws Exception {
        MvcResult mvcResult;
        mvcResult = mvc.perform(get(url)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn();
        return mvcResult;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void postError(String url, Object o) throws Exception {
        mvc.perform(post(url).content(asJsonString(o)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }

    private void login(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword(password);
        loginRequest.setUsername(username);
        MvcResult mvcResult = getMvcResult("/api/v1/auth/login", loginRequest);
        extractToken(mvcResult);
    }

    private MvcResult getMvcResult(String url, Object o) throws Exception {
        MvcResult mvcResult = mvc.perform(post(url).content(asJsonString(o)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn();
        return mvcResult;
    }


    private void extractToken(MvcResult mvcResult) throws UnsupportedEncodingException {
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(response.contains("token"));
        response = response.replace("{\"token\":\"", "");
        token = response.replace("\"}", "");
    }
}
