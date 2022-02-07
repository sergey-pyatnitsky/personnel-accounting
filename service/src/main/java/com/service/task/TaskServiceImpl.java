package com.service.task;

import com.core.domain.Employee;
import com.core.domain.Project;
import com.core.domain.ReportCard;
import com.core.domain.Task;
import com.core.enums.TaskStatus;
import com.dao.report_card.ReportCardDAO;
import com.dao.task.TaskDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{
    private TaskDAO taskDAO;
    private ReportCardDAO reportCardDAO;

    @Autowired
    public void setTaskDAO(TaskDAO taskDAO){
        this.taskDAO = taskDAO;
    }

    @Autowired
    public void setReportCardDAO(ReportCardDAO reportCardDAO){
        this.reportCardDAO = reportCardDAO;
    }

    @Override
    @Transactional
    public Task changeTaskStatus(Task task, TaskStatus taskStatus) {
        task.setTaskStatus(taskStatus);
        return taskDAO.save(task);
    }

    @Override
    @Transactional
    public ReportCard assigneeTime(Employee assignee, Task task, Time workingTime) {
        return reportCardDAO.save(new ReportCard(
                new Date(System.currentTimeMillis()), task, assignee, new Time(4, 35, 0)));
    }

    @Override
    @Transactional
    public Task find(Long id) {
        return taskDAO.find(id);
    }

    @Override
    @Transactional
    public List<Task> findAll() {
        return taskDAO.findAll();
    }

    @Override
    @Transactional
    public Task findByName(String name) {
        return taskDAO.findByName(name);
    }

    @Override
    @Transactional
    public List<Task> findByProject(Project project) {
        return taskDAO.findByProject(project);
    }

    @Override
    @Transactional
    public List<Task> findByReporter(Employee reporter) {
        return taskDAO.findByReporter(reporter);
    }

    @Override
    @Transactional
    public List<Task> findByAssignee(Employee assignee) {
        return taskDAO.findByAssignee(assignee);
    }

    @Override
    @Transactional
    public List<Task> findByStatus(TaskStatus status) {
        return taskDAO.findByStatus(status);
    }

    @Override
    @Transactional
    public Task save(Task task) {
        return taskDAO.save(task);
    }

    @Override
    @Transactional
    public Task update(Task task) {
        return taskDAO.update(task);

    }

    @Override
    @Transactional
    public boolean remove(Task task) {
        return taskDAO.remove(task);
    }
}
