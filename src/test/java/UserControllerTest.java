import be.larp.mylarpmanager.WardenApplication;
import be.larp.mylarpmanager.controllers.UserController;
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
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("changeme");
        loginRequest.setUsername("admin");
        MvcResult mvcResult = mvc.perform(post("/api/v1/auth/login").content(asJsonString(loginRequest)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(response.contains("token"));
        response = response.replace("{\"token\":\"", "");
        token = response.replace("\"}", "");
        mvcResult = mvc.perform(get("/api/v1/auth/whoami")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn();
        response = mvcResult.getResponse().getContentAsString();
        assertTrue(response.contains("admin"));
    }

    @Test
    public void step_02_renew_token() throws Exception {
        TimeUnit.SECONDS.sleep(1);
        String oldToken = token;
        MvcResult mvcResult = mvc.perform(get("/api/v1/auth/renew")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(response.contains("token"));
        response = response.replace("{\"token\":\"", "");
        token = response.replace("\"}", "");
        assertNotEquals(oldToken, token);
        mvcResult = mvc.perform(get("/api/v1/auth/whoami")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn();
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
        createUserRequest.setPassword("sekwet");
        createUserRequest.setPasswordConfirmation("sekwet");
        createUserRequest.setUsername("thelegend27");
        createUserRequest.setEmail("thelegend.27@yopmail.com");
        MvcResult mvcResult = mvc.perform(post("/api/v1/user/create").content(asJsonString(createUserRequest)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(response.contains("Bobby"));
        assertFalse(response.contains("sekwet"));
        ChangeUserDetailsRequest changeUserDetailsRequest = gson.fromJson(response, ChangeUserDetailsRequest.class);
        userUuid = changeUserDetailsRequest.getUuid();
    }

    @Test
    public void step_04_admin_creates_nation() throws Exception {
        CreateNationRequest createNationRequest = new CreateNationRequest();
        createNationRequest.setContributionMandatory(false);
        createNationRequest.setContributionInCents(9000);
        createNationRequest.setFamilyFriendly(false);
        createNationRequest.setName(new TranslatedItem().setFr("Les chaussettes"));
        createNationRequest.setFullDescription(new TranslatedItem().setFr("C'est l'histoire racontée par des chaussettes. C'est très marrant. Ce texte fait un petit peu plus que 255 caractères. Quelle belle cueillette nous fîmes : quelques hyménomycètes, des hypholomes, des helvelles, des polypores versicoles, des géasters fimbriés, de superbes coulemelles et un hydne imbriqué ma foi fort appétissant."));
        createNationRequest.setIntroText(new TranslatedItem().setFr("C'est l'histoire racontée par des chaussettes. C'est très marrant."));
        createNationRequest.setInternationalFriendly(true);
        MvcResult mvcResult = mvc.perform(post("/api/v1/nation/create").content(asJsonString(createNationRequest)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(response.contains("chaussettes"));
        ChangeNationDetailsRequest changeNationDetailsRequest = gson.fromJson(response, ChangeNationDetailsRequest.class);
        nationUuid = changeNationDetailsRequest.getUuid();
    }

    @Test
    public void step_05_admin_forces_new_user_join_new_nation() throws Exception {
        ForceJoinNationRequest forceJoinNationRequest = new ForceJoinNationRequest();
        forceJoinNationRequest.setNationUuid(nationUuid);
        forceJoinNationRequest.setPlayerUuid(userUuid);
        mvc.perform(post("/api/v1/nation/forcejoinnation").content(asJsonString(forceJoinNationRequest)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
