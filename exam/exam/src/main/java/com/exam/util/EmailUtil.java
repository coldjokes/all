package com.exam.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.exam.config.ExamConfig;

@Component
public class EmailUtil {

	@Autowired
	private ExamConfig cabinetKnifeConfig;

	/**
	 * 
	 * @param subject       邮件标题
	 * @param mailContent   邮件正文内容
	 * @param addresseeList 邮件收件人
	 * @param ccList        邮件抄送人
	 * @param attachment    附件名称
	 * @param filePath      附件地址
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException 
	 */
	public void sendEmail(String subject, String mailContent, List<String> addresseeList, String attachment,
			String filePath) throws MessagingException, UnsupportedEncodingException {

		String mailHost = cabinetKnifeConfig.getMailHost();// 服务器地址
		String mailPort = cabinetKnifeConfig.getMailPort();// 端口
		String mailProtocol = cabinetKnifeConfig.getMailProtocol();// 协议

		String mailSender = cabinetKnifeConfig.getMailSender();// 发件人
		String mailSenderName = cabinetKnifeConfig.getMailSenderName();// 发件人昵称
		String mailAuthCode = cabinetKnifeConfig.getMailAuthCode();// 授权码

		Properties properties = new Properties();
		properties.setProperty("mail.host", mailHost);
		properties.setProperty("mail.smtp.port", mailPort);
		properties.setProperty("mail.transport.protocol", mailProtocol);
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		Session session = Session.getDefaultInstance(properties);
		session.setDebug(true);

		MimeMessage mimeMessage = new MimeMessage(session);

		// 邮件主题
		if (subject != null && !"".equals(subject)) {
			mimeMessage.setSubject(subject);
		}

		// 发件人
		if (mailSender != null && !"".equals(mailSender)) {
//			mimeMessage.setFrom(mailSender);
			
			mimeMessage.setFrom(new InternetAddress (mailSender, mailSenderName, "UTF-8"));
			
		}
		// 收件人
		if (addresseeList != null && addresseeList.size() > 0) {
			addresseeList.forEach(addressee -> {
				try {
					mimeMessage.addRecipients(Message.RecipientType.TO, addressee);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			});
		}

		// 抄送
//		if (ccList != null && ccList.size() > 0) {
//			ccList.forEach(cc -> {
//				try {
//					mimeMessage.addRecipients(Message.RecipientType.CC, cc);
//				} catch (MessagingException e) {
//					e.printStackTrace();
//				}
//			});
//		}

		// 设置整封邮件的MIME消息体为混合的组合关系
		MimeMultipart mixed = new MimeMultipart("mixed");
		mimeMessage.setContent(mixed);

		// 创建邮件正文
		MimeBodyPart content = new MimeBodyPart();
		// 将正文添加到消息体中
		mixed.addBodyPart(content);

		// 设置正文的MIME类型
		MimeMultipart bodyMimeMultipart = new MimeMultipart("related");
		// 将bodyMimeMultipart添加到正文消息体中
		content.setContent(bodyMimeMultipart);

		// 正文的HTML部分
		// 将正文的HTML部分分别添加到bodyMimeMultipart中
		MimeBodyPart bodyPart = new MimeBodyPart();
		if (mailContent != null && !"".equals(mailContent)) {
			bodyPart.setContent(mailContent, "text/html;charset=utf-8");
			bodyMimeMultipart.addBodyPart(bodyPart);
		}

		// 创建附件1
		if (filePath != null && !"".equals(filePath)) {
			// 将附件一添加到MIME消息体中
			MimeBodyPart attach1 = new MimeBodyPart();
			mixed.addBodyPart(attach1);

			// 构造附件一的数据源
			FileDataSource file1 = new FileDataSource(new File(filePath));
			// 数据处理
			DataHandler dh1 = new DataHandler(file1);
			// 设置附件一的数据源
			attach1.setDataHandler(dh1);
			// 设置附件一的文件名
			if (attachment != null && !"".equals(attachment)) {
				attach1.setFileName(MimeUtility.encodeText(attachment));// 设置附件一的文件名
			}
		}

		/*
		 * // 创建附件2 MimeBodyPart attach2 = new MimeBodyPart(); // 将附件二添加到MIME消息体中
		 * mixed.addBodyPart(attach2); // 附件二的操作与附件一类似，这里就不一一注释了 FileDataSource fds2 =
		 * new FileDataSource(new File("E:\\book.xlsx")); DataHandler dh2 = new
		 * DataHandler(fds2); attach2.setDataHandler(dh2);
		 * attach2.setFileName(MimeUtility.encodeText("book.xls"));//设置文件名时，如果有中文，
		 * 可以通过MimeUtility类中的encodeText方法进行编码，避免乱码
		 * 
		 * // 将正文的图片部分分别添加到bodyMimeMultipart中 MimeBodyPart picPart = new
		 * MimeBodyPart();//正文的图片部分 DataHandler dataHandler = new DataHandler(new
		 * FileDataSource("F:\\img\\2.jpg")); picPart.setDataHandler(dataHandler);
		 * picPart.setContentID("2.jpg"); bodyMimeMultipart.addBodyPart(picPart);
		 */

		mimeMessage.saveChanges();

		Transport transport = session.getTransport();
		transport.connect(mailSender, mailAuthCode);
		// 发送邮件，第二个参数为收件人
		transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
		transport.close();
	}
}
