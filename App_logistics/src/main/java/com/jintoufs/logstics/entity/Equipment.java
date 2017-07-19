package com.jintoufs.logstics.entity;

/**
 * 自助设备离线操作，上传数据实体类
 * Created by Administrator on 2017/6/12 0012.
 */

public class Equipment extends Entity {
    private long taskDetailId;
    private String withdrawStatus;//撤防状态（0-未执行，1-正常，2-异常）
    private String swallowedCard;//吞没卡（0-未执行，1-有吞没卡，2-无吞没卡）
    private String checkMachine;//检查机器（0-未执行，1-正常，2-异常）
    private String replaceCashBox;//更换钞箱（0-待执行，1-钞箱取出，2-钞箱放入，3-取出钞箱异常，4-放入钞箱异常）
    private String closeDoor;//关闭保险箱（0-未执行，1-正常，2-异常）
    private String addCash;//清机加钞（0-待执行，1-完成，2-异常）
    private String checkMaterial;//检查耗材（0-待执行，1-正常，2-异常）
    private String safetyProtection;//安全防护（0-待执行，1-正常，2异常）
    private String technicalProtection;//技防（0-待执行，1-正常，2-异常）
    private String sanitation;//卫生（0-未执行，1-正常，2-异常）
    private String inspection;//物业巡检（0-待执行，1-正常，2-异常）
    private String photoStatus;//自拍存留照（0-待执行，1-正常，2-异常）
    private String endTime;//结束时间，时间戳
    private String createTime;//开始时间，时间戳
    private String withdrawImg;//撤防图片路径
    private String swallowedCardImg;//吞没卡照片路径
    private String machineImg;//检查机器照片路径
    private String closeDoorImg;//关闭保险箱门图片路径
    private String safetyProtectionImg;//安防异常图片路径
    private String technicalProtectionImg;//技防异常图片路径
    private String sanitationImg;//卫生图片路径
    private String inspectionImg;//物业巡检异常图片路径
    private String photo;//自拍存留照路径
    private String finishedStatus;//设备操作完成状态

    public String getFinishedStatus() {
        return finishedStatus;
    }

    public void setFinishedStatus(String finishedStatus) {
        this.finishedStatus = finishedStatus;
    }

    public String getCheckMachine() {
        return checkMachine;
    }

    public void setCheckMachine(String checkMachine) {
        this.checkMachine = checkMachine;
    }

    public String getCloseDoor() {
        return closeDoor;
    }

    public void setCloseDoor(String closeDoor) {
        this.closeDoor = closeDoor;
    }

    public String getSanitation() {
        return sanitation;
    }

    public void setSanitation(String sanitation) {
        this.sanitation = sanitation;
    }

    public String getPhotoStatus() {
        return photoStatus;
    }

    public void setPhotoStatus(String photoStatus) {
        this.photoStatus = photoStatus;
    }

    public String getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(String withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public String getSwallowedCard() {
        return swallowedCard;
    }

    public void setSwallowedCard(String swallowedCard) {
        this.swallowedCard = swallowedCard;
    }

    public String getReplaceCashBox() {
        return replaceCashBox;
    }

    public void setReplaceCashBox(String replaceCashBox) {
        this.replaceCashBox = replaceCashBox;
    }

    public String getAddCash() {
        return addCash;
    }

    public void setAddCash(String addCash) {
        this.addCash = addCash;
    }

    public String getCheckMaterial() {
        return checkMaterial;
    }

    public void setCheckMaterial(String checkMaterial) {
        this.checkMaterial = checkMaterial;
    }

    public String getSafetyProtection() {
        return safetyProtection;
    }

    public void setSafetyProtection(String safetyProtection) {
        this.safetyProtection = safetyProtection;
    }

    public String getTechnicalProtection() {
        return technicalProtection;
    }

    public void setTechnicalProtection(String technicalProtection) {
        this.technicalProtection = technicalProtection;
    }

    public String getInspection() {
        return inspection;
    }

    public void setInspection(String inspection) {
        this.inspection = inspection;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getWithdrawImg() {
        return withdrawImg;
    }

    public void setWithdrawImg(String withdrawImg) {
        this.withdrawImg = withdrawImg;
    }

    public String getSwallowedCardImg() {
        return swallowedCardImg;
    }

    public void setSwallowedCardImg(String swallowedCardImg) {
        this.swallowedCardImg = swallowedCardImg;
    }

    public String getMachineImg() {
        return machineImg;
    }

    public void setMachineImg(String machineImg) {
        this.machineImg = machineImg;
    }

    public String getCloseDoorImg() {
        return closeDoorImg;
    }

    public void setCloseDoorImg(String closeDoorImg) {
        this.closeDoorImg = closeDoorImg;
    }

    public String getSafetyProtectionImg() {
        return safetyProtectionImg;
    }

    public void setSafetyProtectionImg(String safetyProtectionImg) {
        this.safetyProtectionImg = safetyProtectionImg;
    }

    public String getTechnicalProtectionImg() {
        return technicalProtectionImg;
    }

    public void setTechnicalProtectionImg(String technicalProtectionImg) {
        this.technicalProtectionImg = technicalProtectionImg;
    }

    public String getSanitationImg() {
        return sanitationImg;
    }

    public void setSanitationImg(String sanitationImg) {
        this.sanitationImg = sanitationImg;
    }

    public String getInspectionImg() {
        return inspectionImg;
    }

    public void setInspectionImg(String inspectionImg) {
        this.inspectionImg = inspectionImg;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public long getTaskDetailId() {
        return taskDetailId;
    }

    public void setTaskDetailId(long taskDetailId) {
        this.taskDetailId = taskDetailId;
    }


}
