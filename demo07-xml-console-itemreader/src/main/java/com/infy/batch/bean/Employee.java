package com.infy.batch.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="employee")
public class Employee {
	private Integer empId;
	private String empName;
	private String email;
	private String jobLoc;
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getJobLoc() {
		return jobLoc;
	}
	public void setJobLoc(String jobLoc) {
		this.jobLoc = jobLoc;
	}
	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", empName=" + empName + ", email=" + email + ", jobLoc=" + jobLoc + "]";
	}
	

}
