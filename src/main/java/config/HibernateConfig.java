package config;


import entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateConfig {
    private static final Logger log = LoggerFactory.getLogger(HibernateConfig.class);

    public SessionFactory sessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();

        try {
            log.info("Creating sessionFactory");
            return new MetadataSources(registry)
                    .addAnnotatedClass(User.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            log.error("Failed to create SessionFactory", e);
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException("Failed to create SessionFactory", e);
        }
    }
}
