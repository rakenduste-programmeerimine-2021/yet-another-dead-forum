package ee.tlu.forum.service;

import ee.tlu.forum.model.Thread;
import ee.tlu.forum.model.input.AddNewThreadInput;

import java.util.List;

public interface ThreadServiceInterface {
    Thread createThread(AddNewThreadInput form, String token);
    void deleteThreadById(Long id);
    Thread editThread (Thread thread, String token);
    List<Thread> getAllThreads();
    Thread getThreadById(Long id);
    List<Thread> getAllThreadsByUserId(Long id);
    List<Thread> getAllThreadsByUsername(String username);
}
