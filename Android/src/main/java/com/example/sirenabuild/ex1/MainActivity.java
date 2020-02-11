package com.example.sirenabuild.ex1;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;


public class MainActivity extends AppCompatActivity{
    private Spinner expressions;
    byte[] lucipacket;
    boolean connected;
    Button bconnect, HiF, StandF, IMU,Looking_down, Looking_right_up, Topple, Get_Back, Navigation, Eager, Looking_left_up, Sleeping_bored, Hello, Greet, UP_Active, Looking_Right, Sad_worried, Play_Time, Looking_left, Happy_bliss, Funny, Mean_middle, Yoga, Fear, Angry, Happy2, Scared, Surprise, Look_Up;
    public boolean pressed = false;
    ImageButton bright, bleft, bfront, bback, bsleep, bstand, barm, bfbend, bbbend, blbend, brbend;
    RobotCom robot;
    TextView recieved_data;

    String[] priority_table={"Router_Connection","A2A","Neutral","Battery","Error","Walk","Expression"};
    int[] priority_table_Values={0,0,0,0,0,0,0};

    Paint p,pcam;
    GameView gv_video;
    int clock,anticlock,a1,a2,a3;
    MotorandLED motorandLED;
    float angle = (float) 150.0;

    float motor1[]=new float[361];
    float motor2[]=new float[361];
    float motor3[]=new float[361];
    EditText etIP;
    SurfaceView surfaceView,surfcam;
    SurfaceHolder surfaceHolder,holdercam;
    private String outputFile;
    DatagramSocket socket_video;
    int Surface1x,Surface1y,Surface2x,Surface2y;
    String Server = "192.168.43.2";
    int i, j,Side_L,Side_R,g;
    byte[] frontz;
    byte[] backz;
    byte[] leftz;
    byte[] rightz;
    byte[] sleep = {(byte) 255, (byte) 255, (byte) 254, 40, (byte) 131, 30, 2, 1, (byte) 200, 0, 4, (byte) 200, 0, 7, (byte) 200, 0, 10, (byte) 200, 0, 5, (byte) 216, 1, 2, 38, 2, 11, (byte) 216, 1, 8, 38, 2, 12, (byte) 202, 3, 3, 50, 0, 6, (byte) 202, 3, 9, 50, 0, (byte) 200};
    byte[] arm = {(byte) 255, (byte) 255, (byte) 254, 40, (byte) 131, 30, 2, 1, 0, 2, 4, 0, 2, 7, 0, 2, 10, 0, 2, 5, (byte) 238, 2, 2, (byte) 238, 0, 11, (byte) 238, 2, 8, (byte) 238, 0, 12, (byte) 255, 3, 3, 0, 0, 6, (byte) 255, 3, 9, 0, 0, 32};
    byte[] stand = {(byte) 255, (byte) 255, (byte) 254, 40, (byte) 131, 30, 2, 1, 0, 2, 4, 0, 2, 7, 0, 2, 10, 0, 2, 5, (byte) 253, 1, 2, 0, 2, 11, (byte) 255, 1, 8, 0, 2, 12, (byte) 238, 2, 3, (byte) 255, 0, 6, (byte) 238, 2, 9, (byte) 255, 0, (byte) 0};
    byte[] fbend = {(byte) 255, (byte) 255, (byte) 254, 40, (byte) 131, 30, 2, 1, 0, 2, 4, 0, 2, 7, 0, 2, 10, 0, 2, 5, 88, 2, 2, (byte) 144, 1, 11, (byte) 144, 1, 8, 88, 2, 12, (byte) 238, 2, 3, (byte) 255, 0, 6, (byte) 238, 2, 9, (byte) 255, 0, 44};
    byte[] lbend = {(byte) 255, (byte) 255, (byte) 254, 40, (byte) 131, 30, 2, 1, 0, 2, 4, 0, 2, 7, 0, 2, 10, 0, 2, 5, 88, 2, 2, 88, 2, 11, (byte) 144, 1, 8, (byte) 144, 1, 12, (byte) 238, 2, 3, (byte) 255, 0, 6, (byte) 238, 2, 9, (byte) 255, 0, 44};
    byte[] rbend = {(byte) 255, (byte) 255, (byte) 254, 40, (byte) 131, 30, 2, 1, 0, 2, 4, 0, 2, 7, 0, 2, 10, 0, 2, 5, (byte) 144, 1, 2, (byte) 144, 1, 11, 88, 2, 8, 88, 2, 12, (byte) 238, 2, 3, (byte) 255, 0, 6, (byte) 238, 2, 9, (byte) 255, 0, 44};
    byte[] bbend = {(byte) 255, (byte) 255, (byte) 254, 40, (byte) 131, 30, 2, 1, 0, 2, 4, 0, 2, 7, 0, 2, 10, 0, 2, 5, (byte) 144, 1, 2, 88, 2, 11, 88, 2, 8, (byte) 144, 1, 12, (byte) 238, 2, 3, (byte) 255, 0, 6, (byte) 238, 2, 9, (byte) 255, 0, 44};


