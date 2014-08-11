package wuliao.in.contact;

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
	private Button bt;
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
		phone_num=et.getText().toString().trim();
		if(phone_num.matches("^1[0-9]{10}")){
			bt.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					new UpdateConnection(Config.URL,"Group", "updaterPhone", "GET", 
							new SuccessCallback() {				
								@Override
								public void onSuccess() {
									Editor edit=sp.edit();
									edit.putString("phone_num", phone_num);
									edit.commit();
									Toast.makeText(getApplicationContext(), "修改成功",0).show();
									finish();
								}
							}, new FailCallback() {
								@Override
								public void onFail() {
									Toast.makeText(getApplicationContext(), "修改失败",0).show();
									finish();
								}
							}, new String[]{"user_id",user_id,"phone_num",PublicUtils.encode(phone_num)});
				}
			});
		}else{
			Toast.makeText(getApplicationContext(), "修改失败",0).show();
		}
		
	}
}
