package wuliao.in.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wuliao.in.contact.config.Config;
import wuliao.in.contact.db.SQLiteDao;
import wuliao.in.contact.domain.Group;
import wuliao.in.contact.domain.User;
import wuliao.in.contact.net.GroupConnection;
import wuliao.in.contact.net.GroupConnection.FailCallback;
import wuliao.in.contact.net.GroupConnection.SuccessCallback;
import wuliao.in.contact.net.UserConnection;
import wuliao.in.contact.provider.ProviderUtils;
import wuliao.in.contact.utils.PublicUtils;
import wuliao.in.contact.view.MyExpendListView;
import wuliao.in.contact.view.MyExpendListView.OnRefreshListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener{
	private MyExpendListView expend;
	private SharedPreferences sp;
	private String user_id;
	private SQLiteDao dao;
	private MyAdapter adapter;
	private ProviderUtils provider;
	private TextView mTmanager;
	private ImageView mImageCreate;
	private PopupWindow localPopupWindow;
	private Dialog progress;
	private List<Group> groups=new ArrayList<Group>();
	private Map<Integer,List<User>> mUsers=new HashMap<Integer,List<User>>(); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list);
		MyApplication.getInstance().addActivity(this);
		sp=getSharedPreferences("token", Activity.MODE_PRIVATE);
		user_id=sp.getString("user_id", null);
		dao=new SQLiteDao(getApplicationContext());
		progress=PublicUtils.createLoadingDialog(HomeActivity.this,"正在加载");
		adapter=new MyAdapter();
		provider=new ProviderUtils(getApplicationContext());
		initView();
		if(provider.getGroup().size()>0){
			groups=provider.getGroup();
			int i=0;
			for(Group group : groups){
				mUsers.put(i,provider.getUser(group));
				i++;
			}
			expend.setAdapter(adapter);
		}else{
			progress.show();
			loadDate();
		}	
	}	
	private void initView() {
		mTmanager=(TextView) findViewById(R.id.tv_manager);
		mTmanager.setOnClickListener(this);
		mImageCreate=(ImageView) findViewById(R.id.iv_do_not_konw);
		mImageCreate.setOnClickListener(this);
		expend=(MyExpendListView) findViewById(R.id.expandableListView1);
		expend.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				dismissPopUpwindow();		
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				dismissPopUpwindow();
			}
		});
		expend.setOnRefreshListener(new OnRefreshListener() {		
			@Override
			public void onRefresh() {		
				new AsyncTask<Void, Void, Void>(){
					@Override
					protected Void doInBackground(Void... params) {
						loadDate();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return null;
					}
					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						expend.onRefreshComplete();
					}
					
				}.execute();
				
			}
		});
		expend.setOnGroupExpandListener(new OnGroupExpandListener(){
            @Override
            public void onGroupExpand(int groupPosition) {
                for(int i=0;i<groups.size();i++){
                    if(groupPosition != i){
                    	expend.collapseGroup(i);                 	
                    }
                }
            }});
		expend.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View view,
					int groupPosition, int childPosition, long id) {
				String phone_num=PublicUtils.decode(mUsers.get(groupPosition).get(childPosition).getPhonenum());
				
				dismissPopUpwindow();
				// 获取当前view对象在窗体中的位置
				int[] arrayOfInt = new int[2];
				view.getLocationInWindow(arrayOfInt);
				int i = arrayOfInt[0] + 100;
				int j = arrayOfInt[1];
				View popupview = View.inflate(HomeActivity.this,
						R.layout.popup_item, null);
				LinearLayout ll_phone = (LinearLayout) popupview
						.findViewById(R.id.ll_start);
				LinearLayout ll_sms = (LinearLayout) popupview
						.findViewById(R.id.ll_uninstall);
				LinearLayout ll_export = (LinearLayout) popupview
						.findViewById(R.id.ll_share);
				ll_export.setTag(phone_num);
				ll_sms.setTag(phone_num);
				ll_phone.setTag(phone_num);
				ll_phone.setOnClickListener(HomeActivity.this);
				ll_sms.setOnClickListener(HomeActivity.this);
				ll_export.setOnClickListener(HomeActivity.this);
				LinearLayout ll = (LinearLayout) popupview
						.findViewById(R.id.ll_popup);
				ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
				sa.setDuration(200);
				localPopupWindow = new PopupWindow(popupview, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				//popupwindow设置背景颜色
				Drawable background = new ColorDrawable(Color.TRANSPARENT);
				//Drawable background = getResources().getDrawable(
						//R.drawable.local_popup_bg);
				localPopupWindow.setBackgroundDrawable(background);
				localPopupWindow.showAtLocation(view, Gravity.RIGHT
						| Gravity.TOP, 10, j);
				ll.startAnimation(sa);
				return false;
			}
			
		});
	}
	
	private void dismissPopUpwindow() {
		/*
		 * 保证只有一个popupwindow的实例存在
		 */
		if (localPopupWindow != null) {
			localPopupWindow.dismiss();
			localPopupWindow = null;
		}
	}
	
	
	@Override
	protected void onPause() {
		dismissPopUpwindow();
		super.onPause();
	}
	private void loadDate() {
		new GroupConnection(Config.URL,"User","test","GET",
			new SuccessCallback() {
				@Override
				public void onSuccess(List<Group> strs) {
					progress.dismiss();
					expend.setBackgroundColor(Color.TRANSPARENT);
					groups=strs;
					for(int i=0;i<groups.size();i++){
						loadUser(groups.get(i).getGroupId(),i);		
					}
				}
		},new FailCallback() {
			@Override
			public void onFail(String code) {
				progress.dismiss();
				groups.clear();
				mUsers.clear();
				expend.setAdapter(adapter);
				//adapter.notifyDataSetChanged();
				//tv1.setVisibility(View.VISIBLE);
				Drawable drawable=getResources().getDrawable(R.drawable.bg_bitmap);
				expend.setBackgroundDrawable(drawable);
			}
		},new String[]{"user_id",user_id});
		
	}
	private class MyAdapter extends BaseExpandableListAdapter{
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return mUsers.get(groupPosition).get(childPosition);
		}
		@Override
		public long getChildId(int groupPosition, int childPosition) {		
			return childPosition;
		}
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			LinearLayout ll=(LinearLayout) View.inflate(getApplicationContext(), R.layout.item_user, null);
			ImageView iv=(ImageView) ll.findViewById(R.id.iv_item_user);
			Drawable colorDrawable=new ColorDrawable(Color.GRAY);
			iv.setImageDrawable(colorDrawable);
			TextView tv=(TextView) ll.findViewById(R.id.tv_item_user);
			//mUsers.get(groupPosition).get(groupPosition)
			tv.setText(mUsers.get(groupPosition).get(childPosition).getUsername());
			return ll;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return mUsers.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groups.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return groups.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			LinearLayout ll=(LinearLayout) View.inflate(getApplicationContext(), R.layout.item_group, null);
			ImageView iv=(ImageView) ll.findViewById(R.id.iv_group);
			TextView tv=(TextView) ll.findViewById(R.id.tv_group);
			if(isExpanded){
				iv.setBackgroundResource(R.drawable.skin_indicator_expanded);
			}else{
				iv.setBackgroundResource(R.drawable.skin_indicator_unexpanded);
			}
			tv.setText( groups.get(groupPosition).getGroupName());
			return ll;
		}

		@Override
		public boolean hasStableIds() {
			
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}	
	}
	private void loadUser(final String group_id,final int n){
		new UserConnection(Config.URL, "User","selectUserAtGroup", "GET", 
				new UserConnection.SuccessCallback() {
					@Override
					public void onSuccess(List<User> users) {
						mUsers.put(n,users);
						dao.addGroup(groups.get(n), users);
						expend.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}					
					
				}, new UserConnection.FailCallback(){
					@Override
					public void onFail(String code) {
						//adapter.notifyDataSetChanged();
					}
					
				}, new String[]{"group_id",group_id});
	}
	@Override
	public void onClick(View v) {
		String phone_num=null;
		if (v.getTag() != null) {
			phone_num = (String) v.getTag();
		}
		dismissPopUpwindow();
		switch (v.getId()) {
		case R.id.tv_manager:
			Intent intent=new Intent(HomeActivity.this,FixGroup.class);
			overridePendingTransition(R.anim.in,R.anim.out);
			startActivity(intent);
			break;
		case R.id.iv_do_not_konw:
			Intent intent2=new Intent(HomeActivity.this,CreateGroup.class);
			overridePendingTransition(R.anim.in,R.anim.out);
			startActivity(intent2);
			break;
		case R.id.ll_start:
			Intent intent3=new Intent();
			intent3.setAction(Intent.ACTION_CALL);
			Uri uri=Uri.parse("tel:"+phone_num);
			intent3.setData(uri);
			startActivity(intent3);
			break;
		case R.id.ll_uninstall:
			Intent intent4=new Intent();
			intent4.setAction(Intent.ACTION_SENDTO);
			Uri uri2 = Uri.parse("smsto:"+phone_num);
			intent4.setData(uri2);
			startActivity(intent4);
			break;
		case R.id.ll_share:
			Toast.makeText(getApplicationContext(), "导出功能暂不开放", 0).show();
			break;
		default:
			break;
		}
		
	}

	
}
