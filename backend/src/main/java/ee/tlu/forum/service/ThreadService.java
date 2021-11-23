package ee.tlu.forum.service;


import ee.tlu.forum.exception.BadRequestException;
import ee.tlu.forum.exception.NotFoundException;
import ee.tlu.forum.model.Thread;
import ee.tlu.forum.model.input.AddNewThreadInput;
import ee.tlu.forum.repository.ThreadRepository;
import ee.tlu.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ThreadService implements ThreadServiceInterface {

    private final UserRepository userRepository;
    private final ThreadRepository threadRepository;

    @Override
    public Thread createThread(AddNewThreadInput form) {
        return null;
    }

    @Override
    public void deleteThreadById(Long id) {
        if (id == null) {
            throw new BadRequestException("Cannot delete without the thread ID.");
        }
        Optional<Thread> thread = threadRepository.findById(id);

        if (thread.isEmpty()) {
            throw new NotFoundException("Thread with ID " + id + " does not exist.");
        }
        log.info("Deleting thread " + thread.get().getTitle());
        threadRepository.deleteById(id);
    }

    @Override
    public Thread editThread(AddNewThreadInput form) {
        return null;
    }

    @Override
    public List<Thread> getAllThreads() {
        return null;
    }

    @Override
    public Thread getThreadById(Long id) {
        return null;
    }

    @Override
    public List<Thread> getAllThreadsByUserId(Long id) {
        return null;
    }
}
