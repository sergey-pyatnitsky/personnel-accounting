package com.personnel_accounting.entity.converter.toDomain;

import com.personnel_accounting.domain.Project;
import com.personnel_accounting.entity.dto.ProjectDTO;
import org.springframework.core.convert.converter.Converter;

import java.sql.Date;

public class ProjectConverter implements Converter<ProjectDTO, Project> {

    @Override
    public Project convert(ProjectDTO source) {
        Project project = new Project();
        project.setId(source.getId());
        project.setName(source.getName());
        project.setActive(source.isActive());
        if (source.getStart_date() != null)
            project.setStartDate(Date.valueOf(source.getStart_date()));
        if (source.getEnd_date() != null)
            project.setEndDate(Date.valueOf(source.getEnd_date()));
        return project;
    }
}
