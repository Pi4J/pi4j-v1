package com.pi4j.io.i2c;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CConstants.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

/**
 * These are constants scraped directly from linux kernel (i2c-dev.h i2c.h). They are to
 * be used with advanced I2C ioctl.
 */
public class I2CConstants {
    /* Flags for i2c_msg.flags */

    public static int I2C_M_RD                  = 0x0001; /* read data, from slave to master
                                                             I2C_M_RD is guaranteed to be = 0x0001! */
    public static int I2C_M_TEN                 = 0x0010; /* this is a ten bit chip address */
    public static int I2C_M_RECV_LEN            = 0x0400; /* length will be first received byte */
    public static int I2C_M_NO_RD_ACK           = 0x0800; /* if I2C_FUNC_PROTOCOL_MANGLING */
    public static int I2C_M_IGNORE_NAK          = 0x1000; /* if I2C_FUNC_PROTOCOL_MANGLING */
    public static int I2C_M_REV_DIR_ADDR        = 0x2000; /* if I2C_FUNC_PROTOCOL_MANGLING */
    public static int I2C_M_NOSTART             = 0x4000; /* if I2C_FUNC_NOSTART */
    public static int I2C_M_STOP                = 0x8000; /* if I2C_FUNC_PROTOCOL_MANGLING */

    /* To determine what functionality is present */

    public static int I2C_FUNC_I2C                      = 0x00000001;
    public static int I2C_FUNC_10BIT_ADDR               = 0x00000002;
    public static int I2C_FUNC_PROTOCOL_MANGLING        = 0x00000004; /* I2C_M_IGNORE_NAK etc. */
    public static int I2C_FUNC_SMBUS_PEC                = 0x00000008;
    public static int I2C_FUNC_NOSTART                  = 0x00000010; /* I2C_M_NOSTART */
    public static int I2C_FUNC_SLAVE                    = 0x00000020;
    public static int I2C_FUNC_SMBUS_BLOCK_PROC_CALL    = 0x00008000; /* SMBus 2.0 */
    public static int I2C_FUNC_SMBUS_QUICK              = 0x00010000;
    public static int I2C_FUNC_SMBUS_READ_BYTE          = 0x00020000;
    public static int I2C_FUNC_SMBUS_WRITE_BYTE         = 0x00040000;
    public static int I2C_FUNC_SMBUS_READ_BYTE_DATA     = 0x00080000;
    public static int I2C_FUNC_SMBUS_WRITE_BYTE_DATA    = 0x00100000;
    public static int I2C_FUNC_SMBUS_READ_WORD_DATA     = 0x00200000;
    public static int I2C_FUNC_SMBUS_WRITE_WORD_DATA    = 0x00400000;
    public static int I2C_FUNC_SMBUS_PROC_CALL          = 0x00800000;
    public static int I2C_FUNC_SMBUS_READ_BLOCK_DATA    = 0x01000000;
    public static int I2C_FUNC_SMBUS_WRITE_BLOCK_DATA   = 0x02000000;
    public static int I2C_FUNC_SMBUS_READ_I2C_BLOCK     = 0x04000000; /* I2C-like block xfer  */
    public static int I2C_FUNC_SMBUS_WRITE_I2C_BLOCK    = 0x08000000; /* w/ 1-byte reg. addr. */
    public static int I2C_FUNC_SMBUS_HOST_NOTIFY        = 0x10000000;

    public static int I2C_FUNC_SMBUS_BYTE        = (I2C_FUNC_SMBUS_READ_BYTE |
                                                    I2C_FUNC_SMBUS_WRITE_BYTE);
    public static int I2C_FUNC_SMBUS_BYTE_DATA   = (I2C_FUNC_SMBUS_READ_BYTE_DATA |
                                                    I2C_FUNC_SMBUS_WRITE_BYTE_DATA);
    public static int I2C_FUNC_SMBUS_WORD_DATA   = (I2C_FUNC_SMBUS_READ_WORD_DATA |
                                                    I2C_FUNC_SMBUS_WRITE_WORD_DATA);
    public static int I2C_FUNC_SMBUS_BLOCK_DATA  = (I2C_FUNC_SMBUS_READ_BLOCK_DATA |
                                                    I2C_FUNC_SMBUS_WRITE_BLOCK_DATA);
    public static int I2C_FUNC_SMBUS_I2C_BLOCK   = (I2C_FUNC_SMBUS_READ_I2C_BLOCK |
                                                    I2C_FUNC_SMBUS_WRITE_I2C_BLOCK);

    public static int I2C_FUNC_SMBUS_EMUL         = (I2C_FUNC_SMBUS_QUICK |
                                                    I2C_FUNC_SMBUS_BYTE |
                                                    I2C_FUNC_SMBUS_BYTE_DATA |
                                                    I2C_FUNC_SMBUS_WORD_DATA |
                                                    I2C_FUNC_SMBUS_PROC_CALL |
                                                    I2C_FUNC_SMBUS_WRITE_BLOCK_DATA |
                                                    I2C_FUNC_SMBUS_I2C_BLOCK |
                                                    I2C_FUNC_SMBUS_PEC);

    /*
     * Data for SMBus Messages
     */
    public static int I2C_SMBUS_BLOCK_MAX       = 32;       /* As specified in SMBus standard */

    /* i2c_smbus_xfer read or write markers */
    public static int I2C_SMBUS_READ            = 1;
    public static int I2C_SMBUS_WRITE           = 0;

    /* SMBus transaction types (size parameter in the above functions)
       Note: these no longer correspond to the (arbitrary) PIIX4 internal codes! */
    public static int I2C_SMBUS_QUICK           = 0;
    public static int I2C_SMBUS_BYTE            = 1;
    public static int I2C_SMBUS_BYTE_DATA       = 2;
    public static int I2C_SMBUS_WORD_DATA       = 3;
    public static int I2C_SMBUS_PROC_CALL       = 4;
    public static int I2C_SMBUS_BLOCK_DATA      = 5;
    public static int I2C_SMBUS_I2C_BLOCK_BROKEN= 6;
    public static int I2C_SMBUS_BLOCK_PROC_CALL = 7;        /* SMBus 2.0 */
    public static int I2C_SMBUS_I2C_BLOCK_DATA  = 8;

    public static int I2C_RETRIES               = 0x0701;   /* number of times a device address should
                                                               be polled when not acknowledging */
    public static int I2C_TIMEOUT               = 0x0702;   /* set timeout in units of 10 ms */

    /* NOTE: Slave address is 7 or 10 bits, but 10-bit addresses
     * are NOT supported! (due to code brokenness)
     */
    public static int I2C_SLAVE                 = 0x0703;   /* Use this slave address */
    public static int I2C_SLAVE_FORCE           = 0x0706;   /* Use this slave address, even if it
                                                               is already in use by a driver! */
    public static int I2C_TENBIT                = 0x0704;   /* 0 for 7 bit addrs, != 0 for 10 bit */

    public static int I2C_FUNCS                 = 0x0705;   /* Get the adapter functionality mask */

    public static int I2C_RDWR                  = 0x0707;   /* Combined R/W transfer (one STOP only) */

    public static int I2C_PEC                   = 0x0708;   /* != 0 to use PEC with SMBus */
    public static int I2C_SMBUS                 = 0x0720;   /* SMBus transfer */
}
