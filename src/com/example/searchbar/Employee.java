package com.example.searchbar;

public class Employee {
	public final String first_name;
    public final String last_name;
    public final String dept_name;
    public final String email;
    public final String extension;
    public final String mobile_num;
    public final String office_num;
    public final String username;
    public final String employee_num;
    public final String job_title;

    public Employee(String first_name, String last_name, String dept_name, String email,
    		         String extension, String mobile_num, String office_num, String username,
    		         String employee_num, String job_title) {
        this.first_name = first_name;
        this.dept_name = dept_name;
        this.last_name = last_name;
        this.email = email;
        this.extension = extension;
        this.mobile_num = mobile_num;
        this.office_num = office_num;
        this.username = username;
        this.employee_num = employee_num;
        this.job_title = job_title;
    }
}
