package org.example.reimbursement.user;

import org.example.reimbursement.PostgreSQLFactory;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    private final UserService userService = new UserService();

    @Test
    void insertAndFindById() throws SQLException {
        try (Connection conn = PostgreSQLFactory.open()) {
            User user = new User();
            user.setEmail("email");
            user.setUsername("username");
            user.setJobTitle(JobTitle.EMPLOYEE);
            user.setName("name");
            user.setPassword("1234");
            userService.insert(conn, user);

            user = userService.findById(conn, user.getId());
            assertEquals("username", user.getUsername());
            conn.rollback();
        }
    }

    @Test
    void updateAndFindAll() throws SQLException {
        try (Connection conn = PostgreSQLFactory.open()) {
            User user = new User();
            user.setEmail("email");
            user.setUsername("username");
            user.setJobTitle(JobTitle.EMPLOYEE);
            user.setName("name");
            user.setPassword("1234");
            userService.insert(conn, user);

            user.setName("name2");
            userService.update(conn, user);

            List<User> users = userService.findAll(conn);
            assertTrue(users.size() > 1);

            conn.rollback();
        }
    }
}