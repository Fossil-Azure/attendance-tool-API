package com.fossil.attendancetracker.repository;

import com.fossil.attendancetracker.model.Attendance;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

@Document(collection = "attendance")
public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    List<Attendance> findByEmailIdAndQuarterAndYearAndMonth(String emailId, String quarter, String year, String month);

    List<Attendance> findByEmailIdAndQuarterAndYear(String emailId, String quarter, String year);

    List<Attendance> findByEmailIdInAndDate(List<String> emailIds, String date);

    List<Attendance> findByEmailIdAndYearAndMonth(String emailId, String year, String month);
}
