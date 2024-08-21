package com.fossil.attendancetracker.controller;

import com.fossil.attendancetracker.model.Users;
import com.fossil.attendancetracker.repository.SearchRepository;
import com.fossil.attendancetracker.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@CrossOrigin(origins = {
        "https://attendance-tracker-gbs.azurewebsites.net",
        "http://localhost:4200"
})
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    UsersRepository usersRepo;

    @Autowired
    SearchRepository searchRepo;

    @GetMapping(value = "/allUsers")
    public List<Users> getAllUsers() {
        return usersRepo.findAll();
    }

    @GetMapping(value = "/searchByEmail/{email}")
    public List<Users> searchUserByEmail(@PathVariable String email) {
        return searchRepo.findUserbyEmail(email);
    }

    @GetMapping(value = "/searchByName/{name}")
    public List<Users> searchUserByName(@PathVariable String name) {
        return searchRepo.findUserbyName(name);
    }

    @PostMapping(value = "/addUser")
    public ResponseEntity<?> addUser(@RequestBody Users user) {
        return searchRepo.createUser(user);
    }

    @GetMapping(value = "/distTeams")
    public List<String> findDistTeam() {
        return searchRepo.findAllDistinctTeams();
    }

    @GetMapping(value = "/distSubDepartment")
    public List<String> findDistSubDepartment() {
        return searchRepo.findAllDistinctSubDept();
    }

    @GetMapping(value = "/distDepartment")
    public List<String> findDistDepartment() {
        return searchRepo.findAllDistinctDepartment();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> checkUserLogin(@RequestBody Users cred) {
        try {
            if (cred.getEmailId() == null || cred.getPassword() == null) {
                return new ResponseEntity<>("Username or password is missing", HttpStatus.BAD_REQUEST);
            }
            return searchRepo.checkUserCred(cred);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/subordinates")
    public List<Users> getSubordinates(@RequestBody Users managerId) {
        return searchRepo.getSubordinates(managerId);
    }

    @PostMapping("/resetPassword")
    public Users resetUserPassword(@RequestBody Users user) {
        return searchRepo.resetUsersPassword(user);
    }

    @PostMapping("/getLeaves")
    public double getUserLeave(@RequestBody Users user) {
        return searchRepo.getUserLeave(user);
    }

    @PostMapping("/updateLeaves")
    public double updateUserLeave(@RequestBody Users user) {
        return searchRepo.updateUserLeave(user);
    }
}
