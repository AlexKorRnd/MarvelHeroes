package com.growapp.marvelheroes.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by http://stackoverflow.com/a/4846511/1393023
 */
public class Md5 {

    public static final String Md5(final String s) {
        final String MD5 = "MD5";
        try {

            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();


            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
