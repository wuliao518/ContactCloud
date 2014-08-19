package wuliao.in.contact.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wuliao.in.contact.config.Config;
import wuliao.in.contact.db.SQLiteDao;
import wuliao.in.contact.domain.Group;
import wuliao.in.contact.domain.User;
import wuliao.in.contact.net.GroupConnection;
import wuliao.in.contact.net.UserConnection;
import wuliao.in.contact.net.GroupConnection.FailCallback;
import wuliao.in.contact.net.GroupConnection.SuccessCallback;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class ProviderUtils {
	private SharedPreferences sp;
	private String user_id;
	private SQLiteDao dao;
	private Context context;
	private List<Group> groups=new ArrayList<Group>();
	private Map<Integer,List<User>> mUsers=new HashMap<Integer,List<User>>();
	public ProviderUtils(Context context){
		this.context=context;
		dao=new SQLiteDao(context);
	}

	
	public void loadDate(final List<Group> groups,Map<Integer,List<User>> mUsers) {
		new GroupConnection(Config.URL,"User","test","GET",
			new SuccessCallback() {
				@Override
				public void onSuccess(List<Group> strs) {
					//groups=strs;
					for(int i=0;i<groups.size();i++){
						
						
					}
				}
		},new FailCallback() {
			@Override
			public void onFail(String code) {
				
			}
		},new String[]{"user_id",user_id});
		
	}

	public void loadUser(String group_id,final Group group){
		new UserConnection(Config.URL, "User","selectUserAtGroup", "GET", 
				new UserConnection.SuccessCallback() {
					@Override
					public void onSuccess(List<User> users) {
						dao.addGroup(group, users);
					}					
					
				}, new UserConnection.FailCallback(){
					@Override
					public void onFail(String code) {
						
					}
					
				}, new String[]{"group_id",group_id});
	}
	
	public List<Group> getGroup(){
		return dao.selectGroup();
	}
	public List<User> getUser(Group group){
		return dao.selectUser(group);
	}
	
	
	
	
	
	
	
	
	
	
	
}
