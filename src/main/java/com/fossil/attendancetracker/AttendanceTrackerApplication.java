package com.fossil.attendancetracker;

import com.fossil.attendancetracker.service.LeaveUpdateScheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AttendanceTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceTrackerApplication.class, args);
    }

}
