package com.personnel_accounting.controller.restController;

import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import com.personnel_accounting.entity.dto.PositionDTO;
import com.personnel_accounting.entity.dto.ProjectDTO;
import com.personnel_accounting.exeption.ActiveStatusDataException;
import com.personnel_accounting.exeption.ExistingDataException;
import com.personnel_accounting.exeption.NoSuchDataException;
import com.personnel_accounting.exeption.OperationExecutionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

@RestController
public class DepartmentRESTController {
    private final DepartmentService departmentService;
    private final ConversionService conversionService;

    public DepartmentRESTController(DepartmentService departmentService, ConversionService conversionService) {
        this.departmentService = departmentService;
        this.conversionService = conversionService;
    }

    @PostMapping("/api/department/add")
    public ResponseEntity<?> addDepartment(@RequestBody DepartmentDTO departmentDTO) {
        Department department = conversionService.convert(departmentDTO, Department.class);
        department.setActive(false);
        department.setCreateDate(new Date(System.currentTimeMillis()));

        department = departmentService.addDepartment(department);
        if(department.getId() == null) throw new ExistingDataException("Данный отдел уже существует!");
        return new ResponseEntity<>(conversionService.convert(department, DepartmentDTO.class), HttpStatus.OK);
    }

    @PostMapping("/api/position/add")
    public ResponseEntity<?> addPosition(@RequestBody PositionDTO positionDTO) {
        Position position = departmentService.addPosition(conversionService.convert(positionDTO, Position.class));
        if(position.getId() == null) throw new ExistingDataException("Данная должность уже существует");
        return new ResponseEntity<>(position, HttpStatus.OK);
    }

    @GetMapping("/api/position/get_all")
    public ResponseEntity<?> getPositions() {
        List<Position> position = departmentService.findAllPositions();
        if(position.size() == 0) throw new NoSuchDataException("Данная должность уже существует");
        return new ResponseEntity<>(position, HttpStatus.OK);
    }

    @GetMapping("/api/department/get_all/open")
    public ResponseEntity<?> getAllOpenDepartments() {
        List<DepartmentDTO> departmentDTOS = departmentService.findAll()
                .stream().filter(department -> department.getEndDate() == null).collect(Collectors.toList())
                .stream().map(department -> conversionService.convert(department, DepartmentDTO.class))
                .collect(Collectors.toList());
        if(departmentDTOS.size() == 0) throw new NoSuchDataException("Список открытых отделов пуст");
        return new ResponseEntity<>(departmentDTOS, HttpStatus.OK);
    }

    @GetMapping("/api/department/get_all/closed")
    public ResponseEntity<?> getAllClosedDepartments() {
        List<DepartmentDTO> departmentDTOS = departmentService.findAll()
                .stream().filter(department -> department.getEndDate() != null).collect(Collectors.toList())
                .stream().map(department -> conversionService.convert(department, DepartmentDTO.class))
                .collect(Collectors.toList());
        if(departmentDTOS.size() == 0) throw new NoSuchDataException("Список закрытых отделов пуст");
        return new ResponseEntity<>(departmentDTOS, HttpStatus.OK);
    }

    @GetMapping("/api/department/projects/open/{id}")
    public ResponseEntity<?> getAllOpenProjectsByDepartment(@PathVariable Long id) {
        List<ProjectDTO> projectList = departmentService.findProjects(departmentService.find(id))
                .stream().filter(obj -> obj.getEndDate() == null).collect(Collectors.toList())
                .stream().map(project -> conversionService.convert(project, ProjectDTO.class)).collect(Collectors.toList());
        if(projectList.size() == 0) throw new NoSuchDataException("В данном отделе отсутствуют открытые проекты");
        return new ResponseEntity<>(projectList, HttpStatus.OK);
    }

    @PutMapping("/api/department/activate/{id}")
    public ResponseEntity<?> activateDepartment(@PathVariable Long id) {
        if(!departmentService.activate(departmentService.find(id)))
            throw new OperationExecutionException("Ошибка активации отдела с ID " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/department/inactivate/{id}")
    public ResponseEntity<?> inactivateDepartment(@PathVariable Long id) {
        if(!departmentService.inactivate(departmentService.find(id)))
            throw new OperationExecutionException("Ошибка деактивации отдела с ID " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/department/edit/{id}")
    public ResponseEntity<?> editDepartment(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO) {
        Department department = departmentService.find(id);
        department.setName(departmentDTO.getName());
        department.setModifiedDate(new Date(System.currentTimeMillis()));
        departmentService.save(department);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/department/close/{id}")
    public ResponseEntity<?> closeDepartment(@PathVariable Long id) {
        Department department = departmentService.find(id);
        if (department.isActive())
            throw new ActiveStatusDataException("Данный отдел активен и недоступен  для закрытия");
        if(!departmentService.closeDepartment(departmentService.find(id)))
            throw new ActiveStatusDataException("В данном отделе существуют незакрытые проекты и недоступен  для закрытия!");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
