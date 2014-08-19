package wuliao.in.contact.test;

import java.util.List;

import wuliao.in.contact.db.SQLiteDao;
import wuliao.in.contact.domain.Group;
import android.test.AndroidTestCase;

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
