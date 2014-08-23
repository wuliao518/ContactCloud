package wuliao.in.contact;


import android.app.TabActivity;
import android.content.Intent;
import android.graphics.RadialGradient;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

public class IndexActivity extends TabActivity {
	private TabHost mTabHost;
	private RadioGroup mRadioGroup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		MyApplication.getInstance().addActivity(this);
		initView();
	}
	private void initView() {
		mTabHost=getTabHost();
		mRadioGroup=(RadioGroup) findViewById(R.id.home_radio_button_group);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
					switch (checkedId) {
					case R.id.home_tab_main:
						mTabHost.setCurrentTabByTag("home");
						break;
					case R.id.home_tab_group:
						mTabHost.setCurrentTabByTag("local");
						break;
					case R.id.home_tab_search:
						mTabHost.setCurrentTabByTag("search");
						break;
					case R.id.home_tab_personal:
						mTabHost.setCurrentTabByTag("setting");
						break;
					default:
						break;
					}		
			}					
		});	
		Intent iHome=new Intent(getApplicationContext(), HomeActivity.class);
		Intent iSearch=new Intent(getApplicationContext(), SearchActivity.class);
		Intent iSetting=new Intent(getApplicationContext(), SettingActivity.class);
		Intent iLocal=new Intent(getApplicationContext(), LocalActivity.class);
		mTabHost.addTab(mTabHost.newTabSpec("home").setIndicator("home").setContent(iHome));
		mTabHost.addTab(mTabHost.newTabSpec("local").setIndicator("local").setContent(iLocal));
		mTabHost.addTab(mTabHost.newTabSpec("search").setIndicator("search").setContent(iSearch));
		mTabHost.addTab(mTabHost.newTabSpec("setting").setIndicator("setting").setContent(iSetting));
		mTabHost.setCurrentTabByTag("home");
	}
}
