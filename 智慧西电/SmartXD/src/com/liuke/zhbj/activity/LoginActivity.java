package com.liuke.zhbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.liuke.zhbj.R;
import com.liuke.zhbj.global.GlobalConstants;
import com.liuke.zhbj.utils.MD5Utils;
import com.liuke.zhbj.utils.PrefUtils;
import com.liuke.zhbj.view.ClearEditText;

public class LoginActivity extends Activity implements OnClickListener{
    private EditText et_pwd;
    private ClearEditText et_phone_num;
    private Button btn_login;
	private EditText etName;
	private String phone_num;
	private String pwd;
	private String name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        init();

    }


    private void init() {
        btn_login = (Button) findViewById(R.id.login_btn_login);
        et_phone_num = (ClearEditText) findViewById(R.id.login_et_phone_num);
        et_pwd = (EditText) findViewById(R.id.login_et_pwd);
        btn_login.setOnClickListener(this);
        etName = (EditText) findViewById(R.id.login_et_name);
        etName.setVisibility(View.INVISIBLE);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.login_btn_find_pwd:
//                startActivity(new Intent(LoginActivity.this, FindPwdActivity.class));
                break;
            case R.id.login_btn_login:
                login();
                break;
        }
    }

    private void login() {
        phone_num = et_phone_num.getText().toString().trim();
        pwd = et_pwd.getText().toString().trim();
        PrefUtils.putString(this, "pwd",MD5Utils.encode(pwd));
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("phone_num", phone_num);
        params.addQueryStringParameter("name",name);
        params.addQueryStringParameter("pwd", MD5Utils.encode(pwd));
        params.addQueryStringParameter("flag", "add");
        httpUtils.send(HttpMethod.POST,GlobalConstants.LOGIN_URL,params,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				if(!TextUtils.isEmpty(result)){
					System.out.println(result);
					setLoginStatus(result);
				}else{
					Toast.makeText(LoginActivity.this, "服务器返回值为空哦", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
			}
		});
        
    }


    

    protected void setLoginStatus(String result) {
    	String code = result.substring(0, 1);
    	System.out.println(result);
    	if(code.equals("0")){//密码错误
    		et_pwd.setError("密码错误");
    		et_pwd.setText("");
    	}else if(code.equals("1")){//登陆成功
    		name=result.substring(1);
    		PrefUtils.putBoolean(this, "has_login", true);
            PrefUtils.putString(this, "phone_num", phone_num);
            PrefUtils.putString(this, "name", name);
            startActivity(new Intent(this, MainActivity.class));
            finish();
    	}
	}


	@Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
