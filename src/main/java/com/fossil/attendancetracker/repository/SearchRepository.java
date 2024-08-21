package com.fossil.attendancetracker.repository;

import com.fossil.attendancetracker.model.Users;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SearchRepository {

    List<Users> findUserbyEmail(String text);

    List<Users> findUserbyName(String text);

    List<String> findAllDistinctDepartment();

    List<String> findAllDistinctSubDept();

    List<String> findAllDistinctTeams();

    ResponseEntity<?> checkUserCred(Users user);

    ResponseEntity<?> createUser(Users user);

    List<Users> getSubordinates(Users managerId);

    Users resetUsersPassword(Users user);

    double getUserLeave(Users user);

    double updateUserLeave(Users user);
}
