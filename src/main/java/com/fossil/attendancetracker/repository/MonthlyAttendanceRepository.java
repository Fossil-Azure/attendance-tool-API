package com.fossil.attendancetracker.repository;

import com.fossil.attendancetracker.model.MonthlyAttendance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MonthlyAttendanceRepository extends MongoRepository<MonthlyAttendance, String> {

    List<MonthlyAttendance> findByYearAndQuarterAndEmailId(String year, String quarter, String emailId);
}
