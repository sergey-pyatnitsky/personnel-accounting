package com.personnel_accounting.entity.converter.toDomain;

import com.personnel_accounting.domain.Project;
import com.personnel_accounting.entity.dto.ProjectDTO;
import org.springframework.core.convert.converter.Converter;

public class ProjectConverter implements Converter<ProjectDTO, Project> {

    @Override
    public Project convert(ProjectDTO source) {
        Project project = new Project();
        project.setId(source.getId());
        project.setName(source.getName());
        project.setActive(source.isActive());
        return project;
    }
}
