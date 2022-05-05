import be.larp.mylarpmanager.WardenApplication;
import be.larp.mylarpmanager.controllers.UserController;
import be.larp.mylarpmanager.models.Role;
import be.larp.mylarpmanager.models.TranslatedItem;
import be.larp.mylarpmanager.models.uuid.Nation;
import be.larp.mylarpmanager.models.uuid.User;
import be.larp.mylarpmanager.requests.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.h2.command.ddl.CreateUser;
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


import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = WardenApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class UserControllerTest {

    public static final String SEKWET = "sekwet";
    public static final String LOGIN = "thelegend27";
    private static String token;
    private static String userUuid;
    private static String nationUuid;
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
        mvcResult = getMvcResult("/api/v1/auth/whoami");
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
        mvcResult = getMvcResult("/api/v1/auth/whoami");
        assertTrue(mvcResult.getResponse().getContentAsString().contains("admin"));
        mvc.perform(get("/api/v1/auth/whoami")
                        .header("Authorization", "Bearer " + oldToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void step_03_admin_creates_user() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setFirstName("Bobby");
        createUserRequest.setLastName("Von Bobbleson");
        createUserRequest.setPassword(SEKWET);
        createUserRequest.setPasswordConfirmation(SEKWET);
        createUserRequest.setUsername(LOGIN);
        createUserRequest.setEmail("thelegend.27@yopmail.com");
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
        String fullDescription = "C'est l'histoire racontée par des chaussettes. C'est très marrant. Ce texte fait un petit peu plus que 255 caractères. Quelle belle cueillette nous fîmes : quelques hyménomycètes, des hypholomes, des helvelles, des polypores versicoles, des géasters fimbriés, de superbes coulemelles et un hydne imbriqué ma foi fort appétissant.";
        CreateNationRequest createNationRequest = new CreateNationRequest();
        createNationRequest.setContributionMandatory(false);
        createNationRequest.setContributionInCents(9000);
        createNationRequest.setFamilyFriendly(false);
        createNationRequest.setName(new TranslatedItem().setFr(nationNameFr));
        createNationRequest.setFullDescription(new TranslatedItem().setFr(fullDescription));
        createNationRequest.setIntroText(new TranslatedItem().setFr("C'est l'histoire racontée par des chaussettes. C'est très marrant."));
        createNationRequest.setInternationalFriendly(true);
        MvcResult mvcResult = getMvcResult("/api/v1/nation/create", createNationRequest);
        String response = mvcResult.getResponse().getContentAsString();
        ChangeNationDetailsRequest changeNationDetailsRequest = gson.fromJson(response, ChangeNationDetailsRequest.class);
        assertEquals(nationNameFr, changeNationDetailsRequest.getName().getFr());
        nationUuid = changeNationDetailsRequest.getUuid();
    }

    @Test
    public void step_05_admin_forces_new_user_join_new_nation() throws Exception {
        ForceJoinNationRequest forceJoinNationRequest = new ForceJoinNationRequest();
        forceJoinNationRequest.setNationUuid(nationUuid);
        forceJoinNationRequest.setPlayerUuid(userUuid);
        getMvcResult("/api/v1/nation/forcejoinnation", forceJoinNationRequest);
    }

    @Test
    public void step_06_admin_design_new_user_nation_admin() throws Exception {
        SetRoleRequest setRoleRequest = new SetRoleRequest();
        setRoleRequest.setRole(Role.NATION_ADMIN.name());
        setRoleRequest.setUserUuid(userUuid);
        getMvcResult("/api/v1/user/setrole", setRoleRequest);
    }

    @Test
    public void step_07_admin_logout() throws Exception {
        getMvcResult("/api/v1/auth/logout");
        mvc.perform(get("/api/v1/auth/whoami")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void step_08_nation_admin_login() throws Exception {
        login(LOGIN, SEKWET);
    }

    @Test
    public void step_09_nation_admin_updates_nation() throws Exception {
        MvcResult mvcResult = getMvcResult("/api/v1/nation/getmynation");
        ChangeNationDetailsRequest changeNationDetailsRequest = gson.fromJson(mvcResult.getResponse().getContentAsString(), ChangeNationDetailsRequest.class);
        assertFalse(changeNationDetailsRequest.isFamilyFriendly());
        changeNationDetailsRequest.setFamilyFriendly(true);
        getMvcResult("/api/v1/nation/changedetails", changeNationDetailsRequest);
        mvcResult = getMvcResult("/api/v1/nation/getmynation");
        changeNationDetailsRequest = gson.fromJson(mvcResult.getResponse().getContentAsString(), ChangeNationDetailsRequest.class);
        assertTrue(changeNationDetailsRequest.isFamilyFriendly());

    }

    @Test
    public void step_10_nation_admin_leave_nation() throws Exception {
        MvcResult mvcResult = getMvcResult("/api/v1/auth/whoami");
        assertTrue(mvcResult.getResponse().getContentAsString().contains(Role.NATION_ADMIN.name()));
        LeaveNationRequest leaveNationRequest = new LeaveNationRequest();
        leaveNationRequest.setUuid(userUuid);
        getMvcResult("/api/v1/nation/leavenation", leaveNationRequest);
        mvc.perform(get("/api/v1/nation/getmynation")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
        mvcResult = getMvcResult("/api/v1/auth/whoami");
        assertFalse(mvcResult.getResponse().getContentAsString().contains(Role.NATION_ADMIN.name()));
        assertTrue(mvcResult.getResponse().getContentAsString().contains(Role.PLAYER.name()));
    }

    @Test
    public void step_11_admin_promotes_other_admin() throws Exception {
        login("admin", "changeme");
        SetRoleRequest setRoleRequest = new SetRoleRequest();
        setRoleRequest.setRole(String.valueOf(Role.ADMIN));
        setRoleRequest.setUserUuid(userUuid);
        getMvcResult("/api/v1/user/setrole", setRoleRequest);
        login(LOGIN, SEKWET);
        MvcResult mvcResult = getMvcResult("/api/v1/auth/whoami");
        assertTrue(mvcResult.getResponse().getContentAsString().contains(Role.ADMIN.name()));
        assertFalse(mvcResult.getResponse().getContentAsString().contains(Role.NATION_ADMIN.name()));
    }

    @Test
    public void step_12_admin_change_his_role_to_error() throws Exception {
        SetRoleRequest setRoleRequest = new SetRoleRequest();
        setRoleRequest.setRole("POTATO_ADMIN");
        setRoleRequest.setUserUuid(userUuid);
        postError("/api/v1/user/setrole", setRoleRequest);
        MvcResult mvcResult = getMvcResult("/api/v1/auth/whoami");
        assertTrue(mvcResult.getResponse().getContentAsString().contains(Role.ADMIN.name()));
        assertFalse(mvcResult.getResponse().getContentAsString().contains(Role.NATION_ADMIN.name()));
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
