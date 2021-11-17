package ee.tlu.forum.service;

import ee.tlu.forum.model.Role;
import ee.tlu.forum.model.User;
import ee.tlu.forum.repository.RoleRepository;
import ee.tlu.forum.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Slf4j
@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user - " + user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role - " + role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }
    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        log.info("Fetching user with id {}", id);
        return userRepository.getById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        log.info("Fetching user with username {}", username);
        return userRepository.findByUsername(username);
    }
}
