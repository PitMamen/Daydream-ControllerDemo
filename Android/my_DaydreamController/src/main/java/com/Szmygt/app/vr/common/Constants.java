package com.Szmygt.app.vr.common;

/**
 * Created by chengkai on 2017/1/16.
 */
public class Constants {

    public static final byte KEYCODE_TYPE   =           0x01;
    public static final byte VIBRATE_TYPE   =           0x02;
    public static final byte STATE_TYPE     =           0x03;
    public static final byte ID_TYPE        =           0x06;
    public static final byte NAME_TYPE      = (byte)    0xF0;
    public static final byte INFO_TYPE      = (byte)    0xF1;
    public static final byte CHANGE_TYPE    =           0x08;

    public static final int GAME_MODEL      = 1;
    public static final int NORMAL_MODEL = 2;
    public static final int MAKE_VIBRATE    = 3;
    public static final int GET_INFO        = 4;
    public static final int GET_STATE       = 5;
    public static final int GET_NAME        = 6;
    public static final int GET_ID          = 7;

    public static final int KEYCODE_TYPE_BYTE_3        =    3;
    public static final int KEYCODE_TYPE_BYTE_4        =    4;
    public static final int KEYCODE_TYPE_BYTE_5        =    5;
    public static final int KEYCODE_TYPE_BYTE_6        =    6;
    public static final int KEYCODE_TYPE_BYTE_7        =    7;
    public static final int KEYCODE_TYPE_BYTE_9        =    9;
    public static final int KEYCODE_TYPE_BYTE_11       =    11;
    public static final int KEYCODE_TYPE_BYTE_13       =    13;

    public static final int BYTE_MAX        =        255;
    public static final int BYTE_MIN        =          0;

    //BYTE[3] KETCODE
    public static final byte KEYCODE_UP             =           1;
    public static final byte KEYCODE_DOWN           =           1<<1;
    public static final byte KEYCODE_LEFT           =           1<<2;
    public static final byte KEYCODE_RIGHT          =           1<<3;
    public static final byte KEYCODE_START          =           1<<4;
    public static final byte KEYCODE_BACK           =           1<<5;
    public static final byte KEYCODE_LEFT_ROCKER_B  =           1<<6;
    public static final byte KEYCODE_RIGHT_ROCKER_B = (byte)    (1<<7);

    //BYTE[4] KETCODE
    public static final byte KEYCODE_HELP           =           1;
    public static final byte KEYCODE_I              =           1<<1;
    public static final byte KEYCODE_A              =           1<<2;
    public static final byte KEYCODE_B              =           1<<3;
    public static final byte KEYCODE_X              =           1<<4;
    public static final byte KEYCODE_Y              =           1<<5;
    public static final byte KEYCODE_L1             =           1<<6;
    public static final byte KEYCODE_R1             = (byte)    (1<<7);

    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    public final static String EXTRA_ADDRESS = "com.example.bluetooth.le.EXTRA_ADDRESS";

}
