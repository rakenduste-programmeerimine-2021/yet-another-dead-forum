package ee.tlu.forum.service;

import ee.tlu.forum.model.Role;
import ee.tlu.forum.model.User;

import java.util.List;

public interface UserServiceInterface{
    User editUser(User user, String token);
    User registerUser(User user);
    Role saveRole(Role role, String token);
    void addRoleToUser(String username, String roleName, String token);
    List<User> getUsers();
    User getUserById(Long id, String token);
    User getUserByUsername(String username, String token);
    User getUserByUsernameAuthorized(String username);
    Long getUserPostCount(String username);
    Long getUserThreadCount(String username);
    Long getUserProfileVisitsCount(String username);
    void deleteUserById(Long id, String token);
    void deleteUserByUsername(String username, String token);
    User getUserProfileByUsername(String username, String token);
}
