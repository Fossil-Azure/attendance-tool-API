package com.fossil.attendancetracker.repository;

import com.fossil.attendancetracker.model.QtrAttendance;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

@Document(collection = "qtrAttendance")
public interface Attendance2Repository extends MongoRepository<QtrAttendance, String> {

}