    @SuppressLint("ClickableViewAccessibility")
    int[][] packet_front = new int[54][12];
    int[][] frontx = {{304, 344, 82, 632, 634, 923, 391, 335, 65, 719, 680, 942},
            {313, 310, 59, 635, 635, 924, 395, 335, 66, 716, 681, 944},
            {323, 277, 40, 638, 635, 924, 398, 335, 67, 713, 682, 946},
            {332, 246, 24, 642, 635, 924, 401, 336, 68, 710, 683, 947},
            {342, 221, 13, 645, 635, 925, 404, 336, 69, 707, 684, 949},
            {352, 203, 5, 649, 635, 925, 408, 337, 70, 704, 685, 950},
            {362, 193, 1, 652, 635, 925, 411, 338, 71, 701, 685, 952},
            {372, 191, 0, 655, 635, 925, 414, 338, 72, 698, 686, 953},
            {383, 198, 3, 659, 635, 925, 417, 339, 73, 694, 687, 954},
            {393, 213, 9, 662, 635, 925, 420, 340, 75, 691, 687, 955},
            {402, 236, 19, 665, 635, 925, 424, 341, 76, 688, 688, 956},
            {412, 264, 33, 669, 635, 924, 427, 342, 78, 685, 688, 957},
            {422, 296, 51, 672, 635, 924, 430, 343, 79, 681, 689, 958},
            {431, 331, 73, 675, 635, 924, 433, 344, 81, 678, 689, 958},
            {432, 344, 81, 679, 634, 923, 429, 325, 69, 675, 690, 959},
            {429, 343, 79, 682, 634, 922, 420, 291, 48, 671, 690, 959},
            {426, 342, 77, 685, 634, 922, 411, 259, 31, 668, 690, 960},
            {423, 341, 76, 689, 633, 921, 401, 231, 17, 665, 690, 960},
            {420, 340, 74, 692, 633, 920, 391, 210, 8, 661, 690, 960},
            {417, 339, 73, 695, 632, 919, 381, 197, 2, 658, 691, 961},
            {413, 338, 72, 698, 632, 918, 371, 191, 0, 655, 691, 961},
            {410, 338, 71, 701, 631, 917, 361, 194, 2, 651, 691, 961},
            {407, 337, 69, 705, 631, 915, 351, 206, 6, 648, 690, 960},
            {404, 336, 68, 708, 630, 914, 341, 225, 14, 644, 690, 960},
            {400, 336, 68, 711, 630, 913, 331, 251, 27, 641, 690, 960},
            {397, 335, 67, 714, 629, 911, 321, 282, 43, 638, 690, 959},
            {394, 335, 66, 717, 628, 909, 312, 316, 63, 634, 690, 959},
            {391, 335, 65, 720, 627, 908, 305, 344, 82, 631, 689, 958},
            {387, 334, 65, 711, 660, 933, 308, 343, 80, 628, 689, 958},
            {384, 334, 64, 701, 692, 956, 311, 342, 78, 624, 688, 957},
            {381, 334, 64, 692, 722, 974, 314, 341, 77, 621, 688, 956},
            {377, 334, 64, 682, 746, 988, 317, 340, 75, 618, 687, 955},
            {374, 333, 63, 672, 764, 997, 320, 339, 74, 615, 687, 954},
            {370, 333, 63, 662, 775, 1003, 323, 339, 72, 611, 686, 953},
            {367, 333, 63, 652, 776, 1003, 326, 338, 71, 608, 685, 951},
            {364, 333, 64, 641, 769, 1000, 330, 337, 70, 605, 684, 950},
            {360, 334, 64, 631, 754, 992, 333, 337, 69, 602, 684, 949},
            {357, 334, 64, 622, 732, 980, 336, 336, 68, 599, 683, 947},
            {354, 334, 64, 612, 704, 963, 339, 336, 67, 596, 682, 946},
            {350, 334, 65, 602, 673, 943, 343, 335, 66, 593, 681, 944},
            {347, 335, 65, 593, 641, 918, 346, 335, 66, 590, 682, 944},
            {344, 335, 66, 592, 628, 909, 349, 334, 65, 599, 716, 966},
            {340, 335, 67, 595, 629, 911, 353, 334, 65, 609, 750, 985},
            {337, 336, 68, 598, 629, 912, 356, 334, 64, 618, 779, 1000},
            {334, 336, 69, 601, 630, 914, 359, 334, 64, 628, 804, 1012},
            {331, 337, 70, 604, 631, 915, 363, 334, 64, 638, 822, 1019},
            {327, 338, 71, 607, 631, 916, 366, 333, 63, 648, 831, 1023},
            {324, 338, 72, 611, 632, 917, 369, 333, 63, 658, 832, 1023},
            {321, 339, 73, 614, 632, 919, 373, 333, 63, 668, 825, 1021},
            {318, 340, 75, 617, 633, 920, 376, 334, 64, 678, 810, 1014},
            {315, 341, 76, 620, 633, 920, 380, 334, 64, 688, 787, 1004},
            {312, 342, 78, 624, 634, 921, 383, 334, 64, 698, 758, 990},
            {308, 343, 79, 627, 634, 922, 386, 334, 65, 707, 725, 972},
            {305, 344, 81, 630, 634, 923, 390, 334, 65, 717, 691, 950}};


