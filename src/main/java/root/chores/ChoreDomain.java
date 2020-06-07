package root.chores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChoreDomain
{
    public boolean isDue(Chore chore)
    {
        return !chore.getDue().isAfter(LocalDate.now());
    }

    public Chore updateDueDate(Chore chore)
    {
        if (isDue(chore))
        {
            return new Chore(chore.getName(),
                    chore.getDaysBetween(),
                    chore.getDurationMinutes(),
                    LocalDate.now().plusDays(chore.getDaysBetween()));

        }
        return chore;
    }

    public List<Chore> filterByUsername(List<Chore> chores, String username)
    {
        List<Chore> filteredChores = new ArrayList<>();
        for (var chore : chores)
            if (chore.getUsername().equals(username))
                filteredChores.add(chore);
        return filteredChores;
    }
}
