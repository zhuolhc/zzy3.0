package com.property.model;

public class ParkingSpace {
    private String spaceId;        // 车位编号
    private String location;       // 车位位置
    private String status;         // 状态（空闲/已占用）
    private String residentId;     // 绑定的住户ID
    private String carNumber;      // 车牌号
    private double monthlyFee;     // 月租费用
    private String createTime;     // 创建时间
    
    public ParkingSpace() {
    }
    
    public ParkingSpace(String spaceId, String location, String status, 
                       String residentId, String carNumber, double monthlyFee, 
                       String createTime) {
        this.spaceId = spaceId;
        this.location = location;
        this.status = status;
        this.residentId = residentId;
        this.carNumber = carNumber;
        this.monthlyFee = monthlyFee;
        this.createTime = createTime;
    }
    
    // Getter和Setter方法
    public String getSpaceId() { return spaceId; }
    public void setSpaceId(String spaceId) { this.spaceId = spaceId; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getResidentId() { return residentId; }
    public void setResidentId(String residentId) { this.residentId = residentId; }
    
    public String getCarNumber() { return carNumber; }
    public void setCarNumber(String carNumber) { this.carNumber = carNumber; }
    
    public double getMonthlyFee() { return monthlyFee; }
    public void setMonthlyFee(double monthlyFee) { this.monthlyFee = monthlyFee; }
    
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
} 