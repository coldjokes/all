package com.dosth.tool;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dosth.tool.rpc.CabinetRpcService;

public class CabinetSetupTest extends AppTest {
	@Autowired
	private CabinetRpcService cabinetRpcService;

	@Test
	public void test() {
		Map<String, Map<String, String>> map = this.cabinetRpcService.getCabinetSetupBySerialNo("001");
		System.out.println(map);
	}
}