    int [][] rightx = {{578,342,125,533,634,878,449,344,127,488,689,913},
            {569,308,103,536,634,878,452,343,125,491,690,914},
            {559,274,84,540,634,877,455,342,123,494,690,914},
            {550,245,69,543,634,876,458,341,122,498,690,915},
            {540,220,57,546,633,875,461,340,120,501,690,915},
            {530,202,50,549,633,874,464,339,119,505,690,915},
            {520,193,46,553,632,873,467,339,117,508,691,916},
            {510,192,46,556,632,872,470,338,116,511,691,916},
            {500,199,48,559,631,871,474,337,115,515,691,916},
            {490,214,55,562,631,870,477,337,114,518,690,915},
            {480,237,65,565,630,868,480,336,113,521,690,915},
            {470,266,79,568,629,867,483,336,112,525,690,915},
            {461,299,97,572,629,865,487,335,111,528,690,914},
            {451,333,119,575,628,864,490,335,111,531,690,914},
            {450,344,126,571,644,876,493,334,110,535,689,913},
            {453,343,124,562,677,900,497,334,110,538,689,913},
            {456,342,122,552,707,920,500,334,109,541,688,912},
            {459,341,121,543,735,936,503,334,109,545,688,911},
            {462,340,119,533,756,948,507,334,109,548,687,910},
            {466,339,118,523,770,956,510,333,108,551,687,909},
            {469,338,117,513,777,958,513,333,108,554,686,908},
            {472,338,116,502,774,957,517,333,108,557,685,906},
            {475,337,114,492,763,952,520,334,109,561,684,905},
            {478,336,113,482,744,942,524,334,109,564,684,904},
            {482,336,113,473,719,927,527,334,109,567,683,902},
            {485,335,112,463,689,908,530,334,110,570,682,901},
            {488,335,111,454,657,886,534,334,110,573,681,899},
            {492,335,110,446,628,863,537,335,111,576,680,897},
            {495,334,110,449,628,865,540,335,111,567,714,920},
            {498,334,109,452,629,866,544,336,112,557,747,939},
            {502,334,109,456,630,868,547,336,113,548,778,955},
            {505,334,109,459,630,869,550,337,114,538,803,966},
            {508,333,108,462,631,871,553,337,115,528,821,974},
            {512,333,108,465,632,872,557,338,116,518,831,978},
            {515,333,108,468,632,873,560,339,118,508,833,979},
            {518,333,109,471,633,874,563,340,119,497,826,976},
            {522,334,109,475,633,875,566,340,120,487,811,970},
            {525,334,109,478,633,876,569,341,122,478,788,960},
            {529,334,109,481,634,877,572,342,123,468,760,946},
            {532,334,110,484,634,877,575,343,125,458,728,928},
            {535,335,110,488,634,878,578,342,125,449,693,906},
            {539,335,111,491,635,879,569,308,103,448,680,898},
            {542,335,112,494,635,879,559,274,84,451,681,900},
            {545,336,113,498,635,879,550,245,69,454,682,902},
            {548,336,114,501,635,880,540,220,57,457,683,903},
            {552,337,115,505,635,880,530,202,50,460,684,905},
            {555,338,116,508,635,880,520,193,46,463,685,906},
            {558,338,117,511,635,880,510,192,46,467,686,907},
            {561,339,118,515,635,880,500,199,48,470,686,908},
            {564,340,120,518,635,880,490,214,55,473,687,910},
            {568,341,121,521,635,880,480,237,65,476,688,911},
            {571,342,123,525,635,879,470,266,79,480,688,911},
            {574,343,124,528,635,879,461,299,97,483,689,912},
            {577,344,126,531,635,879,451,333,119,486,689,913}};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        connected = Boolean.FALSE;
        IMU=findViewById(R.id.IMU);
        bconnect = findViewById(R.id.Wake_up);
        bsleep = findViewById(R.id.ssleep);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.csv";
        Navigation = findViewById(R.id.Navigation);
        StandF = findViewById(R.id.StandF);
        HiF = findViewById(R.id.HiF);
        barm = findViewById(R.id.sarm);
        bstand = findViewById(R.id.sstand);
        Looking_down = findViewById(R.id.Looking_down);
        Looking_right_up = findViewById(R.id.Looking_right_up);
        Eager = findViewById(R.id.Eager);
        Looking_left_up = findViewById(R.id.Looking_left_up);
        Sleeping_bored = findViewById(R.id.Sleeping_bored);
        UP_Active = findViewById(R.id.UP_Active);
        Looking_Right = findViewById(R.id.Looking_Right);
        Sad_worried = findViewById(R.id.Sad_worried);
        Looking_left = findViewById(R.id.Looking_left);
        Happy_bliss = findViewById(R.id.Happy_bliss);
        Greet = findViewById(R.id.Greet);
        Funny = findViewById(R.id.Funny);
        Mean_middle = findViewById(R.id.Mean_middle);
        Hello = findViewById(R.id.Hello);
        Play_Time = findViewById(R.id.Play_Time);
        Yoga = findViewById(R.id.Yoga);
        Angry = findViewById(R.id.Angry);
        Happy2 = findViewById(R.id.Happy2);
        Scared = findViewById(R.id.Scared);
        Surprise = findViewById(R.id.Surprise);
        Look_Up = findViewById(R.id.Look_Up);
        Fear = findViewById(R.id.Fear);
        recieved_data=findViewById(R.id.recieved_data);
        Get_Back = findViewById(R.id.Get_Back);
        robot = new RobotCom();
        motorandLED = new MotorandLED();
        gv_video = findViewById(R.id.gv_video);

