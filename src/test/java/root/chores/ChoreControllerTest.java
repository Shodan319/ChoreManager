package root.chores;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ChoreController.class)
public class ChoreControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChoreRepository choreRepository;

    @BeforeEach
    public void setup()
    {
        var chores = new ArrayList<Chore>();
        chores.add(new Chore("due today", 7));
        chores.add(new Chore("due yesterday", 5, 15, LocalDate.now().minusDays(1)));
        chores.add(new Chore("due tomorrow", 5, 15, LocalDate.now().plusDays(1)));

        when(choreRepository.findAll())
                .thenReturn(chores);
    }

    @Test
    @WithMockUser(username="user")
    public void viewForAllDueChores() throws Exception
    {
        var result = mockMvc.perform(get("/chores"))
                .andExpect(status().isOk())
                .andExpect(view().name("chores"))
                .andReturn();
        var contents = result.getResponse().getContentAsString();
        assertTrue(contents.contains("due today"));
        assertTrue(contents.contains("due yesterday"));
    }

}
