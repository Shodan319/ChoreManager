package root.chores;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ChoreRepository extends JpaRepository<Chore, Long>
{
    public List<Chore> findByDueBefore(LocalDate day);
}
