package com.cnbaosi;

import java.awt.EventQueue;

import com.cnbaosi.scan.util.SpringContextUtils;
import com.cnbaosi.scan.view.page.MainPage;

/**
 *  页面启动
 * 
 * @author Yifeng Wang  
 */
public class ViewStart {
	public static void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SpringContextUtils.getBean(MainPage.class).setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

