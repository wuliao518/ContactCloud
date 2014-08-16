package wuliao.in.contact.net;

import android.content.SharedPreferences;


public class JSONConnection {
		private SharedPreferences sp;
		public JSONConnection(String url,final String model,final String action,final String method,final SuccessCallback successCallback,final FailCallback failCallback,String ...args){	
			new NetConnection(url, model, action, method, 
				new NetConnection.SuccessCallback(){
					@Override
					public void onSuccess(String result) {
						try {
							System.out.println(result);
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