package com.xavi.scrapingciudadeswikipedia.entidad;

import com.google.android.gms.maps.model.LatLng;
import com.xavi.scrapingciudadeswikipedia.MapsActivity;

import org.jsoup.nodes.Element;

public class Coordenadas {
    private double latitude;
    private double longitude;
    private String nombre;

    public Coordenadas() {
        super();
    }


    public Coordenadas(double latitude, double longitude, String nombre) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.nombre = nombre;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
