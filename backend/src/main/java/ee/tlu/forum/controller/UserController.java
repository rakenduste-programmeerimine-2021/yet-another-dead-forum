package ee.tlu.forum.controller;

import ee.tlu.forum.model.User;
import ee.tlu.forum.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        // ServletUriComponentsBuilder.fromCurrentContextPath() - returns current link up to the port (8080)
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.registerUser(user));
    }

    @PostMapping("/user/edit")
    public ResponseEntity<User> saveUser(@RequestBody User user, @RequestHeader("Authorization") String token) {
        token = token.substring("Bearer ".length());
        return ResponseEntity.ok().body(userService.editUser(user, token));
    }

    @GetMapping("/user/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @GetMapping("/user/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

    @DeleteMapping("/user/delete/id/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/delete/username/{username}")
    public ResponseEntity<User> deleteUserByUsername(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{username}/postcount")
    public ResponseEntity<?> getPostCountByUserUsername(@PathVariable String username) {
        Map<String, Long> response = new HashMap<>();
        response.put("postCount", userService.getUserPostCount(username));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/user/{username}/threadcount")
    public ResponseEntity<?> getThreadCountByUserUsername(@PathVariable String username) {
        Map<String, Long> response = new HashMap<>();
        response.put("threadCount", userService.getUserThreadCount(username));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/user/{username}/profile")
    public ResponseEntity<User> getUserProfile(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.getUserProfileByUsername(username));
    }

    @GetMapping("/user/{username}/visits")
    public ResponseEntity<?> getUserVisitsCount(@PathVariable String username) {
        Map<String, Long> response = new HashMap<>();
        response.put("visits", userService.getUserProfileVisitsCount(username));
        return ResponseEntity.ok().body(response);
    }

}
