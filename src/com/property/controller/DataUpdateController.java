package com.property.controller;

import java.util.ArrayList;
import java.util.List;

public class DataUpdateController {
    private static List<DataUpdateListener> listeners = new ArrayList<>();
    
    public static void addListener(DataUpdateListener listener) {
        listeners.add(listener);
    }
    
    public static void removeListener(DataUpdateListener listener) {
        listeners.remove(listener);
    }
    
    public static void notifyDataChanged(String dataType) {
        for (DataUpdateListener listener : listeners) {
            listener.onDataChanged(dataType);
        }
    }
    
    public interface DataUpdateListener {
        void onDataChanged(String dataType);
    }
} 