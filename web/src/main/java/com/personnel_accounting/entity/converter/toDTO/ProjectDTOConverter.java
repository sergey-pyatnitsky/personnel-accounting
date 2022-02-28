package com.personnel_accounting.entity.converter.toDTO;

import com.personnel_accounting.domain.Project;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import com.personnel_accounting.entity.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class ProjectDTOConverter implements Converter<Project, ProjectDTO> {
    private ConversionService conversionService;

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public ProjectDTO convert(Project source) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(source.getId());
        projectDTO.setName(source.getName());
        projectDTO.setDepartment(
                conversionService.convert(source.getDepartment(), DepartmentDTO.class));
        projectDTO.setActive(source.isActive());
        return projectDTO;
    }
}
