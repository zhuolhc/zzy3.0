package com.property.dao;

import com.property.model.Community;
import com.property.util.FileUtil;
import java.util.List;
import java.util.ArrayList;

public class CommunityDAO {
    private static final String FILE_NAME = "data/communities.txt";
    
    public void addCommunity(Community community) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        lines.add(convertToString(community));
        FileUtil.writeToFile(FILE_NAME, lines);
    }
    
    public void updateCommunity(Community community) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<String> newLines = new ArrayList<>();
        
        for (String line : lines) {
            Community existingCommunity = parseCommunity(line);
            if (existingCommunity != null && 
                existingCommunity.getCommunityId().equals(community.getCommunityId())) {
                newLines.add(convertToString(community));
            } else {
                newLines.add(line);
            }
        }
        
        FileUtil.writeToFile(FILE_NAME, newLines);
    }
    
    public void deleteCommunity(String communityId) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<String> newLines = new ArrayList<>();
        
        for (String line : lines) {
            Community community = parseCommunity(line);
            if (community != null && !community.getCommunityId().equals(communityId)) {
                newLines.add(line);
            }
        }
        
        FileUtil.writeToFile(FILE_NAME, newLines);
    }
    
    public List<Community> getAllCommunities() {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<Community> communities = new ArrayList<>();
        
        for (String line : lines) {
            Community community = parseCommunity(line);
            if (community != null) {
                communities.add(community);
            }
        }
        
        return communities;
    }
    
    private Community parseCommunity(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 7) {
            return new Community(
                parts[0].trim(),
                parts[1].trim(),
                Integer.parseInt(parts[2].trim()),
                Integer.parseInt(parts[3].trim()),
                parts[4].trim(),
                parts[5].trim(),
                parts[6].trim()
            );
        }
        return null;
    }
    
    private String convertToString(Community community) {
        return String.join(",",
            community.getCommunityId(),
            community.getName(),
            String.valueOf(community.getBuildingCount()),
            String.valueOf(community.getUnitCount()),
            community.getAddress(),
            community.getDescription(),
            community.getCreateTime()
        );
    }
} 