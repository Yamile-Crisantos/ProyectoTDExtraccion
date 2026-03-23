package org.example.proyectotdextraccion.modelo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Vuelo {

    private StringProperty icao24;
    private StringProperty pais;

    private DoubleProperty longitud;
    private DoubleProperty latitud;
    private DoubleProperty altitud;

    private DoubleProperty velocidad;

    public Vuelo(String icao24, String pais, double lon, double lat, double alt, double vel) {

        this.icao24 = new SimpleStringProperty(icao24);
        this.pais = new SimpleStringProperty(pais);

        this.longitud = new SimpleDoubleProperty(lon);
        this.latitud = new SimpleDoubleProperty(lat);
        this.altitud = new SimpleDoubleProperty(alt);

        this.velocidad = new SimpleDoubleProperty(vel);
    }

    public StringProperty icao24Property() {
        return icao24;
    }

    public StringProperty paisProperty() {
        return pais;
    }

    public DoubleProperty longitudProperty() {
        return longitud;
    }

    public DoubleProperty latitudProperty() {
        return latitud;
    }

    public DoubleProperty altitudProperty() {
        return altitud;
    }

    public DoubleProperty velocidadProperty() {
        return velocidad;
    }
}