        HiF.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();
                if (action == MotionEvent.ACTION_DOWN) {

                    String string = "HiF";
                    byte[] b = string.getBytes();
                    robot.LUCI_WriteUARTPacket(222222, b);

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{200, 150, 50, 100, 150, 250, 200, 150, 0, 100, 150, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    pressed = true;
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bleft.setImageResource(R.drawable.walkdot);
                    pressed = false;
                    return true;
                }

                return false;

            }

        });

        IMU.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    float[] readarray=imu();
                    recieved_data.setText("roll: " +String.valueOf(readarray[0]) + " , " + "pitch: " + String.valueOf(readarray[1]));
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
//                    bleft.setImageResource(R.drawable.walkdot);
                    pressed = false;
                    return true;
                }
                return false;
            }

        });

        StandF.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    bleft.setImageResource(R.drawable.walkdotpressed);

                    String string = "StandF";
                    byte[] b = string.getBytes();
                    robot.LUCI_WriteUARTPacket(222222, b);

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 150, 50, 150, 150, 250, 150, 150, 50, 150, 150, 250}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    pressed = true;
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bleft.setImageResource(R.drawable.walkdot);
                    pressed = false;
                    return true;


                }

                return false;

            }

        });

        bsleep.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub


                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });

        bstand.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub


                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{50, 180, 0, 50, 120, 300, 50, 180, 0, 50, 120, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    bstand.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bstand.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });


