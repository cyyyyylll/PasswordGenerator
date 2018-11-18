package com.cy.innovationproject;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import static com.cy.innovationproject.Util.seed;

public class workActivity extends AppCompatActivity {
    private Button logout_Btn,changePwdBtn;
    private EditText pwdA;
    private AutoCompleteTextView urlE;
    private static String str;
    private static String newPwd="null";
    public Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_work_one);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setTitle("Innovation project");
        permission();

        logout_Btn = findViewById(R.id.logout_button);
        pwdA = findViewById(R.id.editText4);
        urlE = findViewById(R.id.autoCompleteTextView);
        changePwdBtn = findViewById(R.id.changePwdButton);

        initAutoComplete("history",urlE);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.up_progress, null);
        builder1.setView(dialogView)   ;
        final AlertDialog alertDialog = builder1.create();
        alertDialog.setCanceledOnTouchOutside(false);

        if(!User.webPassword.equals("null")){
            if(User.webPassword.equals("false")){
                Toast toast = Toast.makeText(workActivity.this,null,Toast.LENGTH_SHORT);
                toast.setText("识别失败，请将脸正对屏幕进行识别");
                toast.show();
            }else if(User.webPassword.equals("orderError")){
                Toast toast = Toast.makeText(workActivity.this,null,Toast.LENGTH_SHORT);
                toast.setText("口令错误");
                toast.show();
            }else if(User.webPassword.equals("true")){

            }else{
                try {
                    str=Aes.decrypt(seed,User.webPassword);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                User.webPassword="null";
                final AlertDialog.Builder builder = new AlertDialog.Builder(workActivity.this);
                builder.setMessage(str);
                builder.setTitle("密码");
                builder.setPositiveButton("复制", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipData mClipData = ClipData.newPlainText("Simple test", str);
                        ClipboardManager mClipboardManager;
                        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        mClipboardManager.setPrimaryClip(mClipData);
                        Toast toast = Toast.makeText(workActivity.this, null, Toast.LENGTH_SHORT);
                        toast.setText("复制成功");
                        toast.show();
                    }
                });
                builder.show();
            }

        }

        logout_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(urlE.getText().toString().equals("")){
                    Toast toast = Toast.makeText(workActivity.this,null,Toast.LENGTH_SHORT);
                    toast.setText("网址不能为空");
                    toast.show();
                }else if(pwdA.getText().toString().equals("")){
                    Toast toast = Toast.makeText(workActivity.this,null,Toast.LENGTH_SHORT);
                    toast.setText("口令不能为空");
                    toast.show();
                }else {
                    EditText webAddressEdit = findViewById(R.id.autoCompleteTextView);
                    EditText passwordEdit = findViewById(R.id.editText4);
                    User.webAddress = webAddressEdit.getText().toString();
                    User.order = passwordEdit.getText().toString();
                    String webAddress = User.webAddress;
                    saveHistory("history",urlE);
                    Intent intent = new Intent(workActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
            }
        });

        changePwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View changePwdView = getLayoutInflater().inflate(R.layout.change_pwd_dialog,null);
                final AlertDialog changePwdDialog = new AlertDialog.Builder(workActivity.this)
                        .setTitle("Change Password")
                        .setPositiveButton("确定",null)
                        .setView(changePwdView)
                        .create();
                final EditText changePwdWebAddressEditText = changePwdView.findViewById(R.id.changePwdWebAddressEditText);
                final EditText changePwdPasswordEditText = changePwdView.findViewById(R.id.changePwdPasswordEditText);
//                final String webAddress = changePwdWebAddressEditText.getText().toString();
//                final String password = changePwdPasswordEditText.getText().toString();
                changePwdDialog.show();
                changePwdDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Util.findThread(webAddress);
