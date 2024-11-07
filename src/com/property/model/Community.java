package com.property.model;

public class Community {
    private String communityId;
    private String name;
    private int buildingCount;
    private int unitCount;
    private String address;
    private String description;
    private String createTime;
    
    public Community() {
        // 默认构造函数
    }
    
    public Community(String communityId, String name, int buildingCount, 
                    int unitCount, String address, String description, 
                    String createTime) {
        this.communityId = communityId;
        this.name = name;
        this.buildingCount = buildingCount;
        this.unitCount = unitCount;
        this.address = address;
        this.description = description;
        this.createTime = createTime;
    }
    
    // Getter方法
    public String getCommunityId() { return communityId; }
    public String getName() { return name; }
    public int getBuildingCount() { return buildingCount; }
    public int getUnitCount() { return unitCount; }
    public String getAddress() { return address; }
    public String getDescription() { return description; }
    public String getCreateTime() { return createTime; }
    
    // Setter方法
    public void setCommunityId(String communityId) { this.communityId = communityId; }
    public void setName(String name) { this.name = name; }
    public void setBuildingCount(int buildingCount) { this.buildingCount = buildingCount; }
    public void setUnitCount(int unitCount) { this.unitCount = unitCount; }
    public void setAddress(String address) { this.address = address; }
    public void setDescription(String description) { this.description = description; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
} 