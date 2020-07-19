package com.example.getprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    ImageButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=findViewById(R.id.imageButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenScanner();
            }
        });
    }

         private void OpenScanner() {
             new IntentIntegrator(this).initiateScan();
         }

         @Override
         protected void onActivityResult(int requestCode, int resultCode, Intent data) {
             IntentResult result =   IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
             if (result != null) {
                 if (result.getContents() == null) {
                     Toast.makeText(this,    "Cancelled",Toast.LENGTH_LONG).show();
                 } else {
                     Toast.makeText(this,"Connecting",Toast.LENGTH_LONG).show();
                     ConnectToWifi(result.getContents());
                 }
             } else {
                 super.onActivityResult(requestCode, resultCode, data);
             }
         }

    void ConnectToWifi(String result){
        String  conf[]=result.split(";");
        String ssid=conf[2];
        ssid=ssid.replaceFirst("S:","");
        String key=conf[1];
        key=key.replaceFirst("P:","");
        //tv.setText("ssid:"+ssid+"\npassword:"+key);
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiConfig.preSharedKey = String.format("\"%s\"", key);

        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
        Toast.makeText(this,"Connected",Toast.LENGTH_LONG).show();
    }
}