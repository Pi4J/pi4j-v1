package com.pi4j.device.access;

import com.pi4j.device.DeviceBase;
import com.pi4j.device.access.OpenerState;

public abstract class OpenerBase extends DeviceBase implements Opener
{
    @Override
    public abstract void open();

    @Override
    public abstract void close();

    @Override
    public abstract OpenerState getState();

    @Override
    public abstract boolean isLocked();

    @Override
    public boolean isOpen()
    {
        return (getState() == OpenerState.OPEN);
    }

    @Override
    public boolean isOpening()
    {
        return (getState() == OpenerState.OPENING);
    }

    @Override
    public boolean isClosed()
    {
        return (getState() == OpenerState.CLOSED);
    }

    @Override
    public boolean isClosing()
    {
        return (getState() == OpenerState.CLOSING);
    }

}
