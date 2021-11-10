package com.sistemascampos.achcliente;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends Activity {
    public static final int MY_PERMISSIONS_ACCESS_NETWORK_STATE = 1;
    public static final int MY_PERMISSIONS_ACCESS_WIFI_STATE = 2;
    public static final int MY_PERMISSIONS_INTERNET = 3;

    private EditText EServidor;
    private EditText EPuerto;
    private Button btnEnviarPeticion;
    private TextView TVMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EServidor = findViewById(R.id.EServidor);
        EPuerto = findViewById(R.id.EPuerto);
        btnEnviarPeticion = findViewById(R.id.btnEnviarPeticion);
        TVMensaje = findViewById(R.id.TVMensaje);
        EServidor.setText("192.168.1.70");
        EPuerto.setText("5000");
        btnEnviarPeticion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarPeticion();
            }
        });
        SolicitarPermisos();
    }
    public void AddText(String msg)
    {
        TVMensaje.setText(TVMensaje.getText().toString()+"\n"+msg);
    }
    public void EnviarPeticion()
    {
        int timeout=1000;//tiempo en milisegundos
        TVMensaje.setText("");
        int puerto=0;
        try {
            puerto = Integer.parseInt(EPuerto.getText().toString());
        }catch (Exception e)
        {
            AddText("Puerto no válido");
            return;
        }
        String servidor=EServidor.getText().toString();
        try {
            AddText("Conectando con el servidor "+servidor+" : " + puerto);
            //Socket socket = new Socket(servidor, puerto);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(servidor, puerto), timeout);
            DataOutputStream salidaServidor = new DataOutputStream(socket.getOutputStream());
            DataInputStream entradaServidor = new DataInputStream( socket.getInputStream() );

            AddText("Enviando mensaje...");
            salidaServidor.writeUTF("Mensaje de prueba\n");
            AddText("Recibiendo mensaje...");
            AddText(entradaServidor.readUTF());

            socket.close();
            AddText("Conexión finalizada");
        }catch (Exception e)
        {
            AddText(e.getMessage());
        }
    }

    public void SolicitarPermisos() {
        //----------------------------------------------------------------------------------------------
        //Cambia los permisos para poder ejecutar procesos que se conectan a la red en el hilo principal
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //------------------------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //para versiones con android 6.0 o superior
            int Check_INTERNET = checkSelfPermission(Manifest.permission.INTERNET);
            int Check_ACCESS_NETWORK_STATE = checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);
            int Check_ACCESS_WIFI__STATE = checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE);

            if (Check_INTERNET != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.INTERNET) == false) {
                    requestPermissions(
                            new String[]{Manifest.permission.INTERNET},
                            MY_PERMISSIONS_INTERNET);
                }
            }
            if (Check_ACCESS_NETWORK_STATE != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_NETWORK_STATE) == false) {
                    requestPermissions(
                            new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                            MY_PERMISSIONS_ACCESS_NETWORK_STATE);
                }
            }
            if (Check_ACCESS_WIFI__STATE != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_WIFI_STATE) == false) {
                    requestPermissions(
                            new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                            MY_PERMISSIONS_ACCESS_WIFI_STATE);
                }
            }
        }
    }


}