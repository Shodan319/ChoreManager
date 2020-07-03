package root.chores;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChoreApplicationTest
{
    private ObjectWriter writer;

    @Autowired
    private ChoreController controller;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp()
    {
        var mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.registerModule(new JavaTimeModule());
        writer = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void choresShouldBeEmptyAtStart() throws Exception
    {
        this.mockMvc.perform(get("/rest/chores"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void showErrorRequestingNotFoundChore() throws Exception
    {
        this.mockMvc.perform(get("/rest/chore/5"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("5")));
    }

    @Test
    public void throwOnMissingChore()
    {
        assertThrows(ChoreNotFoundException.class, () -> this.controller.getChore(0L));
    }

    @Test
    public void testCanAddNewChore() throws Exception
    {
        var chore = new Chore("test", 6);
        addChoreForUser(chore, "user", "password");

        getChoresForUser("user", "password")
                .andExpect(content().string(containsString("\"name\":\"test\"")));
    }

    private void addChoreForUser(Chore chore, String username, String password) throws Exception
    {
        var requestJson = writer.writeValueAsString(chore);

        this.mockMvc.perform(post("/rest/chore").contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson)
                .with(httpBasic("user", "password")).with(csrf()))
                .andExpect(status().isOk());
    }

    private ResultActions getChoresForUser(String username, String password) throws Exception
    {
        return this.mockMvc.perform(get("/rest/chores")
                .with(httpBasic(username, password)));
    }

    @Test
    public void addingChoreIsSpecificForUser() throws Exception
    {
        var chore = new Chore("test", 6);

        addChoreForUser(chore, "user", "password");

        getChoresForUser("user", "password")
                .andExpect(content().string(containsString("test")));

        getChoresForUser("user2", "password")
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.not(containsString("test"))));

    }
}
