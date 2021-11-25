package ee.tlu.forum.service;


import ee.tlu.forum.exception.BadRequestException;
import ee.tlu.forum.exception.NotFoundException;
import ee.tlu.forum.model.Post;
import ee.tlu.forum.model.Thread;
import ee.tlu.forum.model.User;
import ee.tlu.forum.model.input.AddNewThreadInput;
import ee.tlu.forum.repository.ThreadRepository;
import ee.tlu.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
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
        if (form.getTitle() == null || form.getTitle().isEmpty()) {
            throw new BadRequestException("Title field cannot be empty.");
        }
        if (form.getContent() == null || form.getContent().isEmpty()) {
            throw new BadRequestException("Content field cannot be empty.");
        }
        if (form.getUsername() == null || form.getUsername().isEmpty()) {
            throw new BadRequestException("Username field cannot be empty.");
        }
        if (form.getTitle().length() > 255) {
            throw new BadRequestException("Title character limit exceeded." +
                    " Maximum: 255 characters. You have: " + form.getTitle().length());
        }
        if (form.getContent().length() > 3096) {
            throw new BadRequestException("Content character limit exceeded." +
                    " Maximum: 3096 characters. You have: " + form.getContent().length());
        }
        Optional<User> user = userRepository.findByUsername(form.getUsername());
        if (user.isEmpty()) {
            throw new NotFoundException("User " + form.getUsername() + " not found");
        }
        Thread thread = new Thread();
        thread.setTitle(form.getTitle());
        thread.setAuthor(user.get());
        thread.setText(form.getContent());
        return threadRepository.save(thread);
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
    public Thread editThread(Thread thread) {
        if (thread.getId() == null) {
            throw new BadRequestException("Field must contain a thread ID");
        }
        Optional<Thread> threadOptional = threadRepository.findById(thread.getId());
        if (threadOptional.isEmpty()) {
            throw new NotFoundException("Thread with ID " + thread.getId() + " does not exist");
        }
        if (thread.getText() != null) {
            if (thread.getText().length() == 0) {
                throw new BadRequestException("Text field cannot be empty!");
            }
            if (thread.getText().length() > 3096) {
                throw new BadRequestException("Content character limit exceeded." +
                        " Maximum: 3096 characters. You have: " + thread.getText().length());
            }
            threadOptional.get().setText(thread.getText());
        }
        if (thread.getTitle() != null) {
            if (thread.getTitle().length() == 0) {
                throw new BadRequestException("Title field cannot be empty!");
            }
            if (thread.getTitle().length() > 255) {
                throw new BadRequestException("Title character limit exceeded." +
                        " Maximum: 255 characters. You have: " + thread.getTitle().length());
            }
            threadOptional.get().setTitle(thread.getTitle());
        }
        log.info("Saving thread");
        return threadOptional.get();
    }

    @Override
    public List<Thread> getAllThreads() {
        log.info("Fetching all threads");
        return threadRepository.findAll();
    }

    public Collection<Post> getAllPostsByThreadId(Long id) {
        if (id == null) {
            throw new BadRequestException("Cannot get thread posts without thread ID");
        }
        Optional<Thread> thread = threadRepository.findById(id);
        if (thread.isEmpty()) {
            throw new NotFoundException("Thread with ID " + id + " does not exist");
        }
        log.info("Fetching thread with ID: " + id);
        return thread.get().getPosts();
    }

    @Override
    public Thread getThreadById(Long id) {
        if (id == null) {
            throw new BadRequestException("Cannot get thread without ID");
        }
        Optional<Thread> thread = threadRepository.findById(id);
        if (thread.isEmpty()) {
            throw new NotFoundException("Thread with ID " + id + " does not exist");
        }
        log.info("Fetching thread with ID: " + id);
        return thread.get();
    }

    @Override
    public List<Thread> getAllThreadsByUserId(Long id) {
        if (id == null) {
            throw new BadRequestException("Cannot get threads without user ID.");
        }
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("No user with ID " + id + " exists.");
        }
        return (List<Thread>) user.get().getThreads();
    }

    @Override
    public List<Thread> getAllThreadsByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Cannot get threads without username.");
        }
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new NotFoundException("No user with username " + username + " exists.");
        }
        return (List<Thread>) user.get().getThreads();
    }
}
