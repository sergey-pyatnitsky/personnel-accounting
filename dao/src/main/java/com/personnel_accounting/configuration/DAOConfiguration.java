package com.personnel_accounting.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("com.personnel_accounting")
@PropertySource("classpath:database.properties")
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class DAOConfiguration {
    private final Environment env;

    public DAOConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        /*HikariConfig hikariConfig = new HikariConfig();
        try {
            hikariConfig.setDataSource((DataSource) new JndiTemplate().lookup(env.getProperty("db.jndi.value")));
        } catch (NamingException e) {
            e.printStackTrace();
        }*/
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(env.getProperty("datasource.setDriverClassName"));
        hikariConfig.setJdbcUrl(System.getProperty("datasource.url"));
        hikariConfig.setUsername(System.getProperty("datasource.username"));
        hikariConfig.setPassword(System.getProperty("datasource.password"));
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
        Flyway.configure().dataSource(dataSource()).baselineOnMigrate(true).load().migrate();
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

