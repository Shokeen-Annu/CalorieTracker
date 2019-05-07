package com.example.myapplication;

import java.util.Date;

class Report {


    private Date date;

    private int totalcaloriesconsumed;

    private int totalcaloriesburned;

    private int totalstepstaken;

    private int setcaloriegoalforthatday;

    private Integer reportid;

    private Users userid;

    public Report() {
    }

    public Report(Integer reportid) {
        this.reportid = reportid;
    }

    public Report(Integer reportid, Date date, int totalcaloriesconsumed, int totalcaloriesburned, int totalstepstaken, int setcaloriegoalforthatday) {
        this.reportid = reportid;
        this.date = date;
        this.totalcaloriesconsumed = totalcaloriesconsumed;
        this.totalcaloriesburned = totalcaloriesburned;
        this.totalstepstaken = totalstepstaken;
        this.setcaloriegoalforthatday = setcaloriegoalforthatday;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotalcaloriesconsumed() {
        return totalcaloriesconsumed;
    }

    public void setTotalcaloriesconsumed(int totalcaloriesconsumed) {
        this.totalcaloriesconsumed = totalcaloriesconsumed;
    }

    public int getTotalcaloriesburned() {
        return totalcaloriesburned;
    }

    public void setTotalcaloriesburned(int totalcaloriesburned) {
        this.totalcaloriesburned = totalcaloriesburned;
    }

    public int getTotalstepstaken() {
        return totalstepstaken;
    }

    public void setTotalstepstaken(int totalstepstaken) {
        this.totalstepstaken = totalstepstaken;
    }

    public int getSetcaloriegoalforthatday() {
        return setcaloriegoalforthatday;
    }

    public void setSetcaloriegoalforthatday(int setcaloriegoalforthatday) {
        this.setcaloriegoalforthatday = setcaloriegoalforthatday;
    }

    public Integer getReportid() {
        return reportid;
    }

    public void setReportid(Integer reportid) {
        this.reportid = reportid;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportid != null ? reportid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Report)) {
            return false;
        }
        Report other = (Report) object;
        if ((this.reportid == null && other.reportid != null) || (this.reportid != null && !this.reportid.equals(other.reportid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "calorietracker.Report[ reportid=" + reportid + " ]";
    }
}
