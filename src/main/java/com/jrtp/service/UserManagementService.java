package com.jrtp.service;

import java.util.List;

import com.jrtp.bindings.ActivateAccountForm;
import com.jrtp.bindings.Login;
import com.jrtp.bindings.User;

public interface UserManagementService {
 // 1st screen
 public boolean saveUser(User user);
 //second screen
 public boolean activateUserAccount(ActivateAccountForm accountForm);
 
 public List<User> getAllUsers();
 //edit particular user
 public User getUserById(Integer userid);
 // hard delete
 public boolean deleteUserById(Integer userid);
 //soft delete (Activate/Deactivate)
 public boolean changeAccountStatus(Integer userId, String accntStatus);
 
 public String   loginUser(Login login);
 
 public String forgotPassword(String email);
 
 
}
