package com.dosth.tool.controller;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dosth.common.controller.BaseController;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;
import com.dosth.tool.common.config.ToolProperties;

/**
 * 通用Controller
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {

	@Autowired
	private ToolProperties toolProperties;

	/**
	 * 上传(上传到临时路径)
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/upload")
	@ResponseBody
	public String upload(@RequestPart("file") MultipartFile file) {
		String fileName = UUID.randomUUID().toString() + ".jpg";
		try {
			String tmpUploadPath = this.toolProperties.getTmpUploadPath();
			file.transferTo(new File(tmpUploadPath + File.separator + fileName));
		} catch (Exception e) {
			throw new DoSthException(DoSthExceptionEnum.SERVER_ERROR);
		}
		return fileName;
	}
}