package com.fossil.attendancetracker.controller;

import com.fossil.attendancetracker.model.Attendance;
import com.fossil.attendancetracker.model.MonthlyAttendance;
import com.fossil.attendancetracker.model.Users;
import com.fossil.attendancetracker.repository.AdminMethodsRepository;
import com.fossil.attendancetracker.repository.AttendanceRepository;
import com.fossil.attendancetracker.repository.SearchRepository;
import com.fossil.attendancetracker.repository.UsersRepository;
import com.fossil.attendancetracker.service.ExcelGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@CrossOrigin(origins = {
        "https://attendance-tracker-gbs.azurewebsites.net",
        "http://localhost:4200"
})
public class ExcelSheetController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AdminMethodsRepository adminMethodsRepository;

    @Autowired
    private ExcelGeneratorService excelGeneratorService;

    @Autowired
    private SearchRepository searchRepository;

    @GetMapping("/download/excel")
    public ResponseEntity<InputStreamResource> downloadExcel(@RequestParam int year, @RequestParam int month, @RequestParam Users user) {
        List<Users> users = searchRepository.getSubordinates(user);
        List<Attendance> attendances = attendanceRepository.findAll();
        List<MonthlyAttendance> monthlyAttendances = adminMethodsRepository.getAllUserMthAttendance(String.valueOf(month), String.valueOf(year));

        ByteArrayInputStream in = excelGeneratorService.generateExcel(users, attendances, year, month, monthlyAttendances);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=attendance.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }
}
