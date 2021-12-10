package ee.tlu.forum.controller;

import ee.tlu.forum.model.Post;
import ee.tlu.forum.model.Thread;
import ee.tlu.forum.model.input.AddNewThreadInput;
import ee.tlu.forum.service.ThreadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class ThreadController {

    public ThreadService threadService;

    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    @GetMapping("/threads")
    public ResponseEntity<List<Thread>> getThreads() {
        return ResponseEntity.ok().body(threadService.getAllThreads());
    }

    @GetMapping("/thread/{id}")
    public ResponseEntity<Thread> getThreadById(@PathVariable Long id) {
        return ResponseEntity.ok().body(threadService.getThreadById(id));
    }

    @PostMapping("/thread/add")
    public ResponseEntity<Thread> createThread(@RequestBody AddNewThreadInput form, @RequestHeader("Authorization") String token) {
        token = token.substring("Bearer ".length());
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/thread/add").toUriString());
        return ResponseEntity.created(uri).body(threadService.createThread(form, token));
    }

    @PatchMapping("/thread/edit")
    public ResponseEntity<Thread> editThread(@RequestBody Thread thread, @RequestHeader("Authorization") String token) {
        token = token.substring("Bearer ".length());
        return ResponseEntity.ok().body(threadService.editThread(thread, token));
    }

    @DeleteMapping("/thread/delete/{id}")
    public ResponseEntity<?> deleteThread(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        token = token.substring("Bearer ".length());
        threadService.deleteThreadById(id, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/thread/{id}/posts")
    public ResponseEntity<Collection<Post>> getAllThreadPosts(@PathVariable Long id) {
        return ResponseEntity.ok().body(threadService.getAllPostsByThreadId(id));
    }

    @GetMapping("/threads/userid/{id}")
    public ResponseEntity<List<Thread>> getThreadsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok().body(threadService.getAllThreadsByUserId(id));
    }

    @GetMapping("/threads/username/{name}")
    public ResponseEntity<List<Thread>> getThreadsByUsername(@PathVariable String name) {
        return ResponseEntity.ok().body(threadService.getAllThreadsByUsername(name));
    }
}