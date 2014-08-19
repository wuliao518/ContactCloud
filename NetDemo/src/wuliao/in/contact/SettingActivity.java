package wuliao.in.contact;

import java.io.File;

import wuliao.in.contact.config.Config;
import wuliao.in.contact.net.DeleteConnection;
import wuliao.in.contact.net.VersionConnection;
import wuliao.in.contact.net.DeleteConnection.CallBackListener;
import wuliao.in.contact.net.VersionConnection.FailCallback;
import wuliao.in.contact.net.VersionConnection.SuccessCallback;
import wuliao.in.contact.utils.DownLoadUtils;
import wuliao.in.contact.utils.PublicUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener{
	private LinearLayout mLexit,mLmanager,mVersion,mSetting,mAbout,mLayoutIdea;
	private String token,phone_num;
	private SharedPreferences sp;
	private ProgressDialog process;;
	private String apkPath;
	private Handler handle=new Handler(){

		@Override
		public void handleMessage(Message msg) {		
			switch (msg.what) {
			case 0:
				process.show();
				break;
			case 1:
				mVersion.setClickable(true);
				process.dismiss();
				install(apkPath);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seting);
		MyApplication.getInstance().addActivity(this);
		sp=getSharedPreferences("token", Activity.MODE_PRIVATE);
		token=sp.getString("token", null);
		phone_num=sp.getString("phone_num", null);
		initView();
	}
	private void initView() {
		mLexit=(LinearLayout) findViewById(R.id.ll_exit_user);
		mVersion=(LinearLayout) findViewById(R.id.version);
		mSetting=(LinearLayout) findViewById(R.id.layoutSet);
		mAbout=(LinearLayout) findViewById(R.id.about);
		mLayoutIdea=(LinearLayout) findViewById(R.id.layoutIdea);
		mLayoutIdea.setOnClickListener(this);
		mSetting.setOnClickListener(this);
		mAbout.setOnClickListener(this);
		process=new ProgressDialog(this);
		process.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		process.setCanceledOnTouchOutside(false);
		process.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mVersion.setClickable(true);
				File file=new File(apkPath);
				if(file.exists()){
					file.delete();
				}
				process.dismiss();
			}
		});
		process.setTitle("正在下载");
		mVersion.setOnClickListener(this);
		mLmanager=(LinearLayout) findViewById(R.id.layoutNumber);
		mLmanager.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(SettingActivity.this,UpdateUserInfo.class);
				startActivity(intent);
			}	
		});
		mLexit.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layoutSet:
			Toast.makeText(getApplicationContext(), "有什么好设置的!", 0).show();
			break;
		case R.id.about:

			AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		    localBuilder.setView(LayoutInflater.from(getApplicationContext()).inflate(R.layout.about, null));
		    localBuilder.show();
			break;
		case R.id.ll_exit_user:
			DeleteConnection conn=new DeleteConnection(Config.URL, "User", "exitUser", 
					"GET", new String[]{"token",token,"phone_num",phone_num});
			conn.setOnCallBackListener(new CallBackListener() {		
				@Override
				public void onSuccess() {
					PublicUtils utils=new PublicUtils(getApplicationContext(), sp);
					utils.clearCache();
					utils.clearDatabase();
					MyApplication.getInstance().exit();
				}
				@Override
				public void onFail() {
					PublicUtils utils=new PublicUtils(getApplicationContext(), sp);
					utils.clearCache();
					utils.clearDatabase();
					MyApplication.getInstance().exit();
				}
			});
			break;
		case R.id.version:
			mVersion.setClickable(false);
			new VersionConnection(Config.URL, "Index", "getVersion", "GET", 
					new SuccessCallback() {	
						@Override
						public void onSuccess(final String path,String version) {
							PublicUtils utils=new PublicUtils(getApplicationContext(), sp);
							if(Float.parseFloat(version)>Float.parseFloat(utils.getVersion())){				
								if (Environment.getExternalStorageState().equals(
										Environment.MEDIA_MOUNTED)) {
									Message msg=new Message();
									msg.what=0;
									handle.handleMessage(msg);									
									apkPath = Environment.getExternalStorageDirectory()
											+ "/ContactCloud.apk";
										new Thread(){
											@Override
											public void run() {
												try {	
													DownLoadUtils.downLoad(path, apkPath, process);
													Message msg2=new Message();
													msg2.what=1;
													handle.handleMessage(msg2);
												} catch (Exception e) {
													e.printStackTrace();
												}
											}	
										}.start();									
									
								} else {
									mVersion.setClickable(true);
									Toast.makeText(SettingActivity.this, "SD卡不可用，请插入SD卡",
											Toast.LENGTH_SHORT).show();
								}
	
							}else{
								mVersion.setClickable(true);
								Toast.makeText(getApplicationContext(), "您已经是最新版本了", 1).show();
							}
						}
					}, new FailCallback() {					
						@Override
						public void onFail() {
							
						}
					}, new String[]{"version","version"});

			break;
		case R.id.layoutIdea:
			Intent intent=new Intent(SettingActivity.this,OptionActivity.class);
			startActivity(intent);
		default:
			break;
		}
		
	}
	private void install(String path){
		File file=new File(path);
		if(!file.exists())
			return;
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		finish();
		startActivity(intent);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
