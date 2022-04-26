package com.epam.esm.repository.config;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagToGiftCertificateRelation;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@PropertySource("classpath:database.properties")
@EnableTransactionManagement
public class SpringJdbcConfig {
    private static final String SQL_SETUP = "classpath:dbSetup.sql";
    private static final String SQL_INIT_DEV = "classpath:dbInitDev.sql";

    @Value("${url}")
    private String dataBaseUrl;
    @Value("${user}")
    private String dataBaseUserName;
    @Value("${password}")
    private String dataBasePassword;
    @Value("${driver}")
    private String dataBaseDriverClassName;
    @Value("${pool.size}")
    private int poolSize;

    @Bean
    public DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(dataBaseUrl);
        hikariDataSource.setUsername(dataBaseUserName);
        hikariDataSource.setPassword(dataBasePassword);
        hikariDataSource.setDriverClassName(dataBaseDriverClassName);
        hikariDataSource.setMaximumPoolSize(poolSize);
        return hikariDataSource;
    }

    @Bean
    @Profile("dev")
    public EmbeddedDatabase embeddedDatabase() {
        EmbeddedDatabaseBuilder databaseBuilder = new EmbeddedDatabaseBuilder();
        return databaseBuilder
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript(SQL_SETUP)
                .addScript(SQL_INIT_DEV)
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    @Profile("dev")
    public JdbcTemplate jdbcTemplate(EmbeddedDatabase embeddedDatabase) {
        return new JdbcTemplate(embeddedDatabase);
    }

    @Bean
    public RowMapper<Tag> tagRowMapper() {
        return new BeanPropertyRowMapper<>(Tag.class);
    }

    @Bean
    public RowMapper<GiftCertificate> giftCertificateRowMapper() {
        return new BeanPropertyRowMapper<>(GiftCertificate.class);
    }

    @Bean
    public RowMapper<TagToGiftCertificateRelation> tagToGiftCertificateRelationRowMapper() {
        return new BeanPropertyRowMapper<>(TagToGiftCertificateRelation.class);
    }
}
