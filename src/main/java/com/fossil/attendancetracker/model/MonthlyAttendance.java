package com.fossil.attendancetracker.model;

import org.bson.Document;
import org.springframework.data.annotation.Id;

public class MonthlyAttendance {

    @Id
    private String id;
    private String name;
    private String emailId;
    private String year;
    private String quarter;
    private String month;
    private String lastUpdatedBy;
    private String lastUpdatedOn;
    private int wfh;
    private int wfo;
    private int wfhFriday;
    private int wfoFriday;
    private int leaves;
    private int holidays;
    private int allowance;
    private int foodAllowance;
    private String attendance;

    public MonthlyAttendance() {
    }

    public MonthlyAttendance(Document document) {
        this.id = document.getString("_id");
        this.name = document.getString("name");
        this.emailId = document.getString("emailId");
        this.wfh = document.getInteger("wfh");
        this.wfo = document.getInteger("wfo");
        this.wfhFriday = document.getInteger("wfhFriday");
        this.wfoFriday = document.getInteger("wfoFriday");
        this.leaves = document.getInteger("leaves");
        this.holidays = document.getInteger("holidays");
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

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public int getAllowance() {
        return allowance;
    }

    public void setAllowance(int allowance) {
        this.allowance = allowance;
    }

    public int getFoodAllowance() {
        return foodAllowance;
    }

    public void setFoodAllowance(int foodAllowance) {
        this.foodAllowance = foodAllowance;
    }
}
