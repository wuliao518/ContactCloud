package wuliao.in.contact.net;

import org.json.JSONObject;

public class OptionNet {
	public OptionNet(String url,final String model,final String action,final String method,
			final SuccessCallback successCallback,final FailCallback failCallback,String ...args){
		new NetConnection(url, model, action, method, 
			new NetConnection.SuccessCallback(){
				@Override
				public void onSuccess(String result) {
					try {
						System.out.println(result);
						JSONObject jsonObject=new JSONObject(result);
						int backNum=jsonObject.getInt("status");
						switch (backNum) {
						case 1:
							successCallback.onSuccess();
							break;
						default:
							successCallback.onSuccess();
							break;
						}	
					} catch (Exception e) {
						failCallback.onFail("Œ¥÷™¥ÌŒÛ");
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
		void onFail(String msg);
	}
}