package com.example.generadorcartas.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class EdenAIService {
    public String getImagen(String nombre, String descripcion) throws IOException {

        //Configuración de la API de edenAI--------------------------------------------------

        OkHttpClient client = new OkHttpClient.Builder()
                //aumento el tiempo de espera predeterminado para el cliente OkHttp3 (la respuesta es lenta y me tira un error java.net.SocketTimeoutException)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        // Armo el JSON a ser usado en el body de la API (necesito hacer esto para que la descripción sea dinámica)
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("response_as_dict", true);
        jsonNode.put("attributes_as_list", false);
        jsonNode.put("show_original_response", false);
        jsonNode.put("resolution", "256x256");
        jsonNode.put("num_images", 1);
        jsonNode.put("providers", "openai");
        jsonNode.put("text", "dibujo de fantasia para un juego de cartas: " + nombre + ". " + descripcion);


        //convierto el json a json string (dado que es lo que pide la api en el body)
        String jsonString = objectMapper.writeValueAsString(jsonNode);


        //API pura y dura------------------------------
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonString);

        Request request = new Request.Builder()
                .url("https://api.edenai.run/v2/image/generation")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiZGE2Y2Q5ZDAtOTVmZC00MTgxLTkxYzAtOWVjMThjMDM4OGU1IiwidHlwZSI6ImFwaV90b2tlbiJ9.QlnUYH63Tf33W4gU0-aMbCzggrqAlPT59DVmdxF3oTQ")
                .build();

        Response response = client.newCall(request).execute();
        //fin --------------------------------------------



        //guardamos el json que nos devuelve la api para poder mapearla
        String json = response.body().string();

        //mapeamos el json obtenido de la API para poder extraer solamente la url de la imagen
        JsonNode jsonNode2 = objectMapper.readTree(json);
        // Accede al valor de image_resource_url
        String imageResourceUrl = jsonNode2
                .path("openai")
                .path("items")
                .path(0)
                .path("image_resource_url")
                .asText();

        return imageResourceUrl;
    }
}
