package org.example.proyectotdextraccion.modelo;

import javafx.beans.property.*;

public class ClimaEstado {
    private StringProperty estado;
    private DoubleProperty temperatura;
    private IntegerProperty humedad;
    public ClimaEstado(String estado, double temperatura, int humedad) {
        this.estado = new SimpleStringProperty(estado);
        this.temperatura = new SimpleDoubleProperty(temperatura);
        this.humedad = new SimpleIntegerProperty(humedad);
    }
    public StringProperty estadoProperty() { return estado; }
    public DoubleProperty temperaturaProperty() { return temperatura; }
    public IntegerProperty humedadProperty() { return humedad; }
}