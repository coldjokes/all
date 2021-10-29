package com.dosth.tool.controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dosth.tool.service.ImportService;
import com.dosth.util.OpTip;

@Controller
@RequestMapping("/importExcel")
public class ImportController {

	private static String PREFIX = "/tool/upload/";

	@Autowired
	private ImportService importService;

	/**
	 * 跳转导入页面
	 */
	@RequestMapping("/uploadView")
	public String uploadView() {
		return PREFIX + "upload.html";
	}

	@PostMapping(value = "/upload")
	@ResponseBody
	public OpTip uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			return new OpTip(201, "文件不能为空");
		}
		InputStream inputStream = file.getInputStream();
		OpTip tip = this.importService.getListByExcel(inputStream, file.getOriginalFilename());
		inputStream.close();
		return tip;
	}
}