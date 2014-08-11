package wuliao.in.contact.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLite extends SQLiteOpenHelper {

	public MySQLite(Context context) {
		super(context, "wuliao.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		String tableUser="create table lu_user(user_id integer not null primary key,"
				+ "username char(30) not null unique,"
				+ "modify_time char(50) not null,"
				+ "phone_num char(11) not null unique);";
		String tableGroup="create table lu_group(group_id integer not null primary key,"
				+ "modify_time char(50) not null,"
				+ "group_name char(80) not null unique);";
		String tableItem="create table lu_item(group_id integer not null,user_id integer not null);";
		database.execSQL(tableUser);
		database.execSQL(tableGroup);
		database.execSQL(tableItem);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int arg1, int arg2) {

	}

}
