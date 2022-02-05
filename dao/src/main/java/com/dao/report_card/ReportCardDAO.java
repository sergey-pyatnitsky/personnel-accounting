package com.dao.report_card;

import com.core.domain.Employee;
import com.core.domain.ReportCard;
import com.core.domain.Task;

import java.sql.Date;
import java.util.List;

public interface ReportCardDAO {
    ReportCard find(Long id);
    List<ReportCard> findAll();
    List<ReportCard> findByDate(Date date);
    ReportCard findByTask(Task task);
    List<ReportCard> findByEmployee(Employee employee);

    ReportCard save(ReportCard reportCard);
    ReportCard update(ReportCard reportCard);
    boolean removeById(Long id);
    boolean remove(ReportCard reportCard);
}
