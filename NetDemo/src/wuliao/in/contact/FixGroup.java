package wuliao.in.contact;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import wuliao.in.contact.config.Config;
import wuliao.in.contact.db.SQLiteDao;
import wuliao.in.contact.domain.Group;
import wuliao.in.contact.net.DeleteConnection;
import wuliao.in.contact.net.GroupConnection;
import wuliao.in.contact.net.DeleteConnection.CallBackListener;
import wuliao.in.contact.net.GroupConnection.FailCallback;
import wuliao.in.contact.net.GroupConnection.SuccessCallback;
import wuliao.in.contact.utils.PublicUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.AlteredCharSequence;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FixGroup extends Activity {
	private ListView mLvFix;
	private List<Group> groups=new ArrayList<Group>();
	private String user_id;
	private AlertDialog.Builder builder;
	private SharedPreferences sp;
	private MyAdapter adapter;
	private TextView tv;
	private Dialog progress;
	private Handler handle=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			builder.show();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fix);
		sp=getSharedPreferences("token", Activity.MODE_PRIVATE);
		user_id=sp.getString("user_id", null);
		adapter=new MyAdapter();
		initView();
		loadData();
		
	}
	private void loadData() {
		new GroupConnection(Config.URL,"User","test","GET",
				new SuccessCallback() {
					@Override
					public void onSuccess(List<Group> strs) {
						progress.dismiss();
						groups=strs;
						mLvFix.setAdapter(adapter);
					}
			},new FailCallback() {
				@Override
				public void onFail(String code) {			
					progress.dismiss();
					tv.setVisibility(View.VISIBLE);
					mLvFix.setEmptyView(tv);
				}
			},new String[]{"user_id",user_id});
	}
	final static class ViewHolder{
		ImageView iv;
		TextView tv;
	}
	private void initView() {
		mLvFix=(ListView) findViewById(R.id.lv_fix_group);
		progress=PublicUtils.createLoadingDialog(FixGroup.this, "正在加载中");
		progress.show();
		tv=(TextView) findViewById(R.id.fix_empty);
		mLvFix.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int pos=position;
				ImageView iv=(ImageView) view.findViewById(R.id.iv_item_fix);
				iv.setOnClickListener(new OnClickListener() {	
					@Override
					public void onClick(View v) {
						Toast.makeText(getApplicationContext(), groups.get(pos).getGroupName(), 0).show();
					}
				});
			}
		});
	}
	private class MyAdapter extends BaseAdapter{
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int pos=position;
			ViewHolder holder;
			if(convertView==null){
				holder=new ViewHolder();
				convertView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_fix, null);
				holder.iv=(ImageView) convertView.findViewById(R.id.iv_item_fix);
				holder.tv=(TextView) convertView.findViewById(R.id.tv_item_fix);
				convertView.setTag(holder);
			}
			holder=(ViewHolder) convertView.getTag();
			holder.tv.setText(groups.get(position).getGroupName());

			holder.iv.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					final View view=v;
					RotateAnimation ra=new RotateAnimation(0f,90f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
					ra.setDuration(300);
					ra.setFillAfter(true);
					v.startAnimation(ra);
					builder=new AlertDialog.Builder(FixGroup.this);
					builder.setTitle("你要删除"+groups.get(pos).getGroupName()+"分组吗？");
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							RotateAnimation ra=new RotateAnimation(90f,0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
							ra.setDuration(300);
							ra.setFillAfter(true);
							view.startAnimation(ra);
						}
					});
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {			
						@Override
						public void onClick(DialogInterface dialog, int which) {
							DeleteConnection delete=new DeleteConnection(Config.URL, "Item", "exitGroup", "GET",
									new String[]{"user_id",user_id,"group_id",groups.get(pos).getGroupId()});
							delete.setOnCallBackListener(new CallBackListener() {		
								@Override
								public void onSuccess() {						
									SQLiteDao dao=new SQLiteDao(getApplicationContext());
									dao.deleteGroup(groups.get(pos).getGroupId());
									groups.remove(pos);
									adapter.notifyDataSetChanged();
									Toast.makeText(getApplicationContext(), "删除成功", 0).show();
								}				
								@Override
								public void onFail() {
									Toast.makeText(getApplicationContext(), "删除失败", 0).show();
								}
							}); 
							
						}
					});
					handle.sendEmptyMessageDelayed(0, 300);
					
				}
			});
			return convertView;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}	
		@Override
		public Object getItem(int position) {
			return groups.get(position);
		}	
		@Override
		public int getCount() {
			return groups.size();
		}
	}
	
	
	
	
}
