package com.example.po;

import java.sql.*;
import java.util.*;

@SuppressWarnings("all")
public class Emp {

	private java.sql.Date birthday;
	private Integer deptId;
	private Integer id;
	private Double salary;
	private Integer age;
	private String emname;


	public java.sql.Date getBirthday(){
		return birthday;
	}
	public Integer getDeptId(){
		return deptId;
	}
	public Integer getId(){
		return id;
	}
	public Double getSalary(){
		return salary;
	}
	public Integer getAge(){
		return age;
	}
	public String getEmname(){
		return emname;
	}
	public void setBirthday(java.sql.Date birthday){
		this.birthday=birthday;
	}
	public void setDeptId(Integer deptId){
		this.deptId=deptId;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setSalary(Double salary){
		this.salary=salary;
	}
	public void setAge(Integer age){
		this.age=age;
	}
	public void setEmname(String emname){
		this.emname=emname;
	}
}
