package wuliao.in.contact.net;


import org.json.JSONException;
import org.json.JSONObject;


public class UpdateConnection {
	public UpdateConnection(String url,final String model,final String action,final String method,
			final SuccessCallback successCallback,final FailCallback failCallback,String ...args){	
		new NetConnection(url, model, action, method, 
			new NetConnection.SuccessCallback(){
				@Override
				public void onSuccess(String result) {
					try {
						JSONObject jsonObj=new JSONObject(result);				
						switch(jsonObj.getInt("status")){
						case 1:successCallback.onSuccess();break;
						case 0:failCallback.onFail("∫≈¬ÎÀ∆∫ı“—¥Ê‘⁄£°");break;
						default:break;
						}
					} catch (JSONException e) {
						failCallback.onFail("Œ¥÷™¥ÌŒÛ");
					}			
				}		
		}, new NetConnection.FailCallback() {		
			@Override
			public void onFail() {
				failCallback.onFail("Õ¯¬Á¥ÌŒÛ£¨«Î…‘∫Û÷ÿ ‘£°");
			}
		}, args);
	}
	public interface SuccessCallback{
		void onSuccess();
	}
	public interface FailCallback{
		void onFail(String msg);
	}
}