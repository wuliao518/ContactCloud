package wuliao.in.contact.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wuliao.in.contact.domain.Group;
import android.content.SharedPreferences;


public class GroupConnection {
	private List<Group> groups=new ArrayList<Group>();
	public GroupConnection(String url,final String model,final String action,final String method,final SuccessCallback successCallback,final FailCallback failCallback,String ...args){
		
		
		new NetConnection(url, model, action, method, 
			new NetConnection.SuccessCallback(){
				@Override
				public void onSuccess(String result) {
					try {
						System.out.println(result);
						JSONObject jsonObj=new JSONObject(result);
						JSONArray array=jsonObj.getJSONArray("groups");
						for(int i=0;i<array.length();i++){
							Group group=new Group();
							group.setGroupName((array.getJSONObject(i).getString("groupname")));
							group.setGroupId((array.getJSONObject(i).getString("group_id")));
							group.setModifyTime((array.getJSONObject(i).getString("modify_time")));
							groups.add(group);
							group=null;
						}
						switch(jsonObj.getInt("status")){
						case 1:successCallback.onSuccess(groups);break;
						case 0:failCallback.onFail(jsonObj.getString("message"));break;
						case 2:failCallback.onFail(jsonObj.getString("message"));break;
						default:break;
						}
					} catch (JSONException e) {
						failCallback.onFail("fail");
					}			
				}		
		}, new NetConnection.FailCallback() {		
			@Override
			public void onFail() {
				failCallback.onFail("ÍøÂçÒì³££¬ÉÔºóÔÙÊÔ");
			}
		}, args);
	}
	public interface SuccessCallback{
		void onSuccess(List<Group> strs);
	}
	public interface FailCallback{
		void onFail(String code);
	}
}