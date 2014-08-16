package wuliao.in.contact;

import wuliao.in.contact.utils.PublicUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashActivity extends Activity {
	private TextView mTVersion;
	private LinearLayout mLSplash;
	private SharedPreferences sp;
	private Handler handle=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		sp=getSharedPreferences("token", Activity.MODE_PRIVATE);
		initView();
	}
	private void initView() {
		mTVersion=(TextView) findViewById(R.id.tv_version);
		mLSplash=(LinearLayout) findViewById(R.id.ll_splash);
		mTVersion.setText("°æ±¾ºÅ£º"+getVersion());
		AlphaAnimation aa=new AlphaAnimation(0.3f,1.0f);
		aa.setDuration(1500);
		mLSplash.startAnimation(aa);
		handle.postDelayed(new Runnable(){
			@Override
			public void run() {
				PublicUtils publicUtils=new PublicUtils(getApplicationContext(), sp);
				if(publicUtils.getToken()==null){
					Intent intent=new Intent(getApplicationContext(), NameActivity.class);
					startActivity(intent);
					finish();
				}else{
					Intent intent=new Intent(getApplicationContext(), IndexActivity.class);
					startActivity(intent);
					finish();
				}
			}
			
		}, 1500);
	}
	private String getVersion(){
		PackageManager pm=getPackageManager();
		try {
			PackageInfo info=pm.getPackageInfo(getPackageName(), PackageManager.GET_INSTRUMENTATION);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	
}
