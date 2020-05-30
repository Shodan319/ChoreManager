package chores;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
public class Chore
{
    private @Id @GeneratedValue Long id;
    private String name;
    private Integer daysBetween;
    private Integer durationMinutes;
    private LocalDate due;

    public Chore()
    {
    }

    public Chore(String name, Integer daysBetween)
    {
        this(name, daysBetween, 0);
    }

    public Chore(String name, Integer daysBetween, Integer durationMinutes)
    {
        this(name, daysBetween, durationMinutes, LocalDate.now());
    }

    public Chore(String name, Integer daysBetween, Integer durationMinutes, LocalDate due)
    {
        this.name = name;
        this.daysBetween = daysBetween;
        this.durationMinutes = durationMinutes;
        this.due = due;
    }
}
