package com.jintoufs.logstics.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jintoufs.logstics.entity.Cashbox;
import com.jintoufs.logstics.entity.Terminal;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by Administrator on 2017/6/9 0009.
 */

public class CashBoxDao {
    private DBHelper helper;
    private SQLiteDatabase db;

    public CashBoxDao(Context context) {
        helper = BeanFactory.getDBHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add Cashbox
     *
     * @param list
     */
    public void add(List<Cashbox> list) {
        db.beginTransaction(); // 开始事务
        try {
            for (Cashbox entity : list) {
                db.execSQL("INSERT INTO cashbox (" +
                                "id," +
                                "DETAIL_ID," +
                                "CASH_BOX_CODE," +
                                "RFID," +
                                "CASH_BOX_ID," +
                                "TYPE," +
                                "USE_TYPE," +
                                "FOUND_STATUS)" +
                                "VALUES(?,?,?,?,?,?,?,?)",
                        new Object[] { entity.getId(),
                                entity.getDetailId(),
                                entity.getCashBoxCode(),
                                entity.getRfid(),
                                entity.getCashBoxId(),
                                entity.getType(),
                                entity.getUseType(),
                                entity.getFoundStatus()});
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
        String sql = "DELETE FROM cashbox ";
        db.execSQL(sql);
    }

    /**
     * query CashBox by useType and detailId
     *
     * @return List<archive>
     */
    public List<Cashbox> queryCashBoxByUseType(String useType, long detailId) {
        ArrayList<Cashbox> list = new ArrayList<Cashbox>();
        Cursor c;
        String sql="SELECT * FROM cashbox where USE_TYPE=? and DETAIL_ID=?";
        c = db.rawQuery(sql, new String[]{useType, String.valueOf(detailId)});
        while (c.moveToNext()) {
            Cashbox cashbox = new Cashbox();
            cashbox.setId(c.getInt(c.getColumnIndex("id")));
            cashbox.setCashBoxCode(c.getString(c.getColumnIndex("CASH_BOX_CODE")));
            cashbox.setRfid(c.getString(c.getColumnIndex("RFID")));
            cashbox.setCashBoxId(c.getInt(c.getColumnIndex("CASH_BOX_ID")));
            cashbox.setType(c.getInt(c.getColumnIndex("TYPE")));
            cashbox.setUseType(c.getString(c.getColumnIndex("USE_TYPE")));
            cashbox.setDetailId(c.getInt(c.getColumnIndex("DETAIL_ID")));
            cashbox.setFoundStatus(c.getInt(c.getColumnIndex("FOUND_STATUS")));
            list.add(cashbox);
        }
        c.close();
        return list;
    }
    /**
     * query CashBox by useType and foundStatus
     *
     * @return List<archive>
     */
    public List<Cashbox> queryCashBox(String useType, long detailId, Integer foundStatus) {
        ArrayList<Cashbox> list = new ArrayList<Cashbox>();
        Cursor c;
        String sql="SELECT * FROM cashbox where USE_TYPE=? and DETAIL_ID=? and FOUND_STATUS=?";
        c = db.rawQuery(sql, new String[]{useType ,String.valueOf(detailId), String.valueOf(foundStatus)});
        while (c.moveToNext()) {
            Cashbox cashbox = new Cashbox();
            cashbox.setId(c.getInt(c.getColumnIndex("id")));
            cashbox.setCashBoxCode(c.getString(c.getColumnIndex("CASH_BOX_CODE")));
            cashbox.setRfid(c.getString(c.getColumnIndex("RFID")));
            cashbox.setCashBoxId(c.getInt(c.getColumnIndex("CASH_BOX_ID")));
            cashbox.setType(c.getInt(c.getColumnIndex("TYPE")));
            cashbox.setUseType(c.getString(c.getColumnIndex("USE_TYPE")));
            cashbox.setDetailId(c.getInt(c.getColumnIndex("DETAIL_ID")));
            cashbox.setFoundStatus(c.getInt(c.getColumnIndex("FOUND_STATUS")));
            list.add(cashbox);
        }
        c.close();
        return list;
    }

    public Cashbox queryCashBox(String useType, long detailId, String cashBoxCode){
        Cursor c;
        String sql="SELECT * FROM cashbox where USE_TYPE=? and DETAIL_ID=? and CASH_BOX_CODE=?";
        c = db.rawQuery(sql, new String[]{useType ,String.valueOf(detailId), cashBoxCode});
        c.moveToFirst();
        Cashbox cashbox = new Cashbox();
        cashbox.setId(c.getInt(c.getColumnIndex("id")));
        cashbox.setCashBoxCode(c.getString(c.getColumnIndex("CASH_BOX_CODE")));
        cashbox.setRfid(c.getString(c.getColumnIndex("RFID")));
        cashbox.setCashBoxId(c.getInt(c.getColumnIndex("CASH_BOX_ID")));
        cashbox.setType(c.getInt(c.getColumnIndex("TYPE")));
        cashbox.setUseType(c.getString(c.getColumnIndex("USE_TYPE")));
        cashbox.setDetailId(c.getInt(c.getColumnIndex("DETAIL_ID")));
        cashbox.setFoundStatus(c.getInt(c.getColumnIndex("FOUND_STATUS")));
        c.close();

        return cashbox;
    }
    /**
     * update foundStatus
     */
    public void updateStatus(Integer foundStatus, long detailId) {
        String sql = "UPDATE cashbox SET FOUND_STATUS=? WHERE DETAIL_ID = ?";
        db.execSQL(sql, new String[]{String.valueOf(foundStatus), String.valueOf(detailId)});
    }

    /**
     * update foundStatus
     */
    public void updateStatus(Integer foundStatus, long detailId, String useType) {
        String sql = "UPDATE cashbox SET FOUND_STATUS=? WHERE DETAIL_ID = ? and USE_TYPE=?";
        db.execSQL(sql, new String[]{String.valueOf(foundStatus), String.valueOf(detailId), useType});
    }

    /**
     * update cashBoxCode
     */
    public void updateCashBox(String newCode,String rfid, Integer cashBoxId, Integer id, String useType) {
        String sql = "UPDATE cashbox SET CASH_BOX_CODE=?,RFID=?,CASH_BOX_ID=? WHERE id = ? and USE_TYPE=?";
        db.execSQL(sql, new String[]{newCode, rfid, String.valueOf(cashBoxId), String.valueOf(id), useType});
    }

    ///**
    // * update Rfid
    // */
    //public void updateRfid(String newCode, Integer id, String useType) {
    //    String sql = "UPDATE cashbox SET RFID=? WHERE id = ? and USE_TYPE=?";
    //    db.execSQL(sql, new String[]{newCode, String.valueOf(id), useType});
    //}
    //
    ///**
    // * update cashBoxId
    // */
    //public void updateCashBoxId(String newCode, Integer id, String useType) {
    //    String sql = "UPDATE cashbox SET CASH_BOX_ID=? WHERE id = ? and USE_TYPE=?";
    //    db.execSQL(sql, new String[]{newCode, String.valueOf(id), useType});
    //}
}
