package wuliao.in.contact;

import java.util.List;

import wuliao.in.contact.config.Config;
import wuliao.in.contact.domain.User;
import wuliao.in.contact.net.LoginConnection;
import wuliao.in.contact.net.LoginConnection.FailCallback;
import wuliao.in.contact.net.LoginConnection.SuccessCallback;
import wuliao.in.contact.utils.PublicUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	private EditText mPhone,mPassword;
	private Button mCancle,mLogin;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		MyApplication.getInstance().addActivity(this);
		initView();
	}
	private void initView() {
		mPhone=(EditText) findViewById(R.id.et_login_phonename);
		mPassword=(EditText) findViewById(R.id.et_login_password);
		mCancle=(Button) findViewById(R.id.bt_login_cancel);
		mLogin=(Button) findViewById(R.id.bt_login_login);
		sp=getSharedPreferences("token",Activity.MODE_PRIVATE);
		mCancle.setOnClickListener(this);
		mLogin.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login_cancel:
			finish();
			break;
		case R.id.bt_login_login:
			String phone=mPhone.getText().toString().trim();
			String password=mPassword.getText().toString().trim();
			if(phone==null||password==null){
				Toast.makeText(getApplicationContext(), "填写不正确", 0).show();
			}else{
				new LoginConnection(Config.URL, "User", "login", "POST", 
						new SuccessCallback() {				
							@Override
							public void onSuccess(List<User> users,String token) {
								Editor edit=sp.edit();
								edit.putString("token", token);
								edit.putString("user_id", users.get(0).getUserId());
								edit.putString("username",users.get(0).getUsername());
								edit.putString("phone_num", users.get(0).getPhonenum());
								edit.commit();
								Toast.makeText(getApplicationContext(), "登录成功", 0).show();
								Intent intent=new Intent(getApplicationContext(), IndexActivity.class);
								startActivity(intent);
								finish();
							}
						}, new FailCallback() {			
							@Override
							public void onFail(String code) {
								Toast.makeText(getApplicationContext(), "用户名或密码错误", 0).show();
							}
						}, new String[]{"phone_num",PublicUtils.encode(phone),"password",password});
			}
			break;

		default:
			break;
		}
		
	}
}
