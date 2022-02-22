package com.web.entity.converter;

import com.core.domain.Department;
import com.core.domain.Project;
import com.service.project.ProjectService;
import com.web.entity.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class ProjectConverter implements Converter<ProjectDTO, Project> {
    private ConversionService conversionService;
    private ProjectService projectService;

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public Project convert(ProjectDTO source) {
        Project project = new Project();
        project.setId(source.getId());
        project.setName(source.getName());
        project.setDepartment(
                conversionService.convert(source.getDepartment(), Department.class));
        project.setActive(source.isActive());
        return projectService.update(project);
    }
}
