package com.cy.innovationproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import static com.cy.innovationproject.Util.seed;

public class loginActivity extends Activity {
    private Button login_Button;
    private Button registered_Button;
    private Button findPwd_button;
    private EditText editTextUsr,editTextPwd;
    private static String username_reg,password_reg,cmfpassword_reg;
    private static String user_find_text;
    private static String verificationCode;
    private static String reg_mail;
    private static String resetPwd,cmfResetPwd;
    private  int time=119;
    public static int mailCode;
    private static TextView timeView;
    public Handler handler1;
    public Handler handler2;
    public Handler handler3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        User.regCamera = 1;


/**
 * 隐藏标题
 */
//        getSupportActionBar().hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        login_Button = findViewById(R.id.login_Button);
        registered_Button = findViewById(R.id.registered_Button);
        findPwd_button = findViewById(R.id.findPwd_btn);


        editTextPwd = findViewById(R.id.pwd_EditText);
        editTextUsr = findViewById(R.id.account_autoCompleteTextView);

        final TimerTask[] task = {null};
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                final String t=String.valueOf(time);
                super.handleMessage(message);
                timeView.setText(t+"s后失效");
                time--;
            }
        };

        final Thread thread = new Thread((new Runnable() {
            final Timer timer = new Timer();
            @Override
            public void run() {
                timer.schedule(task[0], 1000, 1000);
            }
        }));
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.login_progress, null);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//去掉这句话，背景会变暗
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final  String username = editTextUsr.getText().toString().trim();
                final  String password = editTextPwd.getText().toString().trim();
                if (username.equals("")) {
                    Toast toast = Toast.makeText(loginActivity.this, null, Toast.LENGTH_SHORT);
                    toast.setText("账号不能为空");
                    toast.show();
                } else if (password.equals("")) {
                    Toast toast = Toast.makeText(loginActivity.this, null, Toast.LENGTH_SHORT);
                    toast.setText("密码不能为空");
                    toast.show();
                } else if (!username.equals(Util.StringFilter(username)) || !password.equals(Util.StringFilter(password))) {
                    Toast toast = Toast.makeText(loginActivity.this, null, Toast.LENGTH_SHORT);
                    toast.setText("含不合法的字符");
                    toast.show();
                } else {
                    alertDialog.show();
                    new Thread() {
                        public void run() {
                            Looper.prepare();
                            try {
                                User.username = Aes.encrypt(seed,username);
                                String result = Util.loginByPost(Aes.encrypt(seed,username), Aes.encrypt(seed,password));
                                if(result.equals("true")) {
                                    Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                                    toast.setText("欢迎");
                                    toast.show();
                                    Intent intent = new Intent(loginActivity.this, workActivity.class);
                                    startActivity(intent);
                                    alertDialog.dismiss();
                                    editTextPwd.setText("");
                                }else if (result.equals("passwordError")){
                                    Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                                    toast.setText("密码错误");
                                    toast.show();
                                }else if (result.equals("nameError")){
                                    Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                                    toast.setText("密码错误");
                                    toast.show();
                                }
                                alertDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Looper.loop();
                        }
                    }.start();
                }

            }

        });

        registered_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        permission();
                    }
                }).start();
                final View registered_view = getLayoutInflater().inflate(R.layout.registered_dialog,null);
                final EditText username_edit = registered_view.findViewById(R.id.username_edit);
                final EditText password_edit = registered_view.findViewById(R.id.password_edit);
                final EditText cmfPassword_edit = registered_view.findViewById(R.id.cmfPassword_edit);
                final EditText regmail_edit = registered_view.findViewById(R.id.mail_edit);
                final AlertDialog registered_dialog = new AlertDialog.Builder(loginActivity.this)
                        .setTitle("User registration")
                        .setView(registered_view)
                        .setPositiveButton("确认",null)
                        .create();
                registered_dialog.show();
                registered_dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        username_reg = username_edit.getText().toString();
                        password_reg = password_edit.getText().toString();
                        cmfpassword_reg = cmfPassword_edit.getText().toString();
                        reg_mail = regmail_edit.getText().toString();
                        if(username_reg.equals("")){
                            Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                            toast.setText("账号不能为空");
                            toast.show();
                        }else if(password_reg.equals("")){
                            Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                            toast.setText("密码不能为空");
                            toast.show();
                        }else if(cmfpassword_reg.equals("")){
                            Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                            toast.setText("确认密码不能为空");
                            toast.show();
                        }else if(!password_reg.equals(cmfpassword_reg)){
                            Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                            toast.setText("两次密码不一致");
                            toast.show();
                        }else{
                            try {
//                                Util.registeredThread(Aes.encrypt(seed,username_reg),Aes.encrypt(seed,password_reg),Aes.encrypt(seed,reg_mail));
//                                Thread.sleep(500);
//                                int regCode = User.regCode;
//                                Log.v("tagC",String.valueOf(regCode));

                                new regThread(Aes.encrypt(seed,username_reg),Aes.encrypt(seed,password_reg),Aes.encrypt(seed,reg_mail)).start();

                                handler1 = new Handler(){
                                    @Override
                                    public void handleMessage(Message msg){
                                        int Code = (int)msg.obj;
                                        Log.v("tagC",String.valueOf(Code));
                                        if(Code==1){
                                            User.regCode=0;
                                            User.regCamera=0;
                                            Intent intent = new Intent(loginActivity.this,CameraActivity.class);
                                            startActivity(intent);
                                            Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                                            toast.setText("欢迎,请将脸正对屏幕");
                                            toast.show();
                                            registered_dialog.dismiss();
                                        }else if(Code==0){
                                            User.regCode=0;
                                            Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                                            toast.setText("注册失败");
                                            toast.show();
                                        }else if(Code==2){
                                            User.regCode=0;
                                            Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                                            toast.setText("用户名已注册");
                                            toast.show();
                                        }
                                        else if(Code==3){
                                            Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                                            toast.setText("邮箱已注册");
                                            toast.show();
                                        }
                                    }
                                };
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
          });

        findPwd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View findpwd_view = getLayoutInflater().inflate(R.layout.findpwd_dialog,null);
                final EditText user_find = findpwd_view.findViewById(R.id.user_find);
                final AlertDialog find_dialog = new AlertDialog.Builder(loginActivity.this)
                        .setTitle("Find back Password")
                        .setView(findpwd_view)
                        .setPositiveButton("获取验证码",null)
                        .create();
                find_dialog.show();
                find_dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user_find_text = user_find.getText().toString();
                        if(user_find_text.equals("")) {
                            Toast toast = Toast.makeText(loginActivity.this, null, Toast.LENGTH_SHORT);
                            toast.setText("用户名不能为空");
                            toast.show();
                        }else{//1.中间执行发送报文，第二个dialog
                            try {
                                final String user_find_text_=Aes.encrypt(seed,user_find_text);
                                Util.findThread(user_find_text_);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            /**
                             * 验证码dialog
                             */
                            final View verification_view = getLayoutInflater().inflate(R.layout.find_verificationcodel,null);
                            final EditText verification_edit = verification_view.findViewById(R.id.verificationCode_edit);
                            timeView = verification_view.findViewById(R.id.time_view);
                            final AlertDialog verification_dialog = new AlertDialog.Builder(loginActivity.this)
                                    .setTitle("Find back password")
                                    .setView(verification_view)
                                    .setPositiveButton("确定",null)
                                    .create();
                            verification_dialog.show();
                            final int[] clickCount = {0};
                            /**
                             * 开启倒计时
                             */
                            task[0] = new TimerTask() {
                                @Override
                                public void run() {
                                    Message message = new Message();
                                    message.what=1;
                                    handler.sendMessage(message);
                                    if(!verification_dialog.isShowing()){
                                        task[0].cancel();
                                        time=119;
                                    }else if(time==0){
                                        verification_dialog.dismiss();
                                        time =119;
                                        task[0].cancel();
                                    }
                                }

                            };
                            final Thread thread1 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    thread.start();
                                }
                            });
                            thread1.start();

                            verification_dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   // clickCount[0]++;
                                    verificationCode = verification_edit.getText().toString();
                                    if(verificationCode.equals("")){
                                        Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                                        toast.setText("验证码不能为空");
                                        toast.show();
                                    }else{//2.中间执行发送报文，第三个dialog
                                        new findVerificationCodeThread(verificationCode).start();
                                        handler2 = new Handler(){
                                            @Override
                                            public void handleMessage(Message msg){
                                                int Code = (int)msg.obj;
                                                Log.v("tagC",String.valueOf(Code));
                                                if(Code==1){
                                                    loginActivity.mailCode=0;
                                                    final View resetPwd_view = getLayoutInflater().inflate(R.layout.reset_pwd,null);
                                                    final EditText resetPwd_edit = resetPwd_view.findViewById(R.id.resetPwd_edit);
                                                    final EditText cmf_resetPwd_edit = resetPwd_view.findViewById(R.id.cmf_resetPwd_edit);
                                                    final AlertDialog resetPwd_dialog = new AlertDialog.Builder(loginActivity.this)
                                                            .setTitle("Reset password")
                                                            .setView(resetPwd_view)
                                                            .setPositiveButton("确定",null)
                                                            .create();
                                                    resetPwd_dialog.show();
                                                    resetPwd_dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            resetPwd = resetPwd_edit.getText().toString();
                                                            cmfResetPwd = cmf_resetPwd_edit.getText().toString();
                                                            if(resetPwd.equals("")){
                                                                Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                                                                toast.setText("新密码不能为空");
                                                                toast.show();
                                                            }else if(cmfResetPwd.equals("")){
                                                                Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                                                                toast.setText("确认密码不能为空");
                                                                toast.show();
                                                            }else if(!resetPwd.equals(cmfResetPwd)){
                                                                Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                                                                toast.setText("两次密码不一致");
                                                                toast.show();
                                                            }else{
//                                                                try {
//                                                                    Util.resetPwd(Aes.encrypt(seed,user_find_text),Aes.encrypt(seed,resetPwd));
//                                                                } catch (Exception e) {
//                                                                    e.printStackTrace();
//                                                                }
//                                                                try {
//                                                                    Thread.sleep(300);
//                                                                } catch (InterruptedException e) {
//                                                                    e.printStackTrace();
//                                                                }
                                                                try {
                                                                    new resetPwdThread(Aes.encrypt(seed,user_find_text),Aes.encrypt(seed,resetPwd)).start();
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                handler3 = new Handler(){
                                                                    @Override
                                                                    public void handleMessage(Message msg){
                                                                        String resetSoF = (String) msg.obj;
                                                                        if(resetSoF.equals("true")){
                                                                            Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                                                                            toast.setText("密码重置成功");
                                                                            toast.show();
                                                                            resetPwd_dialog.dismiss();
                                                                        }else{
                                                                            Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                                                                            toast.setText("密码重置失败");
                                                                            toast.show();
                                                                        }
                                                                    }
                                                                };
                                                                //int resetSoF = User.resetSorF;

                                                            }
                                                        }
                                                    });
                                                //    clickCount[0]=0;
                                                    verification_dialog.dismiss();
                                                }else{
                                                 //   if(clickCount[0]>=2){
                                                        Toast toast = Toast.makeText(loginActivity.this,null,Toast.LENGTH_SHORT);
                                                        toast.setText("验证码错误");
                                                        toast.show();
                                                  //  }
                                                }
                                            }
                                        };
