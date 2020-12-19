package com.example.reposicion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.mtp.MtpConstants;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.reposicion.Adaptadores.listViewProductoAdapter;
import com.example.reposicion.Models.Producto;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText marca, modelo, year, motor, chasis;
    ListView listViewCarro;

    private ArrayList<Producto> listProductos = new ArrayList<Producto>();
    ArrayAdapter<Producto> arrayAdapterProducto;
    listViewProductoAdapter ListViewProductoAdapter;
    LinearLayout linearEditar;
    Producto productoSeleccionado;

    Button btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        marca = findViewById(R.id.marcaVehiculo);
        modelo = findViewById(R.id.modeloVehiculo);
        year = findViewById(R.id.yearVehiculo);
        motor = findViewById(R.id.motorVehiculo);
        chasis = findViewById(R.id.chasisVehiculo);

        btnCancelar = findViewById(R.id.btnCancelar);

        listViewCarro = findViewById(R.id.listaCarro);
        linearEditar = findViewById(R.id.linearEditar);

        listViewCarro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                productoSeleccionado = (Producto) adapterView.getItemAtPosition(i);

                marca.setText(productoSeleccionado.getMarca());
                modelo.setText(productoSeleccionado.getModelo());
                year.setText(productoSeleccionado.getYear());
                motor.setText(productoSeleccionado.getMotor());
                chasis.setText(productoSeleccionado.getChasis());

                //Hace visible el linear layout
                linearEditar.setVisibility(View.VISIBLE);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearEditar.setVisibility(View.GONE);
                productoSeleccionado = null;
            }
        });

        insertarDataFirebase();
        listarProducto();
    }

    public  void insertarDataFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void listarProducto(){
        databaseReference.child("Vehiculos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProductos.clear();
                for(DataSnapshot objSnaptsHost : snapshot.getChildren()){
                    Producto p = objSnaptsHost.getValue(Producto.class);
                    listProductos.add(p);
                }

                //inicia el adaptador
                ListViewProductoAdapter = new listViewProductoAdapter(MainActivity.this, listProductos);

                listViewCarro.setAdapter(ListViewProductoAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String Marca = marca.getText().toString();
        String Modelo = modelo.getText().toString();
        String Year = year.getText().toString();
        String Motor = motor.getText().toString();
        String Chasis = chasis.getText().toString();

        switch (item.getItemId()){
            case R.id.menu_agregar:
                insertar();
                break;
            case R.id.menu_modificar:
                if (productoSeleccionado != null){
                    if (validarInputs()==false){
                        Producto p = new Producto();
                        p.setId(productoSeleccionado.getId());
                        p.setMarca(Marca);
                        p.setModelo(Modelo);
                        p.setYear(Year);
                        p.setMotor(Motor);
                        p.setChasis(Chasis);

                        databaseReference.child("Vehiculos").child(p.getId()).setValue(p);
                        Toast.makeText(MainActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        linearEditar.setVisibility(View.GONE);
                        productoSeleccionado = null;
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Seleccione un vehiculo", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.menu_eliminar:
                if (productoSeleccionado != null){
                    Producto pro = new Producto();
                    pro.setId(productoSeleccionado.getId());

                    databaseReference.child("Vehiculos").child(pro.getId()).removeValue();
                    Toast.makeText(MainActivity.this, "Registro eliminado exitosamente", Toast.LENGTH_SHORT).show();
                    linearEditar.setVisibility(View.GONE);
                    productoSeleccionado = null;
                    Toast.makeText(MainActivity.this, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Seleccione un vehiculo para eliminar", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void insertar(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                MainActivity.this
        );

        View mView = getLayoutInflater().inflate(R.layout.insertar, null);
        Button btnInsert = (Button) mView.findViewById(R.id.insertarVehiculo);

        final EditText txtMarca = (EditText)mView.findViewById(R.id.txtmarca);
        final EditText txtModelo = (EditText)mView.findViewById(R.id.txtmodelo);
        final EditText txtYear = (EditText)mView.findViewById(R.id.txtyear);
        final EditText txtMotor = (EditText)mView.findViewById(R.id.txtmotor);
        final EditText txtChasis = (EditText)mView.findViewById(R.id.txtchasis);

        mBuilder.setView(mView);
        final  AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String marcatext = txtMarca.getText().toString();
                String modelotext = txtModelo.getText().toString();
                String yeartext = txtYear.getText().toString();
                String motortext = txtMotor.getText().toString();
                String chasistext = txtChasis.getText().toString();

                if (marcatext.isEmpty() || marcatext.length() < 3){
                    showError(txtMarca, "Descripcion no es validad 'Min. 3 letras '");

                }else if (modelotext.isEmpty() || modelotext.length() < 1){
                    showError(txtModelo, "Precio no es validad 'Min. 1 numero '");

                }else if (yeartext.isEmpty() || yeartext.length() < 1){
                    showError(txtYear, "Precio no es validad 'Min. 1 numero '");

                }else if (motortext.isEmpty() || motortext.length() < 1){
                    showError(txtMotor, "Precio no es validad 'Min. 1 numero '");

                }else if (chasistext.isEmpty() || chasistext.length() < 1){
                    showError(txtChasis, "Precio no es validad 'Min. 1 numero '");

                }else {
                    Producto p = new Producto();
                    p.setId(UUID.randomUUID().toString());
                    p.setMarca(marcatext);
                    p.setModelo(modelotext);
                    p.setYear(yeartext);
                    p.setMotor(motortext);
                    p.setChasis(chasistext);

                    databaseReference.child("Vehiculos").child(p.getId()).setValue(p);
                    Toast.makeText(MainActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            }
        });
    }

    public void showError(EditText input, String s){
        input.requestFocus();
        input.setError(s);
    }

    public boolean validarInputs() {

        String Marca = marca.getText().toString();
        String Modelo = modelo.getText().toString();
        String Year = year.getText().toString();
        String Motor = motor.getText().toString();
        String Chasis = chasis.getText().toString();

        if (Marca.isEmpty() || Marca.length() < 1) {
            showError(marca, "No es validad 'Min. 1 letras '");
            return true;
        } else if (Modelo.isEmpty() || Modelo.length() < 1) {
            showError(modelo, "No es validad 'Min. 1 letras '");
            return true;
        }else if (Year.isEmpty() || Year.length() < 1) {
            showError(year, "No es validad 'Min. 1 letras '");
            return true;
        }else if (Motor.isEmpty() || Motor.length() < 1) {
            showError(motor, "No es validad 'Min. 1 letras '");
            return true;
        }else if (Chasis.isEmpty() || Chasis.length() < 1) {
            showError(chasis, "No es validad 'Min. 1 letras '");
            return true;
        }else   {
            return false;
        }
    }
}