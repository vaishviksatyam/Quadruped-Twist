package com.example.sirenabuild.ex1;

import android.util.Log;

/**
 * Created by sirenabuild on 5/1/18.
 */

public class i2c_write {
    public static final byte motor1_add = 0x60;
    public static final byte motor2_add = 0x62;
    public static final byte max_vel = 63;
    public static final byte min_vel = 6;
    public static final byte motor_fdir = 2;
    public static final byte motor_bdir = 1;
    public static final byte motor_stop = 0;
    public static final byte motor1 = 1;
    public static final byte motor2 = 2;
    public static final byte led_add = 0x60;
    public static final int buffer_size = 512;
    public static final byte luci_mode = 0;
    public static final byte length0_lowbyte = 1;
    public static final byte length0_highbyte = 2;
    public static final byte length1_lowbyte = 3;
    public static final byte length1_highbyte = 4;
    public static final byte i2c_internal_mode = 5;
    public static final byte i2c_nu_of_dev = 6;
    public static final byte data_pointer = 7;

    public byte[] message = new byte[buffer_size];
    int length0, length1;
    int present_data_pointer = data_pointer;


    void set_change_internal_mode(byte mode) {
        message[i2c_internal_mode] = mode;
    }

    byte get_change_internal_mode() {
        return message[i2c_internal_mode];
    }

    byte get_nu_of_device() {
        return message[i2c_nu_of_dev];
    }

    void set_nu_of_dev(byte number) {
        message[i2c_nu_of_dev] = number;
    }

    void set_data_length(int length, byte whichone) {
        switch (whichone) {
            case 0: {
                message[length0_lowbyte] = (byte) length;
                //printf("message[length0_lowbyte]=%d\n",message[length0_lowbyte]);
                message[length0_highbyte] = (byte) (length >> 8);
                //printf("message[length0_highbyte]=%d\n",message[length0_highbyte]);
                length0=length;
            }
            break;
            case 1: {
                message[length1_lowbyte] = (byte) length;
                message[length1_highbyte] = (byte) (length >> 8);
                length1=length;
            }
            break;
        }
    }

    int get_data_length(byte whichone) {
        int ret = 0;
        switch (whichone) {
            case 0: {
                ret = message[length0_lowbyte] + (message[length0_highbyte] << 8);
            }
            break;
            case 1: {
                ret = message[length1_lowbyte] + (message[length1_highbyte] << 8);
            }
            break;
        }
        return ret;
    }

    void generate_luci_packet() {
        message[luci_mode] = 2;
        int i;
        int length = get_data_length((byte) 0);
        for (i = 0; i < length + 5; i++) {
            //printf("data at message[%d]=%d\n",i,message[i]);
        }

    }

    int add_data_to_buffer(byte len, byte[] data) {
        int i;
        if (len < 1)
            return 0;
        for (i = 0; i < len; i++) {
            message[present_data_pointer + i] = data[i];
            //printf("data=%d\n",data[i]);
        }
        present_data_pointer += len;
        return len;
    }

    public void refresh() {
        /*
        for (int i = 0; i < buffer_size; i++) {
            message[i] = 0;
        }*/
        length0 =0;
        length1 =0;
        present_data_pointer = data_pointer;
        message=new byte[buffer_size];
    }

    public byte[] set_device_data(byte dev_add, byte data_length, byte[] data) {
        int i;
        byte nu_of_dev;
        byte imode, len;
        byte[] a = new byte[2];

//if(!(dev_add>0&&data_length>0))
// return -1;
        imode = get_change_internal_mode();
        nu_of_dev = get_nu_of_device();
        if (imode == 0 && nu_of_dev == 1) {
            imode = 1;
            set_change_internal_mode(imode);
        }
        nu_of_dev++;


        if(nu_of_dev==1){
            set_data_length((byte)2,(byte)0);
        }
        set_nu_of_dev(nu_of_dev);
        len = 2;
        byte[] arr = new byte[2];
        arr[0] = dev_add;
        arr[1] = data_length;
        if (add_data_to_buffer(len, arr) != len)
            Log.d("error", "error");
        else {
            int length;
            length = get_data_length((byte) 0);
            length += len;
            Log.d("i2c_write","length1= "+length);
            set_data_length(length, (byte) 0);
        }
        if (add_data_to_buffer(data_length, data) != data_length)
            Log.d("error", "error in data copy");
        else {
            int length;
            length = get_data_length((byte) 0);
            length += data_length;
            //printf("length=%d",length);
            Log.d("i2c_write","length2= "+length);
            set_data_length(length, (byte) 0);
        }

        byte[] retmessage = new byte[5 + length0 + length1];

        for (int k = 0; k < retmessage.length; k++) {
            retmessage[k] = message[k];

        }
        retmessage[0]=2;
        return retmessage;
    }


}
