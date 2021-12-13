package ee.tlu.forum.repository;

import ee.tlu.forum.model.Thread;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThreadRepository extends JpaRepository<Thread, Long> {
    public List<Thread> findAllByOrderByIdDesc();
}
