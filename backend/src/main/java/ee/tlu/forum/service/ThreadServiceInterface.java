package ee.tlu.forum.service;

import ee.tlu.forum.model.Post;
import ee.tlu.forum.model.Thread;
import ee.tlu.forum.model.input.AddNewThreadInput;

import java.util.List;

public interface ThreadServiceInterface {
    Thread createThread(AddNewThreadInput form);
    void deleteThreadById(Long id);
    Thread editThread (AddNewThreadInput form);
    List<Thread> getAllThreads();
    Thread getThreadById(Long id);
    List<Thread> getAllThreadsByUserId(Long id);
}
