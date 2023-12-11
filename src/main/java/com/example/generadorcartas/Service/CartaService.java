package com.example.generadorcartas.Service;

import com.example.generadorcartas.Entity.Carta;
import com.example.generadorcartas.Repository.MisCartasRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartaService {
    @Autowired
    MisCartasRepository misCartasRepository;

    @Autowired
    EdenAIService edenAIService;

    public void save(Carta carta){
        misCartasRepository.save(carta);
    }

    public void delete(Long idCarta){
        //con el id, me traigo la carta que voy a eliminar
        Carta carta = misCartasRepository.findById(idCarta).get();

        misCartasRepository.delete(carta);
    }

    public List<Carta> getCartas(){
        return misCartasRepository.findAll();
    }


    //Esta es una de las funciones principales de la aplicación. Se encarga de orquestar las dos apis para poder generar las cartas
    public List<Carta> generarCartas() throws IOException {
        String json = ChatGPTUtil.getCartas();


        // Convierto el JSON a un objeto JSONObject y le agrego la url_imagen
        JSONObject jsonObject = new JSONObject(json);


        // Obtengo el array de cartas
        JSONArray cartas = jsonObject.getJSONArray("cartas");

        // Iterar sobre cada carta y agregar el atributo "url_imagen"
        for (int i = 0; i < cartas.length(); i++) {
            JSONObject carta = cartas.getJSONObject(i);

            //Obtener la descripción y nombre de la carta para armar el prompt que genera la imagen
            String nombre = carta.getString("nombre");
            String descripcion = carta.getString("descripcion");

            //Agrego la URL de la imagen correspondiente a cada carta
            carta.put("url_imagen", edenAIService.getImagen(nombre, descripcion));
        }


        //convierto el JSONArray de cartas en una Lista de cartas
        List<Carta> list = new ArrayList<Carta>();
        for (int i=0; i<cartas.length(); i++) {
            JSONObject cartaJson = cartas.getJSONObject(i);

            // Extract values from JSON and create a Carta object
            String nombre = cartaJson.getString("nombre");
            String descripcion = cartaJson.getString("descripcion");
            int poder = cartaJson.getInt("poder");
            int defensa = cartaJson.getInt("defensa");
            String urlImagen = cartaJson.getString("url_imagen");

            Carta carta = new Carta(nombre,descripcion,poder, defensa, urlImagen);

            // Add the Carta object to the list
            list.add(carta);
        }

        return list;
    }
}
