package ee.tlu.forum.service;

import ee.tlu.forum.model.Role;
import ee.tlu.forum.model.User;

import java.util.List;

public interface UserServiceInterface{
    User editUser(User user, String token);
    User registerUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    List<User> getUsers();
    User getUserById(Long id);
    User getUserByUsername(String username);
    Long getUserPostCount(String username);
    Long getUserThreadCount(String username);
    Long getUserProfileVisitsCount(String username);
    void deleteUserById(Long id);
    void deleteUserByUsername(String username);
    User getUserProfileByUsername(String username);
}
