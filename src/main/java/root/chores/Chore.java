package root.chores;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Chore
{
    private @Id @GeneratedValue Long id;
    private String name;
    private String username;
    private Integer daysBetween;
    private Integer durationMinutes;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate due;

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
