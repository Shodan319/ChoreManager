package root.chores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        addChoresToModel(model);
        model.addAttribute("chore", new Chore());
        return "chores";
    }

    @PostMapping("/chores")
    public String addNewChore(@ModelAttribute @Valid Chore chore,
                              Errors errors,
                              Model model)
    {
        if (errors.hasErrors())
        {
            model.addAttribute("error", errors);
            addChoresToModel(model);
            return "chores";
        }
        else
        {
            chore.setDue(LocalDate.now().plusDays(chore.getDaysBetween()));
            choreRepository.save(chore);
        }

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

    @PostMapping("/deleteChore")
    public String deleteChore(@RequestParam Long[] choreIds)
    {
        for (Long id : choreIds)
            this.choreRepository.deleteById(id);
        return "redirect:/chores";
    }

    private void addChoresToModel(Model model)
    {
        var tomorrow = LocalDate.now().plusDays(1);
        var chores = choreRepository.findByDueBefore(tomorrow);
        model.addAttribute("choresDue", chores);
        model.addAttribute("allChores", choreRepository.findAll());
    }
}
