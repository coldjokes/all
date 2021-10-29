package com.dosth.tool.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.dto.FeedingEmail;
import com.dosth.tool.common.state.NoticeType;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.ManufacturerCustom;
import com.dosth.tool.entity.NoticeMgr;
import com.dosth.tool.repository.NoticeMgrRepository;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.tool.service.ManufacturerCustomService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewUser;
import com.dosth.util.DateUtil;

/**
 * 
 * @description 补料邮件工具类
 * @author guozhidong
 *
 */
@Component
public class FeedingEmailUtil {

	public static final Logger logger = LoggerFactory.getLogger(FeedingEmailUtil.class);

	/**
	 * @description 归还信息处理线程池
	 */
	private ExecutorService service = Executors.newFixedThreadPool(3);

	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private EquDetailStaService equDetailStaService;
	@Autowired
	private ManufacturerCustomService manufacturerCustomService;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private NoticeMgrRepository noticeMgrRepository;
	@Autowired
	private UserService userService;

	/**
	 * @description 补料邮件通知队列
	 */
	private static BlockingQueue<FeedingEmail> feedingEmailQueue = new LinkedBlockingQueue<>();

	/**
	 * @description 设置补料邮件到队列
	 * @param feedingEmail 补料邮件
	 * @throws InterruptedException
	 */
	public static void putFeedingEmailInfo(FeedingEmail feedingEmail) throws InterruptedException {
		feedingEmailQueue.put(feedingEmail);
	}

	/**
	 * @description 启动线程池执行补料邮件通知
	 */
	public void start() {
		this.service.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					logger.info("执行补料邮件通知队列时间:" + DateUtil.getTime(new Date()));
					FeedingEmail email = null;
					try {
						email = feedingEmailQueue.take();
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					HSSFWorkbook workbook = null;
					OutputStream os = null;
					try {
						if (email != null) {
							logger.info("--------------");
							EquDetailSta equDetailSta = equDetailStaService.get(email.getEquDetailStaId());
							// 获取供货商
							String manufacturerId = equDetailSta.getMatInfo().getManufacturerId();
							String equSettingId = equDetailSta.getEquDetail().getEquSettingId();
							List<EquDetailSta> staList = equDetailStaService
									.getEquDetailStaListByCabinetId(equSettingId);

							workbook = new HSSFWorkbook();
							int rowNo = 0;
							HSSFSheet sheet = workbook.createSheet("库存预警通知");
							sheet.setDefaultRowHeightInPoints(20);
							sheet.setDefaultColumnWidth(20);
							HSSFCellStyle cellStyle = workbook.createCellStyle();
							cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
							HSSFFont fontStyle = workbook.createFont();
							fontStyle.setFontName("宋体");
							cellStyle.setFont(fontStyle);

							HSSFRow row1 = sheet.createRow(rowNo++);
							row1.createCell(0).setCellValue("刀具柜名称");
							row1.createCell(1).setCellValue("物料名称");
							row1.createCell(2).setCellValue("物料编号");
							row1.createCell(3).setCellValue("物料型号");
							row1.createCell(4).setCellValue("包装数量");
							row1.createCell(5).setCellValue("包装单位");
							row1.createCell(6).setCellValue("当前库存");
							row1.createCell(7).setCellValue("建议补料数量");

							staList = staList.stream()
									.filter(item -> item.getMatInfo() != null
											&& item.getMatInfo().getManufacturerId().equals(manufacturerId))
									.collect(Collectors.toList());

							HSSFRow row2 = null;
							EquDetailSta sta = null;
							for (int i = 0; i < staList.size(); i++) {
								row2 = sheet.createRow(i + 1);
								sta = staList.get(i);
								row2.createCell(0).setCellValue(sta.getEquDetail().getEquSetting().getEquSettingName()
										+ "(" + sta.getEquDetail().getRowNo() + "--" + sta.getColNo() + ")");
								row2.createCell(1).setCellValue(sta.getMatInfo().getMatEquName());
								row2.createCell(2)
										.setCellValue(sta.getMatInfo() != null ? sta.getMatInfo().getBarCode() : "");
								row2.createCell(3)
										.setCellValue(sta.getMatInfo() != null ? sta.getMatInfo().getSpec() : "");
								row2.createCell(4)
										.setCellValue(sta.getMatInfo() != null ? sta.getMatInfo().getNum() : 0);
								row2.createCell(5).setCellValue(
										sta.getMatInfo() != null ? sta.getMatInfo().getBorrowType().getMessage() : "");
								row2.createCell(6).setCellValue(sta.getCurNum());
								row2.createCell(7).setCellValue(sta.getMaxReserve() - sta.getCurNum());
							}
							String filePath = toolProperties.getTmpUploadPath() + "库存预警通知-" + DateUtil.getAllTime()
									+ ".xls";
							File file = new File(filePath);
							os = new FileOutputStream(file);
							workbook.write(os);
							if (!file.exists()) {
								logger.error("生成库存预警通知异常,请刷新后再试!");
							}
							List<String> list = new ArrayList<>();
							List<ManufacturerCustom> cusList = manufacturerCustomService
									.getCustomListByManufacturerId(manufacturerId);
							cusList.forEach(cus -> {
								list.add(cus.getMailAddress());
							});

							List<NoticeMgr> noticeMgrList = noticeMgrRepository.findAll().stream()
									.filter(noticeMgr -> noticeMgr.getNoticeType().equals(NoticeType.STOCK))
									.collect(Collectors.toList());

							if (noticeMgrList != null && noticeMgrList.size() > 0) {
								ViewUser viewUser;
								NoticeMgr notice = noticeMgrList.get(0);
								if (notice != null && notice.getAccountId() != null) {
									String accountIds[] = notice.getAccountId().split(",");
									for (int i = 0; i < accountIds.length; i++) {
										viewUser = userService.getViewUser(accountIds[i]);
										if (viewUser != null && viewUser.getEmail() != null) {
											if(viewUser.getEmail() == null || viewUser.getEmail().equals("")) {
												continue;
											}
											list.add(viewUser.getEmail());
										}
									}
								}
							}

							String mailContent = "<p>您好:<br/>&nbsp;&nbsp;&nbsp;&nbsp;刀具柜已达存储警告值,望贵司及时提供补料服务(附件为库存预警通知),谢谢!</p>";
							emailUtil.sendEmail(mailContent, "库存预警通知", filePath, "库存预警通知", list);
						}
					} catch (Exception e) {
						logger.error("异常库存预警通知:" + email.toString());
						e.printStackTrace();
					} finally {
						if (os != null) {
							try {
								os.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (workbook != null) {
							try {
								workbook.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
	}

	/**
	 * @description 关闭线程池
	 */
	public void stop() {
		this.service.shutdown();
	}
}