package com.xavi.scrapingciudadeswikipedia.recursos;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.xavi.scrapingciudadeswikipedia.MapsActivity;
import com.xavi.scrapingciudadeswikipedia.entidad.Coordenadas;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AccesoDatos implements Runnable {
    private static LatLng coord;
    private String ciudad;
    private Handler puente;
    private pedirDatos llamante;

    public AccesoDatos(String ciudad, Handler puente, pedirDatos llamante) {
        this.ciudad = ciudad;
        this.puente = puente;
        this.llamante = llamante;
    }

    public LatLng datoWeb() {
        ArrayList<Coordenadas> lista_posiciones = new ArrayList<>();

        try {
            String ruta = "http://es.wikipedia.org/wiki/" + ciudad;
            Document documento = Jsoup.connect(ruta).get();


            Elements latitud = documento.getElementsByClass("latitude");
            Elements longitud = documento.getElementsByClass("longitude");


            String[] lat = latitud.get(0).text().split("[\"°\"″′]");
            String[] longi = longitud.get(0).text().split("[\"°\"″′]");
            double lati = Double.parseDouble(lat[0]) + (Double.parseDouble(lat[1]) / 60)
                    + (Double.parseDouble(lat[2]) / 3600);
            double lon = Double.parseDouble(longi[0]) + (Double.parseDouble(longi[1]) / 60)
                    + (Double.parseDouble(longi[2]) / 3600);
            if (lat[3].equals("S")) {
                lati = -lati;
            }
            if (longi[3].equals("O")) {
                lon = -lon;
            }
            coord = new LatLng(lati, lon);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            coord = null;
        }

        return coord;
    }

    @Override
    public void run() {
        coord = datoWeb();
        puente.post(new Runnable() {
            @Override
            public void run() {
                llamante.avisadordeDatos(coord);

            }
        });
    }

    public interface pedirDatos {
        public void avisadordeDatos(LatLng coord);
    }

}

