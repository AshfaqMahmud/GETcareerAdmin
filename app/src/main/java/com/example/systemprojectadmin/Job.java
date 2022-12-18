package com.example.systemprojectadmin;

public class Job {
    private String JobTitle, JobCompany, JobSite, JobKey, JobDate;

    public Job(String jobTitle, String jobCompany, String jobSite, String jobDate) {
        JobTitle = jobTitle;
        JobCompany = jobCompany;
        JobSite = jobSite;
        JobDate = jobDate;
    }

    public String getJobDate() {
        return JobDate;
    }

    public void setJobDate(String jobDate) {
        JobDate = jobDate;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public String getJobCompany() {
        return JobCompany;
    }

    public void setJobCompany(String jobCompany) {
        JobCompany = jobCompany;
    }

    public String getJobSite() {
        return JobSite;
    }

    public void setJobSite(String jobSite) {
        JobSite = jobSite;
    }

}