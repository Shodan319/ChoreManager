package root.chores;

import org.springframework.web.bind.annotation.*;

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
    List<Chore> all()
    {
        return this.repository.findAll();
    }

    @PostMapping("/chore")
    Chore newChore(@RequestBody Chore newChore)
    {
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
