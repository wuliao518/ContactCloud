package com.example.netdemo.net;

import org.json.JSONException;
import org.json.JSONObject;

public class DeleteConnection {
	private CallBackListener listener;
	public DeleteConnection(String url,String model,String action,String method,String ...args){
		new NetConnection(url, model, action, method, 
			new NetConnection.SuccessCallback(){
				@Override
				public void onSuccess(String result) {
					System.out.println(result);
					try {
						JSONObject jsonObj=new JSONObject(result);
						int status=jsonObj.getInt("status");
						switch (status) {
						case 1:
							listener.onSuccess();
							break;
						case 0:
							listener.onFail();
							break;	
						default:
							listener.onFail();
							break;
						}
					} catch (JSONException e) {
						listener.onFail();
					}
					
					
				}		
			}, new NetConnection.FailCallback() {		
				@Override
				public void onFail() {
					listener.onFail();
				}
			}, args);
	}
	public void setOnCallBackListener(CallBackListener listener){
		this.listener=listener;
	}
	
	public interface CallBackListener{
		public void onSuccess();
		public void onFail();
	}
}
	

