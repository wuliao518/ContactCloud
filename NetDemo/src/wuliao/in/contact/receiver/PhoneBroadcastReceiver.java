package wuliao.in.contact.receiver;


import wuliao.in.contact.provider.ProviderUtils;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import android.widget.TextView;

public class PhoneBroadcastReceiver extends BroadcastReceiver {
	private WindowManager winManager;
	private static TextView  tv;
	private String phoneNum=null;
	@Override
	public void onReceive(Context context, Intent intent) {
		TelephonyManager manager=(TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
		winManager=(WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
		switch (manager.getCallState()) {
			case TelephonyManager.CALL_STATE_IDLE:
				System.out.println("CALL_STATE_IDLE");
				if(tv!=null){
					winManager.removeView(tv);
					tv=null;
					phoneNum=null;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				phoneNum=intent.getExtras().getString("incoming_number");
				ProviderUtils utils=new ProviderUtils(context);			
				if(utils.getUserInfo(phoneNum)!=null){
					tv=new TextView(context);
					System.out.println("user"+utils.getUserInfo(phoneNum));
					tv.setText(utils.getUserInfo(phoneNum));
					tv.setTextSize(24);
					LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.gravity=Gravity.LEFT | Gravity.TOP;
					params.x=200;
					params.y=300;
					params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		            params.format = PixelFormat.TRANSLUCENT;
					winManager.addView(tv, params);
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				System.out.println("CALL_STATE_OFFHOOK");
				if(tv!=null){
					winManager.removeView(tv);
					tv=null;
					phoneNum=null;
				}
				break;
			default:
				break;
			}
	}
	

}
