package com.dosth.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.dosth.DosthApplicationTests;
import com.dosth.admin.util.face.FeatureMatchingUtil;

public class FeatureMatchingUtilTest extends DosthApplicationTests {

	@Test
	public void testSame() {
		int result = FeatureMatchingUtil.doMaping("F://test61.jpg", "F://test62.jpg");
		System.out.println(result);
		assertNotEquals(result, 0);
	}
	
	@Test
	public void testNoSame() {
		int result = FeatureMatchingUtil.doMaping("F://test61.jpg", "F://test63.jpg");
		System.out.println(result);
		assertEquals(result, 0);
	}
}