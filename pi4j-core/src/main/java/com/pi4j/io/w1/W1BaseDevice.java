package com.pi4j.io.w1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Peter Schuebl
 */
public abstract class W1BaseDevice implements W1Device {

    private final String id;

    private String name;

    private File deviceDir;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    public W1BaseDevice(final File deviceDir) {
        String deviceName;
        try {
            deviceName = new String(Files.readAllBytes(new File(deviceDir, "name").toPath()));
        } catch (IOException e) {
            // FIXME logging
            deviceName = deviceDir.getName();
        }
        name = deviceName;
        id = deviceName;
        this.deviceDir = deviceDir;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof W1Device && id.equals(((W1Device) obj).getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String getValue() throws IOException {
        return new String(Files.readAllBytes(new File(deviceDir, "w1_slave").toPath()));
    }
}
