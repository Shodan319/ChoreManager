package root.chores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.validation.Valid;

@Controller
@RequestMapping
public class ChoreController
{
    @Autowired
    ChoreRepository choreRepository;

    @GetMapping("/chores")
    public String getDueChores(Model model)
    {
        var tomorrow = LocalDate.now().plusDays(1);
        var chores = choreRepository.findByDueBefore(tomorrow);
        model.addAttribute("choresDue", chores);
        model.addAttribute("chore", new Chore());
        model.addAttribute("chores", new ArrayList<Chore>());
        model.addAttribute("allChores", choreRepository.findAll());
        return "chores";
    }

    @PostMapping("/chores")
    public String addNewChore(@ModelAttribute @Valid Chore chore, Errors errors) throws IOException
    {
        if (errors.hasErrors())
            return "chores";

        chore.setDue(LocalDate.now().plusDays(chore.getDaysBetween()));
        choreRepository.save(chore);
        return "redirect:/chores";
    }

    @PostMapping("/chore")
    public String updateChore(@RequestParam Long[] choreIds)
    {
        for (Long id : choreIds)
        {
            var chore = this.choreRepository.findById(id).orElseThrow();
            chore.setDue(LocalDate.now().plusDays(chore.getDaysBetween()));
            this.choreRepository.save(chore);
        }
        return "redirect:/chores";
    }
}
