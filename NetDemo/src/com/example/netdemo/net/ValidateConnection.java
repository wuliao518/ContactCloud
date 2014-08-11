package com.example.netdemo.net;

import java.util.ArrayList;
import java.util.List;


import android.content.SharedPreferences;

public class ValidateConnection {

		private SharedPreferences sp;
		private List<String> groups=new ArrayList<String>();
		public ValidateConnection(String url,final String model,final String action,final String method,final SuccessCallback successCallback,final FailCallback failCallback,String ...args){
			
			
			new NetConnection(url, model, action, method, 
				new NetConnection.SuccessCallback(){
					@Override
					public void onSuccess(String result) {
						try {
							System.out.println(result);
							successCallback.onSuccess();
						} catch (Exception e) {
							failCallback.onFail("fail");
						}			
					}		
			}, new NetConnection.FailCallback() {		
				@Override
				public void onFail() {
					failCallback.onFail("Õ¯¬Á“Ï≥££¨…‘∫Û‘Ÿ ‘");
				}
			}, args);
		}
		public interface SuccessCallback{
			void onSuccess();
		}
		public interface FailCallback{
			void onFail(String code);
		}
}

