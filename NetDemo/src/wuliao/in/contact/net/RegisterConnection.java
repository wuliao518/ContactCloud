package wuliao.in.contact.net;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;

public class RegisterConnection {
	public RegisterConnection(String url,final String model,final String action,final String method,final SuccessCallback successCallback,final FailCallback failCallback,String ...args){
		
		
		new NetConnection(url, model, action, method, 
			new NetConnection.SuccessCallback(){
				@Override
				public void onSuccess(String result) {
					try {
						JSONObject jsonObj=new JSONObject(result);
						switch(jsonObj.getInt("status")){
						case 1:successCallback.onSuccess(jsonObj.getString("token"),jsonObj.getString("user_id"));break;
						case 0:failCallback.onFail(jsonObj.getString("message"));break;
						case 2:failCallback.onFail(jsonObj.getString("message"));break;
						default:break;
						}
					} catch (JSONException e) {
						failCallback.onFail("服务器出现错误");
					}			
				}		
		}, new NetConnection.FailCallback() {		
			@Override
			public void onFail() {
				failCallback.onFail("网络连接超时");
			}
		}, args);
	}
	public interface SuccessCallback{
		void onSuccess(String token,String user_id);
	}
	public interface FailCallback{
		void onFail(String code);
	}
}
