package com.property.model;

public class Resident {
    private String residentId;
    private String name;
    private String phone;
    private String address;
    private double prepayAmount;
    private double arrearsAmount;
    private String createTime;
    
    public Resident() {
        // 默认构造函数
    }
    
    public Resident(String residentId, String name, String phone, String address, 
                   double prepayAmount, double arrearsAmount, String createTime) {
        this.residentId = residentId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.prepayAmount = prepayAmount;
        this.arrearsAmount = arrearsAmount;
        this.createTime = createTime;
    }
    
    // Getter方法
    public String getResidentId() { return residentId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public double getPrepayAmount() { return prepayAmount; }
    public double getArrearsAmount() { return arrearsAmount; }
    public String getCreateTime() { return createTime; }
    
    // Setter方法
    public void setResidentId(String residentId) { this.residentId = residentId; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setPrepayAmount(double prepayAmount) { this.prepayAmount = prepayAmount; }
    public void setArrearsAmount(double arrearsAmount) { this.arrearsAmount = arrearsAmount; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
} 