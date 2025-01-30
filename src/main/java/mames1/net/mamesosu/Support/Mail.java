package mames1.net.mamesosu.Support;

import mames1.net.mamesosu.Object.MySQL;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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


}
