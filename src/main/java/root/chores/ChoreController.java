package root.chores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        //var chores = choreRepository.findByDueBefore(tomorrow);
        var chores = choreRepository.findAll();
        model.addAttribute("choresDue", chores);
        model.addAttribute("chore", new Chore());
        model.addAttribute("chores", new ArrayList<Chore>());
        return "chores";
    }

    @PostMapping("/chores")
    public String addNewChore(@ModelAttribute Chore chore) throws IOException
    {
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
