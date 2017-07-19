package com.jintoufs.logstics.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jintoufs.logstics.entity.Equipment;

import java.util.ArrayList;
import java.util.List;

public class EquipmentDao {
    private DBHelper helper;
    private SQLiteDatabase db;

    public EquipmentDao(Context context) {
        helper = BeanFactory.getDBHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add Equipment
     *
     * @param entity
     */
    public void add(Equipment entity) {
        db.beginTransaction(); // 开始事务
        try {
            db.execSQL("INSERT INTO equipment (" +
                            "DETAIL_ID," +
                            "withdrawStatus," +
                            "swallowedCard," +
                            "checkMachine," +
                            "replaceCashBox," +
                            "closeDoor," +
                            "addCash," +
                            "checkMaterial," +
                            "safetyProtection," +
                            "technicalProtection," +
                            "sanitation," +
                            "inspection," +
                            "photoStatus," +
                            "createTime," +
                            "endTime," +
                            "withdrawImg," +
                            "swallowedCardImg," +
                            "machineImg," +
                            "closeDoorImg," +
                            "safetyProtectionImg," +
                            "technicalProtectionImg," +
                            "sanitationImg," +
                            "inspectionImg," +
                            "photo," +
                            "finished_status) " +
                            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    new Object[]{
                            entity.getTaskDetailId(),
                            entity.getWithdrawStatus(),
                            entity.getSwallowedCard(),
                            entity.getCheckMachine(),
                            entity.getReplaceCashBox(),
                            entity.getCloseDoor(),
                            entity.getAddCash(),
                            entity.getCheckMaterial(),
                            entity.getSafetyProtection(),
                            entity.getTechnicalProtection(),
                            entity.getSanitation(),
                            entity.getInspection(),
                            entity.getPhotoStatus(),
                            entity.getCreateTime(),
                            entity.getEndTime(),
                            entity.getWithdrawImg(),
                            entity.getSwallowedCardImg(),
                            entity.getMachineImg(),
                            entity.getCloseDoorImg(),
                            entity.getSafetyProtectionImg(),
                            entity.getTechnicalProtectionImg(),
                            entity.getSanitationImg(),
                            entity.getInspectionImg(),
                            entity.getPhoto(),
                            entity.getFinishedStatus()
                    });
            db.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * 清空表中数据
     */
    public void deleteAll() {
        String sql = "delete from equipment";
        db.execSQL(sql);
    }

    /**
     * 根据detailId和step查询状态
     * @return
     */
    public Equipment queryStatus(long detailId){
        Equipment equipment = new Equipment();
        String sql = "select * from equipment where DETAIL_ID=?";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(detailId)});
        c.moveToFirst();
        equipment.setId(c.getInt(c.getColumnIndex("id")));
        equipment.setTaskDetailId(c.getLong(c.getColumnIndex("DETAIL_ID")));
        equipment.setWithdrawStatus(c.getString(c.getColumnIndex("withdrawStatus")));
        equipment.setSwallowedCard(c.getString(c.getColumnIndex("swallowedCard")));
        equipment.setCheckMachine(c.getString(c.getColumnIndex("checkMachine")));
        equipment.setReplaceCashBox(c.getString(c.getColumnIndex("replaceCashBox")));
        equipment.setCloseDoor(c.getString(c.getColumnIndex("closeDoor")));
        equipment.setAddCash(c.getString(c.getColumnIndex("addCash")));
        equipment.setCheckMaterial(c.getString(c.getColumnIndex("checkMaterial")));
        equipment.setSafetyProtection(c.getString(c.getColumnIndex("safetyProtection")));
        equipment.setTechnicalProtection(c.getString(c.getColumnIndex("technicalProtection")));
        equipment.setSanitation(c.getString(c.getColumnIndex("sanitation")));
        equipment.setInspection(c.getString(c.getColumnIndex("inspection")));
        equipment.setPhotoStatus(c.getString(c.getColumnIndex("photoStatus")));
        equipment.setCreateTime(c.getString(c.getColumnIndex("createTime")));
        equipment.setEndTime(c.getString(c.getColumnIndex("endTime")));
        equipment.setWithdrawImg(c.getString(c.getColumnIndex("withdrawImg")));
        equipment.setSwallowedCardImg(c.getString(c.getColumnIndex("swallowedCardImg")));
        equipment.setMachineImg(c.getString(c.getColumnIndex("machineImg")));
        equipment.setCloseDoorImg(c.getString(c.getColumnIndex("closeDoorImg")));
        equipment.setSafetyProtectionImg(c.getString(c.getColumnIndex("safetyProtectionImg")));
        equipment.setTechnicalProtectionImg(c.getString(c.getColumnIndex("technicalProtectionImg")));
        equipment.setSanitationImg(c.getString(c.getColumnIndex("sanitationImg")));
        equipment.setInspectionImg(c.getString(c.getColumnIndex("inspectionImg")));
        equipment.setPhoto(c.getString(c.getColumnIndex("photo")));
        equipment.setFinishedStatus(c.getString(c.getColumnIndex("finished_status")));
        c.close();
        return equipment;
    }

    /**
     * query Equipment
     *
     * @return List<Equipment>
     */
    public List<Equipment> queryEquipment(String finishedStatus) {
        ArrayList<Equipment> list = new ArrayList<Equipment>();
        Cursor c;
        String sql="SELECT * FROM equipment where finished_status=?";
        c = db.rawQuery(sql, new String[]{finishedStatus});
        while (c.moveToNext()) {
            Equipment equipment = new Equipment();
            equipment.setTaskDetailId(c.getLong(c.getColumnIndex("DETAIL_ID")));
            equipment.setWithdrawStatus(c.getString(c.getColumnIndex("withdrawStatus")));
            equipment.setSwallowedCard(c.getString(c.getColumnIndex("swallowedCard")));
            equipment.setCheckMachine(c.getString(c.getColumnIndex("checkMachine")));
            equipment.setReplaceCashBox(c.getString(c.getColumnIndex("replaceCashBox")));
            equipment.setCloseDoor(c.getString(c.getColumnIndex("closeDoor")));
            equipment.setAddCash(c.getString(c.getColumnIndex("addCash")));
            equipment.setCheckMaterial(c.getString(c.getColumnIndex("checkMaterial")));
            equipment.setSafetyProtection(c.getString(c.getColumnIndex("safetyProtection")));
            equipment.setTechnicalProtection(c.getString(c.getColumnIndex("technicalProtection")));
            equipment.setSanitation(c.getString(c.getColumnIndex("sanitation")));
            equipment.setInspection(c.getString(c.getColumnIndex("inspection")));
            equipment.setPhotoStatus(c.getString(c.getColumnIndex("photoStatus")));
            equipment.setCreateTime(c.getString(c.getColumnIndex("createTime")));
            equipment.setEndTime(c.getString(c.getColumnIndex("endTime")));
            equipment.setWithdrawImg(c.getString(c.getColumnIndex("withdrawImg")));
            equipment.setSwallowedCardImg(c.getString(c.getColumnIndex("swallowedCardImg")));
            equipment.setMachineImg(c.getString(c.getColumnIndex("machineImg")));
            equipment.setCloseDoorImg(c.getString(c.getColumnIndex("closeDoorImg")));
            equipment.setSafetyProtectionImg(c.getString(c.getColumnIndex("safetyProtectionImg")));
            equipment.setTechnicalProtectionImg(c.getString(c.getColumnIndex("technicalProtectionImg")));
            equipment.setSanitationImg(c.getString(c.getColumnIndex("sanitationImg")));
            equipment.setInspectionImg(c.getString(c.getColumnIndex("inspectionImg")));
            equipment.setPhoto(c.getString(c.getColumnIndex("photo")));
            equipment.setFinishedStatus(c.getString(c.getColumnIndex("finished_status")));
            list.add(equipment);
        }
        c.close();
        return list;
    }

    /**
     * query All Equipment
     *
     * @return List<Equipment>
     */
    public List<Equipment> queryAllEquipment() {
        ArrayList<Equipment> list = new ArrayList<Equipment>();
        Cursor c;
        String sql="SELECT * FROM equipment";
        c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            Equipment equipment = new Equipment();
            equipment.setId(c.getInt(c.getColumnIndex("id")));
            equipment.setTaskDetailId(c.getLong(c.getColumnIndex("DETAIL_ID")));
            equipment.setWithdrawStatus(c.getString(c.getColumnIndex("withdrawStatus")));
            equipment.setSwallowedCard(c.getString(c.getColumnIndex("swallowedCard")));
            equipment.setCheckMachine(c.getString(c.getColumnIndex("checkMachine")));
            equipment.setReplaceCashBox(c.getString(c.getColumnIndex("replaceCashBox")));
            equipment.setCloseDoor(c.getString(c.getColumnIndex("closeDoor")));
            equipment.setAddCash(c.getString(c.getColumnIndex("addCash")));
            equipment.setCheckMaterial(c.getString(c.getColumnIndex("checkMaterial")));
            equipment.setSafetyProtection(c.getString(c.getColumnIndex("safetyProtection")));
            equipment.setTechnicalProtection(c.getString(c.getColumnIndex("technicalProtection")));
            equipment.setSanitation(c.getString(c.getColumnIndex("sanitation")));
            equipment.setInspection(c.getString(c.getColumnIndex("inspection")));
            equipment.setPhotoStatus(c.getString(c.getColumnIndex("photoStatus")));
            equipment.setCreateTime(c.getString(c.getColumnIndex("createTime")));
            equipment.setEndTime(c.getString(c.getColumnIndex("endTime")));
            equipment.setWithdrawImg(c.getString(c.getColumnIndex("withdrawImg")));
            equipment.setSwallowedCardImg(c.getString(c.getColumnIndex("swallowedCardImg")));
            equipment.setMachineImg(c.getString(c.getColumnIndex("machineImg")));
            equipment.setCloseDoorImg(c.getString(c.getColumnIndex("closeDoorImg")));
            equipment.setSafetyProtectionImg(c.getString(c.getColumnIndex("safetyProtectionImg")));
            equipment.setTechnicalProtectionImg(c.getString(c.getColumnIndex("technicalProtectionImg")));
            equipment.setSanitationImg(c.getString(c.getColumnIndex("sanitationImg")));
            equipment.setInspectionImg(c.getString(c.getColumnIndex("inspectionImg")));
            equipment.setPhoto(c.getString(c.getColumnIndex("photo")));
            equipment.setFinishedStatus(c.getString(c.getColumnIndex("finished_status")));
            list.add(equipment);
        }
        c.close();
        return list;
    }
    /**
     * update status
     */
    public void updateStatus(long taskDetailId, Integer step, String status) {
        String sql = "UPDATE equipment SET ";
        switch (step){
            case 1:
                sql+="withdrawStatus=?";
                break;
            case 2:
                sql+="swallowedCard=?";
                break;
            case 4:
                sql+="checkMachine=?";
                break;
            case 5:
                sql+="replaceCashBox=?";
                break;
            case 6:
                sql+="closeDoor=?";
                break;
            case 7:
                sql+="addCash=?";
                break;
            case 8:
                sql+="checkMaterial=?";
                break;
            case 9:
                sql+="safetyProtection=?";
                break;
            case 10:
                sql+="technicalProtection=?";
                break;
            case 11:
                sql+="sanitation=?";
                break;
            case 12:
                sql+="inspection=?";
                break;
            case 13:
                sql+="photoStatus=?";
                break;
        }
        sql += " WHERE DETAIL_ID = ?";
        db.execSQL(sql, new String[]{status, String.valueOf(taskDetailId)});
    }

