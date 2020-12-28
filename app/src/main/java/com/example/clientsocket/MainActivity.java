package com.example.clientsocket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import main.Datos;
import main.Departamento;
import main.Empleado;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText e1;
    private EditText e2;
    private  Button b1;
    private Button b2;
    private Datos datos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = (EditText)findViewById(R.id.et1);
        e2 = (EditText)findViewById(R.id.editTextTextMultiLine);
        b1 = (Button) findViewById(R.id.btn1);
        b1.setOnClickListener(this);
        b2 = (Button)findViewById(R.id.btn2);
        b2.setOnClickListener(this);

    }

    public void sendText(View v, String consulta, int tipoConsulta) throws InterruptedException {
        datos = new Datos();
        datos.setConsulta(consulta);
        datos.setTipoConsulta(tipoConsulta);
        datos.setContenido(" " + e1.getText().toString());
        IOListenerClt IOListenerClt = new IOListenerClt(datos);
        Thread hiloSender = new Thread(IOListenerClt);
        hiloSender.start();
        hiloSender.join();
        datos = IOListenerClt.getDatos();
        Log.i("CONTENIDO", datos.getContenido());
        e2.setText("");
        switch (tipoConsulta){
            case 1:
                ArrayList<Empleado> listaEmp = (ArrayList<Empleado>) datos.getObjeto();
                for (Empleado emp : listaEmp){
                    e2.append(emp.getApellido() + "\n");
                    Log.i("EMPLEADOS", emp.getApellido());
                }
                break;
            case 2:
                ArrayList<Departamento> listaDep = (ArrayList<Departamento>) datos.getObjeto();
                for (Departamento dep : listaDep){
                    e2.append(dep.getNombre() + "\n");
                    Log.i("EMPLEADOS", dep.getNombre());
                }
                break;
        }
        e1.setText(datos.getContenido());
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn1:
                try {
                    sendText(v, "select * from empleados;", 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn2:
                try {
                    sendText(v, "select * from departamentos;", 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}