package ee.tlu.forum.service;

import ee.tlu.forum.exception.AlreadyExistsException;
import ee.tlu.forum.exception.BadRequestException;
import ee.tlu.forum.exception.NotFoundException;
import ee.tlu.forum.model.Role;
import ee.tlu.forum.model.Thread;
import ee.tlu.forum.model.User;
import ee.tlu.forum.repository.RoleRepository;
import ee.tlu.forum.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Transactional
@Slf4j
@Service
public class UserService implements UserServiceInterface, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
           log.error("User {} not found", username);
           throw new UsernameNotFoundException("User " + username + " not found");
        } else {
            log.info("User {} found in the database", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.get().getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), authorities);
        // return spring security's User class location to not mix up with the forum's own User class.
    }

    @Override
    public User editUser(User user) {
        if (user.getId() == null) {
            throw new BadRequestException("You must specify an ID for the user");
        }
        if (userRepository.findById(user.getId()).isEmpty()) {
            throw new NotFoundException("The user with id: " + user.getId() + " does not exist");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new AlreadyExistsException("Username " + user.getUsername() + " is already taken");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new AlreadyExistsException("E-mail " + user.getEmail() + " is already in use");
        }
        log.info("Saving new user - " + user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public User registerUser(User user) {
        if (user.getUsername() == null || user.getUsername().length() < 2) {
            throw new BadRequestException("Username must be at least 3 characters long.");
        }
        if (user.getEmail() == null) {
            throw new BadRequestException("E-mail cannot be empty.");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters long.");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new AlreadyExistsException("Username " + user.getUsername() + " is already taken.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new AlreadyExistsException("E-mail " + user.getEmail() + " is already in use.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Saving new user - " + user.getUsername());
        Role role = roleRepository.findByName("ROLE_USER").get();
        User newUser = userRepository.save(user);
        newUser.getRoles().add(role);
        return newUser;
    }



    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role - " + role.getName());
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new AlreadyExistsException("A role with the name " + role.getName() + " already exists.");
        }
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new NotFoundException("User does not exist.");
        }
        if (roleRepository.findByName(roleName).isEmpty()) {
            throw new NotFoundException("Role does not exist.");
        }
        User user = userRepository.findByUsername(username).get();
        Role role = roleRepository.findByName(roleName).get();
        log.info("Adding role {} to user {}", roleName, username);
        user.getRoles().add(role);
    }
    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    public List<Role> getRoles() {
        log.info("Fetching all roles");
        return roleRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("User does not exist");
        }
        log.info("Fetching user with id {}", id);
        return userRepository.getById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new NotFoundException("Username does not exist");
        }
        log.info("Fetching user with username {}", username);
        return userRepository.findByUsername(username).get();
    }

    @Override
    public Long getUserPostCount(String username) {
        if (username == null) {
            throw new BadRequestException("Cannot get post count without username.");
        }
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new NotFoundException("No user with username " + username + " exists.");
        }
        return (long) user.get().getPosts().size();
    }

    @Override
    public Long getUserThreadCount(String username) {
        if (username == null) {
            throw new BadRequestException("Cannot get thread count without username.");
        }
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new NotFoundException("No user with username " + username + " exists.");
        }
        return (long) user.get().getThreads().size();
    }

    @Override
    public Long getUserProfileVisitsCount(String username) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User with ID " + id + " does not exist");
        }
        log.info("Deleting user with ID: {}", id);
        userRepository.delete(user.get());
    }

    @Override
    public void deleteUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new NotFoundException("Username does not exist");
        }
        log.info("Deleting user: {}", username);
        userRepository.delete(user.get());
    }

    public User getUserByEmail(String email) {
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new NotFoundException("E-mail does not exist");
        }
        log.info("Fetching user with email {}", email);
        return userRepository.findByEmail(email).get();
    }

    public Role getRoleByName(String name) {
        if (roleRepository.findByName(name).isEmpty()) {
            throw new NotFoundException("Role does not exist");
        }
        return roleRepository.findByName(name).get();
    }
}
