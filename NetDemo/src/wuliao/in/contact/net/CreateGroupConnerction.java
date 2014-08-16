package wuliao.in.contact.net;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateGroupConnerction {
	private CallBackListener listener;
	public CreateGroupConnerction(String url,String model,String action,String method,String ...args){
		new NetConnection(url, model, action, method, 
			new NetConnection.SuccessCallback(){
				@Override
				public void onSuccess(String result) {
					System.out.println(result);
					try {
						JSONObject jsonObj=new JSONObject(result);
						int status=jsonObj.getInt("status");
						String message=jsonObj.getString("message");
						switch (status) {
						case 1:
							listener.onSuccess();
							break;
						case 0:
							listener.onFail(message);
							break;	
						default:
							listener.onFail(message);
							break;
						}
					} catch (JSONException e) {
						listener.onFail("出现未知错误");
					}
					
					
				}		
			}, new NetConnection.FailCallback() {		
				@Override
				public void onFail() {
					listener.onFail("网络问题稍后请重试");
				}
			}, args);
	}
	public void setOnCallBackListener(CallBackListener listener){
		this.listener=listener;
	}
	
	public interface CallBackListener{
		public void onSuccess();
		public void onFail(String code);
	}
}
