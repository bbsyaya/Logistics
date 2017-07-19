package com.jintoufs.logstics.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jintoufs.logstics.entity.Terminal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/12 0012.
 */

public class TerminalDao {
    private DBHelper helper;
    private SQLiteDatabase db;

    public TerminalDao(Context context) {
        helper = BeanFactory.getDBHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add Terminal
     *
     * @param list
     */
    public void add(List<Terminal> list) {
        db.beginTransaction(); // 开始事务
        try {
            for (Terminal entity : list) {
                db.execSQL("INSERT INTO terminal (ID,NAME,CODE,ADDRESS,VERIFY_CODE,ID_CODE,RFID,DETAIL_ID," +
                                "LOCK_TYPE,SITE_NAME,SITE_SHORT,STATUS,STATE,TYPE,DELIVER_TYPE,LONGITUDE,LATITUDE," +
                                "ID_CODE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        new Object[]{
                                entity.getId(),
                                entity.getName(),

                                entity.getCode(),
                                entity.getAddress(),
                                entity.getVerifyCode(),
                                entity.getIdCode(),
                                entity.getRfid(),
                                entity.getDetailId(),
                                entity.getLockType(),
                                entity.getSiteName(),
                                entity.getSiteShort(),
                                entity.getStatus(),
                                entity.getState(),
                                entity.getType(),
                                entity.getDeliverType(),
                                entity.getLongitude(),
                                entity.getLatitude(),
                                entity.getIdCode(),
                });
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * add Terminal
     *
     * @param entity
     */
    public void add(Terminal entity) {
        db.beginTransaction(); // 开始事务
        try {
                db.execSQL("INSERT INTO terminal (id,NAME,CODE,ADDRESS,VERIFY_CODE,ID_CODE,RFID,DETAIL_ID," +
                                "LOCK_TYPE,SITE_NAME,SITE_SHORT,STATUS,STATE,TYPE,DELIVER_TYPE,LONGITUDE,LATITUDE," +
                                "ID_CODE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        new Object[]{entity.getId(),
                                entity.getName(),
                                entity.getCode(),
                                entity.getAddress(),
                                entity.getVerifyCode(),
                                entity.getIdCode(),
                                entity.getRfid(),
                                entity.getDetailId(),
                                entity.getLockType(),
                                entity.getSiteName(),
                                entity.getSiteShort(),
                                entity.getStatus(),
                                entity.getState(),
                                entity.getType(),
                                entity.getDeliverType(),
                                entity.getLongitude(),
                                entity.getLatitude(),
                                entity.getIdCode(),
                        });
            db.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            db.endTransaction(); // 结束事务
        }
    }
    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }



    /**
     * 清空表中数据
     */
    public void deleteAll() {
        String sql = "delete from terminal";
        db.execSQL(sql);
    }


    /**
     * query all terminal, return list
     *
     * @return List<archive>
     */
    public List<Terminal> queryAllTerminal() {
        ArrayList<Terminal> list = new ArrayList<Terminal>();
        Cursor c;
        String sql="SELECT * FROM terminal";
        c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            Terminal terminal = new Terminal();
            terminal.setId(c.getInt(c.getColumnIndex("id")));
            terminal.setName(c.getString(c.getColumnIndex("NAME")));
            terminal.setCode(c.getString(c.getColumnIndex("CODE")));
            terminal.setAddress(c.getString(c.getColumnIndex("ADDRESS")));
            terminal.setVerifyCode(c.getString(c.getColumnIndex("VERIFY_CODE")));
            terminal.setRfid(c.getString(c.getColumnIndex("RFID")));
            terminal.setDetailId(c.getLong(c.getColumnIndex("DETAIL_ID")));
            terminal.setLockType(c.getInt(c.getColumnIndex("LOCK_TYPE")));
            terminal.setSiteName(c.getString(c.getColumnIndex("SITE_NAME")));
            terminal.setSiteShort(c.getString(c.getColumnIndex("SITE_SHORT")));
            terminal.setStatus(c.getInt(c.getColumnIndex("STATUS")));
            terminal.setState(c.getInt(c.getColumnIndex("STATE")));
            terminal.setType(c.getString(c.getColumnIndex("TYPE")));
            terminal.setDeliverType(c.getInt(c.getColumnIndex("DELIVER_TYPE")));
            terminal.setLongitude(c.getDouble(c.getColumnIndex("LONGITUDE")));
            terminal.setLatitude(c.getDouble(c.getColumnIndex("LATITUDE")));
            terminal.setIdCode(c.getString(c.getColumnIndex("ID_CODE")));
            list.add(terminal);
        }
        c.close();
        return list;
    }

    public void deleteById(String checkId) {
        String sql = "delete from t_check_store where CHECK_ID=?";
        db.execSQL(sql, new String[]{checkId});
    }
}
