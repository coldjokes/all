package com.dosth;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;

import com.dosth.admin.common.config.properties.DosthProperties;
import com.dosth.admin.common.config.properties.DruidProperties;
import com.dosth.admin.entity.User;

public class OtherTest {

	@Autowired
	DosthProperties dosthProperties;
	
	@Autowired
	DruidProperties druidProperties;
	
	@Test
	public void getProperties() {
		System.out.println(this.dosthProperties);
		System.out.println(this.druidProperties);
	}
	
	@Test
	public void makeUser() {
		Field[] fields = User.class.getDeclaredFields();
		StringBuilder sb = new StringBuilder("\"User{");
		for (Field field : fields) {
			sb.append(" + \", ");
			sb.append(field.getName());
			sb.append("=\" + ");
			sb.append(field.getName());
		}
		sb.append("}");
		System.out.println(sb.toString());
	}
}