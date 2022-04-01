package com.personnel_accounting.department;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.EmployeePosition;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.domain.Project;
import com.personnel_accounting.employee.EmployeeDAO;
import com.personnel_accounting.employee_position.EmployeePositionDAO;
import com.personnel_accounting.enums.TaskStatus;
import com.personnel_accounting.position.PositionDAO;
import com.personnel_accounting.project.ProjectDAO;
import com.personnel_accounting.task.TaskDAO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentDAO departmentDAO;
    private final ProjectDAO projectDAO;
    private final EmployeeDAO employeeDAO;
    private final EmployeePositionDAO employeePositionDAO;
    private final PositionDAO positionDAO;
    private final TaskDAO taskDAO;

    public DepartmentServiceImpl(DepartmentDAO departmentDAO, ProjectDAO projectDAO,
                                 EmployeeDAO employeeDAO, EmployeePositionDAO employeePositionDAO,
                                 PositionDAO positionDAO, TaskDAO taskDAO) {
        this.departmentDAO = departmentDAO;
        this.projectDAO = projectDAO;
        this.employeeDAO = employeeDAO;
        this.employeePositionDAO = employeePositionDAO;
        this.positionDAO = positionDAO;
        this.taskDAO = taskDAO;
    }

    @Override //TODO test
    public Department addDepartment(Department department) {
        return departmentDAO.findByName(department.getName())
                .stream().allMatch(obj -> obj.getEndDate() != null)
                ? departmentDAO.save(department)
                : department;
    }

    @Override //TODO test
    public boolean closeDepartment(Department department) {
        Department tempDepartment = departmentDAO.update(department);
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
    public List<Project> findProjects(Department department) {
        return projectDAO.findByDepartment(department);
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
        return employeeDAO.update(employee);
    }

    @Override
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
    public List<Department> findAll() {
        return departmentDAO.findAll();
    }

    @Override
    public List<Department> findByName(String name) {
        return departmentDAO.findByName(name);
    }

    @Override
    public Department save(Department department) {
        return departmentDAO.save(department);
    }

    @Override
    public Department update(Department department) {
        return departmentDAO.update(department);
    }

    @Override
    public boolean remove(Department department) {
        return departmentDAO.remove(department);
    }

    @Override
    public boolean removeById(Long id) {
        return departmentDAO.removeById(id);
    }

    @Override //TODO test
    public boolean inactivate(Department department) {
        projectDAO.findByDepartment(department).forEach(project -> {
            taskDAO.findByProject(project).forEach(task -> task.setTaskStatus(TaskStatus.CLOSED));
            employeePositionDAO.findByProject(project).forEach(employeePositionDAO::inactivate);
            projectDAO.inactivate(project);
        });
        return departmentDAO.inactivate(department);
    }

    @Override //TODO test
    public boolean activate(Department department) {
        List<Project> projects = projectDAO.findByDepartment(department);
        projects.forEach(project -> {
            employeePositionDAO.findByProject(project).forEach(employeePositionDAO::activate);
            projectDAO.activate(project);
        });
        return departmentDAO.activate(department);
    }

    @Override //TODO test
    public Position addPosition(Position position) {
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
        return positionDAO.update(position);
    }

    @Override
    public EmployeePosition addEmployeePosition(EmployeePosition employeePosition) {
        return employeePositionDAO.save(employeePosition);
    }

    @Override
    public EmployeePosition mergeEmployeePosition(EmployeePosition employeePosition) {
        return employeePositionDAO.update(employeePosition);
    }
}
