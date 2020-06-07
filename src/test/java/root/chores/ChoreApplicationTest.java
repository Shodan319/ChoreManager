package root.chores;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
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
        this.mockMvc.perform(get("/chores"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void showErrorRequestingNotFoundChore() throws Exception
    {
        this.mockMvc.perform(get("/chore/5"))
                .andExpect(content().string(containsString("5")));
    }

    @Test
    public void throwOnMissingChore()
    {
        assertThrows(ChoreNotFoundException.class, () -> this.controller.getChore(0L));
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void testCanAddNewChore() throws Exception
    {
        var chore = new Chore("test", 6);
        var requestJson = writer.writeValueAsString(chore);

        this.mockMvc.perform(post("/chore").contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson).with(csrf()))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/chores")).
                andExpect(content().string(containsString("\"name\":\"test\"")));
    }

    @Test
    public void addingChoreIsSpecificForUser() throws Exception
    {
        var chore = new Chore("test", 6);
        var requestJson = writer.writeValueAsString(chore);

        this.mockMvc.perform(post("/chore").contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson).with(httpBasic("user", "password")).with(csrf()))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/chores").with(httpBasic("user", "password")))
                .andExpect(content().string(containsString("test")));

        this.mockMvc.perform(get("/chores").with(httpBasic("user2", "password")))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.not(containsString("test"))));
    }
}
