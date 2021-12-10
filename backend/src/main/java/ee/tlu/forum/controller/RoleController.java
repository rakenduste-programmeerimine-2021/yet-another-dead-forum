package ee.tlu.forum.controller;

import ee.tlu.forum.model.Role;
import ee.tlu.forum.service.UserService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class RoleController {

    UserService userService;

    public RoleController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok().body(userService.getRoles());
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role, @RequestHeader("Authorization") String token) {
        // ServletUriComponentsBuilder.fromCurrentContextPath() - returns current link up to the port (8080)
        token = token.substring("Bearer ".length());
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role, token));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody AddRoleToUserForm form, @RequestHeader("Authorization") String token) {
        token = token.substring("Bearer ".length());
        userService.addRoleToUser(form.getUsername(), form.getRolename(), token);
        return ResponseEntity.ok().build();
    }
}

@Data
class AddRoleToUserForm {
    private String username;
    private String rolename;
}
