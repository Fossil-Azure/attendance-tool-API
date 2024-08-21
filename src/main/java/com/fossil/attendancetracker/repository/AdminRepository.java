package com.fossil.attendancetracker.repository;

import com.fossil.attendancetracker.model.MonthlyAttendance;
import com.fossil.attendancetracker.model.QtrAttendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AdminRepository extends MongoRepository<QtrAttendance, String> {

}
