package chores;

import java.time.LocalDate;

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
}
