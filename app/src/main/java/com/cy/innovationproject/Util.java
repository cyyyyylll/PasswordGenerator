package com.cy.innovationproject;

import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by S on 2018/9/9.
 */

public class Util {
    public static String seed = "1100101101011101010101";
    public static String loginByPost(String username,String password) throws IOException {
        try {
            URL url=new URL("http://111.231.223.218:8081/login");
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            String data="name="+ URLEncoder.encode(username, "utf-8") + "&password="
                    + URLEncoder.encode(password, "utf-8");
            urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
            urlConnection.connect();
            OutputStream os = urlConnection.getOutputStream();
            os.write(data.getBytes()); os.flush();
            if (urlConnection.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();

                // 创建字节输出流对象
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    baos.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                baos.close();
                // 返回字符串
                String result = new String(baos.toByteArray());
                return result;
            } else {
                System.out.println("------------------连接失败-----------------");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public   static   String StringFilter(String   str)   throws PatternSyntaxException {
        String regEx="[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }
    public static void resetPwd(final String username, final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String name = username;
                    final String newPassword = password;
                    URL url = new URL("http://111.231.223.218:8081/user/updatepass");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(5000);
                    con.setReadTimeout(5000);
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
                    String data = "name="+URLEncoder.encode(name)+"&password=" + URLEncoder.encode(newPassword, "utf-8");
                    con.setRequestProperty("Connection", "keep-alive");
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    con.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    con.connect();
                    BufferedOutputStream bos = new BufferedOutputStream(con.getOutputStream());
                    bos.write(data.getBytes());
                    bos.flush();
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();

                        // 创建字节输出流对象
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // 定义读取的长度
                        int len = 0;
                        // 定义缓冲区
                        byte buffer[] = new byte[1024];
                        // 按照缓冲区的大小，循环读取
                        while ((len = is.read(buffer)) != -1) {
                            // 根据读取的长度写入到os对象中
                            baos.write(buffer, 0, len);
                        }
                        // 释放资源
                        is.close();
                        baos.close();
                        // 返回字符串
                        String result = new String(baos.toByteArray());
                        if(result.equals("true")){
                            User.resetSorF=1;
                        }else{
                            User.resetSorF=0;
                        }
                    }
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void registeredThread(final String _username, final String _password, final String _mail){
        new Thread(new Runnable() {
            private Looper looper;
            @Override
            public void run() {
                try {
                    final String username = _username;
                    final String password = _password;
                    final String mail = _mail;
                    URL url = new URL("http://111.231.223.218:8081/adduser");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    String data = "name=" + URLEncoder.encode(username, "utf-8") +
                            "&password=" + URLEncoder.encode(password, "utf-8")+"&mail="+URLEncoder.encode(mail,"utf-8");
                    con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
                    con.setRequestProperty("Connection", "keep-alive");
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    con.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    con.connect();
                    BufferedOutputStream bos = new BufferedOutputStream(con.getOutputStream());
                    bos.write(data.getBytes());
                    bos.flush();
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();
                        byte[] b = new byte[1];
                        is.read(b);
                        User.regCode = (int)b[0]-48;
                        Log.v("tagC",String.valueOf(User.regCode)+"4");
                        Looper.prepare();
                        looper = Looper.myLooper();
                        Message message = new Message();
                        message.obj = (int)b[0]-48;
                        Looper.loop();
                    }
                    User.webPassword="null";
                    User.username=username;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void find_verificationCode(final String _verificationCode){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    final String verificationCode = _verificationCode;
                    URL url = new URL("http://111.231.223.218:8081/email/yz");
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setConnectTimeout(5000);
                    con.setReadTimeout(5000);
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
                    String data = "code="+URLEncoder.encode(verificationCode,"utf-8");
                    con.setRequestProperty("Connection", "keep-alive");
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    con.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    con.connect();
                    BufferedOutputStream bos = new BufferedOutputStream(con.getOutputStream());
                    bos.write(data.getBytes());
                    bos.flush();
                    if(con.getResponseCode()==200){
                        InputStream is = con.getInputStream();

                        // 创建字节输出流对象
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // 定义读取的长度
                        int len = 0;
                        // 定义缓冲区
                        byte buffer[] = new byte[32];
                        // 按照缓冲区的大小，循环读取
                        while ((len = is.read(buffer)) != -1) {
                            // 根据读取的长度写入到os对象中
                            baos.write(buffer, 0, len);
                        }
                        // 释放资源
                        is.close();
                        baos.close();
                        // 返回字符串
                        String result = new String(baos.toByteArray());
                        if(result.equals("true")){
                            loginActivity.mailCode=1;
                        }else{
                            loginActivity.mailCode=0;
                        }
                    }
                    Log.v("tagC",String.valueOf(User.mailCode));
                    bos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void findThread(final String _username){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    final String username = _username;
                    URL url = new URL("http://111.231.223.218:8081/email/sendTemplateEmail");
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setConnectTimeout(5000);
                    con.setReadTimeout(5000);
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
                    String data = "name="+URLEncoder.encode(username,"utf-8");
                    con.setRequestProperty("Connection", "keep-alive");
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    con.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    con.connect();
                    BufferedOutputStream bos = new BufferedOutputStream(con.getOutputStream());
                    bos.write(data.getBytes());
                    bos.flush();
                    if(con.getResponseCode()==200){
                        System.out.print("success");
                    }
                    bos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static String MD5(String inputStr) throws NoSuchAlgorithmException {
        String md5Str = inputStr;
        if(inputStr != null) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inputStr.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            md5Str = hash.toString(16);
            if((md5Str.length() % 2) != 0) {
                md5Str = "0" + md5Str;
            }
        }
        return md5Str;
    }
    public static void doPostPicture(String urlStr, Map<String, Object> paramMap, File pictureFile,String webAddress,String order) throws Exception {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        String str;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false);
        conn.setReadTimeout(10 * 100000); // 缓存的最长时间
        conn.setRequestMethod("POST");

        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
        conn.setRequestProperty("filename",pictureFile.getName().substring(9,pictureFile.getName().length()-4));
        conn.setRequestProperty("name",User.username);
        conn.setRequestProperty("webadds",Aes.encrypt(seed,webAddress));
        conn.setRequestProperty("worder",Aes.encrypt(seed,order));
        DataOutputStream os = new DataOutputStream(conn.getOutputStream());

        StringBuilder sb = new StringBuilder(); //用StringBuilder拼接报文，用于上传图片数据
        sb.append(PREFIX);
        sb.append(BOUNDARY);
        sb.append(LINEND);
        sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" +
                pictureFile.getName().substring(9,pictureFile.getName().length()-4) + ".jpg\"" + LINEND);
        sb.append("Content-Type: image/jpg; charset=" + CHARSET + LINEND);
        sb.append(LINEND);
        os.write(sb.toString().getBytes());
        InputStream is = new FileInputStream(pictureFile);//error
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len); //写入图片数据
        }
        is.close();
        os.write(LINEND.getBytes());
        StringBuilder text = new StringBuilder();
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) { //在for循环中拼接报文，上传文本数据
            text.append("--");
            text.append(BOUNDARY);
            text.append("\r\n");
            text.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
            text.append(entry.getValue());
            text.append("\r\n");
        }
        os.write(text.toString().getBytes("utf-8")); //写入文本数据
        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        os.write(end_data);
        os.flush();
        os.close();
        if(conn.getResponseCode()==200){
            str=readParse(conn); //不执行该语句无法得到复制的小窗口     ？？？
            User.webPassword = str;
        }
    }
    public static String readParse(HttpURLConnection conn) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        InputStream inStream = conn.getInputStream();
        while ((len = inStream.read(data)) != -1) {
            outStream.write(data, 0, len);
        }
        inStream.close();
        return new String(outStream.toByteArray());
    }
    public static String changePwd(String webAddress,String password)throws IOException{
        try{
            URL url = new URL("http://111.231.223.218:8081/webadd/updatemima");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(50000);
            con.setReadTimeout(50000);
            con.setDoInput(true);
            con.setDoOutput(true);
            String data = "name="+URLEncoder.encode(User.username,"utf-8")+"&password="+URLEncoder.encode(password,"utf-8")+"&webadds="
                    +URLEncoder.encode(webAddress,"utf-8");
            con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
            con.connect();
            OutputStream os = con.getOutputStream();
            os.write(data.getBytes());
            os.close();
            if(con.getResponseCode() == 200){
                InputStream is = con.getInputStream();
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len = 0;
                while ((len = is.read(buffer)) != -1){
                    baos.write(buffer,0,len);
                }
                is.close();
                baos.close();
                String result = new String(baos.toByteArray());
                return result;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}


 class Aes {

    public static String encrypt(String seed, String cleartext) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] me = {47, 21, 60, -116, -12, 75, -120, 40, -82, 32, -81, -114, 26, -14, 78, 118};//指定 不变
        byte[] result = encrypt(me, cleartext.getBytes());
        return toHex(result);
    }

    public static String decrypt(String seed, String encrypted) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] me = {47, 21, 60, -116, -12, 75, -120, 40, -82, 32, -81, -114, 26, -14, 78, 118};//指定 不变
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(me, enc);
        return new String(result);
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }


    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }
    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }
    private final static String HEX = "0123456789ABCDEF";
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }

}
