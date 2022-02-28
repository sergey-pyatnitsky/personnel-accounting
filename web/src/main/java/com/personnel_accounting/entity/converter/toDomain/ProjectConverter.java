package com.personnel_accounting.entity.converter.toDomain;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.entity.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class ProjectConverter implements Converter<ProjectDTO, Project> {
    private ConversionService conversionService;

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Project convert(ProjectDTO source) {
        Project project = new Project();
        project.setId(source.getId());
        project.setName(source.getName());
        project.setDepartment(
                conversionService.convert(source.getDepartment(), Department.class));
        project.setActive(source.isActive());
        return project;
    }
}
