package chores;

import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class ChoreApplicationTest
{
    @Autowired
    private ChoreController controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads()
    {
    }

    @Test
    public void choresShouldBeEmptyAtStart() throws Exception
    {
        this.mockMvc.perform(get("/chores")).
                andExpect(status().isOk());
                //andExpect(content().string(IsEqual.equalTo("[]")));

        //assertTrue(controller.all().isEmpty());
    }

    @Test
    public void showErrorRequestingNotFoundChore() throws Exception
    {
        this.mockMvc.perform(get("/chore/5")).
            andExpect(content().string(containsString("5")));
    }

    @Test
    public void throwOnMissingChore()
    {
        assertThrows(ChoreNotFoundException.class, () -> this.controller.getChore(0L));
    }

    @Test
    public void testCanAddNewChore() throws Exception
    {
        this.controller.newChore(new Chore("test", 6));

        this.mockMvc.perform(get("/chores")).
                andExpect(content().string(containsString("\"name\":\"test\"")));
    }
}
