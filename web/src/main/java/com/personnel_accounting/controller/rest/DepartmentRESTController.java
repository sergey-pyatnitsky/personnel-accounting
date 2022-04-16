package com.personnel_accounting.controller.rest;

import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import com.personnel_accounting.entity.dto.PositionDTO;
import com.personnel_accounting.entity.dto.ProjectDTO;
import com.personnel_accounting.exception.ActiveStatusDataException;
import com.personnel_accounting.exception.ExistingDataException;
import com.personnel_accounting.exception.NoSuchDataException;
import com.personnel_accounting.exception.OperationExecutionException;
import com.personnel_accounting.pagination.entity.Page;
import com.personnel_accounting.pagination.entity.PagingRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

@RestController
public class DepartmentRESTController {
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ConversionService conversionService;

    @PostMapping("/api/department/add")
    public ResponseEntity<?> addDepartment(@RequestBody DepartmentDTO departmentDTO) {
        Department department = conversionService.convert(departmentDTO, Department.class);
        department.setActive(false);
        department.setCreateDate(new Date(System.currentTimeMillis()));

        department = departmentService.addDepartment(department);
        if (department.getId() == null) throw new ExistingDataException("Данный отдел уже существует!");
        return new ResponseEntity<>(conversionService.convert(department, DepartmentDTO.class), HttpStatus.OK);
    }

    @PostMapping("/api/position/add")
    public ResponseEntity<?> addPosition(@RequestBody PositionDTO positionDTO) {
        Position position = departmentService.addPosition(conversionService.convert(positionDTO, Position.class));
        if (position.getId() == null) throw new ExistingDataException("Данная должность уже существует");
        return new ResponseEntity<>(position, HttpStatus.OK);
    }

    @GetMapping("/api/position/get_all")
    public ResponseEntity<?> getPositions() {
        List<Position> position = departmentService.findAllPositions();
        if (position.size() == 0) throw new NoSuchDataException("Данная должность уже существует");
        return new ResponseEntity<>(position, HttpStatus.OK);
    }

    @PostMapping("/api/department/get_all/open")
    public ResponseEntity<?> getAllOpenDepartments(@RequestBody PagingRequest pagingRequest) {
        return new ResponseEntity<>(getPage(departmentService.findAll(pagingRequest)
                .stream().filter(department -> department.getEndDate() == null).collect(Collectors.toList())
                .stream().map(department -> conversionService.convert(department, DepartmentDTO.class))
                .collect(Collectors.toList()), pagingRequest.getDraw(), departmentService.getDepartmentCount().intValue()),
                HttpStatus.OK);
    }

    @PostMapping("/api/department/get_all/closed")
    public ResponseEntity<?> getAllClosedDepartments(@RequestBody PagingRequest pagingRequest) {
        return new ResponseEntity<>(getPage(departmentService.findAll(pagingRequest)
                .stream().filter(department -> department.getEndDate() != null).collect(Collectors.toList())
                .stream().map(department -> conversionService.convert(department, DepartmentDTO.class))
                .collect(Collectors.toList()), pagingRequest.getDraw(), departmentService.getDepartmentCount().intValue()),
                HttpStatus.OK);
    }

    @PostMapping("/api/department/projects/open/{id}")
    public ResponseEntity<?> getAllOpenProjectsByDepartment(@RequestBody PagingRequest pagingRequest,
                                                            @PathVariable Long id) {
        Department department = departmentService.find(id);
        return new ResponseEntity<>(getPage(departmentService.findProjectsPaginated(pagingRequest, department)
                        .stream().filter(obj -> obj.getEndDate() == null).collect(Collectors.toList())
                        .stream().map(project -> conversionService.convert(project, ProjectDTO.class))
                        .collect(Collectors.toList()),
                pagingRequest.getDraw(), departmentService.getProjectsByDepartmentCount(department).intValue()),
                HttpStatus.OK);
    }

    @PutMapping("/api/department/activate/{id}")
    public ResponseEntity<?> activateDepartment(@PathVariable Long id) {
        if (!departmentService.activate(departmentService.find(id)))
            throw new OperationExecutionException("Ошибка активации отдела с ID " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/department/inactivate/{id}")
    public ResponseEntity<?> inactivateDepartment(@PathVariable Long id) {
        if (!departmentService.inactivate(departmentService.find(id)))
            throw new OperationExecutionException("Ошибка деактивации отдела с ID " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/department/edit/{id}")
    public ResponseEntity<?> editDepartment(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO) {
        Department department = departmentService.find(id);
        departmentService.editDepartmentName(department, departmentDTO.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/department/close/{id}")
    public ResponseEntity<?> closeDepartment(@PathVariable Long id) {
        Department department = departmentService.find(id);
        if (department.isActive())
            throw new ActiveStatusDataException("Данный отдел активен и недоступен  для закрытия");
        if (!departmentService.closeDepartment(departmentService.find(id)))
            throw new ActiveStatusDataException("В данном отделе существуют незакрытые проекты и недоступен  для закрытия!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Page<?> getPage(List<?> list, int draw, int count) {
        Page<?> page = new Page<>(list);
        page.setRecordsFiltered(count);
        page.setDraw(draw);
        page.setRecordsTotal(count);
        return page;
    }
}
