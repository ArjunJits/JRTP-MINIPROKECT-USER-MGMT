package com.jrtp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jrtp.bindings.ActivateAccountForm;
import com.jrtp.bindings.Login;
import com.jrtp.bindings.User;
import com.jrtp.service.UserManagementService;

@RestController
public class UserManagementController {
	@Autowired
	private UserManagementService userManagementService;

	@PostMapping("/adduser")
	public ResponseEntity<String> saveUser(@RequestBody User user) {
		String msg = "";
		boolean result = userManagementService.saveUser(user);

		if (result == true) {
			msg = "User added suucessfully" + " email sent your \n " + user.getEmail()
					+ " Please change your password and activate it";
		} else {
			msg = "User add failed";
		}
		return new ResponseEntity<>(msg, HttpStatus.CREATED);
	}

	@GetMapping("/allusers")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> allUsers = userManagementService.getAllUsers();
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

	@PostMapping("/activate")
	public ResponseEntity<String> activateAccount(@RequestBody ActivateAccountForm acc) {
		boolean activateUserAccount = userManagementService.activateUserAccount(acc);
		if (activateUserAccount) {
			return new ResponseEntity<>("Account Activated", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Invalid Temporary Password", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
		User user = userManagementService.getUserById(userId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable Integer userId) {
		boolean deleteUserById = userManagementService.deleteUserById(userId);
		if (deleteUserById) {
			return new ResponseEntity<>("deleted", HttpStatus.OK);

		} else {
			return new ResponseEntity<>("user notdeleted", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/status/{userId}/{status}")
	public ResponseEntity<String> statusChnage(@PathVariable Integer userId, @PathVariable String status) {
		boolean changeAccountStatus = userManagementService.changeAccountStatus(userId, status);
		if (changeAccountStatus) {
			return new ResponseEntity<>("status changed", HttpStatus.OK);

		} else {
			return new ResponseEntity<>("status failed to change", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody Login login) {
		String status = userManagementService.loginUser(login);
			return new ResponseEntity<>(status, HttpStatus.OK);

	}

	@GetMapping("/forgotpwd/{email}")
	public ResponseEntity<String> forgotpassword(@PathVariable String email) {
		String status = userManagementService.forgotPassword(email);

			return new ResponseEntity<>(status, HttpStatus.OK);
	}

}
