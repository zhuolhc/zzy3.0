package dao;

import model.Resident;
import util.FileUtil;
import java.util.List;
import java.util.ArrayList;

public class ResidentDAO {
    private static final String FILE_NAME = "residents.txt";
    
    public void saveResident(Resident resident) {
        List<String> residents = FileUtil.readFromFile(FILE_NAME);
        residents.add(convertToString(resident));
        FileUtil.writeToFile(FILE_NAME, residents);
    }
    
    public Resident findById(String residentId) {
        List<String> residents = FileUtil.readFromFile(FILE_NAME);
        for (String line : residents) {
            Resident resident = convertToResident(line);
            if (resident.getResidentId().equals(residentId)) {
                return resident;
            }
        }
        return null;
    }
    
    private String convertToString(Resident resident) {
        return String.join(",", 
            resident.getResidentId(),
            resident.getName(),
            resident.getPhone(),
            resident.getAddress(),
            String.valueOf(resident.getPrepaidAmount()),
            String.valueOf(resident.getArrearsAmount())
        );
    }
    
    private Resident convertToResident(String line) {
        String[] parts = line.split(",");
        return new Resident(parts[0], parts[1], parts[2], parts[3], 
                          Double.parseDouble(parts[4]), 
                          Double.parseDouble(parts[5]));
    }
} 