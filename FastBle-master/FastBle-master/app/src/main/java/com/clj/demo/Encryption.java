package com.clj.demo;

import android.content.Context;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
    // will be set based on mac id in the constructor
    private String ENCRYPTION_IV = "4e5Wa31fYoT7MFEX";

    Context app_context;

    public Encryption(Context x) {
        app_context = x;
        if (android.os.Build.SERIAL.length() == 16){
            ENCRYPTION_IV = android.os.Build.SERIAL;
        }else if (android.os.Build.SERIAL.length() == 8){
            ENCRYPTION_IV = android.os.Build.SERIAL + android.os.Build.SERIAL;
        }
    }

    public String encrypt(String src, String encstr) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
            cipher.init(Cipher.ENCRYPT_MODE, makeKey(encstr), makeIv());
            return Base64.encodeToString(cipher.doFinal(src.getBytes()), Base64.DEFAULT).trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String src, String encstr) {
        String decrypted = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, makeKey(encstr), makeIv());
            decrypted = new String(cipher.doFinal(Base64.decode(src, Base64.DEFAULT)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return decrypted.trim();
    }

    public String encryptServer(String src, String encstr) {
        try {
            MessageDigest shaCode = MessageDigest.getInstance("SHA-256");
            shaCode.update(encstr.getBytes());
            byte [] keybyte = shaCode.digest();
            byte [] ivbyte = new byte[16];
            for (int i = 0; i < 16; i ++){
                ivbyte[i] = keybyte[i];
            }
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
            Key key = new SecretKeySpec(keybyte,"AES");
            AlgorithmParameterSpec iv = new IvParameterSpec(ivbyte);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            return Base64.encodeToString(cipher.doFinal(src.getBytes()), Base64.DEFAULT).trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decryptServer(String src, String encstr) {
        String decrypted = "";
        try {
            MessageDigest shaCode = MessageDigest.getInstance("SHA-256");
            shaCode.update(encstr.getBytes());
            byte [] keybyte = shaCode.digest();
            byte [] ivbyte = new byte[16];
            for (int i = 0; i < 16; i ++){
                ivbyte[i] = keybyte[i];
            }
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            Key key = new SecretKeySpec(keybyte,"AES");
            AlgorithmParameterSpec iv = new IvParameterSpec(ivbyte);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            decrypted = new String(cipher.doFinal(Base64.decode(src, Base64.DEFAULT)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return decrypted.trim();
    }

    AlgorithmParameterSpec makeIv() {
        try {
            return new IvParameterSpec(ENCRYPTION_IV.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    Key makeKey(String enckey) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] key = md.digest(enckey.getBytes("UTF-8"));
            return new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String byte2Hex(byte[] data) {
        String hexString = "";
        String stmp = "";

        for(int i = 0; i < data.length; i++) {
            stmp = Integer.toHexString(data[i] & 0XFF);

            if(stmp.length() == 1) {
                hexString = hexString + "0" + stmp;
            }
            else {
                hexString = hexString + stmp;
            }
        }
        return hexString;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
