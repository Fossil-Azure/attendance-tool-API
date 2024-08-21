package com.fossil.attendancetracker.repository;

import com.fossil.attendancetracker.model.Attendance;
import com.fossil.attendancetracker.model.QtrAttendance;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DateWiseRepository {

    ResponseEntity<?> findByUserId(Attendance attendance);

    ResponseEntity<?> findAttendanceById(Attendance attendance);

    ResponseEntity<?> addUserAttendance(QtrAttendance attendance);

    QtrAttendance getUserAttendance(QtrAttendance qtrAttendance);

    List<Attendance> findByAttendanceType(String emailId, String quarter, String year, String attendance);

    List<Attendance> getUpcomingLeaves();

}
