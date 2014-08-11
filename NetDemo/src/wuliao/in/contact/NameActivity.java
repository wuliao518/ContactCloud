package wuliao.in.contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NameActivity extends Activity implements OnClickListener{
	private Button mBReg,mBLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name);
		MyApplication.getInstance().addActivity(this);
		initView();
	}
	private void initView() {
		mBReg=(Button) findViewById(R.id.bt_name_reg);
		mBLogin=(Button) findViewById(R.id.bt_name_login);
		mBReg.setOnClickListener(this);
		mBLogin.setOnClickListener(this);;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_name_reg:
			Intent reg=new Intent(this,RegistActivity.class);
			startActivity(reg);
			finish();
			break;
		case R.id.bt_name_login:
			Intent login=new Intent(this,LoginActivity.class);
			startActivity(login);
			finish();
			break;
		default:
			break;
		}
		
	}
}
