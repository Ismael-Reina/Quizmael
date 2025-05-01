package com.quizmael.dao.impl;

import com.quizmael.dao.UserDao;
import com.quizmael.model.User;
import com.quizmael.model.enums.Role;

import java.util.Optional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link UserDaoImpl}.
 * Tests basic CRUD operations on {@link User} entities.
 * Uses JUnit 5 framework.
 *
 * @author Ismael
 * @version 1.0
 */
class UserDaoTest {

    private static UserDao userDao;

    @BeforeAll
    static void setup() {
        userDao = new UserDaoImpl();
    }

    // Verifies that a user can be saved and correctly retrieved by its ID.
    @Test
    void testSaveAndFindById() {
        User user = new User();
        user.setName("IsmaTech");
        user.setPassword("password123");
        user.setRole(Role.REGISTERED);
        userDao.save(user);

        assertNotNull(user.getId(), "ID should be assigned after save");
        Optional<User> found = userDao.findById(user.getId());

        assertTrue(found.isPresent(), "User should be found");
        assertEquals("IsmaTech", found.get().getName(), "Username should match");
    }

    // Verifies that an existing user can be updated and changes are persisted.
    @Test
    void testUpdate() {
        User user = new User();
        user.setName("user1");
        user.setPassword("abc123");
        user.setRole(Role.REGISTERED);
        userDao.save(user);

        user.setPassword("newPassword456");
        userDao.update(user);

        Optional<User> updated = userDao.findById(user.getId());
        assertTrue(updated.isPresent());
        assertEquals("newPassword456", updated.get().getPassword());
    }

    // Verifies that a user can be deleted and cannot be retrieved afterward.
    @Test
    void testDelete() {
        User user = new User();
        user.setName("user_to_delete");
        user.setPassword("pass");
        user.setRole(Role.REGISTERED);
        userDao.save(user);

        userDao.delete(user);

        Optional<User> deleted = userDao.findById(user.getId());
        assertTrue(deleted.isEmpty());
    }

    // Verifies that searching for a non-existent user returns an empty Optional.
    @Test
    void testFindByIdNotFound() {
        Optional<User> result = userDao.findById(9999);
        assertTrue(result.isEmpty());
    }

}
