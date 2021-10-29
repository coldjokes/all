package com.dosth.tool;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.junit.Test;


public class MailTest {
	
	
	@Test
    public void sendEmail() throws MessagingException, UnsupportedEncodingException {
		
		Properties properties = new Properties();
		properties.setProperty("mail.host", "smtp.exmail.qq.com");
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.smtp.port", "465");
		Session session = Session.getDefaultInstance(properties);
		session.setDebug(true);
		
		MimeMessage mimeMessage = new MimeMessage(session);
		mimeMessage.addRecipients(Message.RecipientType.TO, "weifeng.li@cnbaosi.com");//设置收信人
		mimeMessage.addRecipients(Message.RecipientType.CC, "");//抄送
		mimeMessage.setFrom("toolbox@cnbaosi.com");//邮件发送人
		mimeMessage.setSubject("测试邮件主题");//邮件主题

		MimeMultipart mixed = new MimeMultipart("mixed");
		mimeMessage.setContent(mixed);//设置整封邮件的MIME消息体为混合的组合关系

		MimeBodyPart attach1 = new MimeBodyPart();//创建附件1
//		MimeBodyPart attach2 = new MimeBodyPart();//创建附件2
		MimeBodyPart content = new MimeBodyPart();//创建邮件正文

		mixed.addBodyPart(attach1);//将附件一添加到MIME消息体中
//		mixed.addBodyPart(attach2);//将附件二添加到MIME消息体中
		mixed.addBodyPart(content);//将正文添加到消息体中

		FileDataSource fds1 = new FileDataSource(new File("F:\\workbook.xls"));//构造附件一的数据源
		DataHandler dh1 = new DataHandler(fds1);//数据处理
		attach1.setDataHandler(dh1);//设置附件一的数据源
		attach1.setFileName("workbook.xls");//设置附件一的文件名

		//附件二的操作与附件一类似，这里就不一一注释了
//		FileDataSource fds2 = new FileDataSource(new File("E:\\book.xlsx"));
//		DataHandler dh2 = new DataHandler(fds2);
//		attach2.setDataHandler(dh2);
//		attach2.setFileName(MimeUtility.encodeText("book.xls"));//设置文件名时，如果有中文，可以通过MimeUtility类中的encodeText方法进行编码，避免乱码

		MimeMultipart bodyMimeMultipart = new MimeMultipart("related");//设置正文的MIME类型
		content.setContent(bodyMimeMultipart);//将bodyMimeMultipart添加到正文消息体中

		MimeBodyPart bodyPart = new MimeBodyPart();//正文的HTML部分
		bodyPart.setContent("<h1>Hello大家好，这是一封测试邮件<img src='cid:2.jpg'/></h1>","text/html;charset=utf-8");

//		MimeBodyPart picPart = new MimeBodyPart();//正文的图片部分
//		DataHandler dataHandler = new DataHandler(new FileDataSource("F:\\img\\2.jpg"));
//		picPart.setDataHandler(dataHandler);
//		picPart.setContentID("2.jpg");

		//将正文的HTML和图片部分分别添加到bodyMimeMultipart中
		bodyMimeMultipart.addBodyPart(bodyPart);
//		bodyMimeMultipart.addBodyPart(picPart);

		mimeMessage.saveChanges();
		
		Transport transport = session.getTransport();
		transport.connect("toolbox@cnbaosi.com", "Bs1234567890");
		transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());//发送邮件，第二个参数为收件人
		transport.close();
    }
}
