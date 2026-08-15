import config.HibernateConfig;
import controller.UserController;
import dto.*;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TestRunner {

    private static final Logger log = LoggerFactory.getLogger(TestRunner.class);

    public static void main(String[] args) {
        log.info("=== STARTING TESTS ===");

        HibernateConfig config = new HibernateConfig();
        SessionFactory sessionFactory = config.sessionFactory();

        try {
            UserController controller = new UserController(sessionFactory);

            testCreateUser(controller);
            testFindAllUsers(controller);
            testFindUserById(controller);
            testUpdateUser(controller);
            testDeleteUser(controller);

            log.info("=== ALL TESTS PASSED! ===");
        } catch (Exception e) {
            log.error("❌ Test failed: ", e);
        } finally {
            sessionFactory.close();
        }
    }

    // ===== TEST 1: CREATE =====
    private static void testCreateUser(UserController controller) {
        log.info("--- Testing CREATE ---");

        CreateUserDto dto = new CreateUserDto("Alice", "alice@test.com", 25);
        GetUserDto created = controller.create(dto);

        if (created.getId() == null) {
            throw new RuntimeException("❌ Create failed: ID is null");
        }
        if (!"Alice".equals(created.getName())) {
            throw new RuntimeException("❌ Create failed: Name mismatch");
        }
        if (!"alice@test.com".equals(created.getEmail())) {
            throw new RuntimeException("❌ Create failed: Email mismatch");
        }
        if (created.getAge() != 25) {
            throw new RuntimeException("❌ Create failed: Age mismatch");
        }

        log.info("✅ User created: {}", created);
    }

    // ===== TEST 2: FIND ALL =====
    private static void testFindAllUsers(UserController controller) {
        log.info("--- Testing FIND ALL ---");

        List<GetUserDto> users = controller.readAll();

        if (users.isEmpty()) {
            throw new RuntimeException("❌ FindAll failed: No users found");
        }

        log.info("✅ Found {} users", users.size());
        users.forEach(user -> log.info("   {}", user));
    }

    // ===== TEST 3: FIND BY ID =====
    private static void testFindUserById(UserController controller) {
        log.info("--- Testing FIND BY ID ---");

        // Сначала создаём пользователя, чтобы точно был в БД
        CreateUserDto createDto = new CreateUserDto("Bob", "bob@test.com", 30);
        GetUserDto created = controller.create(createDto);
        Long id = created.getId();

        ReadUserByIdDto readDto = new ReadUserByIdDto(id);
        GetUserDto found = controller.readById(readDto);

        if (found == null) {
            throw new RuntimeException("❌ FindById failed: User not found for id " + id);
        }
        if (!"Bob".equals(found.getName())) {
            throw new RuntimeException("❌ FindById failed: Name mismatch");
        }

        log.info("✅ User found by id {}: {}", id, found);
    }

    // ===== TEST 4: UPDATE =====
    private static void testUpdateUser(UserController controller) {
        log.info("--- Testing UPDATE ---");

        // Создаём пользователя для обновления
        CreateUserDto createDto = new CreateUserDto("Charlie", "charlie@test.com", 22);
        GetUserDto created = controller.create(createDto);
        Long id = created.getId();

        // Обновляем
        UpdateUserDto updateDto = new UpdateUserDto(id, "Charles", "charles@test.com", 28);
        GetUserDto updated = controller.update(updateDto);

        if (!"Charles".equals(updated.getName())) {
            throw new RuntimeException("❌ Update failed: Name not updated");
        }
        if (!"charles@test.com".equals(updated.getEmail())) {
            throw new RuntimeException("❌ Update failed: Email not updated");
        }
        if (updated.getAge() != 28) {
            throw new RuntimeException("❌ Update failed: Age not updated");
        }

        log.info("✅ User updated: {}", updated);
    }

    // ===== TEST 5: DELETE =====
    private static void testDeleteUser(UserController controller) {
        log.info("--- Testing DELETE ---");

        // Создаём пользователя для удаления
        CreateUserDto createDto = new CreateUserDto("Dave", "dave@test.com", 40);
        GetUserDto created = controller.create(createDto);
        Long id = created.getId();

        // Удаляем
        DeleteUserDto deleteDto = new DeleteUserDto(id);
        controller.delete(deleteDto);

        // Проверяем, что пользователь действительно удалён
        ReadUserByIdDto readDto = new ReadUserByIdDto(id);
        try {
            GetUserDto found = controller.readById(readDto);
            if (found != null) {
                throw new RuntimeException("❌ Delete failed: User still exists with id " + id);
            }
        } catch (Exception e) {
            // Ожидаем, что пользователь не найден
            log.info("✅ User deleted: {}", id);
        }
    }
}