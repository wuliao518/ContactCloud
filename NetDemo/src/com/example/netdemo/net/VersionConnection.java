package com.example.netdemo.net;

import org.json.JSONException;
import org.json.JSONObject;


public class VersionConnection {
	public VersionConnection(String url,final String model,final String action,final String method,
			final SuccessCallback successCallback,final FailCallback failCallback,String ...args){	
		new NetConnection(url, model, action, method, 
			new NetConnection.SuccessCallback(){
				@Override
				public void onSuccess(String result) {
					try {
						System.out.println(result);
						JSONObject jsonObj=new JSONObject(result);
						String path=jsonObj.getString("url");
						String version=jsonObj.getString("version");
						successCallback.onSuccess(path,version);
					} catch (JSONException e) {
						failCallback.onFail();
					}	
				}				
		}, new NetConnection.FailCallback() {		
			@Override
			public void onFail() {
				failCallback.onFail();
			}
		}, args);
	}
		public interface SuccessCallback{
			void onSuccess(String path,String version);
		}
		public interface FailCallback{
			void onFail();
		}
	}