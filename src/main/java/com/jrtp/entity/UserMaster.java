package com.jrtp.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "USER_MASTER")
@Data
public class UserMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	private String fullName;
	private String email;
	private String mobile;
	private String gender;
	private LocalDate dob;
	private Long ssn;
    private String password;
    private String accountStatus;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private String createdBy;
    private String updatedBy;
    // what is the difference between binding class(request input/output) and entity class is 
    // for mapping for database table
}
