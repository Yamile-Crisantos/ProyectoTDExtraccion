package org.example.proyectotdextraccion.modelo;

import javafx.beans.property.*;

public class Aeronave {

    private StringProperty icao;
    private StringProperty estado;
    private StringProperty operador;
    private StringProperty tipoOperacion;

    private StringProperty fechaHora;

    public Aeronave(String icao, String estado, String operador) {
        this(icao, estado, operador, "", "");
    }

    public Aeronave(String icao, String estado, String operador, String tipoOperacion, String fechaHora) {
        this.icao = new SimpleStringProperty(icao);
        this.estado = new SimpleStringProperty(estado);
        this.operador = new SimpleStringProperty(operador);
        this.tipoOperacion = new SimpleStringProperty(tipoOperacion);
        this.fechaHora = new SimpleStringProperty(fechaHora);
    }

    public StringProperty icaoProperty() { return icao; }
    public StringProperty estadoProperty() { return estado; }
    public StringProperty operadorProperty() { return operador; }
    public StringProperty tipoOperacionProperty() { return tipoOperacion; }
    public StringProperty fechaHoraProperty() { return fechaHora; }
}