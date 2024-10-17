package com.fossil.attendancetracker.repository;

import com.fossil.attendancetracker.model.ApprovalList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalListRepository extends MongoRepository<ApprovalList, String> {
    List<ApprovalList> findByRaisedByAndYearAndMonthAndStatus(String raisedBy, String year, String month, String status);
}
