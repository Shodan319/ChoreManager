package chores;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChoreEntityTest
{
    @Test
    public void choreIsDueOnDueDate()
    {
        assertTrue(new Chore("sample chore", 7).isDue());
    }

    @Test
    public void choreIsDueAfterDueDate()
    {
        var chore = new Chore("sample chore", 7, 0, LocalDate.now().minusDays(1));
        assertTrue(chore.isDue());
    }

    @Test
    public void choreIsNotDueBeforeDueDate()
    {
        var chore = new Chore("sample chore", 7, 0, LocalDate.now().plusDays(1));
        assertFalse(chore.isDue());
    }
}
