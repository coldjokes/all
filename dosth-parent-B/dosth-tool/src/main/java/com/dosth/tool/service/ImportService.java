package com.dosth.tool.service;

import java.io.InputStream;

import com.dosth.util.OpTip;

public interface ImportService {

	/**
	 * 解析上传excel
	 * @param in
	 * @param fileName
	 * @throws Exception
	 */
	public OpTip getListByExcel(InputStream in, String fileName) throws Exception;
}