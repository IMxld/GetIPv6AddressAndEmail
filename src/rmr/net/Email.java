package rmr.net;

import rmr.fs.ReadConfig;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

public class Email {
    public static String  username;

    public static Session creatSession(){
        String smtp = "smtp.qq.com";

		//读取配置文件
        Properties properties = null;
        try {
            properties = ReadConfig.getConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

        username = properties.getProperty("username");
        String password = properties.getProperty("password");

        //SMTP服务器信息
        Properties props = new Properties();
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls", "true");

        //连接信息和用户认证对象
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        //session.setDebug(true);

        return session;
    }

    public static void sendMassage(String recipientAddress, String content){
        try{
            //创建session对话
            Session session = creatSession();

            //邮件对象
            MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(username));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));//设置收件人
            msg.setSubject("IPv6", "utf-8");

            //正文
            BodyPart textPart = new MimeBodyPart();
            textPart.setContent(MessageFormat.format("<h1>{0}</h1>", content), "text/html;charset=utf-8");

            //正文加附件
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            //设置邮件内容
            msg.setContent(multipart);

            //发送邮件
            Transport.send(msg);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
