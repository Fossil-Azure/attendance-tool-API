package com.fossil.attendancetracker.repository;

import com.fossil.attendancetracker.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UsersRepository extends MongoRepository<Users, String> {

    List<Users> findByManagerId(String managerId);

}
