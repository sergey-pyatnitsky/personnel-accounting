package com.service.department;

import com.core.domain.Department;
import com.dao.department.DepartmentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{
    private DepartmentDAO departmentDAO;

    @Autowired
    public void setDepartmentDAO(DepartmentDAO departmentDAO){
        this.departmentDAO = departmentDAO;
    }

    @Override
    @Transactional
    public Department find(Long id) {
        return departmentDAO.find(id);
    }

    @Override
    @Transactional
    public List<Department> findByActive(boolean isActive) {
        return departmentDAO.findByActive(isActive);
    }

    @Override
    @Transactional
    public List<Department> findAll() {
        return departmentDAO.findAll();
    }

    @Override
    @Transactional
    public Department findByName(String name) {
        return departmentDAO.findByName(name);
    }

    @Override
    @Transactional
    public Department save(Department department) {
        return departmentDAO.save(department);
    }

    @Override
    @Transactional
    public Department update(Department department) {
        return departmentDAO.update(department);
    }

    @Override
    @Transactional
    public boolean remove(Department department) {
        return departmentDAO.remove(department);
    }

    @Override
    @Transactional
    public boolean inactivate(Department department) {
        return departmentDAO.inactivate(department);
    }

    @Override
    @Transactional
    public boolean activate(Department department) {
        return departmentDAO.activate(department);
    }
}
