package wuliao.in.contact;

import wuliao.in.contact.config.Config;
import wuliao.in.contact.net.CreateGroupConnerction;
import wuliao.in.contact.net.CreateGroupConnerction.CallBackListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGroup extends Activity implements OnClickListener{
	private Button mCreate,mCancle;
	private SharedPreferences sp;
	private EditText mName,mDesc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);
		sp=getSharedPreferences("token", Activity.MODE_PRIVATE);
		initView();
	}
	private void initView() {
		mCancle=(Button) findViewById(R.id.bt_create_button1);
		mCreate=(Button) findViewById(R.id.bt_create_button2);
		mName=(EditText) findViewById(R.id.et_create_name);
		mDesc=(EditText) findViewById(R.id.et_create_desc);
		mCancle.setOnClickListener(this);
		mCreate.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_create_button1:
			finish();
			break;
		case R.id.bt_create_button2:
			String user_id=sp.getString("user_id", null);
			String groupname=mName.getText().toString().trim();
			String group_desc=mDesc.getText().toString().trim();
			CreateGroupConnerction create=new CreateGroupConnerction(Config.URL,"Group",
					"createGroup","GET",new String[]{"user_id",user_id,"groupname",groupname,"group_desc",group_desc});
			create.setOnCallBackListener(new CallBackListener() {
				@Override
				public void onSuccess() {
					Toast.makeText(getApplicationContext(), "创建成功", 0).show();
				}			
				@Override
				public void onFail(String message) {
					Toast.makeText(getApplicationContext(), message, 0).show();
				}
			});
			break;

		default:
			break;
		}
		
	}
}
