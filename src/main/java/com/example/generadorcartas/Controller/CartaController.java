package com.example.generadorcartas.Controller;



import com.example.generadorcartas.Entity.Carta;
import com.example.generadorcartas.Service.CartaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartaController {

    @Autowired
    CartaService cartaService;


    @GetMapping("/")
    public String generar(Model model) throws IOException {
        List<Carta> listaCartas = cartaService.generarCartas();

        //Cargo la lista de cartas en el modelo para poder usarlas en el front
        model.addAttribute("carta",listaCartas);

        return "home";
    }



    @GetMapping("/guardar")
    public String guardar(@ModelAttribute Carta carta){
        cartaService.save(carta);

        return "redirect:/mis_cartas";
    }

    @GetMapping("/mis_cartas")
    public String misCartas(Model model){
        List<Carta> listaMisCartas = cartaService.getCartas();

        //modelo para poder cargar las cartas en el front
        model.addAttribute("carta",listaMisCartas);

        return "misCartas";
    }

    @RequestMapping("/eliminar/{idCarta}")
    public String eliminar(@PathVariable("idCarta") Long idCarta){
        cartaService.delete(idCarta);

        return "redirect:/mis_cartas";
    }
}
