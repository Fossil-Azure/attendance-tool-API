package com.fossil.attendancetracker.model;

import org.bson.Document;
import org.springframework.data.annotation.Id;

import java.util.List;

public class ApprovalList {

    @Id
    private String id;
    private String raisedBy;
    private String raisedTo;
    private String comments;
    private String status;
    private String type;
    private List<Document> raisedByList;
    private List<Document> raisedToList;
    private String prevAttendance;
    private String newAttendance;
    private String newShift;
    private String prevShift;
    private String date;
    private String year;
    private String quarter;
    private String month;
    private String name;

    public String getNewAttendance() {
        return newAttendance;
    }

    public void setNewAttendance(String newAttendance) {
        this.newAttendance = newAttendance;
    }

    public String getNewShift() {
        return newShift;
    }

    public void setNewShift(String newShift) {
        this.newShift = newShift;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public String getRaisedTo() {
        return raisedTo;
    }

    public void setRaisedTo(String raisedTo) {
        this.raisedTo = raisedTo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Document> getRaisedByList() {
        return raisedByList;
    }

    public void setRaisedByList(List<Document> raisedByList) {
        this.raisedByList = raisedByList;
    }

    public List<Document> getRaisedToList() {
        return raisedToList;
    }

    public void setRaisedToList(List<Document> raisedToList) {
        this.raisedToList = raisedToList;
    }

    public String getPrevAttendance() {
        return prevAttendance;
    }

    public void setPrevAttendance(String prevAttendance) {
        this.prevAttendance = prevAttendance;
    }

    public String getPrevShift() {
        return prevShift;
    }

    public void setPrevShift(String prevShift) {
        this.prevShift = prevShift;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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
}