//                                        try {
//                                            Util.find_verificationCode(Aes.encrypt(seed,verificationCode));
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        int Code = loginActivity.mailCode;
//                                        Log.v("tagC","Code="+String.valueOf(Code));

                                    }//2.中间执行发送报文，第三个dialog
                                }
                            });
                            find_dialog.dismiss();
                        }//1.中间执行发送报文，第二个dialog
                    }
                });//findDialog.getButton
            }//findPwd_button onClick
        });
    }//onCreate

    public void onBackPressed() {
        // super.onBackPressed(); 	不要调用父类的方法
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private int permission(){
        int permissionCode = ContextCompat.checkSelfPermission(loginActivity.this,"android.permission.CAMERA");
        if(permissionCode== PermissionChecker.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(loginActivity.this,new String[]{"android.permission.CAMERA"}, 0);
        }
        return permissionCode;
    }



    class regThread extends Thread{
        private Looper looper;
        private String _username;
        private String _password;
        private String _mail;
        public regThread(String _username,String _password,String _mail){
            this._mail=_mail;
            this._password=_password;
            this._username=_username;
        }
        public void run(){
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
                    Looper.prepare();
                    looper = Looper.myLooper();
                    Message message = new Message();
                    message.obj = (int)b[0]-48;
                    User.webPassword="null";
                    User.username=username;
                    handler1.sendMessage(message);
                    Looper.loop();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class findVerificationCodeThread extends Thread{
        private String verificationCode;
        private Looper looper;
        public findVerificationCodeThread(String verificationCode){
            this.verificationCode = verificationCode;
        }
        public void run(){
            try{
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
                    Looper.prepare();
                    looper = Looper.myLooper();
                    Message message = new Message();
                    if(result.equals("true")){
                        message.obj = 1;
                        handler2.sendMessage(message);
                    }else{
                        message.obj=0;
                        handler2.sendMessage(message);
                    }
                    Looper.loop();
                }
                bos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class resetPwdThread extends Thread{
        private String username;
        private String password;
        private Looper looper;
        public resetPwdThread(String username,String password){
            this.username = username;
            this.password = password;
        }
        @Override
        public void run(){
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
//                    if(result.equals("true")){
//                        User.resetSorF=1;
//                    }else{
//                        User.resetSorF=0;
//                    }
                    Looper.prepare();
                    looper = Looper.myLooper();
                    Message message = new Message();
                    message.obj = result;
                    handler3.sendMessage(message);
                    Looper.loop();
                }
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}














