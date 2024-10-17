package com.fossil.attendancetracker.controller;

import com.fossil.attendancetracker.model.ApprovalList;
import com.fossil.attendancetracker.repository.ApprovalListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {
        "https://attendance-tracker-gbs.azurewebsites.net",
        "http://localhost:4200"
})
public class ApprovalListController {

    @Autowired
    ApprovalListRepository approvalListRepository;

    @PostMapping(value = "/pendingReq")
    public List<ApprovalList> getPendingApprovals(@RequestBody ApprovalList approvalList) {
        String raisedBy = approvalList.getRaisedBy();  // Changed from emailId to raisedBy
        String year = approvalList.getYear();
        String month = approvalList.getMonth();
        return approvalListRepository.findByRaisedByAndYearAndMonthAndStatus(raisedBy, year, month, "Pending");
    }
}
