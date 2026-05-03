package org.example.proyectotdextraccion.modelo;

import javafx.beans.property.*;

public class Vuelo {

    private StringProperty icao24;
    private StringProperty callsign;

    private DoubleProperty longitud;
    private DoubleProperty latitud;
    private DoubleProperty altitud;
    private DoubleProperty velocidad;

    private StringProperty tipoVuelo;
    private StringProperty aerolinea;
    private StringProperty nivel;
    private StringProperty categoriaVelocidad;
    private StringProperty origen;

    private StringProperty fechaHora;

    public Vuelo(String icao24, String callsign, double lon, double lat, double alt, double vel) {
        this(icao24, callsign, lon, lat, alt, vel, "", "", "", "", "", "");
    }

    public Vuelo(String icao24, String callsign,
                 double lon, double lat, double alt, double vel,
                 String tipoVuelo, String aerolinea,
                 String nivel, String categoriaVelocidad,
                 String origen, String fechaHora) {

        this.icao24 = new SimpleStringProperty(icao24);
        this.callsign = new SimpleStringProperty(callsign);

        this.longitud = new SimpleDoubleProperty(lon);
        this.latitud = new SimpleDoubleProperty(lat);
        this.altitud = new SimpleDoubleProperty(alt);
        this.velocidad = new SimpleDoubleProperty(vel);

        this.tipoVuelo = new SimpleStringProperty(tipoVuelo);
        this.aerolinea = new SimpleStringProperty(aerolinea);
        this.nivel = new SimpleStringProperty(nivel);
        this.categoriaVelocidad = new SimpleStringProperty(categoriaVelocidad);
        this.origen = new SimpleStringProperty(origen);

        this.fechaHora = new SimpleStringProperty(fechaHora);
    }

    public StringProperty icao24Property() { return icao24; }
    public StringProperty callsignProperty() { return callsign; }
    public DoubleProperty longitudProperty() { return longitud; }
    public DoubleProperty latitudProperty() { return latitud; }
    public DoubleProperty altitudProperty() { return altitud; }
    public DoubleProperty velocidadProperty() { return velocidad; }

    public StringProperty tipoVueloProperty() { return tipoVuelo; }
    public StringProperty aerolineaProperty() { return aerolinea; }
    public StringProperty nivelProperty() { return nivel; }
    public StringProperty categoriaVelocidadProperty() { return categoriaVelocidad; }
    public StringProperty origenProperty() { return origen; }
    public StringProperty fechaHoraProperty() { return fechaHora; }
}