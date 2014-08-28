package wuliao.in.contact.receiver;


import wuliao.in.contact.R;
import wuliao.in.contact.provider.ProviderUtils;
import wuliao.in.contact.view.MyLinearLayout;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PhoneBroadcastReceiver extends BroadcastReceiver {
	private WindowManager winManager;
	private TextView  tv;
	private static MyLinearLayout mLayout;
	private String phoneNum=null;
	@Override
	public void onReceive(Context context, Intent intent) {
		TelephonyManager manager=(TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
		winManager=(WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
		switch (manager.getCallState()) {
			case TelephonyManager.CALL_STATE_IDLE:
				System.out.println("CALL_STATE_IDLE");
				if(mLayout!=null){
					winManager.removeView(mLayout);
					mLayout=null;
					phoneNum=null;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				phoneNum=intent.getExtras().getString("incoming_number");
				ProviderUtils utils=new ProviderUtils(context);			
				if(utils.getUserInfo(phoneNum)!=null){
					if(mLayout==null){						
						mLayout=(MyLinearLayout) LayoutInflater.from(context).inflate(R.layout.out_ring, null);
					}
					tv=(TextView) mLayout.findViewById(R.id.tv_name);
					System.out.println("user"+utils.getUserInfo(phoneNum));
					tv.setText(utils.getUserInfo(phoneNum));
					LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.gravity=Gravity.CENTER;
					params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		            params.format = PixelFormat.TRANSLUCENT;
					winManager.addView(mLayout, params);
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				System.out.println("CALL_STATE_OFFHOOK");
				if(mLayout!=null){
					winManager.removeView(mLayout);
					mLayout=null;
					phoneNum=null;
				}
				break;
			default:
				break;
			}
	}
	

}
