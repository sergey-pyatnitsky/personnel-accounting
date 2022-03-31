package com.personnel_accounting.controller.RESTController;

import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import com.personnel_accounting.entity.dto.PositionDTO;
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
        return department.getId() == null
                ? new ResponseEntity<>(HttpStatus.LOCKED)
                : new ResponseEntity<>(conversionService.convert(department, DepartmentDTO.class), HttpStatus.OK);
    }

    @PostMapping("/api/position/add")
    public ResponseEntity<?> addPosition(@RequestBody PositionDTO positionDTO) {
        Position position = departmentService.addPosition(conversionService.convert(positionDTO, Position.class));
        return position.getId() == null
                ? new ResponseEntity<>(HttpStatus.LOCKED)
                : new ResponseEntity<>(position, HttpStatus.OK);
    }

    @GetMapping("/api/department/get_all/open")
    public ResponseEntity<?> getAllOpenDepartments() {
        return new ResponseEntity<>(
                departmentService.findAll()
                        .stream().filter(department -> department.getEndDate() == null).collect(Collectors.toList())
                        .stream().map(department -> conversionService.convert(department, DepartmentDTO.class))
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/api/department/get_all/closed")
    public ResponseEntity<?> getAllClosedDepartments() {
        return new ResponseEntity<>(
                departmentService.findAll()
                        .stream().filter(department -> department.getEndDate() != null).collect(Collectors.toList())
                        .stream().map(department -> conversionService.convert(department, DepartmentDTO.class))
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PutMapping("/api/department/activate/{id}")
    public ResponseEntity<?> activateDepartment(@PathVariable Long id) {
        return departmentService.activate(departmentService.find(id))
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/api/department/inactivate/{id}")
    public ResponseEntity<?> inactivateDepartment(@PathVariable Long id) {
        return departmentService.inactivate(departmentService.find(id))
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
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
        if (department.isActive()) return new ResponseEntity<>(HttpStatus.LOCKED);
        return departmentService.closeDepartment(departmentService.find(id))
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
