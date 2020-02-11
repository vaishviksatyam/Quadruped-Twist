package com.example.sirenabuild.ex1;

import android.util.Log;

/**
 * Created by sirenabuild on 5/1/18.
 */

public class MotorandLED extends i2c_write {

    public static final byte motor1_add = 0x60;
    public static final byte motor2_add = 0x62;
    public static final byte max_vel = 63;
    public static final byte min_vel = 6;
    public static final byte motor_fdir = 1;
    public static final byte motor_bdir = 2;
    public static final byte motor_stop = 0;
    public static final byte motor1 = 1;
    public static final byte motor2 = 2;
    public static final byte led_add = 0x60;
    byte motor1_direction, motor2_direction;
    byte motor1_vel, motor2_vel;
    byte motor1_vel_prev, motor2_vel_prev;

    public void set_vel(byte slider_value, byte motor) {
        switch (motor) {
            case 1:
                motor1_vel = (byte) (6.0 + (slider_value / 100.0) * 57.0);
                Log.d("Vel1",""+motor1_vel);
                motor1_vel_prev=motor1_vel;
                break;
            case 2:
                motor2_vel = (byte) (6.0 + (slider_value / 100.0) * 57.0);
                Log.d("Vel2",""+motor2_vel);
                motor2_vel_prev=motor2_vel_prev;
                break;
        }
    }

    public void set_dir(byte dir, byte motor) {
        switch (motor) {
            case 1:
                motor1_direction = dir;
                break;
            case 2:
                motor2_direction = dir;
                break;
        }
    }

    public byte[] send_data() {
        refresh();
        if(motor1_direction==motor_stop && motor2_direction==motor_stop){
            motor1_vel=motor1_vel_prev;
            motor2_vel=motor2_vel_prev;
        }
        byte[] arr = new byte[2];
        arr[0] = 0;
        arr[1] = (byte) ((motor1_direction & 0x03) | ((motor1_vel & 0x3f)<<2));
        Log.d("arr[1]=",""+arr[1]);

        set_device_data(motor1_add, (byte) 2, arr);
        arr[0] = 0;
        arr[1] = (byte) ((motor2_direction & 0x03) | ((motor2_vel & 0x3f)<<2));
        Log.d("arr[1]=",""+arr[1]);

        return set_device_data(motor2_add, (byte) 2, arr);
    }

    public byte[] forward(byte slider_value) {
        set_dir(motor_bdir,motor1);
        set_dir(motor_fdir,motor2);
        set_vel(slider_value, motor1);
        set_vel(slider_value, motor2);
        return send_data();
    }

    public byte[] stop() {
        set_dir(motor_stop, motor1);
        set_dir(motor_stop, motor2);
        return send_data();
    }

    public byte[] shrap_left(byte slider_value) {
        set_dir(motor_bdir, motor1);
        set_dir(motor_bdir, motor2);
        set_vel(slider_value, motor1);
        set_vel(slider_value, motor2);
        return send_data();
    }

    public byte[] sharp_right(byte slider_value) {
        set_dir(motor_fdir, motor1);
        set_dir(motor_fdir, motor2);
        set_vel(slider_value, motor1);
        set_vel(slider_value, motor2);
        return send_data();
    }

    public byte[] left(byte slider_value) {
        set_dir(motor_bdir, motor1);
        set_dir(motor_stop, motor2);
        set_vel(slider_value, motor1);
        set_vel((byte) 0, motor2);
        return send_data();
    }

    public byte[] right(byte slider_value) {
        set_dir(motor_fdir, motor2);
        set_dir(motor_stop, motor1);
        set_vel(slider_value, motor2);
        set_vel((byte) 0, motor1);
        return send_data();
    }

    public byte[] led_red() {
        byte[] arr = new byte[8];
        arr[0] = 18;
        arr[1] = 0;
        arr[2] = (byte) 255;
        arr[3] = 0;
        arr[4] = (byte)255;
        arr[5] = 69;
        arr[6] = 81;
        arr[7] = 20;
//        refresh();
        return set_device_data(led_add, (byte) 8, arr);
    }

    public byte[] led_green() {
        byte[] arr = new byte[8];
        arr[0] = 18;
        arr[1] = 0;
        arr[2] = (byte) 255;
        arr[3] = 0;
        arr[4] = (byte)255;
        arr[5] = 81;
        arr[6] = 20;
        arr[7] = 69;
//        refresh();
        return set_device_data(led_add, (byte) 8, arr);
    }

    public byte[] led_blue() {
        byte[] arr = new byte[8];
        arr[0] = 18;
        arr[1] = 0;
        arr[2] = (byte) 255;
        arr[3] = 0;
        arr[4] = (byte)255;
        arr[5] = 20;
        arr[6] = 69;
        arr[7] = 81;
//        refresh();
        return set_device_data(led_add, (byte) 8, arr);
    }
}
