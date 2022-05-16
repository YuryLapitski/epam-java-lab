package com.epam.esm.repository.config;

import org.hibernate.SessionFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@SpringBootConfiguration
@Profile("dev")
@ComponentScan("com.epam.esm")
public class TestConfig {
    private static final String SQL_SETUP = "classpath:dbSetup.sql";
    private static final String SQL_INIT = "classpath:dbInitDev.sql";

    private static final String SCANNED_PACKAGE = "com.epam.esm";

    @Bean
    public DataSource embeddedDatabase() {
        EmbeddedDatabaseBuilder databaseBuilder = new EmbeddedDatabaseBuilder();
        return databaseBuilder
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript(SQL_SETUP)
                .addScript(SQL_INIT)
                .build();
    }

    @Bean
    public EntityManager entityManager() {
        SessionFactory localSessionFactoryBean =  sessionFactory().getObject();
        return Objects.requireNonNull(localSessionFactoryBean).createEntityManager();
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(embeddedDatabase());
        sessionFactory.setPackagesToScan(SCANNED_PACKAGE);
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "none");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        return hibernateProperties;
    }

    @Bean
    PlatformTransactionManager transactionManager(EntityManagerFactory managerFactory) {
        return new JpaTransactionManager(managerFactory);
    }
}
