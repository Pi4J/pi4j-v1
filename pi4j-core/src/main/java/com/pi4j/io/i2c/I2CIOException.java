package com.pi4j.io.i2c;

import com.pi4j.jni.I2C;

import java.io.IOException;

public class I2CIOException extends IOException {
    int rawCode;
    String baseMessage;

    /**
     * @param message Exception message
     * @param rawCode negative POSIX error code with pi4j offsets
     */
    public I2CIOException(String message, int rawCode) {
        super(message);

        this.rawCode = Math.abs(rawCode);
    }

    /**
     * Gets the POSIX code associated with this IO error
     *
     * @return POSIX error code
     */
    public int getCode() {
        return rawCode - getType() * 10000;
    }

    /**
     * @return true if is ioctl error
     */
    public boolean isIOCTL() {
        return getType() == 1;
    }

    /**
     * @return true if is write error
     */
    public boolean isWrite() {
        return getType() == 2;
    }

    /**
     * @return true if is read error
     */
    public boolean isRead() {
        return getType() == 3;
    }

    private int getType() {
        return rawCode / 10000;
    }
}