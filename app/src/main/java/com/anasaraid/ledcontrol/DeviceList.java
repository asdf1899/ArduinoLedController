package com.anasaraid.ledcontrol;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.Set;
import java.util.ArrayList;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class DeviceList extends AppCompatActivity {
    private BluetoothAdapter myBluetooth = null;
    private Set pairedDevices;
    private ListView deviceList;
    public static String EXTRA_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button btnPaired = (Button) findViewById(R.id.btnPairedDevies);
        deviceList = (ListView) findViewById(R.id.lstPairedDevices);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (myBluetooth == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth device not available", Toast.LENGTH_LONG).show();
            finish();
        } else {
            if (myBluetooth.isEnabled()) {
                Toast.makeText(getApplicationContext(), "Bluetooth already on", Toast.LENGTH_LONG).show();
            } else {
                Intent turnBTOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTOn, 1);
            }
            btnPaired.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pairedDevicesList();
                }
            });
        }
    }

    private void pairedDevicesList() {
        Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();
        for (BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
        }
        Toast.makeText(getApplicationContext(), "Showing paired devices", Toast.LENGTH_LONG).show();
        final ArrayAdapter adapter = new ArrayAdapter(DeviceList.this, android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        //deviceList.setOnItemClickListener(myListClickListener);
    }

    AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView av, View v, int arg2, long arg3) {
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Intent i = new Intent(v.getContext(), ledContol.class);
            i.putExtra(EXTRA_ADDRESS, address);
            startActivity(i);
        }
    };
    private void showAlert(String message){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setTitle("Info");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setCancelable(true);
        alert.create().show();
    }
}