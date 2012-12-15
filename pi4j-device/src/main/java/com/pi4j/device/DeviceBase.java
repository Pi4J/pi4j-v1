package com.pi4j.device;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DeviceBase implements Device {
    
    private String name = null;
    private Object tag = null;
    private final Map<String, String> properties = new ConcurrentHashMap<String, String>();
    
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        if (properties.containsKey(key)) {
            if(properties.get(key) == null || properties.get(key).isEmpty())
                return defaultValue;
            else
                return properties.get(key);
        }
        return defaultValue;
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, null);
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public void removeProperty(String key) {
        if (properties.containsKey(key)) {
            properties.remove(key);
        }
    }

    @Override
    public void clearProperties() {
        properties.clear();
    }    
    
}
