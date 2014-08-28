package wuliao.in.contact;

import wuliao.in.contact.config.Config;
import wuliao.in.contact.domain.Group;
import wuliao.in.contact.net.JoinConnection;
import wuliao.in.contact.net.JoinConnection.SuccessCallback;
import wuliao.in.contact.provider.ProviderUtils;
import wuliao.in.contact.utils.PublicUtils;
import wuliao.in.contact.utils.PublicUtils.CallBackListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class GroupShowActivity extends Activity implements OnClickListener{
	private SharedPreferences sp;
	private Button mButton;
	private TextView mTextView;
	private String token,username,group_id,groupname,user_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_desc);
		MyApplication.getInstance().addActivity(this);
		sp=getSharedPreferences("token", Activity.MODE_PRIVATE);
		token=sp.getString("token", null);
		username=sp.getString("username", null);
		user_id=sp.getString("user_id", null);
		initView();
		groupname=(String) getIntent().getExtras().get("groupname");
		group_id=(String) getIntent().getExtras().get("group_id");
		mTextView.setText(groupname);	
	}
	private void initView() {
		mButton=(Button) findViewById(R.id.button1);
		mTextView=(TextView) findViewById(R.id.tv_group_show);
		mButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:	
			PublicUtils utils=new PublicUtils(GroupShowActivity.this, sp);
			utils.setCallBackListener(new CallBackListener() {			
				@Override
				public void onSuccess() {					
					joinGroup();
				}	
				@Override
				public void onFail() {				
				}
			});
			utils.validate();
			break;

		default:
			break;
		}
		
	}
	public void joinGroup(){
		new JoinConnection(Config.URL, "Item", "index", "POST",
				new SuccessCallback() {				
					@Override
					public void onSuccess(Group group) {
						Toast.makeText(getApplicationContext(), "成功加入", 1).show();
						ProviderUtils provider=new ProviderUtils(getApplicationContext());
						provider.loadUser(group_id, group);
						
					}
				}, new wuliao.in.contact.net.JoinConnection.FailCallback() {		
					@Override
					public void onFail(String code) {						
						Toast.makeText(getApplicationContext(), code, 1).show();
					}
				}, "group_id",group_id,"user_id",user_id);
	}
	
	
	
	
	
	
	
	
}
