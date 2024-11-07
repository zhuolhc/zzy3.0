package com.property.model;

public class HouseType {
    private String typeId;
    private String typeName;
    private double area;
    private String description;
    private String createTime;
    
    public HouseType() {
        // 默认构造函数
    }
    
    public HouseType(String typeId, String typeName, double area, 
                    String description, String createTime) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.area = area;
        this.description = description;
        this.createTime = createTime;
    }
    
    // Getter方法
    public String getTypeId() { return typeId; }
    public String getTypeName() { return typeName; }
    public double getArea() { return area; }
    public String getDescription() { return description; }
    public String getCreateTime() { return createTime; }
    
    // Setter方法
    public void setTypeId(String typeId) { this.typeId = typeId; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public void setArea(double area) { this.area = area; }
    public void setDescription(String description) { this.description = description; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
} 