package org.example.proyectotdextraccion.modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Aeropuerto {
    private StringProperty nombre, ciudad, icao;
    public Aeropuerto(String nombre, String ciudad, String icao) {
        this.nombre = new SimpleStringProperty(nombre);
        this.ciudad = new SimpleStringProperty(ciudad);
        this.icao = new SimpleStringProperty(icao);
    }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty ciudadProperty() { return ciudad; }
    public StringProperty icaoProperty() { return icao; }
}