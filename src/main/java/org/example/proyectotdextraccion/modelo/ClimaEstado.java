package org.example.proyectotdextraccion.modelo;

import javafx.beans.property.*;

public class ClimaEstado {

    private StringProperty estado;
    private DoubleProperty temperatura;
    private IntegerProperty humedad;

    private StringProperty tipoClima;
    private StringProperty nivelHumedad;

    public ClimaEstado(String estado, double temp, int hum) {
        this(estado, temp, hum, "", "");
    }

    public ClimaEstado(String estado, double temp, int hum,
                       String tipoClima, String nivelHumedad) {

        this.estado = new SimpleStringProperty(estado);
        this.temperatura = new SimpleDoubleProperty(temp);
        this.humedad = new SimpleIntegerProperty(hum);

        this.tipoClima = new SimpleStringProperty(tipoClima);
        this.nivelHumedad = new SimpleStringProperty(nivelHumedad);
    }

    public StringProperty estadoProperty() { return estado; }
    public DoubleProperty temperaturaProperty() { return temperatura; }
    public IntegerProperty humedadProperty() { return humedad; }

    public StringProperty tipoClimaProperty() { return tipoClima; }
    public StringProperty nivelHumedadProperty() { return nivelHumedad; }
}