package org.example.proyectotdextraccion.modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Aeronave {

    private StringProperty icao;
    private StringProperty estado;
    private StringProperty operador;

    public Aeronave(String icao, String estado, String operador) {
        this.icao = new SimpleStringProperty(icao);
        this.estado = new SimpleStringProperty(estado);
        this.operador = new SimpleStringProperty(operador);
    }

    public StringProperty icaoProperty() {
        return icao;
    }

    public StringProperty estadoProperty() {
        return estado;
    }

    public StringProperty operadorProperty() {
        return operador;
    }

    public String getIcao() {
        return icao.get();
    }

    public String getEstado() {
        return estado.get();
    }

    public String getOperador() {
        return operador.get();
    }

    public void setIcao(String icao) {
        this.icao.set(icao);
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    public void setOperador(String operador) {
        this.operador.set(operador);
    }
}