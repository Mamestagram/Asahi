package mames1.net.mamesosu.Utils;

import mames1.net.mamesosu.Object.MySQL;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public abstract class Password {
    // ハッシュ化の仕組みを書く

    public static boolean checkPassword(String password, String hashedPassword) throws NoSuchAlgorithmException {

        // password -> md5
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        byte[] rs = md5.digest(password.getBytes());
        int[] i = new int[rs.length];
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < rs.length; j++) {
            i[j] = (int) rs[j] & 0xff;
            if (i[j] <= 15) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(i[j]));
        }
        String md5Password = sb.toString();
        String encodedPassword = encoder.encode(md5Password);

        return encoder.matches(md5Password, hashedPassword);
    }
}
