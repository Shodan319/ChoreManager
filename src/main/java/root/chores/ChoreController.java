package root.chores;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChoreController
{
    private final ChoreRepository repository;

    ChoreController(ChoreRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/chores")
    List<Chore> all(Authentication authentication)
    {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        var username = details.getUsername();
        var allChores = this.repository.findAll();
        List<Chore> choresForUser = new ArrayList<>();
        for (var chore : allChores)
            if (chore.getUsername().equals(username))
                choresForUser.add(chore);
        return choresForUser;
    }

    @PostMapping("/chore")
    Chore newChore(@RequestBody Chore newChore, Authentication authentication)
    {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        var username = details.getUsername();
        newChore.setUsername(username);
        return repository.save(newChore);
    }

    @GetMapping("/chore/{id}")
    Chore getChore(@PathVariable Long id) throws ChoreNotFoundException
    {
        return this.repository.findById(id).orElseThrow(() -> new ChoreNotFoundException(id.toString()));
    }

    @PutMapping("/chore/{id}")
    Chore rescheduleChore(@PathVariable Long id)
    {
        return this.repository.findById(id).
            map(this::updateDueDate).orElseThrow();
    }

    private Chore updateDueDate(Chore chore)
    {
        Chore updatedChore = new ChoreDomain().updateDueDate(chore);
        return repository.save(updatedChore);

    }
}
