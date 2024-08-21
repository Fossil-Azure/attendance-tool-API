package com.fossil.attendancetracker.repository;

import com.fossil.attendancetracker.model.ApprovalList;
import com.fossil.attendancetracker.model.MonthlyAttendance;
import com.fossil.attendancetracker.model.QtrAttendance;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminMethodsRepository {

    List<QtrAttendance> getAllUserAttendance(String quarter, String year);

    List<MonthlyAttendance> getAllUserMthAttendance(String month, String year);

    List<String> getAllDistinctYears();

    List<String> getAllDistinctQuarters();

    ResponseEntity<?> addUserMonthlyAttendance(MonthlyAttendance attendance);

    List<String> getAllDistinctMonths();

    MonthlyAttendance getUserMonthlyAttendance(MonthlyAttendance monthlyAttendance);

    List<MonthlyAttendance> getUserMonthlyAttendanceQtr(MonthlyAttendance monthlyAttendance);

    ApprovalList getApprovalList(String raisedBy, String raisedTo);

    String saveApprovalList(ApprovalList approvalList);

    String updateAttendanceData(ApprovalList approvalList);
}
