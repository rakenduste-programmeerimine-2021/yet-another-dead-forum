package ee.tlu.forum.service;

import ee.tlu.forum.model.Thread;
import ee.tlu.forum.model.input.AddNewThreadInput;

import java.util.List;

public interface ThreadServiceInterface {
    Thread createThread(AddNewThreadInput form);
    void deleteThreadById(Long id);
    Thread editThread (Thread thread);
    List<Thread> getAllThreads();
    Thread getThreadById(Long id);
    List<Thread> getAllThreadsByUserId(Long id);
    List<Thread> getAllThreadsByUsername(String username);
}
