package org.example.proyectotdextraccion;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.proyectotdextraccion.modelo.*;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Interfas extends Application {

    private ServicioConexion servicio = new ServicioConexion();
    private Timer timer;

    @Override
    public void start(Stage stage) {

        TabPane tabs = new TabPane();

        tabs.getTabs().add(new Tab("Vuelos", panelVuelos()));
        tabs.getTabs().add(new Tab("Aeropuertos", panelAeropuertos()));
        tabs.getTabs().add(new Tab("Clima", panelClima()));
        tabs.getTabs().add(new Tab("Aeronaves", panelAeronaves()));
        tabs.getTabs().add(new Tab("ETL", panelETL()));

        stage.setScene(new Scene(tabs, 900, 600));
        stage.setTitle("Sistema ETL Aeronáutico");
        stage.show();
    }

    // ===================== VUELOS =====================

    private VBox panelVuelos() {

        TableView<Vuelo> tabla = new TableView<>();
        configurarTablaVuelos(tabla);

        Label contador = new Label("Vuelos: 0");

        Button btnConsultar = new Button("Consultar Vuelos");
        Button btnAuto = new Button("Auto 10s");
        Button btnStop = new Button("Stop");

        btnConsultar.setOnAction(e -> cargarVuelos(tabla, contador));

        btnAuto.setOnAction(e -> {
            if (timer != null) timer.cancel();

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    cargarVuelos(tabla, contador);
                }
            }, 0, 10000);
        });

        btnStop.setOnAction(e -> {
            if (timer != null) timer.cancel();
        });

        HBox botones = new HBox(10, btnConsultar, btnAuto, btnStop, contador);
        botones.setPadding(new Insets(10));

        return new VBox(botones, tabla);
    }

    private void cargarVuelos(TableView<Vuelo> tabla, Label contador) {

        servicio.obtenerVuelosOpenSky().thenAccept(lista -> {

            Platform.runLater(() -> {
                tabla.setItems(FXCollections.observableArrayList(lista));
                contador.setText("Vuelos: " + lista.size());
            });

        });
    }

    private void configurarTablaVuelos(TableView<Vuelo> tabla) {

        TableColumn<Vuelo, String> c1 = new TableColumn<>("ICAO");
        c1.setCellValueFactory(d -> d.getValue().icao24Property());

        TableColumn<Vuelo, String> c2 = new TableColumn<>("Callsign");
        c2.setCellValueFactory(d -> d.getValue().callsignProperty());

        TableColumn<Vuelo, Number> c3 = new TableColumn<>("Longitud");
        c3.setCellValueFactory(d -> d.getValue().longitudProperty());

        TableColumn<Vuelo, Number> c4 = new TableColumn<>("Latitud");
        c4.setCellValueFactory(d -> d.getValue().latitudProperty());

        TableColumn<Vuelo, Number> c5 = new TableColumn<>("Altitud");
        c5.setCellValueFactory(d -> d.getValue().altitudProperty());

        TableColumn<Vuelo, Number> c6 = new TableColumn<>("Velocidad");

        c6.setCellValueFactory(d -> d.getValue().velocidadProperty());

        TableColumn<Vuelo, String> c7 = new TableColumn<>("Tipo");
        c7.setCellValueFactory(d -> d.getValue().tipoVueloProperty());

        TableColumn<Vuelo, String> c8 = new TableColumn<>("Aerolínea");
        c8.setCellValueFactory(d -> d.getValue().aerolineaProperty());

        TableColumn<Vuelo, String> c9 = new TableColumn<>("Nivel");
        c9.setCellValueFactory(d -> d.getValue().nivelProperty());

        TableColumn<Vuelo, String> c10 = new TableColumn<>("Velocidad Cat");
        c10.setCellValueFactory(d -> d.getValue().categoriaVelocidadProperty());

        TableColumn<Vuelo, String> c11 = new TableColumn<>("Origen");
        c11.setCellValueFactory(d -> d.getValue().origenProperty());

        TableColumn<Vuelo, String> c12 = new TableColumn<>("Fecha");
        c12.setCellValueFactory(d -> d.getValue().fechaHoraProperty());

        tabla.getColumns().addAll(c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12);
    }

    // ===================== AEROPUERTOS =====================

    private VBox panelAeropuertos() {

        TableView<Aeropuerto> tabla = new TableView<>();
        configurarTablaAeropuertos(tabla);

        Button btn = new Button("Cargar Aeropuertos");

        btn.setOnAction(e ->
                servicio.obtenerAeropuertosAFAC().thenAccept(lista ->
                        Platform.runLater(() ->
                                tabla.setItems(FXCollections.observableArrayList(lista))
                        )
                )
        );

        return new VBox(btn, tabla);
    }

    private void configurarTablaAeropuertos(TableView<Aeropuerto> tabla) {

        TableColumn<Aeropuerto, String> c1 = new TableColumn<>("Nombre");
        c1.setCellValueFactory(d -> d.getValue().nombreProperty());

        TableColumn<Aeropuerto, String> c2 = new TableColumn<>("Ciudad");
        c2.setCellValueFactory(d -> d.getValue().ciudadProperty());

        TableColumn<Aeropuerto, String> c3 = new TableColumn<>("ICAO");
        c3.setCellValueFactory(d -> d.getValue().icaoProperty());

        TableColumn<Aeropuerto, String> c4 = new TableColumn<>("Tipo");
        c4.setCellValueFactory(d -> d.getValue().tipoProperty());

        TableColumn<Aeropuerto, String> c5 = new TableColumn<>("Fecha");
        c5.setCellValueFactory(d -> d.getValue().fechaHoraProperty());

        tabla.getColumns().addAll(c1,c2,c3,c4,c5);
    }

    // ===================== CLIMA =====================

    private VBox panelClima() {

        TableView<ClimaEstado> tabla = new TableView<>();
        configurarTablaClima(tabla);

        Button btn = new Button("Cargar Clima");

        btn.setOnAction(e ->
                servicio.obtenerClimaEstados().thenAccept(lista ->
                        Platform.runLater(() ->
                                tabla.setItems(FXCollections.observableArrayList(lista))
                        )
                )
        );

        return new VBox(btn, tabla);
    }

    private void configurarTablaClima(TableView<ClimaEstado> tabla) {

        TableColumn<ClimaEstado, String> c1 = new TableColumn<>("Estado");
        c1.setCellValueFactory(d -> d.getValue().estadoProperty());

        TableColumn<ClimaEstado, Number> c2 = new TableColumn<>("Temp");
        c2.setCellValueFactory(d -> d.getValue().temperaturaProperty());

        TableColumn<ClimaEstado, Number> c3 = new TableColumn<>("Humedad");
        c3.setCellValueFactory(d -> d.getValue().humedadProperty());

        TableColumn<ClimaEstado, String> c4 = new TableColumn<>("Clima");
        c4.setCellValueFactory(d -> d.getValue().tipoClimaProperty());

        TableColumn<ClimaEstado, String> c5 = new TableColumn<>("Nivel Humedad");
        c5.setCellValueFactory(d -> d.getValue().nivelHumedadProperty());

        TableColumn<ClimaEstado, String> c6 = new TableColumn<>("Fecha");
        c6.setCellValueFactory(d -> d.getValue().fechaHoraProperty());

        tabla.getColumns().addAll(c1,c2,c3,c4,c5,c6);

    }

    // ===================== AERONAVES =====================

    private VBox panelAeronaves() {

        TableView<Aeronave> tabla = new TableView<>();
        configurarTablaAeronaves(tabla);

        Button btn = new Button("Cargar Aeronaves");

        btn.setOnAction(e ->
                servicio.obtenerBaseAeronaves().thenAccept(lista ->
                        Platform.runLater(() ->
                                tabla.setItems(FXCollections.observableArrayList(lista))
                        )
                )
        );

        return new VBox(btn, tabla);
    }

    private void configurarTablaAeronaves(TableView<Aeronave> tabla) {

        TableColumn<Aeronave, String> c1 = new TableColumn<>("ICAO");
        c1.setCellValueFactory(d -> d.getValue().icaoProperty());

        TableColumn<Aeronave, String> c2 = new TableColumn<>("Estado");
        c2.setCellValueFactory(d -> d.getValue().estadoProperty());

        TableColumn<Aeronave, String> c3 = new TableColumn<>("Operador");
        c3.setCellValueFactory(d -> d.getValue().operadorProperty());

        TableColumn<Aeronave, String> c4 = new TableColumn<>("Tipo Operación");
        c4.setCellValueFactory(d -> d.getValue().tipoOperacionProperty());

        TableColumn<Aeronave, String> c5 = new TableColumn<>("Fecha");
        c5.setCellValueFactory(d -> d.getValue().fechaHoraProperty());

        tabla.getColumns().addAll(c1,c2,c3,c4,c5);
    }

    // ===================== ETL =====================

    private VBox panelETL() {

        TabPane tabs = new TabPane();

        // ================= VUELOS =================
        TableView<Vuelo> tv = new TableView<>();
        configurarTablaVuelos(tv);

        Button b1 = new Button("Transformar Vuelos");
        b1.setOnAction(e ->
                servicio.obtenerVuelosOpenSky().thenAccept(lista -> {

                    var transformados = servicio.transformarVuelos(lista);

                    servicio.guardarVuelosMongo(transformados);


                    Platform.runLater(() ->
                            tv.setItems(FXCollections.observableArrayList(transformados))
                    );
                })
        );

        // ================= CLIMA =================
        TableView<ClimaEstado> tc = new TableView<>();
        configurarTablaClima(tc);

        Button b2 = new Button("Transformar Clima");
        b2.setOnAction(e ->
                servicio.obtenerClimaEstados().thenAccept(lista -> {

                    var transformados = servicio.transformarClima(lista);

                    servicio.guardarClimaMongo(transformados); // 🔥

                    Platform.runLater(() ->
                            tc.setItems(FXCollections.observableArrayList(transformados))
                    );
                })
        );

        // ================= AEROPUERTOS =================
        TableView<Aeropuerto> ta = new TableView<>();
        configurarTablaAeropuertos(ta);

        Button b3 = new Button("Transformar Aeropuertos");
        b3.setOnAction(e ->
                servicio.obtenerAeropuertosAFAC().thenAccept(lista -> {

                    var transformados = servicio.transformarAeropuertos(lista);

                    servicio.guardarAeropuertosMongo(transformados); // 🔥

                    Platform.runLater(() ->
                            ta.setItems(FXCollections.observableArrayList(transformados))
                    );
                })
        );

        // ================= AERONAVES =================
        TableView<Aeronave> tn = new TableView<>();
        configurarTablaAeronaves(tn);

        Button b4 = new Button("Transformar Aeronaves");
        b4.setOnAction(e ->
                servicio.obtenerBaseAeronaves().thenAccept(lista -> {

                    var transformados = servicio.transformarAeronaves(lista);

                    servicio.guardarAeronavesMongo(transformados); // 🔥

                    Platform.runLater(() ->
                            tn.setItems(FXCollections.observableArrayList(transformados))
                    );
                })
        );

        tabs.getTabs().addAll(
                new Tab("Vuelos", new VBox(b1, tv)),
                new Tab("Clima", new VBox(b2, tc)),
                new Tab("Aeropuertos", new VBox(b3, ta)),
                new Tab("Aeronaves", new VBox(b4, tn))
        );

        return new VBox(tabs);
    }

    public static void main(String[] args) {
        launch();
    }
}