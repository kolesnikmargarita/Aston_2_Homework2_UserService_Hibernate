package repository;

import config.HibernateConfig;
import entity.User;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static SessionFactory sessionFactory;
    private UserRepository userRepository;

    @BeforeAll
    static void setUpAll() {
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgres.getUsername());
        System.setProperty("DB_PASSWORD", postgres.getPassword());

        HibernateConfig config = new HibernateConfig();
        sessionFactory = config.sessionFactory();
    }

    @AfterAll
    static void tearDownAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository(sessionFactory);
        sessionFactory.inTransaction(session -> session.createMutationQuery("DELETE FROM User").executeUpdate());
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@test.com");
        user.setAge(25);

        User created = userRepository.create(user);

        assertNotNull(created.getId());
        assertEquals("Alice", created.getName());
        assertEquals("alice@test.com", created.getEmail());
        assertEquals(25, created.getAge());
        assertNotNull(created.getCreated_at());
    }

    @Test
    void testFindById_UserExists() {
        User user = new User();
        user.setName("Bob");
        user.setEmail("bob@test.com");
        user.setAge(30);
        User created = userRepository.create(user);

        Optional<User> found = userRepository.findById(created.getId());

        assertTrue(found.isPresent());
        assertEquals("Bob", found.get().getName());
        assertEquals("bob@test.com", found.get().getEmail());
        assertEquals(30, found.get().getAge());
    }

    @Test
    void testFindById_UserNotFound() {
        Optional<User> found = userRepository.findById(999L);
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        User user1 = new User();
        user1.setName("Alice");
        user1.setEmail("alice@test.com");
        user1.setAge(25);
        userRepository.create(user1);

        User user2 = new User();
        user2.setName("Bob");
        user2.setEmail("bob@test.com");
        user2.setAge(30);
        userRepository.create(user2);

        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
        List<String> names = users.stream().map(User::getName).toList();
        assertTrue(names.contains("Alice"));
        assertTrue(names.contains("Bob"));
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setName("Charlie");
        user.setEmail("charlie@test.com");
        user.setAge(22);
        User created = userRepository.create(user);

        created.setName("Charles");
        created.setAge(28);
        userRepository.update(created);

        Optional<User> updated = userRepository.findById(created.getId());
        assertTrue(updated.isPresent());
        assertEquals("Charles", updated.get().getName());
        assertEquals(28, updated.get().getAge());
        assertEquals("charlie@test.com", updated.get().getEmail());
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setName("Dave");
        user.setEmail("dave@test.com");
        user.setAge(40);
        User created = userRepository.create(user);

        userRepository.removeById(created.getId());

        Optional<User> found = userRepository.findById(created.getId());
        assertFalse(found.isPresent());
    }
}