////////////////////////////////////////////////////////////////////////////////////////////////////
//        Body Language                                                                           //
////////////////////////////////////////////////////////////////////////////////////////////////////
        barm.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                String string = "barm";
                byte[] b = string.getBytes();
                robot.LUCI_WriteUARTPacket(222222, b);


                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {

                    barm.setImageResource(R.drawable.buttonspressed);
                    robot.direct_play("TwistAgree.mp3");
                    neutral();
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {

                    barm.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });

        Looking_down.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 70, 0, 150, 220, 300, 150, 90, 0, 150, 210, 300, 170, 150}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 30, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    neutral();
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });

        Look_Up.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {


                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 0, 150, 210, 300, 150, 70, 0, 150, 220, 300, 90, 150}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 30, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    neutral();
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });


        Eager.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                String string = "Eager";
                byte[] b = string.getBytes();
                robot.LUCI_WriteUARTPacket(222222, b);


                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 120, 30, 150, 180, 270, 150, 120, 30, 150, 180, 270}, new float[]{30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    robot.direct_play("TwistEager.mp3");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    neutral();
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });

        Looking_right_up.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 120, 0, 150, 240, 300, 150, 60, 0, 150, 180, 300, 120, 100}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 30}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    neutral();
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });

        Looking_left_up.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 60, 0, 150, 180, 300, 150, 120, 0, 150, 240, 300, 120, 200}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 30}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    neutral();
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });


        Sleeping_bored.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{200, 90, 0, 100, 210, 300, 200, 90, 0, 100, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{180, 90, 0, 120, 210, 300, 180, 180, 0, 120, 120, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{180, 180, 0, 120, 120, 300, 180, 180, 0, 120, 120, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{250, 180, 0, 50, 120, 300, 200, 180, 0, 100, 120, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);

                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });

        UP_Active.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 0, 150, 210, 300, 150, 90, 0, 150, 210, 300, 120, 150}, new float[]{60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60}, 222222);
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{180, 90, 0, 180, 210, 300, 180, 90, 0, 180, 210, 300, 120, 180}, new float[]{60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60}, 222222);
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{120, 90, 0, 120, 210, 300, 120, 90, 0, 120, 210, 300, 120, 120}, new float[]{60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60}, 222222);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 0, 150, 210, 300, 150, 90, 0, 150, 210, 300, 120, 150}, new float[]{30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 90}, 222222);
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });


        Looking_Right.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{170, 90, 0, 170, 210, 300, 170, 70, 0, 170, 220, 300, 120, 120}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    neutral();
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });


        Greet.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{200, 90, 0, 100, 210, 300, 150, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{200, 140, 150, 100, 160, 150, 150, 90, 0, 150, 210, 300}, new float[]{30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    neutral();
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });


        Sad_worried.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{230, 90, 0, 70, 210, 300, 170, 70, 0, 170, 220, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{230, 90, 30, 70, 210, 270, 170, 70, 0, 170, 220, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    neutral();
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });


        Looking_left.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{120, 90, 0, 120, 210, 300, 120, 90, 0, 120, 210, 300, 120, 200}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });


        Happy_bliss.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 50, 150, 210, 300, 150, 90, 15, 150, 210, 300}, new float[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 15, 150, 210, 250, 150, 90, 15, 150, 210, 300}, new float[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 15, 150, 210, 300, 150, 90, 50, 150, 210, 300}, new float[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 15, 150, 210, 300, 150, 90, 15, 150, 210, 250}, new float[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    neutral();


                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });

        Yoga.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 0, 150, 210, 300, 150, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{100, 90, 0, 200, 210, 300, 150, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{100, 150, 150, 200, 150, 150, 150, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{100, 150, 150, 200, 150, 150, 200, 260, 150, 100, 40, 150}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{50, 120, 150, 220, 150, 150, 200, 260, 150, 100, 40, 150}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{50, 170, 50, 220, 150, 250, 200, 260, 150, 100, 40, 150}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
/////////////////////////////////////////
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{50, 170, 50, 220, 150, 250, 200, 150, 150, 100, 150, 150}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
/////////////////////////////////////////

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{50, 170, 50, 220, 150, 250, 200, 260, 150, 100, 40, 150}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{50, 120, 150, 220, 150, 150, 200, 260, 150, 100, 40, 150}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{100, 150, 150, 200, 150, 150, 200, 260, 150, 100, 40, 150}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{100, 150, 150, 200, 150, 150, 150, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{100, 90, 0, 200, 210, 300, 150, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 0, 150, 210, 300, 150, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });


        Funny.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {
                robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 15, 150, 210, 285, 150, 90, 15, 150, 210, 285, 100, 150}, new float[]{50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50}, 222222);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 15, 150, 210, 285, 150, 90, 15, 150, 210, 285, 140, 150}, new float[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, 222222);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 15, 150, 210, 285, 150, 90, 15, 150, 210, 285, 100, 150}, new float[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, 222222);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 15, 150, 210, 285, 150, 90, 15, 150, 210, 285, 140, 150}, new float[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, 222222);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 15, 150, 210, 285, 150, 90, 15, 150, 210, 285, 100, 150}, new float[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, 222222);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 15, 150, 210, 285, 150, 90, 15, 150, 210, 285, 140, 150}, new float[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, 222222);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {

                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });


        Mean_middle.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    bleft.setImageResource(R.drawable.walkdotpressed);
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bleft.setImageResource(R.drawable.walkdot);
                    pressed = false;
                    return true;


                }

                return false;

            }

        });

        Angry.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{140, 120, 30, 140, 180, 270, 140, 120, 30, 140, 180, 270}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int h = 0; h <= 20; h++) {
                        robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{140, 120, 30, 140, 180, 270, 140, 120, 30, 140, 180, 270}, new float[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, 222222);
                        //bsleep.setImageResource(R.drawable.buttonspressed);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{160, 120, 30, 160, 180, 270, 160, 120, 30, 160, 180, 270}, new float[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, 222222);
                        //bsleep.setImageResource(R.drawable.buttonspressed);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    neutral();

                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });

        Fear.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 15, 150, 210, 285, 150, 90, 15, 150, 210, 285, 180, 150}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{140, 90, 30, 140, 210, 270, 140, 90, 30, 140, 210, 270, 180, 150}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    //bsleep.setImageResource(R.drawable.buttonspressed);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int h = 0; h <= 20; h++) {
                        robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{140, 90, 30, 140, 210, 270, 140, 90, 30, 140, 210, 270, 180, 150}, new float[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, 222222);
                        //bsleep.setImageResource(R.drawable.buttonspressed);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{160, 90, 30, 160, 210, 270, 160, 90, 30, 160, 210, 270, 180, 150}, new float[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, 222222);
                        //bsleep.setImageResource(R.drawable.buttonspressed);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    neutral();

                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });


        Hello.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{180, 90, 0, 120, 210, 300, 150, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{180, 30, 0, 120, 210, 300, 150, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{180, 90, 90, 120, 210, 300, 150, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{180, 30, 0, 120, 210, 300, 150, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{180, 90, 0, 120, 210, 300, 150, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    neutral();

                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });
        Happy2.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    bleft.setImageResource(R.drawable.walkdotpressed);

                    new send_action().execute("Left");
                    pressed = true;
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bleft.setImageResource(R.drawable.walkdot);
                    pressed = false;
                    return true;


                }

                return false;

            }

        });

        Navigation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                int action = arg1.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
