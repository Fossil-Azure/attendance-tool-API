package com.fossil.attendancetracker.repositoryImpl;

import com.fossil.attendancetracker.controller.UsersController;
import com.fossil.attendancetracker.model.Attendance;
import com.fossil.attendancetracker.model.QtrAttendance;
import com.fossil.attendancetracker.repository.Attendance2Repository;
import com.fossil.attendancetracker.repository.AttendanceRepository;
import com.fossil.attendancetracker.repository.DateWiseRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class QtrAttendanceRepositoryImpl implements DateWiseRepository {

    private static final Logger logger = LoggerFactory.getLogger(QtrAttendanceRepositoryImpl.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");

    @Autowired
    MongoClient client;
    @Autowired
    AttendanceRepository attendanceRepo;
    @Autowired
    Attendance2Repository attendance2Repository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private static QtrAttendance setAttendanceForNewUser(QtrAttendance attendance) {
        QtrAttendance qtrAttendance = new QtrAttendance();
        qtrAttendance.setId(attendance.getId());
        qtrAttendance.setEmailId(attendance.getEmailId());
        qtrAttendance.setQuarter(attendance.getQuarter());
        qtrAttendance.setYear(attendance.getYear());
        qtrAttendance.setName(attendance.getName());

        switch (attendance.getAttendance()) {
            case "Work From Home" -> {
                qtrAttendance.setWfh(1);
                qtrAttendance.setWfo(0);
                qtrAttendance.setWfhFriday(0);
                qtrAttendance.setWfoFriday(0);
                qtrAttendance.setHolidays(0);
                qtrAttendance.setLeaves(0);
            }
            case "Work From Office" -> {
                qtrAttendance.setWfh(0);
                qtrAttendance.setWfo(1);
                qtrAttendance.setWfhFriday(0);
                qtrAttendance.setWfoFriday(0);
                qtrAttendance.setHolidays(0);
                qtrAttendance.setLeaves(0);
            }
            case "Public Holiday" -> {
                qtrAttendance.setWfh(0);
                qtrAttendance.setWfo(0);
                qtrAttendance.setWfhFriday(0);
                qtrAttendance.setWfoFriday(0);
                qtrAttendance.setHolidays(1);
                qtrAttendance.setLeaves(0);
            }
            case "Leave" -> {
                qtrAttendance.setWfh(0);
                qtrAttendance.setWfo(0);
                qtrAttendance.setWfhFriday(0);
                qtrAttendance.setWfoFriday(0);
                qtrAttendance.setHolidays(0);
                qtrAttendance.setLeaves(1);
            }
            case "Work From Office - Friday" -> {
                qtrAttendance.setWfh(0);
                qtrAttendance.setWfo(0);
                qtrAttendance.setWfhFriday(0);
                qtrAttendance.setWfoFriday(1);
                qtrAttendance.setHolidays(0);
                qtrAttendance.setLeaves(0);
            }
            case "Work From Home - Friday" -> {
                qtrAttendance.setWfh(0);
                qtrAttendance.setWfo(0);
                qtrAttendance.setWfhFriday(1);
                qtrAttendance.setWfoFriday(0);
                qtrAttendance.setHolidays(0);
                qtrAttendance.setLeaves(0);
            }
        }
        return qtrAttendance;
    }

    @Override
    public ResponseEntity<?> findAttendanceById(Attendance attendance) {
        MongoDatabase database = client.getDatabase("digital-GBS");
        MongoCollection<Document> collection = database.getCollection("attendance");
        Document query = new Document("_id", attendance.getId());
        Document found = collection.find(query).first();

        if (found != null) {
            return ResponseEntity.ok(Map.of("status", "Exist"));
        } else {
            return ResponseEntity.ok(Map.of("status", "Not Exist"));
        }
    }

    @Override
    public ResponseEntity<?> findByUserId(Attendance attendance) {
        MongoDatabase database = client.getDatabase("digital-GBS");
        MongoCollection<Document> collection = database.getCollection("attendance");
        Document query = new Document("_id", attendance.getId());
        Document found = collection.find(query).first();

        if (found != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Attendance record already exists for this user.");
        } else {
            Attendance savedAttendance = attendanceRepo.save(attendance);
            return ResponseEntity.ok(savedAttendance);
        }
    }

    @Override
    public ResponseEntity<?> addUserAttendance(QtrAttendance attendance) {
        MongoDatabase database = client.getDatabase("digital-GBS");
        MongoCollection<Document> collection2 = database.getCollection("qtrAttendance");
        Document query = new Document("_id", attendance.getId());
        Document found = collection2.find(query).first();
        if (found != null) {
            String input = attendance.getAttendance();
            switch (input) {
                case "Work From Home - Friday" -> {
                    int newWfhCount = found.getInteger("wfhFriday", 0) + 1;
                    Document update = new Document("$set", new Document("wfhFriday", newWfhCount));
                    collection2.updateOne(query, update);
                    found.put("wfhFriday", newWfhCount);
                }
                case "Work From Office - Friday" -> {
                    int newWfhCount = found.getInteger("wfoFriday", 0) + 1;
                    Document update = new Document("$set", new Document("wfoFriday", newWfhCount));
                    collection2.updateOne(query, update);
                    found.put("wfoFriday", newWfhCount);
                }
                case "Leave" -> {
                    int newWfhCount = found.getInteger("leaves", 0) + 1;
                    Document update = new Document("$set", new Document("leaves", newWfhCount));
                    collection2.updateOne(query, update);
                    found.put("leaves", newWfhCount);
                }
                case "Public Holiday" -> {
                    int newWfhCount = found.getInteger("holidays", 0) + 1;
                    Document update = new Document("$set", new Document("holidays", newWfhCount));
                    collection2.updateOne(query, update);
                    found.put("holidays", newWfhCount);
                }
                case "Work From Office" -> {
                    int newWfhCount = found.getInteger("wfo", 0) + 1;
                    Document update = new Document("$set", new Document("wfo", newWfhCount));
                    collection2.updateOne(query, update);
                    found.put("wfo", newWfhCount);
                }
                case "Work From Home" -> {
                    int newWfhCount = found.getInteger("wfh", 0) + 1;
                    Document update = new Document("$set", new Document("wfh", newWfhCount));
                    collection2.updateOne(query, update);
                    found.put("wfh", newWfhCount);
                }
            }
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } else {
            QtrAttendance qtrAttendance = setAttendanceForNewUser(attendance);
            attendance2Repository.save(qtrAttendance);
            return ResponseEntity.ok(qtrAttendance);
        }
    }

    @Override
    public QtrAttendance getUserAttendance(QtrAttendance qtrAttendance) {
        MongoDatabase database = client.getDatabase("digital-GBS");
        MongoCollection<Document> collection2 = database.getCollection("qtrAttendance");
        Document query = new Document("_id", qtrAttendance.getId());
        Document found = collection2.find(query).first();
        QtrAttendance att = new QtrAttendance();

        if (found != null) {
            att.setYear(found.getString("year"));
            att.setQtr(found.getString("quarter"));
            att.setName(found.getString("name"));
            att.setWfo(found.getInteger("wfo"));
            att.setWfoFriday(found.getInteger("wfoFriday"));
            att.setWfh(found.getInteger("wfh"));
            att.setWfhFriday(found.getInteger("wfhFriday"));
            att.setLeaves(found.getInteger("leaves"));
            att.setHolidays(found.getInteger("holidays"));
        }
        return att;
    }

    @Override
    public List<Attendance> findByAttendanceType(String emailId, String quarter, String year, String attendance) {
        Query query = new Query();
        query.addCriteria(Criteria.where("emailId").is(emailId).and("quarter").is(quarter).and("year").is(year).and("attendance").is(attendance));

        return mongoTemplate.find(query, Attendance.class);
    }

    @Override
    public List<Attendance> getUpcomingLeaves() {
        MongoDatabase database = client.getDatabase("digital-GBS");
        MongoCollection<Document> collection = database.getCollection("attendance");
        Date today = new Date();
        Date twoWeeksFromToday = new Date();
        twoWeeksFromToday.setTime(today.getTime() + (7L * 24 * 60 * 60 * 1000));

        List<Document> documents = collection.find().into(new ArrayList<>());
        List<Attendance> attendanceList = new ArrayList<>();

        for (Document doc : documents) {
            try {
                String dateStr = doc.getString("date");
                if (dateStr == null) {
                    continue;
                }

                Date attendanceDate = dateFormat.parse(dateStr);

                if (attendanceDate.after(today) && attendanceDate.before(twoWeeksFromToday) && "Leave".equals(doc.getString("attendance"))) {
                    Attendance attendance = new Attendance();
                    attendance.setEmailId(doc.getString("emailId"));
                    attendance.setDate(dateFormat.format(attendanceDate));
                    attendance.setAttendance(doc.getString("attendance"));
                    attendanceList.add(attendance);
                }
            } catch (ParseException e) {
                logger.error("Failed to parse date: {}", doc.getString("date"), e);
            } catch (NullPointerException e) {
                logger.error("Null value encountered in document: {}", doc.toJson(), e);
            }
        }
        return attendanceList;
    }
}
