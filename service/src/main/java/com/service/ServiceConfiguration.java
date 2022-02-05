package com.service;

import com.dao.configuration.DAOConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.service")
@Import(DAOConfiguration.class)
public class ServiceConfiguration {
}
