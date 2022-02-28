package com.personnel_accounting.configuration;

import com.personnel_accounting.entity.converter.toDTO.*;
import com.personnel_accounting.entity.converter.toDomain.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
@ComponentScan(basePackages = "com.personnel_accounting")
@Import(ServiceConfiguration.class)
@EnableWebMvc
public class ApplicationConfiguration extends WebMvcConfigurerAdapter implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new DepartmentConverter());
        registry.addConverter(new EmployeeConverter());
        registry.addConverter(new EmployeePositionConverter());
        registry.addConverter(new PositionConverter());
        registry.addConverter(new ProjectConverter());
        registry.addConverter(new ReportCardConverter());
        registry.addConverter(new TaskConverter());
        registry.addConverter(new UserConverter());
        registry.addConverter(new ProfileConverter());

        registry.addConverter(new DepartmentDTOConverter());
        registry.addConverter(new EmployeeDTOConverter());
        registry.addConverter(new EmployeePositionDTOConverter());
        registry.addConverter(new PositionDTOConverter());
        registry.addConverter(new ProjectDTOConverter());
        registry.addConverter(new ReportCardDTOConverter());
        registry.addConverter(new TaskDTOConverter());
        registry.addConverter(new UserDTOConverter());
        registry.addConverter(new ProfileDTOConverter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }
}
