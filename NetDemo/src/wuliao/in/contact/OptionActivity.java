package wuliao.in.contact;

import wuliao.in.contact.config.Config;
import wuliao.in.contact.net.OptionNet;
import wuliao.in.contact.net.OptionNet.FailCallback;
import wuliao.in.contact.net.OptionNet.SuccessCallback;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OptionActivity extends Activity implements OnClickListener{
	private EditText mEditText;
	private Button button;
	private SharedPreferences sp;
	private String user_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_option);
		initView();
	}
	private void initView() {
		mEditText=(EditText) findViewById(R.id.et_option);
		button=(Button) findViewById(R.id.bt_option);
		sp=getSharedPreferences("token", Activity.MODE_PRIVATE);
		user_id=sp.getString("user_id", null);
		button.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_option:
			
			String option=mEditText.getText().toString().trim();
			if(option.length()>500){
				Toast.makeText(getApplicationContext(), "您太热心了！写的有点多。", 0).show();
			}else{
				new OptionNet(Config.URL, "Option", "addOption", "POST", new SuccessCallback() {			
					@Override
					public void onSuccess() {
						button.setClickable(true);
						Toast.makeText(getApplicationContext(), "提交成功，我们会尽快修改！", 0).show();
						finish();
					}
				}, new FailCallback() {	
					@Override
					public void onFail(String msg) {
						button.setClickable(true);
						Toast.makeText(getApplicationContext(), msg, 0).show();
					}
				}, new String[]{"user_id",user_id,"add_time",System.currentTimeMillis()+"","user_option",option});
			}
			break;

		default:
			break;
		}
		
	}
	
}
