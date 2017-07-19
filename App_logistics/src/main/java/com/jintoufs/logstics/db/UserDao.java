package com.jintoufs.logstics.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jintoufs.logstics.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private DBHelper helper;
    private SQLiteDatabase db;

    public UserDao(Context context) {
        helper = BeanFactory.getDBHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add User
     */
    public void add(List<User> users) {

        db.beginTransaction(); // 开始事务
        try {
            for (User entity : users) {
                db.execSQL("INSERT INTO user (" +
                                "id," +
                                "USER_NAME," +
                                "KEYSTR," +
                                "REAL_NAME," +
                                "PASSWORD," +
                                "FINGERPRINT)" +
                                "VALUES(?,?,?,?,?,?)",
                        new Object[] {
                                entity.getId(),
                                entity.getUserName(),
                                entity.getKeyStr(),
                                entity.getRealName(),
                                entity.getPassword(),
                                entity.getFingerprint()});
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * 删除所有数据
     */
    public void deleteAll() {
        String sql = "DELETE FROM user ";
        db.execSQL(sql);
    }

    /**
     * query User
     *
     * @return List<archive>
     */
    public List<User> queryUser() {
        ArrayList<User> list = new ArrayList<User>();
        Cursor c;
        String sql = "SELECT * FROM user";
        c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            User user = new User();
            user.setId(c.getInt(c.getColumnIndex("id")));
            user.setUserName(c.getString(c.getColumnIndex("USER_NAME")));
            user.setKeyStr(c.getString(c.getColumnIndex("KEYSTR")));
            user.setRealName(c.getString(c.getColumnIndex("REAL_NAME")));
            user.setPassword(c.getString(c.getColumnIndex("PASSWORD")));
            user.setFingerprint(c.getString(c.getColumnIndex("FINGERPRINT")));
            list.add(user);
        }
        c.close();
        return list;
    }
}