//                            }
//                        }).start();
//                        final View verification_view = getLayoutInflater().inflate(R.layout.find_verificationcodel,null);
//                        final EditText verification_edit = verification_view.findViewById(R.id.verificationCode_edit);
//                        timeView = verification_view.findViewById(R.id.time_view);
//                        final AlertDialog dialog1 = new AlertDialog.Builder(workActivity.this)
//                                .setTitle("Change Password")
//                                .setPositiveButton("确定",null)
//                                .setView(verification_view)
//                                .create();
//                            task[0] = new TimerTask() {
//                            @Override
//                            public void run() {
//                                Message message = new Message();
//                                message.what=1;
//                                handler.sendMessage(message);
//                                if(!dialog1.isShowing()){
//                                    task[0].cancel();
//                                    time=119;
//                                }else if(time==0){
//                                    dialog1.dismiss();
//                                    time =119;
//                                    task[0].cancel();
//                                }
//                            }
//
//                        };
//                        final Thread thread1 = new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                thread.start();
//                            }
//                        });
//                        thread1.start();
//                        dialog1.show();
//                        changePwdDialog.dismiss();
//                        final String verificationCode = verification_edit.getText().toString();
//
//                        dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        try {
//                                            Util.find_verificationCode(Aes.encrypt(Util.seed,verificationCode));
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }).start();
//                            }
//                        });
                        final String webAddress;
                        final String password;
                        if(changePwdWebAddressEditText.getText().toString().equals("")){
                            Toast toast = Toast.makeText(workActivity.this,null,Toast.LENGTH_SHORT);
                            toast.setText("网址不能为空");
                            toast.show();
                        }else if(changePwdPasswordEditText.getText().toString().equals("")){
                            Toast toast = Toast.makeText(workActivity.this,null,Toast.LENGTH_SHORT);
                            toast.setText("密码不能为空");
                            toast.show();
                        }else{
                            try {
                                webAddress = Aes.encrypt(Util.seed,changePwdWebAddressEditText.getText().toString());
                                password = Aes.encrypt(Util.seed,changePwdPasswordEditText.getText().toString());
                                Log.v("tagC","web:"+webAddress);
                                Log.v("tagC","pwd:"+password);
                                if(webAddress.equals("")){
                                    Toast toast = Toast.makeText(workActivity.this,null,Toast.LENGTH_SHORT);
                                    toast.setText("网址不能为空");
                                    toast.show();
                                }else{
//                                    new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            try{
//                                                Looper.prepare();
//                                                String result = Util.changePwd(webAddress,password);
//                                                Message message = new Message();
//                                                message.obj = result;
//                                                handler.sendMessage(message);
//                                                Looper.loop();
//                                                newPwd = result;
//                                                Log.v("tagC",result);
//                                                if(newPwd.equals("webNull")){
//                                                    Toast toast = Toast.makeText(workActivity.this,null,Toast.LENGTH_SHORT);
//                                                    toast.setText("网址不存在");
//                                                    toast.show();
//                                                    Log.v("tagC",result+"1");
//                                                    alertDialog.dismiss();
//                                                }else if(newPwd.equals("false")){
//                                                    Toast toast = Toast.makeText(workActivity.this,null,Toast.LENGTH_SHORT);
//                                                    toast.setText("密码错误");
//                                                    toast.show();
//                                                    Log.v("tagC",result+"2");
//                                                    alertDialog.dismiss();
//                                                } else{
//                                                    newPwd = Aes.decrypt(seed,result);
//                                                    Log.v("tagC",result+"3");
//                                                    //Looper.prepare();
//                                                    final AlertDialog.Builder builder = new AlertDialog.Builder(workActivity.this);
//                                                    builder.setMessage(newPwd);
//                                                    Log.v("tagC",newPwd);
//                                                    builder.setTitle("密码");
//                                                    builder.setPositiveButton("复制", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//                                                            Looper.prepare();
//                                                            ClipData mClipData = ClipData.newPlainText("Simple test", newPwd);
//                                                            ClipboardManager mClipboardManager;
//                                                            mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                                                            mClipboardManager.setPrimaryClip(mClipData);
//                                                            Toast toast = Toast.makeText(workActivity.this, null, Toast.LENGTH_SHORT);
//                                                            toast.setText("复制成功");
//                                                            toast.show();
//                                                            builder.create();
//                                                            alertDialog.dismiss();
//                                                            changePwdDialog.dismiss();
//                                                            builder.show();
//                                                            Looper.loop();
//                                                        }
//                                                    });

//                                                }
//                                            }catch (Exception e){
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }).start();

                                    new changePwdThread(webAddress,password).start();
                                    handler = new Handler(){
                                        @Override
                                        public void handleMessage(Message message){
                                            String newPwd = (String) message.obj;
                                            Log.v("tagC",newPwd+"11");
                                            if(newPwd.equals("webNull")){
                                                Log.v("tagC",newPwd+"2");
                                                Toast toast = Toast.makeText(workActivity.this,null,Toast.LENGTH_SHORT);
                                                toast.setText("网址不存在");
                                                toast.show();
                                                // Log.v("tagC",result+"1");
                                                alertDialog.dismiss();
                                            }else if(newPwd.equals("false")){
                                                Toast toast = Toast.makeText(workActivity.this,null,Toast.LENGTH_SHORT);
                                                toast.setText("密码错误");
                                                toast.show();
                                                // Log.v("tagC",result+"2");
                                                alertDialog.dismiss();
                                            } else{
                                                Log.v("tagC",newPwd+"3");
                                                alertDialog.dismiss();
                                                changePwdDialog.dismiss();
                                                try {
                                                    newPwd = Aes.decrypt(seed,newPwd);
                                                    Log.v("tagC",newPwd+"666");
                                                    final String result = newPwd;
                                                    // Log.v("tagC",result+"3");
                                                    //Looper.prepare();
                                                    final AlertDialog.Builder builder = new AlertDialog.Builder(workActivity.this);
                                                    builder.setMessage(newPwd);
                                                    Log.v("tagC",newPwd);
                                                    builder.setTitle("密码");
                                                    builder.setPositiveButton("复制", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            ClipData mClipData = ClipData.newPlainText("Simple test", result);
                                                            ClipboardManager mClipboardManager;
                                                            mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                                            mClipboardManager.setPrimaryClip(mClipData);
                                                            Toast toast = Toast.makeText(workActivity.this, null, Toast.LENGTH_SHORT);
                                                            toast.setText("复制成功");
                                                            toast.show();
                                                        }
                                                    });
                                                    //builder.create();
                                                    builder.show();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        }
                                    };
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // changePwdDialog.dismiss();
                            alertDialog.show();


                        }
                    }
                });
            }
        });
    }

    class changePwdThread extends Thread{
        private String webAddress;
        private String password;
        private Looper looper;
        public changePwdThread(String webAddress,String password){
            this.webAddress=webAddress;
            this.password=password;
        }
        @Override
        public void run(){
            try {
                Looper.prepare();
                looper = Looper.myLooper();
                String result ;
                result = Util.changePwd(webAddress,password);
                Message message = new Message();
                message.obj = result;
                handler.sendMessage(message);
                Looper.loop();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    void initAutoComplete(String filed, AutoCompleteTextView autoCompleteTextView){
        SharedPreferences sharedPreferences = getSharedPreferences("network_url",0);
        String longHistory = sharedPreferences.getString("history","");
        if(longHistory.length()>1){
            String[]  hisArrays = longHistory.split(",");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, hisArrays);
            if(hisArrays.length > 5){
                String[] newArrays = new String[5];
                System.arraycopy(hisArrays, 0, newArrays, 0, 5);
                adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, newArrays);
            }
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setDropDownHeight(350);
            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.setCompletionHint("最近5条记录");
            autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    AutoCompleteTextView view = (AutoCompleteTextView) v;
                    if (hasFocus) {
                        view.showDropDown();
                    }
                }
            });
        }

    }

    private void saveHistory(String field,AutoCompleteTextView auto) {
        String text = auto.getText().toString();
        SharedPreferences sp = getSharedPreferences("network_url", 0);
        String longHistory = sp.getString(field, "");
        if (!longHistory.contains(text + ",")) {
            StringBuilder sb = new StringBuilder(longHistory);
            sb.insert(0, text + ",");
            sp.edit().putString("history", sb.toString()).commit();
        }
    }

    private void permission(){
        int permissionCode = ContextCompat.checkSelfPermission(workActivity.this,"android.permission.CAMERA");
        if(permissionCode==-1){
            Log.v("tagC",String.valueOf(permissionCode));
            ActivityCompat.requestPermissions(workActivity.this,new String[]{"android.permission.STORAGE","android.permission.CAMERA"}, 0);
        }
    }
}
