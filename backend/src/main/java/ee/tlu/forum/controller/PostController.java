package ee.tlu.forum.controller;

import ee.tlu.forum.model.Post;
import ee.tlu.forum.model.input.AddNewPostInput;
import ee.tlu.forum.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class PostController {
    
    PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts() {
        return ResponseEntity.ok().body(postService.getAllPosts());
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok().body(postService.getPostById(id));
    }

    @PostMapping("/post/add")
    public ResponseEntity<Post> createPost(@RequestBody AddNewPostInput form, @RequestHeader("Authorization") String token) {
        token = token.substring("Bearer ".length());
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/post/add").toUriString());
        return ResponseEntity.created(uri).body(postService.createPost(form, token));
    }

    @PatchMapping("/post/edit")
    public ResponseEntity<Post> editPost(@RequestBody Post post, @RequestHeader("Authorization") String token) {
        token = token.substring("Bearer ".length());
        return ResponseEntity.ok().body(postService.editPost(post, token));
    }

    @DeleteMapping("/post/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePostById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/userid/{id}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok().body(postService.getAllPostsByUserId(id));
    }

    @GetMapping("/posts/username/{name}")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable String name) {
        return ResponseEntity.ok().body(postService.getAllPostsByUsername(name));
    }
}
