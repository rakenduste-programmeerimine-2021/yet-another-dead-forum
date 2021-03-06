package ee.tlu.forum.service;

import ee.tlu.forum.exception.AlreadyExistsException;
import ee.tlu.forum.exception.BadRequestException;
import ee.tlu.forum.exception.NotFoundException;
import ee.tlu.forum.model.Role;
import ee.tlu.forum.model.User;
import ee.tlu.forum.model.input.EditUserAboutInput;
import ee.tlu.forum.model.input.EditUserSignatureInput;
import ee.tlu.forum.repository.RoleRepository;
import ee.tlu.forum.repository.UserRepository;
import ee.tlu.forum.utils.RoleHelper;
import ee.tlu.forum.utils.TokenHelper;
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
    private final TokenHelper tokenHelper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, TokenHelper tokenHelper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenHelper = tokenHelper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username.toLowerCase());
        if (user.isEmpty()) {
           log.error("User {} not found", username);
           throw new UsernameNotFoundException("User " + username + " not found");
        } else {
            log.info("User {} found in the database", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.get().getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.get().getUsername().toLowerCase(), user.get().getPassword(), authorities);
        // return spring security's User class location to not mix up with the forum's own User class.
    }

    @Override
    public User editUser(User user, String token) {
        String[] allowedRoles = {"ROLE_ADMIN"};
        if (user.getId() == null) {
            throw new BadRequestException("You must specify an ID for the user");
        }
        if (userRepository.findById(user.getId()).isEmpty()) {
            throw new NotFoundException("The user with id: " + user.getId() + " does not exist");
        }
        if (userRepository.findByUsername(user.getUsername().toLowerCase()).isPresent()) {
            throw new AlreadyExistsException("Username " + user.getUsername().toLowerCase() + " is already taken");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new AlreadyExistsException("E-mail " + user.getEmail() + " is already in use");
        }
        Optional<User> userOptional = userRepository.findById(user.getId());
        tokenHelper.hasRoleOrUsername(token, userOptional.get().getUsername().toLowerCase(), allowedRoles);
        log.info("Saving new user - " + user.getUsername().toLowerCase());
        user.setDisplayName(user.getUsername());
        user.setUsername(user.getUsername().toLowerCase());
        return userRepository.save(user);
    }

    public User editUserAbout(EditUserAboutInput user, String token) {
        String[] allowedRoles = {"ROLE_ADMIN"};
        if (user.getUsername() == null) {
            throw new BadRequestException("You must specify a username for the user");
        }
        if (user.getAbout() == null) {
            throw new BadRequestException("You must specify a new 'about' for the user");
        }
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername().toLowerCase());
        if (userOptional.isEmpty()) {
            throw new NotFoundException("The user with username: " + user.getUsername().toLowerCase() + " does not exist");
        }
        tokenHelper.hasRoleOrUsername(token, user.getUsername().toLowerCase(), allowedRoles);
        log.info("Saving about for user " + user.getUsername().toLowerCase());
        userOptional.get().setAbout(user.getAbout());
        return userOptional.get();
    }

    public User editUserSignature(EditUserSignatureInput user, String token) {
        String[] allowedRoles = {"ROLE_ADMIN"};
        if (user.getUsername() == null) {
            throw new BadRequestException("You must specify a username for the user");
        }
        if (user.getSignature() == null) {
            throw new BadRequestException("You must specify a new 'signature' for the user");
        }
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername().toLowerCase());
        if (userOptional.isEmpty()) {
            throw new NotFoundException("The user with username: " + user.getUsername().toLowerCase() + " does not exist");
        }
        tokenHelper.hasRoleOrUsername(token, user.getUsername().toLowerCase(), allowedRoles);
        log.info("Saving signature for user - " + user.getUsername().toLowerCase());
        userOptional.get().setSignature(user.getSignature());
        return userOptional.get();
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
        if (userRepository.findByUsername(user.getUsername().toLowerCase()).isPresent()) {
            throw new AlreadyExistsException("Username " + user.getUsername().toLowerCase() + " is already taken.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new AlreadyExistsException("E-mail " + user.getEmail() + " is already in use.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Saving new user - " + user.getUsername().toLowerCase());
        Role role = roleRepository.findByName("ROLE_USER").get();
        user.setDisplayName(user.getUsername());
        user.setUsername(user.getUsername().toLowerCase());
        User newUser = userRepository.save(user);
        newUser.getRoles().add(role);
        return newUser;
    }



    @Override
    public Role saveRole(Role role) {
        if (role.getName() == null || role.getName().isEmpty()) {
            throw new BadRequestException("Cannot add role without 'name' field");
        }
        if (role.getBodyCss() == null || role.getBodyCss().isEmpty()) {
            throw new BadRequestException("Cannot add role without 'bodyCss' field");
        }
        if (role.getTextCss() == null || role.getTextCss().isEmpty()) {
            throw new BadRequestException("Cannot add role without 'bodyCss' field");
        }

        role.setName(RoleHelper.toRoleName(role.getName()));

        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new AlreadyExistsException("A role with the name " + role.getName() + " already exists.");
        }
        log.info("Saving new role - " + role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        if (userRepository.findByUsername(username.toLowerCase()).isEmpty()) {
            throw new NotFoundException("User does not exist.");
        }
        if (roleRepository.findByName(roleName).isEmpty()) {
            throw new NotFoundException("Role does not exist.");
        }
        User user = userRepository.findByUsername(username.toLowerCase()).get();
        Role role = roleRepository.findByName(roleName).get();
        log.info("Adding role {} to user {}", roleName, username);
        user.getRoles().add(role);
    }

    public void deleteRoleFromUser(String username, String roleName) {
        if (userRepository.findByUsername(username.toLowerCase()).isEmpty()) {
            throw new NotFoundException("User does not exist.");
        }
        if (roleRepository.findByName(roleName).isEmpty()) {
            throw new NotFoundException("Role does not exist.");
        }

        User user = userRepository.findByUsername(username.toLowerCase()).get();
        Role role = roleRepository.findByName(roleName).get();

        if (!user.getRoles().contains(role)) {
            throw new BadRequestException("User does not have this role!");
        }

        log.info("Removing role {} from user {}", roleName, username);
        user.getRoles().remove(role);
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
        log.info("Fetching user with id {}", id);
        return userRepository.getById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        if (userRepository.findByUsername(username.toLowerCase()).isEmpty()) {
            throw new NotFoundException("Username does not exist");
        }
        log.info("Fetching user with username {}", username);
        return userRepository.findByUsername(username.toLowerCase()).get();
    }

    @Override
    public Long getUserPostCount(String username) {
        if (username == null) {
            throw new BadRequestException("Cannot get post count without username.");
        }
        Optional<User> user = userRepository.findByUsername(username.toLowerCase());
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
        Optional<User> user = userRepository.findByUsername(username.toLowerCase());
        if (user.isEmpty()) {
            throw new NotFoundException("No user with username " + username + " exists.");
        }
        return (long) user.get().getThreads().size();
    }

    @Override
    public User getUserProfileByUsername(String username) {
        User user = getUserByUsername(username.toLowerCase());
        user.setVisits(user.getVisits() + 1);
        return user;
    }

    @Override
    public Long getUserProfileVisitsCount(String username) {
        Optional<User> user = userRepository.findByUsername(username.toLowerCase());
        if (user.isEmpty()) {
            throw new NotFoundException("Username does not exist");
        }
        log.info("Fetching user profile visits for: {}", username);
        return user.get().getVisits();
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
        Optional<User> user = userRepository.findByUsername(username.toLowerCase());
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
