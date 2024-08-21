package com.fossil.attendancetracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "qtrAttendance")
public class QtrAttendance {

    @Id
    private String id;
    private String name;
    private String emailId;
    private String date;
    private String attendance;
    private String year;
    private String quarter;
    private String lastUpdatedBy;
    private String lastUpdatedOn;
    private int wfh;
    private int wfo;
    private int wfhFriday;
    private int wfoFriday;
    private int leaves;
    private int holidays;

    public QtrAttendance(String id, int leaves, String emailId, String qtr, String year, int wfh, int wfo, int wfhFriday, int wfoFriday, int holidays) {
        this.id = id;
        this.leaves = leaves;
        this.emailId = emailId;
        this.quarter = qtr;
        this.year = year;
        this.wfh = wfh;
        this.wfo = wfo;
        this.wfhFriday = wfhFriday;
        this.wfoFriday = wfoFriday;
        this.holidays = holidays;
    }

    public QtrAttendance(String id) {
        this.id = id;
    }

    public QtrAttendance() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getQtr() {
        return quarter;
    }

    public void setQtr(String qtr) {
        this.quarter = qtr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getWfh() {
        return wfh;
    }

    public void setWfh(int wfh) {
        this.wfh = wfh;
    }

    public int getWfo() {
        return wfo;
    }

    public void setWfo(int wfo) {
        this.wfo = wfo;
    }

    public int getWfhFriday() {
        return wfhFriday;
    }

    public void setWfhFriday(int wfhFriday) {
        this.wfhFriday = wfhFriday;
    }

    public int getWfoFriday() {
        return wfoFriday;
    }

    public void setWfoFriday(int wfoFriday) {
        this.wfoFriday = wfoFriday;
    }

    public int getLeaves() {
        return leaves;
    }

    public void setLeaves(int leaves) {
        this.leaves = leaves;
    }

    public int getHolidays() {
        return holidays;
    }

    public void setHolidays(int holidays) {
        this.holidays = holidays;
    }
}
