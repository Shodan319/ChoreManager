package root.chores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/chores")
public class ChoreController
{
    @Autowired
    ChoreRepository choreRepository;

    @GetMapping
    public String getDueChores(Model model)
    {
        var tomorrow = LocalDate.now().plusDays(1);
        var chores = choreRepository.findByDueBefore(tomorrow);
        model.addAttribute("choresDue", chores);
        return "chores";
    }

    @PostMapping
    public String addNewChore(Chore chore) throws IOException
    {
        chore.setDue(LocalDate.now().plusDays(chore.getDaysBetween()));
        choreRepository.save(chore);
        return "redirect:/chores";
    }
}
