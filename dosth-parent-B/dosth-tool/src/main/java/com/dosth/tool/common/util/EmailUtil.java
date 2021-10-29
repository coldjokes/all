package com.dosth.tool.common.util;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dosth.tool.common.config.ToolProperties;

/**
 * 
 * @description 邮箱工具类
 * @author guozhidong
 *
 */
@Component
public class EmailUtil {

	private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

	@Autowired
	private ToolProperties toolProperties;

	/**
	 * @description 发送邮件
	 * @param mailContent  邮件正文
	 * @param mailTheme    邮件主题
	 * @param filePath     附件地址
	 * @param annexName    显示附件名称
	 * @param receiverList 收件人列表
	 * @throws MessagingException           邮件异常
	 * @throws UnsupportedEncodingException 不支持编码异常
	 */
	public void sendEmail(String mailContent, String mailTheme, String filePath, String annexName,
			List<String> receiverList) throws MessagingException, UnsupportedEncodingException {
		if (receiverList == null || receiverList.size() < 1) {
			logger.info("邮件没有接收人");
			return;
		}

		Transport transport = null;
		try {
			Properties properties = new Properties();
			properties.setProperty("mail.host", this.toolProperties.getMailHost());
			properties.setProperty("mail.transport.protocol", this.toolProperties.getMailProtocol());
			properties.setProperty("mail.smtp.auth", "true");
			properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			properties.setProperty("mail.smtp.port", "465");
			Session session = Session.getDefaultInstance(properties);
			session.setDebug(true);

			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.addRecipients(Message.RecipientType.TO, receiverList.get(0));// 设置收信人

			if (receiverList != null && receiverList.size() > 0) {
				receiverList.stream().skip(1).forEach(receiver -> {
					try {
						mimeMessage.addRecipients(Message.RecipientType.CC, receiver);
					} catch (MessagingException e) {
						e.printStackTrace();
					} // 抄送
				});
			}

			mimeMessage.setFrom(new InternetAddress("\"" + MimeUtility.encodeText(this.toolProperties.getMailSender())
					+ "\"<" + this.toolProperties.getMailAddr() + ">"));// 邮件发送人

			mimeMessage.setSubject(mailTheme);// 邮件主题

			MimeMultipart mixed = new MimeMultipart("mixed");
			mimeMessage.setContent(mixed);// 设置整封邮件的MIME消息体为混合的组合关系
			MimeBodyPart content = new MimeBodyPart();// 创建邮件正文
			mixed.addBodyPart(content);// 将正文添加到消息体中

			if (filePath != null && !"".equals(filePath) && annexName != null && !"".equals(annexName)) {
				MimeBodyPart attach = new MimeBodyPart();// 创建附件1
				mixed.addBodyPart(attach);// 将附件一添加到MIME消息体中

				FileDataSource fds = new FileDataSource(new File(filePath));// 构造附件一的数据源
				DataHandler dh = new DataHandler(fds);// 数据处理
				attach.setDataHandler(dh);// 设置附件一的数据源
				attach.setFileName(MimeUtility.encodeText(annexName
						+ (filePath.indexOf(".") == -1 ? "" : filePath.substring(filePath.lastIndexOf(".")))));// 设置附件一的文件名

			}

			MimeMultipart bodyMimeMultipart = new MimeMultipart("related");// 设置正文的MIME类型
			content.setContent(bodyMimeMultipart);// 将bodyMimeMultipart添加到正文消息体中

			MimeBodyPart bodyPart = new MimeBodyPart();// 正文的HTML部分
			bodyPart.setContent(mailContent, "text/html;charset=utf-8");
			// 将正文的HTML和图片部分分别添加到bodyMimeMultipart中
			bodyMimeMultipart.addBodyPart(bodyPart);
			mimeMessage.saveChanges();

			transport = session.getTransport();
			transport.connect(this.toolProperties.getMailHost(), this.toolProperties.getMailAddr(),
					this.toolProperties.getMailAuthorCode());
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());// 发送邮件，第二个参数为收件人
		} finally {
			if (transport != null) {
				transport.close();
			}
		}
	}
}