package com.jrtp.serviceImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jrtp.bindings.ActivateAccountForm;
import com.jrtp.bindings.Login;
import com.jrtp.bindings.User;
import com.jrtp.entity.UserMaster;
import com.jrtp.repo.UserMasterRepo;
import com.jrtp.service.UserManagementService;
import com.jrtp.utils.EmailUtils;

@Component
@Transactional
public class UserMangementServiceImpl implements UserManagementService {
	private static final Logger log = LoggerFactory.getLogger(UserMangementServiceImpl.class);
	@Autowired
	private UserMasterRepo userMasterRepo;
	@Autowired
	private EmailUtils emailUtils;

	@Override
	public boolean saveUser(User user) {
		var usermaster = new UserMaster();
		BeanUtils.copyProperties(user, usermaster);
		usermaster.setAccountStatus("In-Activate");
		usermaster.setPassword(UUID.randomUUID().toString().substring(0, 6));
		usermaster.setCreatedBy("arjun");
		usermaster.setUpdatedBy("arjun");
		usermaster.setCreatedDate(LocalDate.now());
		usermaster.setUpdatedDate(LocalDate.now());
		UserMaster result = userMasterRepo.save(usermaster);
		if (result.getUserId() > 0) {
			log.info("User created {}", usermaster.toString());
			// TODO Send Registration Email
			String subject = " Your Registration Success";
			String fileName = "REG-EMAIL-BODY.txt";
			String body = readEmailBody(usermaster.getFullName(), usermaster.getPassword(), fileName);
			emailUtils.sendEmail(usermaster.getEmail(), subject, body);
			return true;
		}
		return false;
	}

	@Override
	public boolean activateUserAccount(ActivateAccountForm accountForm) {
		UserMaster entity = new UserMaster();
		entity.setPassword(accountForm.getTempPassword());
		entity.setEmail(accountForm.getEmail());
		Example<UserMaster> example = Example.of(entity);
		List<UserMaster> findAll = userMasterRepo.findAll(example);

		if (findAll.isEmpty()) {
			return false; // No record found in DB
		} else {
			UserMaster userMaster = findAll.get(0);
			userMaster.setPassword(accountForm.getNewPassword());
			userMaster.setAccountStatus("Active");
			userMasterRepo.save(userMaster);
			return true;
		}
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		List<UserMaster> userMasterList = userMasterRepo.findAll();
		for (UserMaster usermaster : userMasterList) {
			var user = new User();
			BeanUtils.copyProperties(usermaster, user);
			users.add(user);
		}
		return users;
	}

	@Override
	public User getUserById(Integer userid) {

		Optional<UserMaster> userFound = userMasterRepo.findById(userid);
		if (userFound.isEmpty()) {
			log.info("User not found {}", userid);
		} else {
			var user = new User();
			UserMaster userMaster = userFound.get();
			BeanUtils.copyProperties(userMaster, user);
			return user;
		}
		return null;
	}

	@Override
	public boolean deleteUserById(Integer userid) {
		try {
			userMasterRepo.deleteById(userid);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean changeAccountStatus(Integer userId, String accntStatus) {

		Optional<UserMaster> findById = userMasterRepo.findById(userId);
		if (findById.isPresent()) {
			UserMaster userMaster = findById.get();
			userMaster.setAccountStatus(accntStatus);
			return true;
		}
		return false;
	}

	@Override
	public String loginUser(Login login) {
		var userMaster = new UserMaster();
		userMaster.setEmail(login.getEmail());
		userMaster.setPassword(login.getPassword());
		Example<UserMaster> userinfo = Example.of(userMaster);
		List<UserMaster> userDetails = userMasterRepo.findAll(userinfo);
		if (userDetails.isEmpty()) {
			return "Invalid Credentials";
		} else {
			UserMaster logedUser = userDetails.get(0);
			if (logedUser.getAccountStatus().equals("Active")) {
				return "login success and account active for " + logedUser.getEmail();
			} else

				return "login success but not active status " + logedUser.getEmail();
		}
	}

	@Override
	public String forgotPassword(String email) {
		UserMaster user = (UserMaster) userMasterRepo.findByEmail(email);
		if (user == null) {
			return "Invalid Email id";
		}
		String fileName = "REG-MAIL-BODY.txt";
		String subject = " Forget Password";
		String body = readEmailBody(user.getEmail(), user.getPassword(), fileName);
		// TODO: send password to user in Email
		return subject + body;
	}

	private String readEmailBody(String fullname, String temppwd, String fileName) {
		StringBuffer sb = new StringBuffer();
		String url = "";
		String mailBody = null;
		try (FileReader fr = new FileReader(fileName); BufferedReader br = new BufferedReader(fr)) {
			// fr = new FileReader(fileName);
			String line;
			line = br.readLine();
			while (line != null) {
				sb.append(line);
				br.readLine();
				mailBody = sb.toString();
				// {TEMP-PWD}
				// {URL}
				mailBody = mailBody.replace("{FULLNAME}", fullname);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mailBody;
	}
}
