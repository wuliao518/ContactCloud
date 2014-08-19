package wuliao.in.contact.db;

import java.util.ArrayList;
import java.util.List;

import wuliao.in.contact.domain.Group;
import wuliao.in.contact.domain.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class SQLiteDao {
	private Context context;
	private MySQLite mSQLite;
	private SQLiteDatabase db;
	public SQLiteDao(Context context) {
		this.context = context;
		mSQLite=new MySQLite(this.context);
	}
	/*
	 * 添加新组时调用
	 * */
	public void addGroup(Group group,List<User> users){
		db=mSQLite.getWritableDatabase();
		if(!findGroup(group)){
			ContentValues groupValues=new ContentValues();
			groupValues.put("group_id", group.getGroupId());
			groupValues.put("group_name", group.getGroupName());
			groupValues.put("modify_time", group.getModifyTime());
			db.insert("lu_group", null, groupValues);
		}
		for(User user : users){
			if(findUser(user)){	
				if(findItem(group.getGroupId(),user.getUserId())){
					db.execSQL("insert into lu_item(group_id,user_id)values('"+group.getGroupId()+"','"+ user.getUserId()+"')");
				}
				db.execSQL("insert into lu_item(group_id,user_id)values('"+group.getGroupId()+"','"+ user.getUserId()+"')");
				continue;
			}
			ContentValues userValues=new ContentValues();
			userValues.put("user_id", user.getUserId());
			userValues.put("username", user.getUsername());
			userValues.put("phone_num", user.getPhonenum());
			userValues.put("modify_time", user.getModifyTime());
			System.out.println(db.isOpen());
			db.insert("lu_user", null, userValues);
			if(findItem(group.getGroupId(),user.getUserId())){
				db.execSQL("insert into lu_item(group_id,user_id)values('"+group.getGroupId()+"','"+ user.getUserId()+"')");
			}
			db.execSQL("insert into lu_item(group_id,user_id)values('"+group.getGroupId()+"','"+ user.getUserId()+"')");
		}
		close();
	}
	public boolean findUser(User user){
		db=mSQLite.getReadableDatabase();
		Cursor cursor=db.query("lu_user", new String[]{"user_id"}, "user_id=?", new String[]{user.getUserId()}, null, null, null);
		if(cursor.moveToNext()){
			if(cursor.getString(0)!=null){
				close(cursor);
				return true;
			}
			close(cursor);
			close();
			return false;
		}
		close(cursor);
		return false;
	}
	public boolean findGroup(Group group){
		db=mSQLite.getReadableDatabase();
		Cursor cursor=db.query("lu_group", new String[]{"group_id"}, "group_id=?", new String[]{group.getGroupId()}, null, null, null);
		if(cursor.moveToNext()){
			if(cursor.getString(0)!=null){
				close(cursor);
				return true;
			}
			close(cursor);
			return false;
		}
		close(cursor);
		return false;
	}
	
	
	public boolean findUser(String user_id){
		db=mSQLite.getReadableDatabase();
		Cursor cursor=db.query("lu_user", new String[]{"user_id"}, "user_id=?", new String[]{user_id}, null, null, null);
		if(cursor.moveToNext()){
			if(cursor.getString(0)!=null){
				close(cursor);
				return true;
			}
			close(cursor);
			return false;
		}
		close(cursor);
		return false;
	}
	/*
	 * 往组内添加新成员是调用
	 * 当组内发生变动，清空item中改组对应的成员
	 * */
	public void addUser(Group group,List<User> users){
		db=mSQLite.getWritableDatabase();
		db.execSQL("delete from lu_item where group_id="+group.getGroupId());
		
		for(User user : users){
			if(findUser(user)){
				db.execSQL("insert into lu_item(group_id,user_id)values('"+group.getGroupId()+"','"+ user.getUserId()+"')");
				continue;
			}
			ContentValues userValues=new ContentValues();
			userValues.put("user_id", user.getUserId());
			userValues.put("username", user.getUsername());
			userValues.put("phone_num", user.getPhonenum());
			userValues.put("phone_num", user.getModifyTime());
			db.insert("lu_user", null, userValues);
			db.execSQL("insert into lu_item(group_id,user_id)values('"+group.getGroupId()+"','"+ user.getUserId()+"')");
		}
		close();
	}
	public boolean findItem(String userId,String itemId){
		db=mSQLite.getReadableDatabase();
		Cursor cursor=db.query("lu_item", new String[]{"user_id"}, "user_id=? and group_id=?", new String[]{userId,itemId}, null, null, null);
		if(cursor.moveToNext()){
			if(cursor.getString(0)!=null){
				close(cursor);
				return true;
			}
			close(cursor);
			return false;
		}
		close(cursor);
		return false;
	}

	public List<Group> selectGroup() {
		List<Group> groups=new ArrayList<Group>();
		db=mSQLite.getReadableDatabase();
		Cursor cursor=db.query("lu_group", new String[]{"group_id","group_name","modify_time"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			Group group=new Group();
			group.setGroupId(cursor.getString(0));
			group.setGroupName(cursor.getString(1));
			group.setModifyTime(cursor.getString(2));
			groups.add(group);
			group=null;
		}
		close(cursor);
		close();
		return groups;
	}
	public List<User> selectUser(Group group){
		String group_id=group.getGroupId();
		db=mSQLite.getReadableDatabase();
		//db.execSQL("select user_id,username,modify_time,phone_num from lu_user "
				//+ "where user_id in(select user_id from lu_item where group_id="+group_id+")");	
		Cursor cursor=db.rawQuery("select user_id,username,modify_time,phone_num from lu_user "
				+ "where user_id in(select user_id from lu_item where group_id=?)",new String[]{group_id});	
		List<User> users=new ArrayList<User>();
		while(cursor.moveToNext()){
			User user=new User();
			user.setUserId(cursor.getString(0));
			user.setUsername(cursor.getString(1));
			user.setModifyTime(cursor.getString(2));
			user.setPhonenum(cursor.getString(3));
			users.add(user);
			group=null;
		}
		close(cursor);
		return users;
		
	}
	
	private void close(Cursor cursor){
		cursor.close();
		
	}
	private void close(){
		db.close();
		mSQLite.close();
	}
	public void clearDatabase(){
		db=mSQLite.getWritableDatabase();
		db.execSQL("delete from lu_user");
		db.execSQL("delete from lu_group");
		db.execSQL("delete from lu_item");
		db.close();
		mSQLite.close();
	}
	public void deleteGroup(String id){
		db=mSQLite.getWritableDatabase();
		db.delete("lu_item", "group_id=?", new String[]{id});
		db.delete("lu_group", "group_id=?", new String[]{id});
		db.close();
		mSQLite.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
