package ee.tlu.forum.controller;

import ee.tlu.forum.model.Role;
import ee.tlu.forum.model.User;
import ee.tlu.forum.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
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
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // ServletUriComponentsBuilder.fromCurrentContextPath() - returns current link up to the port (8080)
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/user/save").toUriString());
        List<Role> r = new ArrayList<>();
        r.add(userService.getRoleByName("ROLE_USER"));
        user.setRoles(r);
        userService.saveUser(user);
        return ResponseEntity.created(uri).build();
        // todo PSQLException - user already exists
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        return ResponseEntity.ok().body(userService.saveUser(user));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }
}
