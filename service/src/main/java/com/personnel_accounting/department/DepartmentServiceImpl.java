package com.personnel_accounting.department;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.employee.EmployeeDAO;
import com.personnel_accounting.employee_position.EmployeePositionDAO;
import com.personnel_accounting.enums.TaskStatus;
import com.personnel_accounting.pagination.entity.PagingRequest;
import com.personnel_accounting.position.PositionDAO;
import com.personnel_accounting.project.ProjectDAO;
import com.personnel_accounting.task.TaskDAO;
import com.personnel_accounting.utils.ValidationUtil;
import com.personnel_accounting.validation.DepartmentValidator;
import com.personnel_accounting.validation.PositionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentDAO departmentDAO;

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    private EmployeePositionDAO employeePositionDAO;

    @Autowired
    private PositionDAO positionDAO;

    @Autowired
    private TaskDAO taskDAO;

    @Autowired
    private DepartmentValidator departmentValidator;

    @Autowired
    private PositionValidator positionValidator;

    @Override
    @Transactional
    public Department addDepartment(Department department) {
        ValidationUtil.validate(department, departmentValidator);
        return departmentDAO.findByName(department.getName())
                .stream().allMatch(obj -> obj.getEndDate() != null)
                ? departmentDAO.save(department)
                : department;
    }

    @Override
    @Transactional
    public boolean closeDepartment(Department department) {
        Department tempDepartment = departmentDAO.merge(department);
        if (tempDepartment.getStartDate() == null)
            return departmentDAO.remove(tempDepartment);
        else {
            List<Project> projects = projectDAO.findByDepartment(tempDepartment);
            if (projects.size() == 0 || projects.stream().noneMatch(obj -> obj.getEndDate() == null)) {
                tempDepartment.setEndDate(new Date(System.currentTimeMillis()));
                tempDepartment.setModifiedDate(new Date(System.currentTimeMillis()));
                departmentDAO.save(tempDepartment);
                return true;
            }
            return false;
        }
    }

    @Override
    public Department editDepartmentName(Department department, String name) {
        department.setName(name);
        department.setModifiedDate(new Date(System.currentTimeMillis()));
        ValidationUtil.validate(department, departmentValidator);
        return departmentDAO.save(department);
    }

    @Override
    public List<Project> findProjects(Department department) {
        return projectDAO.findByDepartment(department);
    }

    @Override
    public List<Project> findProjectsPaginated(PagingRequest pagingRequest, Department department) {
        return projectDAO.findByDepartmentPaginated(pagingRequest, department);
    }

    @Override
    public List<Employee> findEmployees(Department department) {
        return employeeDAO.findByDepartment(department);
    }

    @Override
    @Transactional
    public Employee assignToDepartment(Employee employee, Department department) {
        if (employee.getDepartment() == null) {
            employee.setDepartment(department);
            return employeeDAO.save(employee);
        } else if (!employee.getDepartment().getId().equals(department.getId())) {
            List<EmployeePosition> employeePositions = employeePositionDAO.findByEmployee(employee);
            employeePositions.forEach(obj -> obj.setActive(false));
            employeePositionDAO.save(employeePositions);

            employee.setDepartment(department);
            return employeeDAO.save(employee);
        }
        return employeeDAO.merge(employee);
    }

    @Override
    @Transactional
    public Department changeDepartmentState(Department department, boolean isActive) {
        List<Project> projects = projectDAO.findByDepartment(department);
        projects.forEach(obj -> obj.setActive(isActive));
        projectDAO.save(projects);

        List<Employee> employees = employeeDAO.findByDepartment(department);
        employees.forEach(obj -> obj.setActive(isActive));
        employeeDAO.save(employees);

        department.setActive(false);
        return departmentDAO.save(department);
    }

    @Override
    public Department find(Long id) {
        return departmentDAO.find(id);
    }

    @Override
    public List<Department> findByActive(boolean isActive) {
        return departmentDAO.findByActive(isActive);
    }

    @Override
    public List<Department> findAll(PagingRequest pagingRequest) {
        return departmentDAO.findAll(pagingRequest);
    }

    @Override
    public Long getDepartmentCount() {
        return departmentDAO.getDepartmentCount();
    }

    @Override
    public List<Department> findByName(String name) {
        return departmentDAO.findByName(name);
    }

    @Override
    public Department save(Department department) {
        ValidationUtil.validate(department, departmentValidator);
        return departmentDAO.save(department);
    }

    @Override
    public Department merge(Department department) {
        ValidationUtil.validate(department, departmentValidator);
        return departmentDAO.merge(department);
    }

    @Override
    public boolean remove(Department department) {
        return departmentDAO.remove(department);
    }

    @Override
    public boolean removeById(Long id) {
        return departmentDAO.removeById(id);
    }

    @Override
    @Transactional
    public boolean inactivate(Department department) {
        projectDAO.findByDepartment(department).forEach(project -> {
            taskDAO.findByProject(project).forEach(task -> task.setTaskStatus(TaskStatus.CLOSED));
            employeePositionDAO.findByProject(project).forEach(employeePositionDAO::inactivate);
            projectDAO.inactivate(project);
        });
        return departmentDAO.inactivate(department);
    }

    @Override
    @Transactional
    public boolean activate(Department department) {
        List<Project> projects = projectDAO.findByDepartment(department);
        projects.forEach(project -> {
            employeePositionDAO.findByProject(project).forEach(employeePositionDAO::activate);
            projectDAO.activate(project);
        });
        return departmentDAO.activate(department);
    }

    @Override
    @Transactional
    public Position addPosition(Position position) {
        ValidationUtil.validate(position, positionValidator);
        return positionDAO.findAll().stream().filter(obj -> obj.getName().equals(position.getName()))
                .findFirst().orElse(null) != null
                ? position
                : positionDAO.save(position);
    }

    @Override
    public List<Position> findAllPositions() {
        return positionDAO.findAll();
    }

    @Override
    public Position findPosition(Long id) {
        return positionDAO.find(id);
    }

    @Override
    public Position mergePosition(Position position) {
        return positionDAO.merge(position);
    }

    @Override
    public EmployeePosition addEmployeePosition(EmployeePosition employeePosition) {
        return employeePositionDAO.save(employeePosition);
    }

    @Override
    public EmployeePosition mergeEmployeePosition(EmployeePosition employeePosition) {
        return employeePositionDAO.merge(employeePosition);
    }

    @Override
    public Long getProjectsByDepartmentCount(Department department) {
        return projectDAO.getProjectsByDepartmentCount(department);
    }
}
