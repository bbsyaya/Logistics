package com.jintoufs.logstics.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jintoufs.logstics.entity.ReplaceCashBox;

import java.util.ArrayList;
import java.util.List;

public class ReplaceCashBoxDao {

    private DBHelper helper;
    private SQLiteDatabase db;

    public ReplaceCashBoxDao(Context context) {
        helper = BeanFactory.getDBHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add Cashbox
     *
     * @param entity
     */
    public void add(ReplaceCashBox entity) {
        db.beginTransaction(); // 开始事务
        try {
            db.execSQL("INSERT INTO changebox (" +
                            "DETAIL_ID," +
                            "ID_OUT_ITEM," +
                            "ID_IN_ITEM)" +
                            "VALUES(?,?,?)",
                    new Object[]{
                            entity.getDetailId(),
                            entity.getOutItemId(),
                            entity.getInItemId()});
            db.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * 删除所有数据
     */
    public void deleteAll() {
        String sql = "DELETE FROM changebox ";
        db.execSQL(sql);
    }

    /**
     * query ReplaceCashBox
     *
     */
    public List<ReplaceCashBox> queryCashBox(long detailId) {
        ArrayList<ReplaceCashBox> list = new ArrayList<ReplaceCashBox>();
        Cursor c;
        String sql="SELECT * FROM changebox where DETAIL_ID=?";
        c = db.rawQuery(sql, new String[]{String.valueOf(detailId)});
        while (c.moveToNext()) {
            ReplaceCashBox cashbox = new ReplaceCashBox();
            cashbox.setDetailId(c.getInt(c.getColumnIndex("DETAIL_ID")));
            cashbox.setOutItemId(c.getInt(c.getColumnIndex("ID_OUT_ITEM")));
            cashbox.setInItemId(c.getInt(c.getColumnIndex("ID_IN_ITEM")));
            list.add(cashbox);
        }
        c.close();
        return list;
    }
}
