package com.jrtp.bindings;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
// 1st screen
private String fullName;
private String email;
private String mobile;
private String gender;
private LocalDate dob;
private Long ssn;

	
}
