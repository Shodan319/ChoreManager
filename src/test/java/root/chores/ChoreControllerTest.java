package root.chores;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        when(choreRepository.findByDueBefore(any()))
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

    @Test
    @WithMockUser(username="user")
    public void addNewChore() throws Exception
    {
        var result = mockMvc.perform(post("/chores").with(csrf())
                .content("name=newChore&daysBetween=3")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues("Location", "/chores"))
                .andReturn();
    }

}
