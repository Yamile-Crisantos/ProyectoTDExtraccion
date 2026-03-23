package org.example.proyectotdextraccion.modelo;

import javafx.beans.property.*;

public class Aeropuerto {

    private StringProperty nombre;
    private StringProperty ciudad;
    private StringProperty icao;
    private StringProperty tipo;

    public Aeropuerto(String nombre, String ciudad, String icao) {
        this(nombre, ciudad, icao, "");
    }

    public Aeropuerto(String nombre, String ciudad, String icao, String tipo) {
        this.nombre = new SimpleStringProperty(nombre);
        this.ciudad = new SimpleStringProperty(ciudad);
        this.icao = new SimpleStringProperty(icao);
        this.tipo = new SimpleStringProperty(tipo);
    }

    public StringProperty nombreProperty() { return nombre; }
    public StringProperty ciudadProperty() { return ciudad; }
    public StringProperty icaoProperty() { return icao; }
    public StringProperty tipoProperty() { return tipo; }
}