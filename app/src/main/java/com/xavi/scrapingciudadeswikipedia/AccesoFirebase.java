package com.xavi.scrapingciudadeswikipedia;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xavi.scrapingciudadeswikipedia.entidad.Coordenadas;

import java.util.ArrayList;

public class AccesoFirebase {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance("https://loginfirebase-62cd0-default-rtdb.europe-west1.firebasedatabase.app/");

    public static void grabarRuta(String city, LatLng c) {
        Log.d("PRUEBA", city);
        Coordenadas c1 = new Coordenadas(c.latitude, c.longitude, city);
        Log.d("PRUEBA6", city);
        DatabaseReference myRef = database.getReference("Ciudades");
        myRef.push().setValue(c1);

    }

    public static void pedirRutasFirebase(iRecuperarDatos callback) {
        DatabaseReference myRef = database.getReference("Ciudades");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Coordenadas> ciudades = new ArrayList<>();
                Iterable<DataSnapshot> datos = snapshot.getChildren();
                while (datos.iterator().hasNext()) {
                    DataSnapshot d = datos.iterator().next();
                    Coordenadas r = d.getValue(Coordenadas.class);
                    ciudades.add(r);
                    Log.d("PRUEBA5", ciudades.toString());
                }
                callback.recuperarRutas(ciudades);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface iRecuperarDatos {
        public void recuperarRutas(ArrayList<Coordenadas> lista_ciudades);
    }
}
