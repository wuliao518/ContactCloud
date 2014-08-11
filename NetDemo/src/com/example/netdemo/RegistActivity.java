package com.example.netdemo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.example.netdemo.config.Config;
import com.example.netdemo.net.RegisterConnection;
import com.example.netdemo.net.RegisterConnection.FailCallback;
import com.example.netdemo.net.RegisterConnection.SuccessCallback;
import com.example.netdemo.utils.PublicUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistActivity extends Activity {
	private Button mButton;
	private EditText edit1,edit2,edit3;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp=getSharedPreferences("token", Activity.MODE_PRIVATE);
		String token=sp.getString("token", null);
		String userVal=sp.getString("username", null);
		if(token!=null&&userVal!=null){
			Intent intent=new Intent(RegistActivity.this,IndexActivity.class);
			startActivity(intent);
			finish();
		}else{
			setContentView(R.layout.activity_reg);
			MyApplication.getInstance().addActivity(this);
			initView();
		}
	}
	private void initView() {
		mButton=(Button) findViewById(R.id.bt_reg);
		edit1=(EditText) findViewById(R.id.et_user);
		edit2=(EditText) findViewById(R.id.et_phone);
		edit3=(EditText) findViewById(R.id.et_pwd);	
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final ProgressDialog dialog=new ProgressDialog(RegistActivity.this);
				dialog.setTitle("loading");
				dialog.setMessage("loading content");
				dialog.show();
				String username=edit1.getText().toString();
				final String phone_num=edit2.getText().toString().trim();
				String password=edit3.getText().toString().trim();
				Pattern pattern = Pattern.compile("^[1][0-9]{10}");
				Matcher matcher = pattern.matcher(phone_num);
				if(matcher.matches()){
					new RegisterConnection(Config.URL, "User", "add", "POST", 
							new SuccessCallback() {
								@Override
								public void onSuccess(String token,String user_id) {
									dialog.dismiss();
									Editor edit=sp.edit();
									edit.putString("token",token);
									edit.putString("user_id",user_id);
									edit.putString("phone_num",PublicUtils.encode(phone_num));
									edit.putString("username", edit1.getText().toString().trim());
									edit.commit();
									Toast.makeText(getApplicationContext(), "注册成功", 0).show();
									Intent intent=new Intent(RegistActivity.this,IndexActivity.class);
									startActivity(intent);
									finish();
								}
							}, 
							new FailCallback() {			
								@Override
								public void onFail(String code) {
									dialog.dismiss();
									Toast.makeText(getApplicationContext(), code, 0).show();					
								}
							}, new String[]{"username",username,"phone_num",PublicUtils.encode(phone_num),"password",password});
				}else{
					dialog.dismiss();
					Toast.makeText(getApplicationContext(), "手机号码不正确", 0).show();
				}
			}
		});
	}
}



















