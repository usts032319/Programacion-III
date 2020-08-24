package com.example.conversorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VolumenActivity extends AppCompatActivity {

    public Button btnCalcular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volumen);
        btnCalcular = (Button)findViewById(R.id.btnConvertir);
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                procesar(view);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void procesar(View vista){
        try {
            Spinner cboOperaciones = (Spinner)findViewById(R.id.cboOperaciones);
            TextView tempVal = (TextView) findViewById(R.id.txtnum1);
            double num1 = Double.parseDouble(tempVal.getText().toString());
            double respuesta = 0;
            switch (cboOperaciones.getSelectedItemPosition()){
                case 1: //galon
                    respuesta = num1 * 0.22;
                    tempVal = (TextView) findViewById(R.id.lblRespuesta);
                    tempVal.setText("El total es" + respuesta + " Galones" );
                    Toast.makeText(getApplicationContext(),"EXITO",Toast.LENGTH_LONG).show();
                    break;
                case 2: //taza
                    respuesta = num1 * 3.52;
                    tempVal = (TextView) findViewById(R.id.lblRespuesta);
                    tempVal.setText("El total es" + respuesta + " Tazas" );
                    Toast.makeText(getApplicationContext(),"EXITO",Toast.LENGTH_LONG).show();
                    break;
                case 3: //metro
                    respuesta = num1 * 0.000999;
                    tempVal = (TextView) findViewById(R.id.lblRespuesta);
                    tempVal.setText("El total es" + respuesta + " Metros Cubicos" );
                    Toast.makeText(getApplicationContext(),"EXITO",Toast.LENGTH_LONG).show();
                    break;
                case 4: //mililitro
                    respuesta = num1 * 1000;
                    tempVal = (TextView) findViewById(R.id.lblRespuesta);
                    tempVal.setText("El total es" + respuesta + " Mililitros" );
                    Toast.makeText(getApplicationContext(),"EXITO",Toast.LENGTH_LONG).show();
                    break;
                case 5: //barril
                    respuesta = num1 * 0.00865;
                    tempVal = (TextView) findViewById(R.id.lblRespuesta);
                    tempVal.setText("El total es" + respuesta + " Barriles" );
                    Toast.makeText(getApplicationContext(),"EXITO",Toast.LENGTH_LONG).show();
                    break;
            }
        }catch (Exception err){
            TextView temp = (TextView) findViewById(R.id.lblRespuesta);
            temp.setText("INGRESE CANTIDAD");
            Toast.makeText(getApplicationContext(),"INGRESE CANTIDAD",Toast.LENGTH_LONG).show();
        }
    }
}