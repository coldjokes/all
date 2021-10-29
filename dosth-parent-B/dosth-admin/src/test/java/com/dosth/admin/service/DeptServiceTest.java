package com.dosth.admin.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dosth.DosthApplicationTests;
import com.dosth.admin.entity.Dept;
import com.dosth.admin.repository.DeptRepository;

public class DeptServiceTest extends DosthApplicationTests {

	@Autowired
	private DeptRepository deptRepository;
	
	@Test
	public void testAddDept() {
		Dept dept = new Dept();
		this.deptRepository.save(dept);
	}
}
