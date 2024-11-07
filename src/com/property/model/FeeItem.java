package com.property.model;

public class FeeItem {
    private String itemId;
    private String name;        // 收费名称
    private double amount;      // 收费金额
    private String unit;        // 收费单位
    private String cycle;       // 收费周期
    private String createTime;  // 创建时间
    
    public FeeItem() {
        // 默认构造函数
    }
    
    public FeeItem(String itemId, String name, double amount, 
                  String unit, String cycle, String createTime) {
        this.itemId = itemId;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.cycle = cycle;
        this.createTime = createTime;
    }
    
    // Getter和Setter方法
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public String getCycle() { return cycle; }
    public void setCycle(String cycle) { this.cycle = cycle; }
    
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
} 