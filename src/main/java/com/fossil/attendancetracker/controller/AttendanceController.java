package com.fossil.attendancetracker.controller;

import com.fossil.attendancetracker.model.Attendance;
import com.fossil.attendancetracker.model.AttendanceRequest;
import com.fossil.attendancetracker.model.MonthlyAttendance;
import com.fossil.attendancetracker.model.QtrAttendance;
import com.fossil.attendancetracker.repository.AdminMethodsRepository;
import com.fossil.attendancetracker.repository.AttendanceRepository;
import com.fossil.attendancetracker.repository.DateWiseRepository;
import com.fossil.attendancetracker.repository.QtrAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {
        "https://attendance-tracker-gbs.azurewebsites.net",
        "http://localhost:4200"
})
public class AttendanceController {

    @Autowired
    AttendanceRepository attendanceRepo;

    @Autowired
    DateWiseRepository dateWiseRepository;

    @Autowired
    AdminMethodsRepository adminMethodsRepository;

    @Autowired
    QtrAttendanceRepository qtrAttendanceRepository;

    @GetMapping(value = "/allAttendance")
    public List<Attendance> getAllUsers() {
        return attendanceRepo.findAll();
    }

    @PostMapping(value = "/addAttendance")
    public ResponseEntity<?> addUser(@RequestBody Attendance attendance) {
        return dateWiseRepository.findByUserId(attendance);
    }

    @PostMapping(value = "/addUserAttendance")
    public ResponseEntity<?> addUserAttendance(@RequestBody QtrAttendance attendance) {
        return dateWiseRepository.addUserAttendance(attendance);
    }

    @PostMapping(value = "/addMonthlyAttendance")
    public ResponseEntity<?> addUserMonthlyAttendance(@RequestBody MonthlyAttendance monthlyAttendance) {
        return adminMethodsRepository.addUserMonthlyAttendance(monthlyAttendance);
    }

    @PostMapping(value = "/getUserAttendance")
    public QtrAttendance getUserAttendance(@RequestBody QtrAttendance qtrAttendance) {
        return dateWiseRepository.getUserAttendance(qtrAttendance);
    }

    @PostMapping(value = "/detailedAttendanceQtr")
    public List<Attendance> getDetailedUserAttendanceQtr(@RequestBody Attendance attendance) {
        return attendanceRepo.findByEmailIdAndQuarterAndYear(attendance.getEmailId(), attendance.getQuarter(), attendance.getYear());
    }

    @PostMapping(value = "/detailedAttendance")
    public List<Attendance> getDetailedUserAttendance(@RequestBody Attendance attendance) {
        return attendanceRepo.findByEmailIdAndQuarterAndYearAndMonth(attendance.getEmailId(), attendance.getQuarter(), attendance.getYear(), attendance.getMonth());
    }

    @PostMapping(value = "/attendanceCategory")
    public List<Attendance> getAttendanceCategory(@RequestBody Attendance attendance) {
        return dateWiseRepository.findByAttendanceType(attendance.getEmailId(), attendance.getQuarter(), attendance.getYear(), attendance.getAttendance());
    }

    @PostMapping(value = "/checkAttendance")
    public ResponseEntity<?> findAttendanceById(@RequestBody Attendance attendance) {
        return dateWiseRepository.findAttendanceById(attendance);
    }

    @PostMapping(value = "/todayAttendance")
    public List<Attendance> findByEmailIdInAndDate(@RequestBody AttendanceRequest attendanceRequest) {
        return attendanceRepo.findByEmailIdInAndDate(attendanceRequest.getEmailIds(), attendanceRequest.getDate());
    }

    @PostMapping(value = "/qtrAttendance")
    public List<QtrAttendance> findByEmailIdAndQuarterAndYear(@RequestBody AttendanceRequest attendanceRequest) {
        return qtrAttendanceRepository.findByEmailIdInAndQuarterAndYear(attendanceRequest.getEmailIds(), attendanceRequest.getQuarter(), attendanceRequest.getYear());
    }

    @PostMapping(value = "/upcomingLeaves")
    public List<Attendance> getUpcomingLeaves() {
        return dateWiseRepository.getUpcomingLeaves();
    }

    @PostMapping(value = "/calendarView")
    public List<Attendance> findByEmailIdAndYearAndMonth(@RequestBody Attendance attendance) {
        return attendanceRepo.findByEmailIdAndYearAndMonth(attendance.getEmailId(), attendance.getYear(), attendance.getMonth());
    }
}
