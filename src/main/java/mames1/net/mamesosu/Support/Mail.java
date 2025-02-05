package mames1.net.mamesosu.Support;

import mames1.net.mamesosu.Object.MySQL;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

public abstract class Mail {

    private static String createVerificationMailFormat(String username, String code) {
        return username + ", Hello!\n" +
                "Thank you so much for playing with Mamestagram!\n\n" +
                "You have performed an action that requires account verification.\n\n" +
                "Your verification code is: " + code + "\n" +
                "Please use this code to verify your account.\n\n" +
                "If you do not recognize this request, your account may have been accessed fraudulently. Please contact our support team immediately.\n\n" +
                "--------------------------------------\n" +
                "Mamestagram: https://web.mamesosu.net/\n" +
                "--------------------------------------";
    }


    private static void setVerificationCode(int id, String code) {
        MySQL mysql = new MySQL();
        PreparedStatement ps;

        try {
            Connection connection = mysql.getConnection();
            ps = connection.prepareStatement("update users set verification_code = ? where id = ?");
            ps.setString(1, code);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String createVerificationCode(String username) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        MySQL mysql = new MySQL();
        PreparedStatement ps;
        ResultSet result;

        Random random = new Random();

        try {
             int verification_code = random.nextInt(100001, 999999);
             String hash = encoder.encode(String.valueOf(verification_code));
             hash = hash.substring(hash.length() - 12);

             Connection connection = mysql.getConnection();

             ps = connection.prepareStatement("select id from users where name = ?");
             ps.setString(1, username);
             result = ps.executeQuery();
             if (result.next()) {
                setVerificationCode(result.getInt("id"), hash);
             }

             connection.close();
             return hash;

        } catch (SQLException e) {
            e.printStackTrace();
            return "err";
        }
    }

   public static boolean sendVerificationMail(String email, String username) throws IOException, FileNotFoundException {

        Properties prop = new Properties();

        prop.load(new FileReader("mail.properties"));

        final String mail = prop.getProperty("mailaddress");
        final String password = prop.getProperty("password");

        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail, password);
            }
        });

        try {

            String verificationCode = createVerificationCode(username);

            if(Objects.equals(verificationCode, "err")) return false;

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(mail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Verification Code for Account Authentication");
            message.setText(createVerificationMailFormat(username, verificationCode));

            Transport.send(message);

            return true;
        } catch (MessagingException e) {
            System.out.println("Email sent unsuccessfully : " + e);
            return false;
        }

   }


}
