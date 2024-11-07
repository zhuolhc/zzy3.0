public class Resident {
    private String residentId;
    private String name;
    private String phone;
    private String address;
    private double prepaidAmount;
    private double arrearsAmount;
    
    // 构造函数
    public Resident(String residentId, String name, String phone, String address, 
                   double prepaidAmount, double arrearsAmount) {
        this.residentId = residentId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.prepaidAmount = prepaidAmount;
        this.arrearsAmount = arrearsAmount;
    }
    
    // Getter方法
    public String getResidentId() { return residentId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public double getPrepaidAmount() { return prepaidAmount; }
    public double getArrearsAmount() { return arrearsAmount; }
    
    // Setter方法
    public void setResidentId(String residentId) { this.residentId = residentId; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setPrepaidAmount(double prepaidAmount) { this.prepaidAmount = prepaidAmount; }
    public void setArrearsAmount(double arrearsAmount) { this.arrearsAmount = arrearsAmount; }
} 