//                    convert();
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,15}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,1}, new float[]{180, 90, 0, 120, 210, 300, 150, 90, 0, 150, 210, 300,15}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,15}, 222222);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    pressed = false;
                    return true;
                }
                return false;
            }
        });

        Scared.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    bleft.setImageResource(R.drawable.walkdotpressed);

                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bleft.setImageResource(R.drawable.walkdot);
                    pressed = false;
                    return true;


                }

                return false;

            }

        });

        Surprise.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{120, 90, 0, 150, 150, 300, 180, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{120, 90, 0, 150, 150, 300, 180, 90, 0, 150, 210, 180}, new float[]{30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30}, 222222);
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{120, 90, 0, 150, 150, 300, 180, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 45}, 222222);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 0, 150, 210, 300, 150, 90, 0, 150, 210, 300}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 45}, 222222);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });

        Play_Time.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View arg0, MotionEvent arg1) {

                // TODO Auto-generated method stub

                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{180, 150, 150, 120, 150, 150, 150, 90, 0, 150, 210, 300}, new float[]{60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60}, 222222);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (int h = 0; h <= 5; h++) {
                        robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{180, 150, 140, 120, 150, 140, 150, 90, 0, 150, 210, 300}, new float[]{60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60}, 222222);
                        try {
                            Thread.sleep(150);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{180, 150, 160, 120, 150, 160, 150, 90, 0, 150, 210, 300}, new float[]{60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60}, 222222);
                        try {
                            Thread.sleep(150);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    neutral();

                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    bsleep.setImageResource(R.drawable.buttons);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return false;

            }

        });

    }

        public static void rotateMatrix(int N, int mat[][])
        {
            // Consider all squares one by one
            for (int x = 0; x < N / 2; x++)
            {
                // Consider elements in group of 4 in
                // current square
                for (int y = x; y < N-x-1; y++)
                {
                    // store current cell in temp variable
                    int temp = mat[x][y];

                    // move values from right to top
                    mat[x][y] = mat[y][N-1-x];

                    // move values from bottom to right
                    mat[y][N-1-x] = mat[N-1-x][N-1-y];

                    // move values from left to bottom
                    mat[N-1-x][N-1-y] = mat[N-1-y][x];

                    // assign temp to left
                    mat[N-1-y][x] = temp;
                }
            }
        }

    public void neutral() {
        //robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{150, 90, 0, 150, 210, 300, 150, 90, 0, 150, 210, 300, 150, 150}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
        robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{110, 90, 0, 190, 210, 300, 110, 90, 0, 190, 210, 300, 150, 150}, new float[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, 222222);
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void b_connect_click(View v) throws InterruptedException {
        if (connected == Boolean.FALSE) {
            connected = Boolean.TRUE;
            bconnect.setText("Disconnect");
            robot.openTcp("192.168.43.2");

            for (i = 0; i <= 53; i++) {
                for (j = 0; j <= 11; j++) {
                    packet_front[i][j] = robot.map(frontx[i][j], 0, 1023, 0, 300);
                }
                Thread.sleep(1);
            }
            Thread.sleep(1);


        } else {
            connected = Boolean.FALSE;
            bconnect.setText("Connect");
            robot.direct_play("bye.mp3");
            robot.mTcpClient.stopClient();
        }

    }

    float[] imu()
    {
        String string = "270 IMU";
        byte[] b = string.getBytes();
        float[] readarray=robot.LUCI_createBarePacket(270,"IMU".getBytes());
        return readarray;
    }
    public void updateGUI(float roll_Float,float pitch_Float)
    {
        //recieved_data.setText( String.valueOf(roll_Float) + "," + String.valueOf(pitch_Float));
        recieved_data.setText("FGHFGD");
    }

    public void MOVE(int steps,int Move_Horizontal,int Move_Verticle) {
        if (Move_Verticle < 0) {
            Move_Verticle=- Move_Verticle;
            for (g = 1; g <= steps; g++) {
                for (i = 0; i <= 53; i++) {
                    try {
                        Thread.sleep(Move_Verticle);
                        robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{packet_front[i][0], packet_front[i][1], packet_front[i][2], packet_front[i][3], packet_front[i][4], packet_front[i][5], packet_front[i][6], packet_front[i][7], packet_front[i][8], packet_front[i][9], packet_front[i][10], packet_front[i][11]}, new float[]{60 - Move_Horizontal, 60, 60, 60 + Move_Horizontal, 60, 60, 60 + 60, 60, 60, 60 - Move_Horizontal, 60, Move_Verticle}, 222222);
                        Log.d("LUCI_Tx", "" + Arrays.toString(lucipacket));
                    } catch (Exception e) {
                        Log.e("SOCKET", "exception", e);
                    }
                }
            }
        }
        else if (Move_Verticle >0 )
        {
            for (g = 1; g <= steps; g++) {
                for (i = 53; i >= 1; i--) {
                    try {
                        Thread.sleep(Move_Verticle);
                        robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{packet_front[i][0], packet_front[i][1], packet_front[i][2], packet_front[i][3], packet_front[i][4], packet_front[i][5], packet_front[i][6], packet_front[i][7], packet_front[i][8], packet_front[i][9], packet_front[i][10], packet_front[i][11]}, new float[]{60 - Move_Horizontal, 60, 60, 60 + Move_Horizontal, 60, 60, 60 + Move_Horizontal, 60, Move_Verticle, 60 - Move_Horizontal, 60, 60}, 222222);
                        Log.d("LUCI_Tx", "" + Arrays.toString(lucipacket));
                    } catch (Exception e) {
                        Log.e("SOCKET", "exception", e);
                    }
                }
            }
        }
    }

    public  void back()
    {
// vijay desai.
    }

    public static byte[] toByteArray(List<Byte> in) {
        final int n = in.size();
        byte ret[] = new byte[n];
        for (int i = 0; i < n; i++) {
            ret[i] = in.get(i);
        }
        return ret;
    }



    private class VideoTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (connected) {
                try {
                    String msg = "REQUEST_VIDEO_LINK";
                    msg = msg.concat("\nFOR_IP:");
                    msg = msg.concat(Server);

                    InetAddress group = null;

                    group = InetAddress.getByName("239.255.255.250");
                    MulticastSocket s = null;
                    s = new MulticastSocket(1700);
                    s.joinGroup(group);
                    DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(),
                            group, 1700);
                    s.send(hi);
                    byte[] buf = new byte[1000];
                    DatagramPacket recv = new DatagramPacket(buf, buf.length);
                    s.setSoTimeout(1000);
                    String received_link = "\n";
                    try {
                        s.receive(recv);
                        s.receive(recv);
                        received_link = (new String(recv.getData())).substring(0, recv.getLength());

                        Log.d("VIDEOLINK:", received_link);
                    } catch (SocketTimeoutException e) {
                        received_link = "\n";
                    }
                    String[] separated = received_link.split("\n");
                    if (separated.length > 2) {

                        Log.d("multicast ip:", separated[2].split(":")[1]);
                        Log.d("multicast port:", separated[3].split(":")[1]);

                        group = InetAddress.getByName(separated[2].split(":")[1]);
                        int port = Integer.parseInt(separated[3].split(":")[1]);


                        TcpClient vid = new TcpClient(new TcpClient.OnMessageReceived() {
                            @Override
                            //here the messageReceived method is implemented
                            public void messageReceived(String message) {
                                //this method calls the onProgressUpdate
                                publishProgress();
                            }
                        });

                        vid.SERVER_IP = group.toString().replace("/", "");
                        vid.SERVER_PORT = port;
                        vid.run();
                        List<Byte> video_data_temp = new ArrayList<Byte>();
                        Bitmap image;
                        while (connected) {
                            byte[] vdata = vid.readVideo();
                            String vdata_s = new String(vdata, "US-ASCII");

                            int sindex = vdata_s.indexOf("START");
                            int eindex = vdata_s.indexOf("END");
                            if (eindex != -1) {
                                for (int i = 0; i < eindex; i++) {
                                    video_data_temp.add(vdata[i]);
                                }
                                byte[] recvideo = toByteArray(video_data_temp);
                                try {
                                    image = BitmapFactory.decodeByteArray(recvideo, 0, recvideo.length);
                                    if (image != null) {
                                        Canvas c = null;
                                        gv_video.bmp = image;

                                        c = gv_video.getHolder().lockCanvas();

                                        synchronized (gv_video.getHolder()) {
                                            gv_video.onDraw(c);
                                        }
                                        gv_video.getHolder().unlockCanvasAndPost(c);
                                        vid.socket.getInputStream().reset();
                                    }
                                } catch (Exception e) {

                                }

                            }

                            if (sindex != -1) {
                                video_data_temp.clear();
                                for (int i = sindex + 5; i < vdata.length; i++) {
                                    video_data_temp.add(vdata[i]);
                                }
                            }

                            if (sindex == -1 && eindex == -1 && vdata.length > 5) {
                                for (int i = 0; i < vdata.length; i++) {
                                    video_data_temp.add(vdata[i]);
                                }
                            }
                        }
                        vid.stopClient();
                    } else {
                        //StandAlone TCP IMAGE STREAM
                        robot.request_video_connection();
                        Thread.sleep(1000);
                        TcpClient vid = new TcpClient(new TcpClient.OnMessageReceived() {
                            @Override
                            //here the messageReceived method is implemented
                            public void messageReceived(String message) {
                                //this method calls the onProgressUpdate
                                publishProgress();
                            }
                        });

                        vid.SERVER_IP = Server;
                        vid.SERVER_PORT = 10000;
                        vid.run();
                        List<Byte> video_data_temp = new ArrayList<Byte>();
                        Bitmap image;
                        while (connected) {
                            byte[] vdata = vid.readVideo();
                            String vdata_s = new String(vdata, "US-ASCII");

                            int sindex = vdata_s.indexOf("START");
                            int eindex = vdata_s.indexOf("END");
                            if (eindex != -1) {
                                for (int i = 0; i < eindex; i++) {
                                    video_data_temp.add(vdata[i]);
                                }
                                byte[] recvideo = toByteArray(video_data_temp);
                                try {
                                    image = BitmapFactory.decodeByteArray(recvideo, 0, recvideo.length);
                                    if (image != null) {
                                        Canvas c = null;
                                        gv_video.bmp = image;

                                        c = gv_video.getHolder().lockCanvas();

                                        synchronized (gv_video.getHolder()) {
                                            gv_video.onDraw(c);
                                        }
                                        gv_video.getHolder().unlockCanvasAndPost(c);
                                        vid.socket.getInputStream().reset();
                                    }
                                } catch (Exception e) {

                                }

                            }

                            if (sindex != -1) {
                                video_data_temp.clear();
                                for (int i = sindex + 5; i < vdata.length; i++) {
                                    video_data_temp.add(vdata[i]);
                                }
                            }

                            if (sindex == -1 && eindex == -1 && vdata.length > 5) {
                                for (int i = 0; i < vdata.length; i++) {
                                    video_data_temp.add(vdata[i]);
                                }
                            }

                        }
                        vid.stopClient();


                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }


    }

    public class send_action extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            while (pressed) {
                String s = params[0];
//                try {
//                    if (s == "Front") {
//                        for (i = 0; i <= 53; i++) {
//                            try {
//                                Thread.sleep(15);
//                                robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{packet_front[i][0], packet_front[i][1], packet_front[i][2], packet_front[i][3], packet_front[i][4], packet_front[i][5], packet_front[i][6], packet_front[i][7], packet_front[i][8], packet_front[i][9], packet_front[i][10], packet_front[i][11]}, new float[]{60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60}, 222222);
//                                Log.d("LUCI_Tx", "" + Arrays.toString(lucipacket));
//                            } catch (Exception e) {
//                                Log.e("SOCKET", "exception", e);
//                            }
//                        }
//                    } else if (s == "Back") {
//                        for (i = 53; i >= 0; i--) {
//
//                            try {
//                                Thread.sleep(15);
//                                robot.writeDegreesSyncAxMx(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new float[]{packet_front[i][0], packet_front[i][1], packet_front[i][2], packet_front[i][3], packet_front[i][4], packet_front[i][5], packet_front[i][6], packet_front[i][7], packet_front[i][8], packet_front[i][9], packet_front[i][10], packet_front[i][11]}, new float[]{60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60}, 222222);
//                                Log.d("LUCI_Tx", "" + Arrays.toString(lucipacket));
//                            } catch (Exception e) {
//                                Log.e("SOCKET", "exception", e);
//                            }
//                        }
//                    } else if (s == "Left") {
//                        for (i = 53; i >= 1; i--) {
//                            leftz = rightx[i];
//                            luciuartpacket = robot.LUCI_WriteUARTPacket(222222, leftz);
//                            lucipacket = robot.LUCI_createPacket(254, 0, luciuartpacket, null);
//                            try {
//                                robot.mTcpClient.sendMessage(lucipacket);
//                                Log.d("LUCI_Tx", "" + Arrays.toString(lucipacket));
//                            } catch (Exception e) {
//                                Log.e("SOCKET", "exception", e);
//                            }
//                            System.out.println("        Front packet  " + i + "   Sent");
//                            try {
//                                Thread.sleep(15);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    } else if (s == "Right") {
//                        for (i = 1; i <= 53; i++) {
//                            rightz = rightx[i];
//                            luciuartpacket = robot.LUCI_WriteUARTPacket(222222, rightz);
//                            lucipacket = robot.LUCI_createPacket(254, 0, luciuartpacket, null);
//                            try {
//                                robot.mTcpClient.sendMessage(lucipacket);
//                                Log.d("LUCI_Tx", "" + Arrays.toString(lucipacket));
//                            } catch (Exception e) {
//                                Log.e("SOCKET", "exception", e);
//                            }
//                            System.out.println("        Front packet  " + i + "   Sent");
//                            try {
//                                Thread.sleep(15);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    } else {
//                        System.out.println("else");
//                        break;
//                    }
//                } catch (Exception e) {
//                    Log.e("TCP", "S: Error", e);
//                }
            }
            return "executed";
        }
    }
    }