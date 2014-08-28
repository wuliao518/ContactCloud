package wuliao.in.contact;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wuliao.in.contact.config.Config;
import wuliao.in.contact.net.RegisterConnection;
import wuliao.in.contact.net.RegisterConnection.FailCallback;
import wuliao.in.contact.net.RegisterConnection.SuccessCallback;
import wuliao.in.contact.utils.PublicUtils;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistActivity extends Activity {
	private Button mButton,mCancle;
	private EditText edit1,edit2,edit3;
	private SharedPreferences sp;
	private Dialog dialog;
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
		mCancle=(Button) findViewById(R.id.bt_cancle);
		dialog=PublicUtils.createLoadingDialog(RegistActivity.this, "正在注册");
		mCancle.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(RegistActivity.this,NameActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String username=edit1.getText().toString();
				final String phone_num=edit2.getText().toString().trim();
				String password=edit3.getText().toString().trim();
				Pattern pattern = Pattern.compile("^[1][0-9]{10}");
				Matcher matcher = pattern.matcher(phone_num);
				if(matcher.matches()&&!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
					dialog.show();
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
					Toast.makeText(getApplicationContext(), "输入不正确", 0).show();
				}
			}
		});
	}
}



















