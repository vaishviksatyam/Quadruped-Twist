package com.example.sirenabuild.ex1;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TcpClient {

    public String SERVER_IP;
    public  int SERVER_PORT = 7777;
    private String mServerMessage;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private PrintWriter mBufferOut;
    private BufferedReader mBufferIn;
    private DataOutputStream sWriter;
    Socket socket;


    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    public void sendMessage(String message) {
        if (mBufferOut != null && !mBufferOut.checkError()) {
            mBufferOut.println(message);
            mBufferOut.flush();
        }
    }

    public void sendMessage(byte[] msg){
        if(sWriter!=null){
            try {
                sWriter.write(msg);
            } catch (IOException e) {
                Log.e("Socket","exception",e);
            }
        }
    }

    public byte[] readVideo(){
        byte[] bufarr=new byte[1024];

        try {
            if(socket.getInputStream().available()>1024){
                socket.getInputStream().read(bufarr);
                return bufarr;
            }


        }
        catch(Exception e){
        }

        return new byte[]{0};
    }

    public byte[] readMessage(){
        byte[] bufarr=new byte[1];

        List<Byte> buflist = new ArrayList<Byte>();
        int count=0;

        while(count<8192){
            try {
                if(socket.getInputStream().available()>0){
                    socket.getInputStream().read(bufarr);
                    buflist.add(bufarr[0]);
                    count+=1;
                }
                else{
                    break;
                }

            }
            catch(Exception e){
            }
        }

        bufarr= toByteArray(buflist);
        return bufarr;
    }

    public void stopClient() {
        try {

            mRun = false;

            if (mBufferOut != null) {
                mBufferOut.flush();
                mBufferOut.close();
            }

            mMessageListener = null;
            mBufferIn = null;
            mBufferOut = null;
            mServerMessage = null;
            socket.close();
        }
        catch(Exception e){

        }
    }
    public static byte[] toByteArray(List<Byte> in) {
        final int n = in.size();
        byte ret[] = new byte[n];
        for (int i = 0; i < n; i++) {
            ret[i] = in.get(i);
        }
        return ret;
    }

    public void run() {

        mRun = true;

        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            Log.e("TCP Client", "C: Connecting...");
            socket = new Socket(serverAddr, SERVER_PORT);

            try {

                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                sWriter = new DataOutputStream(socket.getOutputStream());
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                byte[] registerAsyncPacket=new byte[]{0,0,2,3,0,0,0,0,0,0};
                if(SERVER_PORT==7777) {
                    sendMessage(new String(registerAsyncPacket, "UTF-8"));
                    Thread.sleep(500);
                    readMessage();
                }

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {

            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }


    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}
