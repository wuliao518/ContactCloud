package com.example.netdemo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.netdemo.R;
import com.example.netdemo.domain.Contact;

public class SortListAdapter extends BaseAdapter {
	private Context context;
	private List<Contact> contacts=new ArrayList<Contact>();;
	public SortListAdapter(Context context, List<Contact> contacts) {
		this.context = context;
		this.contacts = contacts;
	}

	@Override
	public int getCount() {
		return contacts.size();
	}

	@Override
	public Object getItem(int position) {
		return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		Contact contact=contacts.get(position);
		if(convertView==null){
			holder=new ViewHolder();
			convertView= View.inflate(context, R.layout.item_sort_adapter, null);
			holder.tvLetter=(TextView) convertView.findViewById(R.id.catalog);
			holder.tvTitle=(TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}
		holder=(ViewHolder) convertView.getTag();
		int section=getLetterFirstPosition(contact.getSortKey());
		holder.tvTitle.setText(contact.getName());
		if(section==position){
			holder.tvLetter.setVisibility(View.VISIBLE);
			holder.tvLetter.setText(contact.getSortKey());
		}else{
			holder.tvLetter.setVisibility(View.GONE);
		}
		return convertView;
	}
	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
	}
	public int getLetterFirstPosition(String str){
		for(int i=0;i<contacts.size();i++){
			if(str.equals(contacts.get(i).getSortKey())){
				return i;
			}
		}
		return -1;
	}

}
