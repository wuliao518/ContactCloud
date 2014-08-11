package com.example.netdemo.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.netdemo.domain.Group;

import android.content.SharedPreferences;

public class JoinConnection {
		private SharedPreferences sp;
		private List<String> groups=new ArrayList<String>();
		public JoinConnection(String url,final String model,
				final String action,final String method,
				final SuccessCallback successCallback,
				final FailCallback failCallback,String ...args){		
			new NetConnection(url, model, action, method, 
				new NetConnection.SuccessCallback(){
					@Override
					public void onSuccess(String result) {
						try {
							System.out.println(result);
							JSONObject jsonObj=new JSONObject(result);
							switch(jsonObj.getInt("status")){
							case 1:
								Group group=new Group();
								JSONObject obj=jsonObj.getJSONArray("message").getJSONObject(0);
								group.setGroupId(obj.getString("group_id"));
								group.setGroupName(obj.getString("groupname"));
								group.setModifyTime(obj.getString("modify_time"));
								successCallback.onSuccess(group);break;
							case 0:failCallback.onFail("失败，稍后再试");break;
							case 2:failCallback.onFail("失败，稍后再试");break;
							case 3:failCallback.onFail("请不要重复加入");break;
							default:break;
							}
						} catch (JSONException e) {
							failCallback.onFail("解析失败");
						}			
					}		
			}, new NetConnection.FailCallback() {		
				@Override
				public void onFail() {
					failCallback.onFail("网络异常，稍后再试");
				}
			}, args);
		}
		public interface SuccessCallback{
			void onSuccess(Group group);
		}
		public interface FailCallback{
			void onFail(String code);
		}
}
