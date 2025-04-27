package com.quizmael.dao.impl;

import com.quizmael.dao.UserDao;
import com.quizmael.model.User;
import com.quizmael.model.enums.Role;

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
class UserDaoImplTest {

    private static UserDao userDao;

    @BeforeAll
    static void setup() {
        userDao = new UserDaoImpl();
    }

    @Test
    void testSaveAndFindById() {
        User user = new User();
        user.setName("test_user");
        user.setPassword("password123");
        user.setRole(Role.REGISTERED);

        userDao.save(user);

        assertNotNull(user.getId(), "ID should be assigned after save");

        var found = userDao.findById(user.getId());

        assertTrue(found.isPresent(), "User should be found");
        assertEquals("test_user", found.get().getName(), "Username should match");
    }
}
