package com.jrtp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jrtp.entity.UserMaster;

public interface UserMasterRepo extends JpaRepository<UserMaster, Integer> {
public List<UserMaster> findByEmail(String email);
}
