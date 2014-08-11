package com.example.netdemo.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.example.netdemo.db.SQLiteDao;
import com.example.netdemo.domain.Group;

public class Test extends AndroidTestCase {
	public SQLiteDao dao;
	public void testFind(){
		dao=new SQLiteDao(getContext());
		assertEquals(false, dao.findUser("1"));
	}
	public void testselect(){
		dao=new SQLiteDao(getContext());
		List<Group> groups=dao.selectGroup();
		for(Group group : groups)
		System.out.println(group.getGroupName());
	}
}
