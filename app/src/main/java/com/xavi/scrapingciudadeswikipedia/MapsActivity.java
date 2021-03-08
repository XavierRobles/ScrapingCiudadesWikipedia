package com.xavi.scrapingciudadeswikipedia;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.xavi.scrapingciudadeswikipedia.entidad.Coordenadas;
import com.xavi.scrapingciudadeswikipedia.recursos.AccesoDatos;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AccesoDatos.pedirDatos, AccesoFirebase.iRecuperarDatos{

    private GoogleMap mMap;
    private Button btn_solicitar;
    private TextView titulo_ciudad;
    private EditText nombreCiudad;
    private String city;
    private int zoomLevel = 13; //De 0 a 21, 0 max zoom
    private AccesoDatos.pedirDatos c = this;
    private Spinner spn_cuidades;
    private ArrayList<Coordenadas> ciudades = new ArrayList<>();
    AccesoFirebase.iRecuperarDatos interfaz_recuperarDatos = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        AccesoFirebase.pedirRutasFirebase(interfaz_recuperarDatos);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        titulo_ciudad = findViewById(R.id.tv_titulo_Ciudad);
        btn_solicitar = findViewById(R.id.btn_solicitar);
        nombreCiudad = findViewById(R.id.it_introducirTexto);
        spn_cuidades = findViewById(R.id.spn_cuidades);


        AdapterView.OnItemSelectedListener oyente_spiner = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ciudadSpiner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        };
        spn_cuidades.setOnItemSelectedListener(oyente_spiner);
        btn_solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                city = nombreCiudad.getText().toString();
                Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
                AccesoDatos h = new AccesoDatos(city, mainThreadHandler, c);
                Thread t1 = new Thread(h, "Hilo1:\n");
                t1.start();
                titulo_ciudad.setText(city.toUpperCase());
                nombreCiudad.requestFocus();
                nombreCiudad.setText("");


            }
        });

    }

    private void ciudadSpiner() {
        mMap.clear();
        Coordenadas ruta_seleccionada = (Coordenadas) spn_cuidades.getSelectedItem();
        LatLng latlog = new LatLng(ruta_seleccionada.getLatitude(), ruta_seleccionada.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latlog).title("Marker in " + city));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlog, zoomLevel));


    }

    public void grabarRutaFireBase(LatLng coord){

        Log.d("PRUEBA1", coord.toString());
        AccesoFirebase.grabarRuta(city, coord);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void avisadordeDatos(LatLng coord) {
        grabarRutaFireBase(coord);
        mMap.addMarker(new MarkerOptions().position(coord).title("Marker in " + city));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, zoomLevel));

    }

    @Override
    public void recuperarRutas(ArrayList<Coordenadas> lista_ciudades) {
        ciudades = lista_ciudades;

        rellenarSpiner();

        Log.d("PRUEBA3", lista_ciudades.toString());
    }
    private void rellenarSpiner() {

        ArrayAdapter<Coordenadas> adaptador_ciudades = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, ciudades);
        spn_cuidades.setAdapter(adaptador_ciudades);

    }
}