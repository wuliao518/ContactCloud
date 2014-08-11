package com.example.netdemo.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.netdemo.domain.User;

public class UserConnection  {
		public List<User> users=new ArrayList<User>();
		public UserConnection(String url,final String model,
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
							JSONArray array=jsonObj.getJSONArray("message");
							if(array!=null){
								for(int i=0;i<array.length();i++){
									User user=new User();
									user.setUserId((array.getJSONObject(i).getString("user_id")));
									user.setUsername((array.getJSONObject(i).getString("username")));
									user.setPhonenum((array.getJSONObject(i).getString("phone_num")));
									user.setModifyTime((array.getJSONObject(i).getString("modify_time")));
									users.add(user);
									user=null;
								}
							}						
							switch(jsonObj.getInt("status")){
							case 1: System.out.println(users.size());successCallback.onSuccess(users);break;
							case 0:failCallback.onFail("失败，稍后再试");break;
							case 2:failCallback.onFail("失败，稍后再试");break;
							case 3:failCallback.onFail("已成功加入");break;
							default:break;
							}
						} catch (JSONException e) {
							failCallback.onFail("fail");
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
			void onSuccess(List<User> users);
		}
		public interface FailCallback{
			void onFail(String code);
		}
}