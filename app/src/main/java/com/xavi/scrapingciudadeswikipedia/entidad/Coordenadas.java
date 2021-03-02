package com.xavi.scrapingciudadeswikipedia.entidad;

import com.xavi.scrapingciudadeswikipedia.MapsActivity;

import org.jsoup.nodes.Element;

public class Coordenadas {
    public com.xavi.scrapingciudadeswikipedia.MapsActivity MapsActivity;
    private String latitude;
    private String longitude;

    public Coordenadas(String latitud, String longitude) {
    }

    public Coordenadas(Element elemento_imagen, Element longitude) {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Corodenadas{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
