package chores;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChoreEntityTest
{
    @Test
    public void choreIsDueOnDueDate()
    {
        var chore = new Chore("sample chore", 7);
        assertTrue(new ChoreDomain().isDue(chore));
    }

    @Test
    public void choreIsDueAfterDueDate()
    {
        var chore = new Chore("sample chore", 7, 0, LocalDate.now().minusDays(1));
        assertTrue(new ChoreDomain().isDue(chore));
    }

    @Test
    public void choreIsNotDueBeforeDueDate()
    {
        var chore = new Chore("sample chore", 7, 0, LocalDate.now().plusDays(1));
        assertFalse(new ChoreDomain().isDue(chore));
    }

    @Test
    public void updateChoreDueDateWhenNotDue()
    {
        var chore = new Chore("sample chore", 7, 0, LocalDate.now().plusDays(1));
        var updatedChore = new ChoreDomain().updateDueDate(chore);
        assertEquals(chore.getDue(), updatedChore.getDue());
    }

    @Test
    public void updateDueChore()
    {
        var chore = new Chore("sample chore", 7, 0, LocalDate.now().minusDays(1));
        var updatedChore = new ChoreDomain().updateDueDate(chore);
        assertEquals(LocalDate.now().plusDays(7), updatedChore.getDue());
    }
}