    /**
     * update imageUrl
     */
    public void updateImageUrl(String imageUrl, long taskDetailId, Integer step) {
        String sql = "UPDATE equipment SET ";
        switch (step){
            case 1:
                sql+="withdrawImg=?";
                break;
            case 2:
                sql+="swallowedCardImg=?";
                break;
            case 4:
                sql+="machineImg=?";
                break;
            case 6:
                sql+="closeDoorImg=?";
                break;
            case 9:
                sql+="safetyProtectionImg=?";
                break;
            case 10:
                sql+="technicalProtectionImg=?";
                break;
            case 11:
                sql+="sanitationImg=?";
                break;
            case 12:
                sql+="inspectionImg=?";
                break;
            case 13:
                sql+="photo=?";
                break;
        }
        sql += " WHERE DETAIL_ID = ?";
        db.execSQL(sql, new String[]{imageUrl, String.valueOf(taskDetailId)});
    }

    /**
     * 更新任务开始时间
     * @param createTime
     * @param taskDetailId
     */
    public void updateCreateTime(long createTime, long taskDetailId){
        String sql="UPDATE equipment SET createTime=? where DETAIL_ID = ?";
        db.execSQL(sql, new String[]{String.valueOf(createTime), String.valueOf(taskDetailId)});
    }

    /**
     * 更新任务开始时间
     * @param endTime
     * @param taskDetailId
     */
    public void updateEndTime(long endTime, long taskDetailId){
        String sql="UPDATE equipment SET endTime=? where DETAIL_ID = ?";
        db.execSQL(sql, new String[]{String.valueOf(endTime), String.valueOf(taskDetailId)});
    }

    /**
     * 更新任务完成状态
     * @param finishedStatus
     * @param taskDetailId
     */
    public void updateFinishedStatus(String finishedStatus, long taskDetailId){
        String sql="UPDATE equipment SET finished_status=? where DETAIL_ID = ?";
        db.execSQL(sql, new String[]{finishedStatus, String.valueOf(taskDetailId)});
    }

    public void deleteByDetailId(String detailId) {
        String sql = "delete from equipment where DETAIL_ID=?";
        db.execSQL(sql, new String[]{detailId});
    }
}
