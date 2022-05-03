import be.larp.mylarpmanager.WardenApplication;
import be.larp.mylarpmanager.controllers.UserController;
import be.larp.mylarpmanager.requests.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = WardenApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserController userController;

    @Test
    public void login_default_account_test() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("changeme");
        loginRequest.setUsername("admin");
        MvcResult mvcResult = mvc.perform(post("/api/v1/auth/login").content(asJsonString(loginRequest)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(response.contains("token"));
        response = response.replace("{\"token\":\"", "");
        String token = response.replace("\"}", "");
        mvcResult =  mvc.perform(get("/api/v1/auth/whoami")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn();
        response = mvcResult.getResponse().getContentAsString();
        assertTrue(response.contains("admin"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
