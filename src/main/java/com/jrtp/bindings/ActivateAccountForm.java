package com.jrtp.bindings;

import lombok.Data;

@Data
public class ActivateAccountForm {
 //binding class used for input and output 
 private String email;
 private String tempPassword;
 private String newPassword;
 private String ConfirmPassword;
 
}
