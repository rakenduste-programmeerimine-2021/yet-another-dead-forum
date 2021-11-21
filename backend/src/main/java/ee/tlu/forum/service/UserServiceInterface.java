package ee.tlu.forum.service;

import ee.tlu.forum.model.Role;
import ee.tlu.forum.model.User;

import java.util.List;

public interface UserServiceInterface{
    User editUser(User user);
    User registerUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    List<User> getUsers();
    User getUserById(Long id);
    User getUserByUsername(String username);
}
