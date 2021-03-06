package root.chores;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class ChoreRestController
{
    private final ChoreRepository repository;
    private final ChoreDomain domain;

    ChoreRestController(ChoreRepository repository,
                        ChoreDomain domain)
    {
        this.repository = repository;
        this.domain = domain;
    }

    @GetMapping("chores")
    List<Chore> all(Authentication authentication)
    {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        var username = details.getUsername();
        var allChores = this.repository.findAll();
        return domain.filterByUsername(allChores, username);
    }

    @PostMapping("chore")
    Chore newChore(@RequestBody Chore newChore, Authentication authentication)
    {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        var username = details.getUsername();
        newChore.setUsername(username);
        return repository.save(newChore);
    }

    @GetMapping("chore/{id}")
    Chore getChore(@PathVariable Long id) throws ChoreNotFoundException
    {
        return this.repository.findById(id).orElseThrow(() -> new ChoreNotFoundException(id.toString()));
    }

    @PutMapping("chore/{id}")
    Chore rescheduleChore(@PathVariable Long id)
    {
        return this.repository.findById(id).
            map(this::updateDueDate).orElseThrow();
    }

    private Chore updateDueDate(Chore chore)
    {
        Chore updatedChore = domain.updateDueDate(chore);
        return repository.save(updatedChore);
    }
}
