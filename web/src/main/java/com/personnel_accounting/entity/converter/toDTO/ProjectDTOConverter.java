package com.personnel_accounting.entity.converter.toDTO;

import com.personnel_accounting.domain.Project;
import com.personnel_accounting.entity.dto.ProjectDTO;
import org.springframework.core.convert.converter.Converter;

public class ProjectDTOConverter implements Converter<Project, ProjectDTO> {

    @Override
    public ProjectDTO convert(Project source) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(source.getId());
        projectDTO.setName(source.getName());
        projectDTO.setActive(source.isActive());
        if(source.getStartDate() != null)
            projectDTO.setStart_date(source.getStartDate().toString());
        if(source.getEndDate() != null)
            projectDTO.setEnd_date(source.getEndDate().toString());
        return projectDTO;
    }
}
