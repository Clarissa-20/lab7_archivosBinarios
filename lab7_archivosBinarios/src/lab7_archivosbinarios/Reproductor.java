/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_archivosbinarios;

import java.io.FileInputStream;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 *
 * @author HP
 */
public class Reproductor {

    private AdvancedPlayer player;
    private String rutaActual;
    private boolean pausado = false;
    private boolean reproduciendo = false;

    public boolean estaReproduciendo() {
        return reproduciendo && !pausado;
    }

    public void play(String ruta) throws Exception {
        if (pausado && ruta.equals(rutaActual)) {
            pausado = false;
        } else {
            stop();
            rutaActual = ruta;
            reproducirNueva();
        }
    }

    private void reproducirNueva() {
        new Thread(() -> {
            try {
                reproduciendo = true;
                FileInputStream fis = new FileInputStream(rutaActual);
                player = new AdvancedPlayer(fis);
                player.play();
                reproduciendo = false;
            } catch (Exception e) {
                reproduciendo = false;
            }
        }).start();
    }

    public void pause() {
        if (reproduciendo) {
            pausado = !pausado; 
            if (pausado && player != null) player.close(); 
            else if (!pausado) reproducirNueva();
        }
    }

    public void stop() {
        if (player != null) player.close();
        reproduciendo = false;
        pausado = false;
    }
}
