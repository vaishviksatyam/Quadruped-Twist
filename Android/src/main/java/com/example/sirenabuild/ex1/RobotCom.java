package com.example.sirenabuild.ex1;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by sirenabuild on 6/12/17.
 */

public class RobotCom {

    public TcpClient mTcpClient;
    public String serverip;
    public MainActivity MA;
    float[] angles = new float[2];
    byte[] LuciPacket= new byte[22];
    public class MotorTypes{
        public int AX12=0;
        public int AX18=1;
        public int MX28=2;
        public int MX64=3;
        public int MX106=4;
        public int XL320=5;

    }



    public MotorTypes MOTORTYPES=new MotorTypes();


    public Long[] BAUDRATES=new Long[]{Long.valueOf(2000000), Long.valueOf(1000000), Long.valueOf(500000), Long.valueOf(222222), Long.valueOf(117647), Long.valueOf(100000), Long.valueOf(57142), Long.valueOf(9615)};
    public byte[] LatestReceivedBytes=new byte[512];
    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.SERVER_IP=serverip;
            mTcpClient.SERVER_PORT=7777;
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....

        }
    }
    public void sendTCP(byte[] x){
        new sendMessageBytes().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,x);
    }

    public void openTcp(String ip){
        serverip=ip;
        new ConnectTask().execute("");
    }
    public class sendMessageBytes extends AsyncTask<byte[], Void, String> {

        @Override
        protected String doInBackground(byte[]... params) {


            try {

                mTcpClient.sendMessage(params[0]);
                Log.d("LUCI_Tx",""+Arrays.toString(params[0]));
            } catch (Exception e) {
                Log.e("SOCKET", "exception", e);
            }
            return "Executed";
        }

    }

    public void direct_play(String filename)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             {
        String datas="PLAYITEM:DIRECT:"+"http://"+getIPAddress(true)+":12345/"+filename;
        byte[] data=datas.getBytes();
        int LuciLength = data.length;
        byte[] lucilengthbytes = getLHbytes(LuciLength);
        byte[] LUCI_packet = new byte[LuciLength + 10];
        System.arraycopy(new byte[]{0,0,2,41,0,0,0,0,lucilengthbytes[0],lucilengthbytes[1]},0,LUCI_packet,0,10);
        System.arraycopy(data,0,LUCI_packet,10,data.length);
        new sendMessageBytes().execute(LUCI_packet);
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (intf.getName().contains("wlan0")) {
                    List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                    for (InetAddress addr : addrs) {
                        if (!addr.isLoopbackAddress()) {
                            String sAddr = addr.getHostAddress();
                            //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                            boolean isIPv4 = sAddr.indexOf(':') < 0;


                            if (useIPv4) {
                                if (isIPv4)
                                    return sAddr;
                            } else {
                                if (!isIPv4) {
                                    int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                    return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    public class sendAndReceiveMessageBytes extends AsyncTask<byte[], Void, String> {

        @Override
        protected String doInBackground(byte[]... params) {
            try {
                mTcpClient.readMessage();
                Thread.sleep(10);
                mTcpClient.sendMessage(params[0]);
                Log.d("LUCI_Tx",""+Arrays.toString(params[0]));
                Thread.sleep(100);
                byte[] reply=mTcpClient.readMessage();
                if(reply.length>1) {
                    LatestReceivedBytes = reply;

                    int roll = (LatestReceivedBytes[10] & 0xFF)
                            | ((LatestReceivedBytes[11] & 0xFF) << 8)
                            | ((LatestReceivedBytes[12] & 0xFF) << 16)
                            | ((LatestReceivedBytes[13] & 0xFF) << 24);
                    float roll_Float = Float.intBitsToFloat(roll);

                    int pitch = (LatestReceivedBytes[14] & 0xFF)
                            | ((LatestReceivedBytes[15] & 0xFF) << 8)
                            | ((LatestReceivedBytes[16] & 0xFF) << 16)
                            | ((LatestReceivedBytes[17] & 0xFF) << 24);
                    float pitch_Float = Float.intBitsToFloat(pitch);

                    angles[0]=roll_Float;
                    angles[1]=pitch_Float;
                    Log.d("LUCI_RxTx", "" + String.valueOf(roll_Float) + "," + String.valueOf(pitch_Float));
                }
                else{
                    Log.d("LUCI_RxTx", "Not got anything");
                }
            } catch (Exception e) {
                Log.e("SOCKET", "exception", e);
            }
            return "executed";
        }
    }
    public byte[] getLHbytes(int x){
        byte[] retbytes=new byte[2];
        retbytes[0]=(byte)(x & 0xFF);
        retbytes[1]=(byte)((x>>8) & 0xFF);
        return retbytes;
    }

    public void setWheelModeSyncAxMx(int[] ids, int[] motortypes, long baudRate){

        byte[][] datas=new byte[ids.length][4];
        for(int i=0;i<ids.length;i++){
            if(motortypes[i]==MOTORTYPES.AX12 || motortypes[i]==MOTORTYPES.AX18){


                datas[i][0]=0;
                datas[i][1]=0;
                datas[i][2]=0;
                datas[i][3]=0;
            }

            else if(motortypes[i]==MOTORTYPES.MX28 || motortypes[i]==MOTORTYPES.MX64 || motortypes[i]==MOTORTYPES.MX106){


                datas[i][0]=0;
                datas[i][1]=0;
                datas[i][2]=0;
                datas[i][3]=0;
            }

        }

        byte[] sync_packet=syncWriteAxMxPacket(4,6,ids,datas);
        byte[] luciuartpacket=LUCI_WriteUARTPacket(baudRate,sync_packet);
        byte[] lucipacket=LUCI_createPacket(254,0,luciuartpacket,null);
        new sendMessageBytes().execute(lucipacket);


    }
    protected byte[] LuciSingleReadUartPacket(int baudRate,byte bytes2Read, byte[] uartPacket)
    {
        byte[] RUPacket = new byte[uartPacket.length + 6];
        int baudrate = Arrays.asList(BAUDRATES).indexOf(Long.valueOf(baudRate));
        RUPacket[0] = (byte)0; // IM Single Read
        RUPacket[1] = (byte)baudrate;
        RUPacket[2] = 1; // Motors To Read
        RUPacket[3] = 0; // ComType (0 for ax, mx, 1 for xl)
        RUPacket[4] = bytes2Read;
        RUPacket[5] = (byte)uartPacket.length;
        System.arraycopy(uartPacket, 0, RUPacket, 6, uartPacket.length);
        return RUPacket;
    }

    protected byte[] readPacketAxMx(byte id)
    {
        byte[] sync_packet;
        byte crc = (byte)(id + 4 + 2 + 24 + 20);
        crc = (byte)(255 - (255 & crc));
        sync_packet = new byte[] { (byte)255, (byte)255, id, (byte)4, (byte)2, (byte)24, (byte)20, crc };
        return sync_packet;
    }

    public void readSingleAxMx(byte id,byte motorType,int baudRate)
    {
        byte bytes2Read = 26;
        byte[] readbytes;
        byte[] readPacket=readPacketAxMx(id);
        byte[] luciuartpacket=LuciSingleReadUartPacket(baudRate,bytes2Read,readPacket);
        byte[] lucipacket=LUCI_createPacket(254,1,luciuartpacket,null);
        new sendAndReceiveMessageBytes().execute(lucipacket);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("Latest Bytes Received",Arrays.toString(LatestReceivedBytes));
    }

    public void writeDegreesSyncAxMx(int[] ids, int[] motortypes, float[] degrees, float []rpms, long baudRate){

        byte[][] datas=new byte[ids.length][4];
        for(int i=0;i<ids.length;i++){
            if(motortypes[i]==MOTORTYPES.AX12 || motortypes[i]==MOTORTYPES.AX18){

                int goalPos=(int)((degrees[i]/300.0)*1023.0);
                int goalVel=(int)((rpms[i]/114.0)*1023.0);
                byte[] goalPosBytes=getLHbytes(goalPos);
                byte[] goalVelBytes=getLHbytes(goalVel);
                datas[i][0]=goalPosBytes[0];
                datas[i][1]=goalPosBytes[1];
                datas[i][2]=goalVelBytes[0];
                datas[i][3]=goalVelBytes[1];
            }

            else if(motortypes[i]==MOTORTYPES.MX28 || motortypes[i]==MOTORTYPES.MX64 || motortypes[i]==MOTORTYPES.MX106){

                int goalPos=(int)((degrees[i]/360.0)*4095.0);
                int goalVel=(int)((rpms[i]/117.0)*1023.0);
                byte[] goalPosBytes=getLHbytes(goalPos);
                byte[] goalVelBytes=getLHbytes(goalVel);
                datas[i][0]=goalPosBytes[0];
                datas[i][1]=goalPosBytes[1];
                datas[i][2]=goalVelBytes[0];
                datas[i][3]=goalVelBytes[1];
            }

        }

        byte[] sync_packet=syncWriteAxMxPacket(4,30,ids,datas);
        byte[] luciuartpacket=LUCI_WriteUARTPacket(baudRate,sync_packet);
        byte[] lucipacket=LUCI_createPacket(254,0,luciuartpacket,null);
        new sendMessageBytes().execute(lucipacket);


    }
    void request_video_connection(){
        byte[] lucipacket=LUCI_createPacket(259,0,new byte[]{0},null);
        new sendMessageBytes().execute(lucipacket);
    }
    int map(int x, int in_min, int in_max, int out_min, int out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    float map1(float x, float in_min, float in_max, float out_min, float out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    void setMotorPWM(MotorandLED motorandLED,byte direction,int velocity){

        motorandLED.set_dir(direction,MotorandLED.motor1);
        motorandLED.set_vel((byte) (map(velocity,0,100,30,100)),MotorandLED.motor1);
        motorandLED.set_dir(direction,MotorandLED.motor2);
        motorandLED.set_vel((byte) ((map(velocity,0,100,30,100))),MotorandLED.motor2);
        byte[] dataTosend = motorandLED.send_data();
        byte[] luciPacket=new byte[dataTosend.length+10];
        System.arraycopy(new byte[]{0,0,2,(byte)254,0,0,0,0,(byte)dataTosend.length,0},0,luciPacket,0,10);
        System.arraycopy(dataTosend,0,luciPacket,10,dataTosend.length);
        new sendMessageBytes().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,luciPacket);
    }
    public void setOutput(int gpio){

        byte[] luciPacket= new byte[]{0,0,2,(byte)254,0,0,0,0,9,0,3,6,0,0,0,0,1,(byte)gpio,1};
        new sendMessageBytes().execute(luciPacket);
    }

    public void setGPIO(int gpio, int state){

        byte[] luciPacket= new byte[]{0,0,2,(byte)254,0,0,0,0,9,0,3,4,0,0,0,1,1,(byte)gpio,(byte)state};
        new sendMessageBytes().execute(luciPacket);
    }

    public float[] LUCI_createBarePacket(int mbnum, byte[] packet0){

            byte[] packet0lenbytes = getLHbytes(packet0.length);
            byte[] mbnumbytes = getLHbytes(mbnum);
            int LuciLength = packet0.length;
            byte[] lucilengthbytes = getLHbytes(LuciLength);
            byte[] LUCI_packet = new byte[LuciLength + 10];
            System.arraycopy(new byte[]{0,0,2,mbnumbytes[0],mbnumbytes[1],0,0,0,lucilengthbytes[0],lucilengthbytes[1]},0,LUCI_packet,0,10);
            System.arraycopy(packet0,0,LUCI_packet,10,packet0.length);
            new RobotCom.sendAndReceiveMessageBytes().execute(LUCI_packet);
            return angles;
    }


    public byte[] LUCI_createPacket(int mbnum, int mode, byte[]packet0,byte[]packet1){
        if(mode!=4 && mode!=5) {
            byte[] packet0lenbytes = getLHbytes(packet0.length);
            byte[] packet1lenbytes = getLHbytes(0);
            byte[] mbnumbytes = getLHbytes(mbnum);
            int LuciLength = packet0.length + 0 + 5;
            byte[] lucilengthbytes = getLHbytes(LuciLength);
            byte[] LUCI_packet = new byte[LuciLength + 10];
            System.arraycopy(new byte[]{0,0,2,mbnumbytes[0],mbnumbytes[1],0,0,0,lucilengthbytes[0],lucilengthbytes[1],(byte)mode,packet0lenbytes[0],packet0lenbytes[1],packet1lenbytes[0],packet1lenbytes[1]},0,LUCI_packet,0,15);
            System.arraycopy(packet0,0,LUCI_packet,15,packet0.length);
            return LUCI_packet;
        }
        else{
            return new byte[]{0,0,2};
        }
    }
    public byte[] LUCI_WriteUARTPacket(long baudRate,byte[] uartPacket){
        int bIndex= Arrays.asList(BAUDRATES).indexOf(Long.valueOf(baudRate));
        byte[] luciuartpacket=new byte[uartPacket.length+2];
        System.arraycopy(uartPacket,0,luciuartpacket,2,uartPacket.length);
        luciuartpacket[0]=(byte)0;
        luciuartpacket[1]=(byte)bIndex;
        return luciuartpacket;
    }
    public byte[] syncWriteAxMxPacket(int lenofdata, int startaddress, int[] ids, byte[][]datas){
        int len=(lenofdata+1)*ids.length+4;
        byte[]sync_packet=new byte[len+4];
        int crc;
        System.arraycopy(new byte[]{(byte)255,(byte)255,(byte)254,(byte)len,(byte)131,(byte)startaddress,(byte)lenofdata},0,sync_packet,0,7);
        crc=254+len+131+startaddress+lenofdata;
        for(int j=0;j<ids.length;j++){
            sync_packet[7+(j*(lenofdata+1))]=(byte)ids[j];
            crc=crc+ids[j];
            for(int k=0;k<lenofdata;k++){
                sync_packet[7+(j*(lenofdata+1))+k+1]=datas[j][k];
                crc=crc+datas[j][k];
            }
        }

        crc=255-(crc & 0xFF);
        sync_packet[(lenofdata+1)*ids.length+8-1]=(byte)crc;
        return sync_packet;

    }



}
