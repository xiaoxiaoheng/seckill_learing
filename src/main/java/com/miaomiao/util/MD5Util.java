package com.miaomiao.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by cjq on 2018-03-07 17:03
 */
public class MD5Util {
    private static final String salt = "1a2b3c4d";

    private static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassToFormPass(String inputPass) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    // 此处啊salt是数据库中的salt
    public static String formPassToDBPass(String formPass , String salt) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDbPass("123456" , "1a2b3c4d"));
    }

}
