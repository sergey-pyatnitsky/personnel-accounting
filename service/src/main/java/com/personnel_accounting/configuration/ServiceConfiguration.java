package com.personnel_accounting.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.personnel_accounting")
@Import(DAOConfiguration.class)
public class ServiceConfiguration {
}
