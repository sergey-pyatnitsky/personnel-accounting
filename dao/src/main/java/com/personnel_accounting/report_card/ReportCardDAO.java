package com.personnel_accounting.report_card;

import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.ReportCard;
import com.personnel_accounting.domain.Task;

import java.sql.Date;
import java.util.List;

public interface ReportCardDAO {
    ReportCard find(Long id);
    List<ReportCard> findAll();
    List<ReportCard> findByDate(Date date);
    ReportCard findByTask(Task task);
    List<ReportCard> findByEmployee(Employee employee);

    ReportCard save(ReportCard reportCard);
    ReportCard merge(ReportCard reportCard);
    boolean removeById(Long id);
    boolean remove(ReportCard reportCard);
}
