package com.example.netdemo.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.netdemo.domain.Group;
import com.example.netdemo.domain.User;

import android.content.SharedPreferences;

public class LoginConnection {
		private SharedPreferences sp;
		private List<User> users=new ArrayList<User>();
		public LoginConnection(String url,final String model,
				final String action,final String method,
				final SuccessCallback successCallback,
				final FailCallback failCallback,String ...args){		
			new NetConnection(url, model, action, method, 
				new NetConnection.SuccessCallback(){
					@Override
					public void onSuccess(String result) {
						try {
							JSONObject jsonObj=new JSONObject(result);
							JSONArray array=jsonObj.getJSONArray("user");
							String token=jsonObj.getString("token");
							if(array!=null){
								for(int i=0;i<array.length();i++){
									User user=new User();
									user.setUserId((array.getJSONObject(i).getString("user_id")));
									user.setUsername((array.getJSONObject(i).getString("username")));
									user.setPhonenum((array.getJSONObject(i).getString("phone_num")));
									users.add(user);
									user=null;
								}
							}						
							switch(jsonObj.getInt("status")){
							case 1:successCallback.onSuccess(users,token);break;
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
			void onSuccess(List<User> users,String token);
		}
		public interface FailCallback{
			void onFail(String code);
		}
}