package com.fossil.attendancetracker.repository;

import com.fossil.attendancetracker.model.QtrAttendance;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

@Document(collection = "qtrAttendance")
public interface QtrAttendanceRepository extends MongoRepository<QtrAttendance, String> {

    List<QtrAttendance> findByEmailIdInAndQuarterAndYear(List<String> emailIds, String quarter, String year);
}
