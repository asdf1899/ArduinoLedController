package com.anasaraid.ledcontrol;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class LedContol extends AppCompatActivity {
    Button btnOn, btnOff, btnDis;
    SeekBar brightness;
    BluetoothAdapter myBluetooth = null;
    Handler bluetoothIn;
    final int handlerState = 0;        				 //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    //private ConnectedThread mConnectedThread;
    public static final UUID myUUIID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_contol);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_led_contol);
        checkBTState();

        Intent newInt = getIntent();
        address = newInt.getStringExtra(DeviceList.EXTRA_ADDRESS);
        btnOn = (Button)findViewById(R.id.btnTurnOn);
        btnOff = (Button)findViewById(R.id.btnTurnOff);
        btnDis = (Button)findViewById(R.id.btnDisconnect);
        brightness = (SeekBar)findViewById(R.id.seekBar);
        boolean connected = false;
        try{
             if (btSocket == null){
                 myBluetooth = BluetoothAdapter.getDefaultAdapter();
                 BluetoothDevice disp = myBluetooth.getRemoteDevice(address);
                 btSocket = disp.createInsecureRfcommSocketToServiceRecord(myUUIID);
                 BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 btSocket.connect();
             }
        }catch (IOException e){
            connected = false;
            showAlert("ERROR", e.getMessage(), true);
        }/*
        btnOn.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
             turnOnLed();
            }
        });
        btnOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                turnOffLed();
            }
        });
        btnDis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                disconnect();
            }
        });*/

    }
    private void disconnect(){
        if(btSocket != null){
            try{
               btSocket.close();
            }catch(IOException e){
                showAlert("ERROR", e.getMessage().toString(), true);
            }
        }
    }
    private void turnOnLed(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("H".toString().getBytes());
            }catch(IOException e){
                showAlert("ERROR", e.getMessage().toString(), true);
            }
        }
    }
    private void turnOffLed(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("L".toString().getBytes());
            }catch(IOException e){
                showAlert("ERROR", e.getMessage().toString(), true);
            }
        }
    }
    private void checkBTState() {
        BluetoothAdapter mBtAdapter=BluetoothAdapter.getDefaultAdapter();
        if(mBtAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBtAdapter.isEnabled()) {

            } else {
                showAlert("Error", "Bluetooth disabled", true);
            }
        }
    }
    private void showAlert(String title, String message, final boolean isFinish){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setTitle(title);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isFinish){
                    finish();
                }
            }
        });
        alert.setCancelable(true);
        alert.create().show();
    }
    private void diconnect(){
        //.disable();
        Toast.makeText(getApplicationContext(),"Turned off" ,Toast.LENGTH_LONG).show();
    }
}
