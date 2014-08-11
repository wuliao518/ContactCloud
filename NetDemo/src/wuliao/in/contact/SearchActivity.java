package wuliao.in.contact;

import java.util.ArrayList;
import java.util.List;

import wuliao.in.contact.config.Config;
import wuliao.in.contact.domain.Group;
import wuliao.in.contact.net.JoinConnection;
import wuliao.in.contact.net.SearchConnection;
import wuliao.in.contact.net.SearchConnection.FailCallback;
import wuliao.in.contact.net.SearchConnection.SuccessCallback;
import wuliao.in.contact.provider.ProviderUtils;
import wuliao.in.contact.utils.PublicUtils;
import wuliao.in.contact.utils.PublicUtils.CallBackListener;
import wuliao.in.contact.view.ClearEditText;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener{
	private EditText mEditText;
	private TextView mTextView;
	private ListView mListView;
	private SharedPreferences sp;
	private String user_id;
	private List<Group> groups=new ArrayList<Group>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		MyApplication.getInstance().addActivity(this);
		initView();
	}
	private void initView() {
		mEditText=(EditText) findViewById(R.id.search_text);
		mTextView=(TextView) findViewById(R.id.tv_search);
		mListView=(ListView) findViewById(R.id.lv_search_group);
		sp=getSharedPreferences("token", Activity.MODE_PRIVATE);
		user_id=sp.getString("user_id", null);
		mTextView.setOnClickListener(this);
		//设置listview事件
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				Intent intent=new Intent(SearchActivity.this,GroupShowActivity.class);
//				intent.putExtra("groupname", groups.get(arg2).getGroupName());
//				intent.putExtra("group_id", groups.get(arg2).getGroupId());
//				startActivity(intent);
//			}
//		});	
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_search:
			final ProgressDialog dialog=new ProgressDialog(SearchActivity.this);
			dialog.setMessage("Searching");
			dialog.show();
			final MyAdapter myAdapter=new MyAdapter();
			String key=mEditText.getText().toString().trim();
			if(TextUtils.isEmpty(key)){
				Toast.makeText(getApplicationContext(), "不能为空", 1).show();
				return;
			}
			new SearchConnection(Config.URL,"Group","searchGroup","GET",
					new SuccessCallback() {
						@Override
						public void onSuccess(List<Group> strs) {
							dialog.dismiss();
							groups=strs;
							myAdapter.notifyDataSetChanged();
							mListView.setAdapter(myAdapter);
						}
				},new FailCallback() {
					@Override
					public void onFail(String code) {
						dialog.dismiss();
						Toast.makeText(getApplicationContext(), code, 1).show();
					}
				},new String[]{"key",key});
				
			break;

		default:
			break;
		}
		
	}
	private class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return groups.size();
		}
		@Override
		public Object getItem(int position) {
			return groups.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Group group=groups.get(position);
			final String group_id=group.getGroupId();
			LinearLayout ll=(LinearLayout) View.inflate(getApplicationContext(), R.layout.search_group, null);
			TextView tv=(TextView) ll.findViewById(R.id.tv_group);
			Button bt=(Button) ll.findViewById(R.id.bt_group_add);
			bt.setOnClickListener(new OnClickListener() {		
				@Override
				public void onClick(View v) {
					
					PublicUtils utils=new PublicUtils(SearchActivity.this, sp);
					utils.setCallBackListener(new CallBackListener() {			
						@Override
						public void onSuccess() {					
							joinGroup(group_id);
						}	
						@Override
						public void onFail() {				
						}
					});
					utils.validate();
				}
			});
			tv.setText(groups.get(position).getGroupName());
			return ll;
		}
		public void joinGroup(final String group_id){
			new JoinConnection(Config.URL, "Item", "index", "POST",
					new JoinConnection.SuccessCallback() {				
						@Override
						public void onSuccess(Group group) {
							Toast.makeText(getApplicationContext(), "成功加入", 1).show();
							ProviderUtils provider=new ProviderUtils(getApplicationContext());
							provider.loadUser(group_id, group);
							
						}
					}, new JoinConnection.FailCallback() {		
						@Override
						public void onFail(String code) {						
							Toast.makeText(getApplicationContext(), code, 1).show();
						}
					}, "group_id",group_id,"user_id",user_id);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
