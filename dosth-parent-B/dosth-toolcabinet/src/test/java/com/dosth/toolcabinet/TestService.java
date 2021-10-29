package com.dosth.toolcabinet;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dosth.dto.Lattice;
import com.dosth.toolcabinet.service.ToolService;

public class TestService extends AppTest {

	@Autowired
	private ToolService toolService;

	@Test
	public void test1() {
		System.out.println(this.toolService);
		Map<Integer, Map<Integer, Lattice>> map = new HashMap<>();
		// this.toolService.getLatticeDetailMap("A001");
		map.forEach((key, value) -> {
			System.out.println("-----------" + key);
			value.forEach((slaveId, lattice) -> {
				System.out.println("==slaveId" + slaveId);
			});
		});
	}
}