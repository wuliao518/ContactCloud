package wuliao.in.contact.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import wuliao.in.contact.NameActivity;
import wuliao.in.contact.R;
import wuliao.in.contact.config.Config;
import wuliao.in.contact.db.SQLiteDao;
import wuliao.in.contact.net.ValidateConnection;
import wuliao.in.contact.net.ValidateConnection.FailCallback;
import wuliao.in.contact.net.ValidateConnection.SuccessCallback;

public class PublicUtils {
	public Context context;
	public SharedPreferences sp;
	private CallBackListener listener;
	private static char[] base64=new char[]{'A','B','C','D','E','F','G','H','I','J','J','L',
		'M','N','O','P','Q','R','S','T','U','V','W','X',
		'Y','Z','a','b','c','d','e','f','g','h','i','j',
		'k','l','m','n','o','p','q','r','s','t','u','v',
		'w','x','y','z','0','1','2','3','4','5','6','7',
		'8','9','+','/'};
	public PublicUtils(Context context,SharedPreferences sp){
		this.sp=sp;
		this.context=context;		
	}
	public String getToken(){
		return sp.getString("token", null);
	}
	public String getUsername(){
		return sp.getString("username", null);
	}
	public void setSharePreferences(String token,String username){
		Editor edit=sp.edit();
		edit.putString("token", token);
		edit.putString("username", username);
		edit.commit();
	}
	public void clearCache() {
		Editor edit=sp.edit();
		System.out.println("clear cache");
		edit.putString("token", null);
		edit.putString("username", null);
		edit.putString("user_id", null);
		edit.putString("phone_num", null);
		edit.commit();
		
	}
	public void validate(){
		String token=sp.getString("token", null);
		String phone_num=sp.getString("phone_num", null);
		new ValidateConnection(Config.URL, "User", "validate", "GET",
				new SuccessCallback() {				
					@Override
					public void onSuccess() {
						listener.onSuccess();
					}
				}, new FailCallback() {
					
					@Override
					public void onFail(String code) {						
						listener.onFail();
						clearCache();
						Intent intent=new Intent(context,NameActivity.class);
						context.startActivity(intent);
						((Activity) context).finish();
					}
				}, "token",token,"phone_num",phone_num);
	}
	public void setCallBackListener(CallBackListener listener){
		this.listener=listener;
	}
	public interface CallBackListener{
		public void onSuccess();
		public void onFail();
	}
	public void clearDatabase(){
		SQLiteDao dao=new SQLiteDao(context);
		dao.clearDatabase();
	}
	public static String encode(String str){
		
		long temp=Long.parseLong(str,16);
		StringBuilder builder=new StringBuilder();
		while(temp!=0){	
			builder.append(base64[(int) (temp%64)]);
			temp=temp/64;
		}
		builder=builder.reverse();
		int size=8-builder.length();
		for(int i=0;i<size;i++){
			builder.insert(0,'A');
		}
		return builder.toString();
	}
	public static String decode(String str){
		int last=str.lastIndexOf("A");
		String code=str.substring(last,str.length());	
		long sum=0;
		char[] chars=code.toCharArray();
		for(int j=0;j<chars.length;j++){
			for(int i=0;i<base64.length;i++){
				if(base64[i]==chars[j]){
					sum+=Math.pow(64, chars.length-j-1)*i;
					break;
				}		
			}
		}	
		return Long.toHexString(sum);
	}
	public String getVersion(){
		PackageManager pm=context.getPackageManager();
		PackageInfo info;
		try {
			info = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_INSTRUMENTATION);
			System.out.println(info.versionName);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "δ֪";
		}
		
	}
	 public static Dialog createLoadingDialog(Context paramContext, String paramString)
	  {
	    View localView = LayoutInflater.from(paramContext).inflate(R.layout.loading_dialog, null);
	    LinearLayout localLinearLayout = (LinearLayout)localView.findViewById(2131230785);
	    ImageView localImageView = (ImageView)localView.findViewById(2131230786);
	    TextView localTextView = (TextView)localView.findViewById(2131230787);
	    localImageView.startAnimation(AnimationUtils.loadAnimation(paramContext, R.anim.load_animation));
	    localTextView.setText(paramString);
	    Dialog localDialog = new Dialog(paramContext, R.style.loading_dialog);
	    localDialog.setCancelable(false);
	    localDialog.setContentView(localLinearLayout, new LinearLayout.LayoutParams(-1, -1));
	    return localDialog;
	  }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
