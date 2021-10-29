package com.dosth.tool;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dosth.app.dto.AppOrderItem;
import com.dosth.tool.service.AppCabinetService;

public class PhoneAppServiceTest extends AppTest {

	@Autowired
	private AppCabinetService appCabinetService;
	
    @org.junit.Before
    public void before() throws Exception{

    }
    @After
    public void after() throws Exception {
    }

    @Test
    public void reOderAppOverdueOrderTest()  {
    	List<AppOrderItem> list = appCabinetService.reOderAppOverdueOrder("1");
    	System.out.println(list);
    }
}
