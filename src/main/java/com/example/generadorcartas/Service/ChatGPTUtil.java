package com.example.generadorcartas.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatGPTUtil {

    public static String getCartas() {
        String prompt = "Estoy creando un juego de cartas de magos. Necesito que inventes 3 cartas. Necesito que me devuelvas un nuevo JSON con el siguiente formato: Nombre de la carta, Descripción, puntos de poder, puntos de defensa.El formato del JSON debe ser similar a esto: {cartas:[{nombre:aqui poner el nombre del personaje,descripcion:aqui poner una breve descripcion del personaje,poder:poner un puntaje de poder entre 1 y 10,defensa:poner un puntaje de defensa entre 1 y 10},{nombre:aqui poner el nombre del personaje,descripcion:aqui poner una breve descripcion del personaje,poder:poner un puntaje de poder entre 1 y 10,defensa:poner un puntaje de defensa entre 1 y 10}]}. Recuerda inventar los campos nombre, descripcion, poder y fuerza Solamente devuelve el json ningun otro texto mas. El json debe estar en una sola linea y sin saltos de linea";
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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"; // API key goes here
        String model = "gpt-3.5-turbo"; // current model of chatgpt api

        try {
            // Create the HTTP POST request
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");

            // Build the request body
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Get the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // returns the extracted contents of the response.
            return extractContentFromResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // This method extracts the response expected from chatgpt and returns it.
    public static String extractContentFromResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode choices = jsonNode.get("choices");
            if (choices != null && choices.isArray() && choices.size() > 0) {
                JsonNode contentNode = choices.get(0).get("message").get("content");
                if (contentNode != null) {
                    return contentNode.asText();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}