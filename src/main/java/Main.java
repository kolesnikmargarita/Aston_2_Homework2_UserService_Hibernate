import config.HibernateConfig;
import console.Console;
import controller.UserController;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) {

        HibernateConfig config = new HibernateConfig();
        SessionFactory sessionFactory = config.sessionFactory();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Closing SessionFactory...");
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }));

        UserController userController = new UserController(sessionFactory);
        Console console = new Console(userController);
        console.run();

        sessionFactory.close();
    }
}
