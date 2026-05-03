package org.example.proyectotdextraccion;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.proyectotdextraccion.modelo.*;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Interfas extends Application {
    private TabPane tabsETL;

    private ServicioConexion servicio = new ServicioConexion();
    private Timer timer;

    @Override
    public void start(Stage stage) {

        TabPane tabs = new TabPane();

       
       tabs.getTabs().add(new Tab("Dashboard", panelDashboard(tabs)));

        tabs.getTabs().add(new Tab("Vuelos", panelVuelos()));
        tabs.getTabs().add(new Tab("Aeropuertos", panelAeropuertos()));
        tabs.getTabs().add(new Tab("Clima", panelClima()));
        tabs.getTabs().add(new Tab("Aeronaves", panelAeronaves()));
        tabs.getTabs().add(new Tab("ETL", panelETL()));
        tabs.getTabs().add(new Tab("KPI", panelKPI()));

        stage.setScene(new Scene(tabs, 900, 600));
        stage.setTitle("Sistema ETL Aeronáutico");
        stage.show();
    }




private VBox panelDashboard(TabPane tabs) {

    Label titulo = new Label("DASHBOARD AERONÁUTICO");
    titulo.setStyle("""
        -fx-font-size:28;
        -fx-font-weight:bold;
        -fx-text-fill:white;
    """);

    Label subtitulo = new Label("Monitoreo en tiempo real de KPIs operativos");
    subtitulo.setStyle("-fx-text-fill:white; -fx-font-size:14;");

    VBox top = new VBox(5, titulo, subtitulo);
    top.setPadding(new Insets(15));
    top.setStyle("-fx-background-color:#1f2d3d;");

   
    Label totalGeneral = new Label("✈ Total vuelos registrados: 0");
    totalGeneral.setStyle("""
        -fx-font-size:18;
        -fx-font-weight:bold;
        -fx-text-fill:#1f2d3d;
    """);

    
    TextArea detalle = new TextArea();
    detalle.setEditable(false);
    detalle.setWrapText(true);
    detalle.setPrefHeight(230);
    detalle.setStyle("-fx-font-size:14;");

    detalle.setText("Haz clic en cualquier dashboard para ver análisis detallado.");

    Button actualizar = new Button("Actualizar Dashboard");
    actualizar.setStyle("""
        -fx-font-size:14;
        -fx-font-weight:bold;
        -fx-background-color:#1976d2;
        -fx-text-fill:white;
    """);


    //TIPOS DE VUELO
PieChart pie = new PieChart();
pie.setTitle("Tipos de vuelos");

// botón
Button btnVuelos = new Button("Ir a Vuelos");
btnVuelos.setStyle(
    "-fx-font-size:11px;" +
    "-fx-background-color:#1976D2;" +
    "-fx-text-fill:white;" +
    "-fx-background-radius:8;"
);

btnVuelos.setOnAction(e -> {

    tabs.getSelectionModel().select(5); 
    tabsETL.getSelectionModel().select(0); 

});


VBox d1 = crearPanelGrafica(new VBox(10, pie, btnVuelos));

    
    //VELOCIDAD PROMEDIO
NumberAxis x1 = new NumberAxis();
NumberAxis y1 = new NumberAxis();

x1.setLabel("Nivel");
y1.setLabel("Velocidad");

LineChart<Number, Number> line = new LineChart<>(x1, y1);
line.setTitle("Velocidad Promedio");
line.setLegendVisible(false);
line.setAnimated(false);
line.setCreateSymbols(true);
line.setPrefSize(360, 280);

Label lblVelProm = new Label("Velocidad promedio: 0");
lblVelProm.setStyle("""
    -fx-font-size:16;
    -fx-font-weight:bold;
    -fx-text-fill:#1f2d3d;
""");


Button btnVelocidad = new Button("Ir a KPI");
btnVelocidad.setStyle(
        "-fx-font-size:11px;" +
        "-fx-background-color:#1976D2;" +
        "-fx-text-fill:white;" +
        "-fx-background-radius:8;"
);

btnVelocidad.setOnAction(e -> {
    tabs.getSelectionModel().select(6); 
});

VBox d2 = crearPanelGrafica(
        new VBox(10, lblVelProm, line, btnVelocidad)
);
    

   // OPERACIÓN AÉREA 
NumberAxis x2 = new NumberAxis();
CategoryAxis y2 = new CategoryAxis();

BarChart<Number, String> horizontal = new BarChart<>(x2, y2);
horizontal.setTitle("Operación Aérea");
horizontal.setLegendVisible(false);
horizontal.setAnimated(false);
horizontal.setPrefSize(360, 280);

// 
Button btnOperacion = new Button("Ir a Vuelos");
btnOperacion.setStyle(
        "-fx-font-size:11px;" +
        "-fx-background-color:#1976D2;" +
        "-fx-text-fill:white;" +
        "-fx-background-radius:8;"
);


btnOperacion.setOnAction(e -> {
    tabs.getSelectionModel().select(1); 
});

VBox d3 = crearPanelGrafica(
        new VBox(10, horizontal, btnOperacion)
);


    
  // CLIMA 
CategoryAxis x3 = new CategoryAxis();
NumberAxis y3 = new NumberAxis();

BarChart<String, Number> clima = new BarChart<>(x3, y3);
clima.setTitle("Clima");
clima.setLegendVisible(false);
clima.setAnimated(false);
clima.setPrefSize(360, 280);

Button btnClima = new Button("Ir a Clima");
btnClima.setStyle(
        "-fx-font-size:11px;" +
        "-fx-background-color:#1976D2;" +
        "-fx-text-fill:white;" +
        "-fx-background-radius:8;"
);


btnClima.setOnAction(e -> {

    tabs.getSelectionModel().select(5);
    tabsETL.getSelectionModel().select(3);

});

VBox d4 = crearPanelGrafica(
        new VBox(10, clima, btnClima)
);

   // ESTABILIDAD OPERATIVA 
PieChart estabilidad = new PieChart();
estabilidad.setTitle("Estabilidad Operativa");
estabilidad.setLabelsVisible(true);
estabilidad.setPrefSize(360, 280);

Button btnEstabilidad = new Button("Ir a KPI");
btnEstabilidad.setStyle(
        "-fx-font-size:11px;" +
        "-fx-background-color:#1976D2;" +
        "-fx-text-fill:white;" +
        "-fx-background-radius:8;"
);


btnEstabilidad.setOnAction(e -> {
    tabs.getSelectionModel().select(6); 
});


VBox d5 = crearPanelGrafica(
        new VBox(10, estabilidad, btnEstabilidad)
);

   
    // ALERTAS

    Label alertas = new Label("Resumen");
    alertas.setWrapText(true);
    alertas.setStyle("-fx-font-size:14; -fx-padding:15;");

    VBox d6 = new VBox(alertas);
    d6.setPrefSize(370, 300);
    d6.setStyle("""
        -fx-background-color:white;
        -fx-background-radius:12;
        -fx-border-radius:12;
        -fx-border-color:#dcdcdc;
    """);


    // CLICK EVENTOS
 
    pie.setOnMouseClicked(e ->
            detalle.setText("""
TIPOS DE VUELOS

Muestra la distribución entre vuelos nacionales e internacionales.

Si predominan nacionales:
Mayor movilidad interna.

Si predominan internacionales:
Mayor conexión global.

Sirve para planear rutas y demanda.
"""));

    line.setOnMouseClicked(e ->
            detalle.setText("""
VELOCIDAD PROMEDIO

Este dashboard compara la distribución de vuelos con velocidad baja, media y alta,
además de mostrar la velocidad promedio general registrada en el sistema. 
Permite evaluar el ritmo operativo del tráfico aéreo y detectar posibles variaciones en el desempeño.

Interpretación de resultados:

Alta velocidad: Indica operaciones fluidas, rutas despejadas y aeronaves en fase de crucero con buen desempeño.
Velocidad media: Representa condiciones normales de operación y tráfico estable.
Velocidad baja: Puede relacionarse con maniobras de despegue, aterrizaje, espera aérea, congestión o retrasos operativos.
Velocidad promedio general: Resume el comportamiento global del sistema y sirve como referencia para comparar periodos o detectar cambios en la eficiencia operativa.
"""));

    horizontal.setOnMouseClicked(e ->
            detalle.setText("""
OPERACIÓN AÉREA

Este dashboard integra indicadores clave del comportamiento operativo del sistema aéreo, permitiendo analizar el desempeño general de los vuelos, la eficiencia del tráfico y las condiciones de operación en tiempo real.

Incluye los siguientes indicadores:

Altitud promedio: Muestra la altura media a la que operan las aeronaves. Ayuda a identificar si predominan vuelos en ascenso, crucero o descenso.
Frecuencia de registros/seg: Indica la cantidad de datos capturados por segundo. Refleja el nivel de actividad y actualización del sistema.
Congestión aérea: Representa la concentración de vuelos en el espacio monitoreado. Valores altos pueden indicar saturación o mayor tráfico aéreo.
Relación velocidad-altura: Analiza cómo cambia la velocidad respecto a la altitud, útil para evaluar patrones normales de vuelo.

Interpretación de resultados:

Altitud alta y velocidad estable: Operaciones normales en fase de crucero.
Frecuencia alta de registros: Mayor actividad aérea o mejor actualización de datos.
Congestión elevada: Posible saturación del espacio aéreo o incremento de vuelos simultáneos.
Relación velocidad-altura balanceada: Comportamiento operacional eficiente.
Valores atípicos: Pueden indicar retrasos, maniobras especiales o cambios en las condiciones del tráfico aéreo.

"""));

    clima.setOnMouseClicked(e ->
            detalle.setText("""
CLIMA

Este dashboard concentra indicadores meteorológicos relevantes que permiten analizar las condiciones ambientales que pueden influir en las operaciones aéreas, la seguridad de los vuelos y la planificación operativa.

Incluye los siguientes indicadores:

Humedad promedio: Muestra el nivel medio de humedad registrado en las zonas monitoreadas. Puede influir en visibilidad, nubosidad y sensación térmica.
Varianza de temperatura: Indica qué tanto cambian las temperaturas entre regiones o periodos analizados. Valores altos reflejan mayor inestabilidad climática.
Correlación clima (temperatura-humedad): Mide la relación entre temperatura y humedad. Permite identificar si al aumentar una variable también cambia la otra.

Interpretación de resultados:

Humedad alta: Posibles lluvias, nubosidad o menor visibilidad.
Humedad baja: Ambiente más seco y generalmente condiciones más estables.
Varianza baja de temperatura:** Clima uniforme y estable en las zonas analizadas.
Varianza alta: Cambios bruscos de temperatura o diferencias importantes entre regiones.
Correlación positiva: Cuando aumenta la temperatura también tiende a aumentar la humedad.
Correlación negativa: Cuando aumenta la temperatura, la humedad tiende a disminuir.
Correlación cercana a cero: No existe una relación significativa entre ambas variables.

Importancia operativa:

Estos indicadores ayudan a anticipar condiciones climáticas adversas, reducir riesgos operacionales y mejorar la toma de decisiones en vuelos, rutas y monitoreo aeronáutico.

"""));

    estabilidad.setOnMouseClicked(e ->
            detalle.setText("""
ESTABILIDAD OPERATIVA

Este dashboard evalúa el grado de consistencia y control en el comportamiento de las operaciones aéreas, principalmente en variables como la velocidad y desempeño de los vuelos. Permite identificar si el sistema trabaja de forma estable o presenta variaciones importantes.

Incluye los siguientes indicadores:

Coeficiente de variación: Mide la variabilidad relativa respecto al promedio. Permite comparar el nivel de dispersión de los datos en porcentaje.
Desviación estándar: Indica cuánto se alejan los valores reales del promedio general. Mientras menor sea, mayor uniformidad existe.
Nivel de estabilidad: Clasificación general del sistema según los resultados obtenidos (alta, media o baja estabilidad).

Interpretación de resultados:

Coeficiente de variación bajo: Operaciones consistentes y controladas.
Coeficiente de variación alto: Cambios frecuentes en el comportamiento operativo.
Desviación estándar baja: Los vuelos mantienen valores cercanos al promedio esperado.
Desviación estándar alta: Existe mayor dispersión y posibles irregularidades operativas.
Alta estabilidad: Sistema eficiente, predecible y con buen control operacional.
Media estabilidad: Funcionamiento normal con ligeras variaciones.
Baja estabilidad: Posibles retrasos, saturación, cambios bruscos o problemas operativos.

Importancia operativa:

Menores niveles de variación reflejan mejor planeación, control y eficiencia en las operaciones aéreas, facilitando la toma de decisiones y reduciendo riesgos.

"""));

    // BOTON ACTUALIZAR
   
    actualizar.setOnAction(e -> {

        new Thread(() -> {

            double nac = servicio.kpiPorcentajeNacionales();
            double inter = servicio.kpiPorcentajeInternacionales();

            double vel = servicio.kpiVelocidadPromedio();
            double alt = servicio.kpiAltitudPromedio();

            double hum = servicio.kpiHumedadPromedio();
            double freq = servicio.kpiFrecuenciaActualizacion();

            double cv = servicio.kpiCoeficienteVariacion();
            double cong = servicio.kpiCongestion();

            double rel = servicio.kpiRelacionVelAlt();
            double corr = servicio.kpiCorrelacionTempHum();

            double var = servicio.kpiVarianzaTemperatura();
            double desv = servicio.kpiDesviacionVelocidad();

            long total = servicio.totalVuelos();

            long n = Math.round(total * nac / 100);
            long i = total - n;

            Platform.runLater(() -> {

                totalGeneral.setText("✈ Total vuelos registrados: " + total);
                lblVelProm.setText("Velocidad promedio: " +
                String.format("%.2f", vel) + " km/h");

                pie.getData().clear();
                pie.getData().addAll(
                        new PieChart.Data("Nacional\n" +
                                String.format("%.1f%%", nac) +
                                "\n(" + n + ")", nac),

                        new PieChart.Data("Internacional\n" +
                                String.format("%.1f%%", inter) +
                                "\n(" + i + ")", inter)
                );


                line.getData().clear();

                XYChart.Series<Number, Number> s1 = new XYChart.Series<>();

                XYChart.Data<Number, Number> p1 = new XYChart.Data<>(1, vel - 30);
                XYChart.Data<Number, Number> p2 = new XYChart.Data<>(2, vel);
                XYChart.Data<Number, Number> p3 = new XYChart.Data<>(3, vel + 30);
                XYChart.Data<Number, Number> p4 = new XYChart.Data<>(4, vel);

                s1.getData().addAll(p1,p2,p3,p4);
                line.getData().add(s1);

                mostrarValorPunto(p1, String.format("%.0f", vel - 30));
                mostrarValorPunto(p2, String.format("%.0f", vel));
                mostrarValorPunto(p3, String.format("%.0f", vel + 30));
                mostrarValorPunto(p4, "PROM\n" + String.format("%.0f", vel));

                horizontal.getData().clear();

                XYChart.Series<Number,String> s2 = new XYChart.Series<>();
                s2.getData().add(new XYChart.Data<>(alt, "Altitud"));
                s2.getData().add(new XYChart.Data<>(freq, "Frecuencia"));
                s2.getData().add(new XYChart.Data<>(cong*100000, "Congestión"));
                s2.getData().add(new XYChart.Data<>(rel*100, "Relación"));

                horizontal.getData().add(s2);

                clima.getData().clear();

                XYChart.Series<String,Number> s3 = new XYChart.Series<>();
                s3.getData().add(new XYChart.Data<>("Humedad", hum));
                s3.getData().add(new XYChart.Data<>("Varianza", var));
                s3.getData().add(new XYChart.Data<>("Correlación", corr*100));

                clima.getData().add(s3);

                estabilidad.getData().clear();

                estabilidad.getData().addAll(
                        new PieChart.Data("Coef.Var\n" +
                                String.format("%.1f%%", cv), cv),

                        new PieChart.Data("Desv.Std\n" +
                                String.format("%.1f", desv), desv),

                        new PieChart.Data("Estable\n" +
                                String.format("%.1f%%", 100-cv),
                                Math.max(1,100-cv))
                );


                String txt = "";

                txt += "RESUMEN\n\n";
                txt += "Total vuelos: " + total + "\n\n";
                txt += nac > inter ?
                        "✔ Predomina operación nacional\n\n" :
                        "✔ Predomina operación internacional\n\n";

                txt += cong < 0.001 ?
                        "✔ Congestión baja\n\n" :
                        "⚠ Congestión elevada\n\n";

                txt += hum > 70 ?
                        "⚠ Humedad alta\n\n" :
                        "✔ Humedad normal\n\n";

                txt += cv < 20 ?
                        "✔ Sistema estable\n\n" :
                        "⚠ Alta variabilidad\n\n";

                txt += "Velocidad promedio: " +
                        String.format("%.2f", vel);

                alertas.setText(txt);

            });

        }).start();
    });

    // GRID
  
    GridPane grid = new GridPane();
grid.setHgap(18);
grid.setVgap(18);

grid.add(d1,0,0);
grid.add(d2,1,0);
grid.add(d3,2,0);

grid.add(d4,0,1);
grid.add(d5,1,1);
grid.add(d6,2,1);

VBox panelDerecho = new VBox(15, detalle);

panelDerecho.setPrefWidth(360);
panelDerecho.setMinWidth(360);

detalle.setPrefHeight(650);

panelDerecho.setPadding(new Insets(5));


// IZQUIERDA = DASHBOARDS
VBox izquierda = new VBox(15,
        actualizar,
        totalGeneral,
        grid
);

   
// CENTRO GENERAL
HBox centro = new HBox(20, izquierda, panelDerecho);
HBox.setHgrow(izquierda, Priority.ALWAYS);


// CONTENEDOR GENERAL
VBox cont = new VBox(15,
        top,
        centro
);

cont.setPadding(new Insets(15));
cont.setStyle("-fx-background-color:#f4f6fa;");

ScrollPane scroll = new ScrollPane(cont);
scroll.setFitToWidth(true);
scroll.setFitToHeight(true);

return new VBox(scroll);
}

private VBox crearPanelGrafica(javafx.scene.Node grafica) {

    VBox box = new VBox(grafica);

    box.setPadding(new Insets(10));
    box.setSpacing(10);

    box.setPrefSize(370, 300);

    box.setStyle("""
        -fx-background-color:white;
        -fx-background-radius:12;
        -fx-border-radius:12;
        -fx-border-color:#dcdcdc;
        -fx-effect:dropshadow(gaussian, rgba(0,0,0,0.10),8,0,0,2);
    """);

    return box;
}

private void mostrarValorPunto(XYChart.Data<Number, Number> data, String texto) {

    Platform.runLater(() -> {

        Label label = new Label(texto);

        label.setStyle("""
            -fx-font-size:11;
            -fx-font-weight:bold;
            -fx-background-color:white;
            -fx-padding:2;
        """);

        StackPane stack = new StackPane();
        stack.getChildren().add(label);

        data.setNode(stack);
    });
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
            }, 0, 60000);
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

    tabsETL = new TabPane();



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

    private VBox panelKPI() {

        Button btnCalcular = new Button("Calcular KPI");

        Label lblVel = new Label("Velocidad Promedio:");
        Label lblAlt = new Label("Altitud Promedio:");
        Label lblNac = new Label("% Nacionales:");
        Label lblInt = new Label("% Internacionales:");
        Label lblAltaVel = new Label("% Alta Velocidad:");
        Label lblHum = new Label("Humedad Promedio:");
        Label lblFreq = new Label("Frecuencia:");
        Label lblDesv = new Label("Desviación Velocidad:");
        Label lblCV = new Label("Coeficiente Variación:");
        Label lblCong = new Label("Congestión:");
        Label lblRel = new Label("Relación Vel/Alt:");
        Label lblCorr = new Label("Correlación Temp-Hum:");
        Label lblVar = new Label("Varianza Temperatura:");

        btnCalcular.setOnAction(e -> {

            new Thread(() -> {

                double vel = servicio.kpiVelocidadPromedio();
                double alt = servicio.kpiAltitudPromedio();
                double nac = servicio.kpiPorcentajeNacionales();
                double inter = servicio.kpiPorcentajeInternacionales();
                double altaVel = servicio.kpiAltaVelocidad();
                double hum = servicio.kpiHumedadPromedio();
                double freq = servicio.kpiFrecuenciaActualizacion();
                double desv = servicio.kpiDesviacionVelocidad();
                double cv = servicio.kpiCoeficienteVariacion();
                double cong = servicio.kpiCongestion();
                double rel = servicio.kpiRelacionVelAlt();
                double corr = servicio.kpiCorrelacionTempHum();
                double var = servicio.kpiVarianzaTemperatura();

                Platform.runLater(() -> {

                    lblVel.setText("Velocidad Promedio: " + String.format("%.2f", vel));
                    lblAlt.setText("Altitud Promedio: " + String.format("%.2f", alt));

                    lblNac.setText("% Nacionales: " + String.format("%.2f", nac) + "%");
                    lblInt.setText("% Internacionales: " + String.format("%.2f", inter) + "%");

                    lblAltaVel.setText("% Alta Velocidad: " + String.format("%.2f", altaVel) + "%");

                    lblHum.setText("Humedad Promedio: " + String.format("%.2f", hum));

                    lblFreq.setText("Frecuencia: " + String.format("%.2f", freq) + " registros/seg");

                    lblDesv.setText("Desviación Velocidad: " + String.format("%.2f", desv));

                    lblCV.setText("Coeficiente Variación: " + String.format("%.2f", cv) + "%");

                    lblCong.setText("Congestión: " + String.format("%.6f", cong));

                    lblRel.setText("Relación Vel/Alt: " + String.format("%.4f", rel));

                    lblCorr.setText("Correlación Temp-Hum: " + String.format("%.4f", corr));

                    lblVar.setText("Varianza Temperatura: " + String.format("%.2f", var));

                });

            }).start();
        });

        VBox layout = new VBox(10,
                btnCalcular,
                lblVel,
                lblAlt,
                lblNac,
                lblInt,
                lblAltaVel,
                lblHum,
                lblFreq,
                lblDesv,
                lblCV,
                lblCong,
                lblRel,
                lblCorr,
                lblVar
        );

        layout.setPadding(new Insets(10));

        return layout;
    }


    public static void main(String[] args) {
        launch();
    }
}