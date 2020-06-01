package root.chores;

public class ChoreNotFoundException extends Exception
{
    public ChoreNotFoundException(String id)
    {
        super("Could not find chore with id: " + id);
    }
}
