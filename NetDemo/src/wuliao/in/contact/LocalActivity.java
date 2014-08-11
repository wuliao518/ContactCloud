package wuliao.in.contact;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wuliao.in.contact.adapter.SortListAdapter;
import wuliao.in.contact.config.Config;
import wuliao.in.contact.domain.Contact;
import wuliao.in.contact.domain.Group;
import wuliao.in.contact.net.JSONConnection;
import wuliao.in.contact.net.JSONConnection.FailCallback;
import wuliao.in.contact.net.JSONConnection.SuccessCallback;
import wuliao.in.contact.view.ClearEditText;
import wuliao.in.contact.view.SideBar;
import wuliao.in.contact.view.SideBar.OnTouchingLetterChangedListener;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class LocalActivity extends Activity {
	private SideBar sideBar;
	private TextView mTextView;
	private SortListAdapter adapter;
	private ClearEditText mClearEditText;
	private ListView mLocaList;
	private List<Contact> contacts=new ArrayList<Contact>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local);
		MyApplication.getInstance().addActivity(this);
		loadData();
		initView();	
	}
	private void initView() {
		mLocaList=(ListView) findViewById(R.id.lv_local);
		mTextView=(TextView) findViewById(R.id.dialog);
		mClearEditText=(ClearEditText) findViewById(R.id.filter_edit);
		mClearEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				fillData(s.toString());
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}	
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		mLocaList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				
				
//				Contact contact=contacts.get(position);
//				
//				new JSONConnection(Config.URL, "User", "json", "POST", 
//						new SuccessCallback() {
//							
//							@Override
//							public void onSuccess() {
//								
//								
//							}
//						}, new FailCallback() {
//							
//							@Override
//							public void onFail(String code) {
//								// TODO Auto-generated method stub
//								
//							}
//						}, new String[]{"json",getContactToJSON()});
//				
//				
//				
//				//view.
//				Toast.makeText(getApplicationContext(), contact.getName(),0).show();
//				//view.get
			}
		});
		
		adapter=new SortListAdapter(this, contacts);
		mLocaList.setAdapter(adapter);
		sideBar =(SideBar) findViewById(R.id.sidrbar);
		sideBar.setTextView(mTextView);
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = adapter.getLetterFirstPosition(s);
				if(position != -1){
					mLocaList.setSelection(position);
				}	
			}
		});
		
		
	}
	private void fillData(String string) {
		//contacts.clear();
		List<Contact> lists=new ArrayList<Contact>();
		if(TextUtils.isEmpty(string)){
			lists=loadData();
		}else{
			for(Contact contact:contacts){
				if(contact.getSortKey().contains(string.substring(0,1))||contact.getName().contains(string)){
					
					lists.add(contact);
				}							
			}
		}
		adapter=new SortListAdapter(this, lists);
		mLocaList.setAdapter(adapter);
	}
	private List<Contact> loadData() {
		ContentResolver resolver=getContentResolver();
		Cursor cursor=resolver.query(Uri.parse("content://com.android.contacts/data/phones"),
				new String[] { "display_name", "sort_key", "contact_id","data1" },null,null,"sort_key");//"display_name COLLATE LOCALIZED"
		while(cursor.moveToNext()){
			Contact contact=new Contact();
			contact.setSortKey(getKey(cursor.getString(1)));
			contact.setName(cursor.getString(0));
			contact.setPhonenum(cursor.getString(3));
			contacts.add(contact);
			contact=null;
		}
		return contacts;
	}
	private String getKey(String sort_key){
		String key=sort_key.substring(0,1).toUpperCase();
		if(key.matches("[A-Z]")){
			return key;
		}else{
			return "#";
		}
	}
	private String getContactToJSON() {
		JSONObject obj=new JSONObject();
		JSONArray array=new JSONArray();
		try {
			array.put(0, "wuliao");
			array.put(1, "15936201059");
			obj.put("user", array);
			obj.put("time", System.currentTimeMillis());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return obj.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
