package org.example.proyectotdextraccion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.proyectotdextraccion.modelo.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class ServicioConexion {

    private HttpClient client = HttpClient.newHttpClient();
    private ObjectMapper mapper = new ObjectMapper();

    private static final String WEATHER_API_KEY = "a94c61e44589eab23d70d594f3285c13";
    private static final String AIRPORTS_URL = "https://davidmegginson.github.io/ourairports-data/airports.csv";

    // ===================== VUELOS (OpenSky) =====================

    public CompletableFuture<List<Vuelo>> obtenerVuelosOpenSky() {

        double[][] zonas = {
                {14, -118, 20, -100},   // sur
                {20, -118, 26, -100},   // centro
                {26, -118, 33, -100},   // norte
                {14, -100, 20, -86},    // sureste
                {20, -100, 26, -86},    // este
                {26, -100, 33, -86}     // noreste
        };

        List<CompletableFuture<List<Vuelo>>> futures = new ArrayList<>();

        for (double[] z : zonas) {

            String url = String.format(
                    "https://opensky-network.org/api/states/all?lamin=%s&lomin=%s&lamax=%s&lomax=%s",
                    z[0], z[1], z[2], z[3]
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            CompletableFuture<List<Vuelo>> future = client
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(resp -> {

                        List<Vuelo> lista = new ArrayList<>();

                        try {

                            JsonNode root = mapper.readTree(resp.body());
                            JsonNode states = root.get("states");

                            if (states != null) {

                                for (JsonNode s : states) {

                                    String icao24 = s.get(0).asText("N/A").toUpperCase();
                                    String callsign = s.get(1).asText("").trim();
                                    double lon = s.get(5).asDouble(0);
                                    double lat = s.get(6).asDouble(0);
                                    double alt = s.get(7).asDouble(0);
                                    double velocidad = s.get(9).asDouble(0);

                                    if (lat != 0 && lon != 0 && !callsign.isBlank()) {
                                        lista.add(new Vuelo(icao24, callsign, lon, lat, alt, velocidad));
                                    }
                                }
                            }

                        } catch (Exception ignored) {}

                        return lista;
                    });

            futures.add(future);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {

                    Map<String, Vuelo> mapa = new HashMap<>();

                    for (CompletableFuture<List<Vuelo>> f : futures) {
                        for (Vuelo vuelo : f.join()) {
                            mapa.put(vuelo.icao24Property().get(), vuelo);
                        }
                    }

                    return new ArrayList<>(mapa.values());
                });
    }

    // ===================== AEROPUERTOS =====================

    public CompletableFuture<List<Aeropuerto>> obtenerAeropuertosAFAC() {

        return client.sendAsync(
                HttpRequest.newBuilder().uri(URI.create(AIRPORTS_URL)).GET().build(),
                HttpResponse.BodyHandlers.ofString()
        ).thenApply(resp -> {

            List<Aeropuerto> lista = new ArrayList<>();
            String[] lineas = resp.body().split("\n");

            for (int i = 1; i < lineas.length; i++) {

                String[] col = lineas[i].split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (col.length > 10) {

                    String pais = col[8].replace("\"", "").trim();

                    // SOLO MEXICO
                    if (pais.equals("MX")) {

                        String nombre = col[3].replace("\"", "");
                        String ciudad = col[10].replace("\"", "");
                        String icao = col[1].replace("\"", "");
                        String tipoRaw = col[2].replace("\"", "");

                        String tipo;

                        switch (tipoRaw) {

                            case "large_airport":
                            case "medium_airport":
                                tipo = "PUBLICO";
                                break;

                            case "small_airport":
                                tipo = "PRIVADO";
                                break;

                            case "heliport":
                                tipo = "HELIPUERTO";
                                break;

                            default:
                                tipo = "OTRO";
                        }

                        // mostramos tipo dentro de la ciudad
                        ciudad = ciudad + " (" + tipo + ")";

                        lista.add(new Aeropuerto(nombre, ciudad, icao));
                    }
                }
            }

            return lista;
        });
    }

    // ===================== CLIMA =====================

    public CompletableFuture<List<ClimaEstado>> obtenerClimaEstados() {

        List<String> estadosMx = List.of(
                "Aguascalientes","Baja California","Baja California Sur","Campeche","Chiapas",
                "Chihuahua","Coahuila","Colima","Ciudad de Mexico","Durango",
                "Guanajuato","Guerrero","Hidalgo","Jalisco","Estado de Mexico","Michoacan",
                "Morelos","Nayarit","Nuevo Leon","Oaxaca","Puebla","Queretaro","Quintana Roo",
                "San Luis Potosi","Sinaloa","Sonora","Tabasco","Tamaulipas","Tlaxcala",
                "Veracruz","Yucatan","Zacatecas"
        );

        List<CompletableFuture<ClimaEstado>> futures = estadosMx.stream().map(estado -> {

            String url = "https://api.openweathermap.org/data/2.5/weather?q="
                    + estado.replace(" ", "%20") + ",MX&units=metric&appid=" + WEATHER_API_KEY;

            return client.sendAsync(HttpRequest.newBuilder().uri(URI.create(url)).GET().build(),
                            HttpResponse.BodyHandlers.ofString())
                    .thenApply(resp -> {
                        try {
                            JsonNode root = mapper.readTree(resp.body());

                            if (root.path("cod").asInt() == 200) {
                                return new ClimaEstado(
                                        estado,
                                        root.path("main").path("temp").asDouble(),
                                        root.path("main").path("humidity").asInt()
                                );
                            }
                        } catch (Exception ignored) {}

                        return null;
                    });

        }).collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
    }

    // ===================== AERONAVES =====================

    public CompletableFuture<List<Aeronave>> obtenerBaseAeronaves() {
        return obtenerVuelosOpenSky().thenApply(vuelos ->
                vuelos.stream()
                        .map(v -> new Aeronave(
                                v.icao24Property().get(),
                                "Vuelo Activo",
                                v.callsignProperty().get()
                        ))
                        .collect(Collectors.toList())
        );
    }

    // ===================== TRANSFORMACIONES =====================

    public List<Vuelo> transformarVuelos(List<Vuelo> lista) {

        return lista.stream().map(v -> {

            String callsign = v.callsignProperty().get().trim().toUpperCase();

            String tipoVuelo = (callsign.startsWith("XA") || callsign.startsWith("XB") || callsign.startsWith("XC"))
                    ? "NACIONAL" : "INTERNACIONAL";

            double metros = Math.round(v.altitudProperty().get() * 0.3048);

            String nivel = (metros < 3000) ? "BAJO" : (metros < 10000) ? "CRUCERO" : "ALTO";

            double vel = v.velocidadProperty().get();
            String categoriaVel = (vel < 200) ? "LENTO" : (vel < 250) ? "COMERCIAL" : "ALTA";

            String aerolinea;
            if (callsign.startsWith("AMX")) aerolinea = "AEROMEXICO";
            else if (callsign.startsWith("VIV")) aerolinea = "VIVA";
            else if (callsign.startsWith("VOI")) aerolinea = "VOLARIS";
            else aerolinea = "OTRA";

            String origen = (v.latitudProperty().get() > 28) ? "NORTE"
                    : (v.latitudProperty().get() > 22) ? "CENTRO" : "SUR";

            String fechaHora = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            return new Vuelo(
                    v.icao24Property().get(),
                    callsign,
                    v.longitudProperty().get(),
                    v.latitudProperty().get(),
                    metros,
                    vel,
                    tipoVuelo,
                    aerolinea,
                    nivel,
                    categoriaVel,
                    origen,
                    fechaHora
            );

        }).toList();
    }

    public List<ClimaEstado> transformarClima(List<ClimaEstado> lista) {

        return lista.stream().map(c -> {

            double t = c.temperaturaProperty().get();
            int h = c.humedadProperty().get();

            String clima = (t < 10) ? "FRIO" : (t <= 25) ? "TEMPLADO" : "CALIENTE";
            String humedad = (h < 40) ? "BAJA" : (h <= 70) ? "MEDIA" : "ALTA";

            String fechaHora = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            return new ClimaEstado(
                    c.estadoProperty().get(),
                    t,
                    h,
                    clima,
                    humedad,
                    fechaHora
            );

        }).toList();
    }

    public List<Aeropuerto> transformarAeropuertos(List<Aeropuerto> lista) {

        return lista.stream().map(a -> {

            String nombre = a.nombreProperty().get().toUpperCase();
            String tipo;

            if (nombre.contains("INTL") || nombre.contains("INTERNACIONAL")) tipo = "PUBLICO";
            else if (nombre.contains("HELIPUERTO")) tipo = "HELIPUERTO";
            else tipo = "PRIVADO";

            String fechaHora = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            return new Aeropuerto(
                    nombre,
                    a.ciudadProperty().get(),
                    a.icaoProperty().get(),
                    tipo,
                    fechaHora
            );

        }).toList();
    }

    public List<Aeronave> transformarAeronaves(List<Aeronave> lista) {

        return lista.stream().map(a -> {

            String tipoOp = a.operadorProperty().get().startsWith("AMX")
                    ? "COMERCIAL" : "GENERAL";

            String fechaHora = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            return new Aeronave(
                    a.icaoProperty().get(),
                    "ACTIVO",
                    a.operadorProperty().get().toUpperCase(),
                    tipoOp,
                    fechaHora
            );

        }).toList();
    }

    public void guardarVuelosMongo(List<Vuelo> lista) {

        MongoCollection<Document> col = MongoConexion.getDatabase().getCollection("vuelos");
        //col.drop();

        for (Vuelo v : lista) {
            col.insertOne(new Document()
                    .append("icao24", v.icao24Property().get())
                    .append("callsign", v.callsignProperty().get())
                    .append("tipoVuelo", v.tipoVueloProperty().get())
                    .append("aerolinea", v.aerolineaProperty().get())
                    .append("nivel", v.nivelProperty().get())
                    .append("velocidad", v.velocidadProperty().get())
                    .append("categoriaVelocidad", v.categoriaVelocidadProperty().get())
                    .append("origen", v.origenProperty().get())
                    .append("fechaHora", v.fechaHoraProperty().get()));
        }
    }

    public void guardarClimaMongo(List<ClimaEstado> lista) {

        MongoCollection<Document> col = MongoConexion.getDatabase().getCollection("clima");
        //col.drop();

        for (ClimaEstado c : lista) {
            col.insertOne(new Document()
                    .append("estado", c.estadoProperty().get())
                    .append("temperatura", c.temperaturaProperty().get())
                    .append("tipoClima", c.tipoClimaProperty().get())
                    .append("humedad", c.humedadProperty().get())
                    .append("nivelHumedad", c.nivelHumedadProperty().get())
                    .append("fechaHora", c.fechaHoraProperty().get()));
        }
    }

    public void guardarAeropuertosMongo(List<Aeropuerto> lista) {

        MongoCollection<Document> col = MongoConexion.getDatabase().getCollection("aeropuertos");
        //col.drop();

        for (Aeropuerto a : lista) {
            col.insertOne(new Document()
                    .append("nombre", a.nombreProperty().get())
                    .append("ciudad", a.ciudadProperty().get())
                    .append("icao", a.icaoProperty().get())
                    .append("tipo", a.tipoProperty().get())
                    .append("fechaHora", a.fechaHoraProperty().get()));
        }
    }

    public void guardarAeronavesMongo(List<Aeronave> lista) {

        MongoCollection<Document> col = MongoConexion.getDatabase().getCollection("aeronaves");
        //col.drop();

        for (Aeronave a : lista) {
            col.insertOne(new Document()
                    .append("icao", a.icaoProperty().get())
                    .append("estado", a.estadoProperty().get())
                    .append("operador", a.operadorProperty().get())
                    .append("tipoOperacion", a.tipoOperacionProperty().get())
                    .append("fechaHora", a.fechaHoraProperty().get()));
        }
    }
}