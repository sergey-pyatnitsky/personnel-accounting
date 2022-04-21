package com.personnel_accounting.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(value = "com.personnel_accounting",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {DAOConfiguration.class})})
@PropertySource("classpath:database.properties")
@EnableTransactionManagement
public class DAOTestConfiguration {
    private final Environment env;

    public DAOTestConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(env.getProperty("dataSource.driverClassName"));
        hikariConfig.setJdbcUrl(env.getProperty("dataSource.url"));
        hikariConfig.setUsername(env.getProperty("dataSource.username"));
        hikariConfig.setPassword(env.getProperty("dataSource.password"));
        hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts",
                env.getProperty("hikari.dataSource.cachePrepStmts"));
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize",
                env.getProperty("hikari.dataSource.prepStmtCacheSize"));
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit",
                env.getProperty("hikari.prepStmtCacheSqlLimit"));
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public void migrateFlyway() {
        Flyway.configure().locations("classpath:test.db.migration").dataSource(dataSource()).baselineOnMigrate(true).load().migrate();
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory =
                new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(env.getProperty("hb.packages.to.scan"));

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hb.hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hb.hibernate.show_sql"));
        hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans", env.getProperty("hb.enable_lazy_load_no_trans"));
        sessionFactory.setHibernateProperties(hibernateProperties);

        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}
