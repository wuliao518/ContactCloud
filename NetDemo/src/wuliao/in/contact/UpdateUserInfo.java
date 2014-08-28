package wuliao.in.contact;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wuliao.in.contact.config.Config;
import wuliao.in.contact.net.UpdateConnection;
import wuliao.in.contact.net.UpdateConnection.FailCallback;
import wuliao.in.contact.net.UpdateConnection.SuccessCallback;
import wuliao.in.contact.utils.PublicUtils;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateUserInfo extends Activity {
	private EditText et;
	private Button bt,cancle;
	private String user_id,phone_num;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		sp=getSharedPreferences("token", Activity.MODE_PRIVATE);
		user_id=sp.getString("user_id", null);
		initView();
	}
	private void initView() {
		et=(EditText) findViewById(R.id.et_update_num);
		bt=(Button) findViewById(R.id.bt_update);
		cancle=(Button) findViewById(R.id.bt_update_cancle);
		cancle.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		bt.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					bt.setClickable(false);
					phone_num=et.getText().toString().trim();
					Toast.makeText(getApplicationContext(), phone_num, 0).show();
					Pattern pattern = Pattern.compile("^[1][0-9]{10}");
					Matcher matcher = pattern.matcher(phone_num);				
					if(matcher.matches()){
						new UpdateConnection(Config.URL,"Group", "updaterPhone", "GET", 
								new SuccessCallback() {				
									@Override
									public void onSuccess() {
										Editor edit=sp.edit();
										edit.putString("phone_num", PublicUtils.encode(phone_num));
										edit.commit();
										Toast.makeText(getApplicationContext(), "修改成功",0).show();
										finish();
									}
								}, new FailCallback() {
									@Override
									public void onFail(String msg) {
										bt.setClickable(true);
										Toast.makeText(getApplicationContext(), msg,0).show();
									}
								}, new String[]{"user_id",user_id,"phone_num",PublicUtils.encode(phone_num)});
					}else{
						bt.setClickable(true);
						Toast.makeText(getApplicationContext(), "输入号码不合法",0).show();
					}
				}
			});
		
	}
}
