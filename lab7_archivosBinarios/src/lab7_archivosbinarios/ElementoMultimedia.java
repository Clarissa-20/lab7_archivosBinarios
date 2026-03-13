/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_archivosbinarios;

import java.io.Serializable;

/**
 *
 * @author HP
 */
public abstract class ElementoMultimedia implements Serializable{
    protected String nombre;
    protected double duracion;
    
    public ElementoMultimedia(String nombre, double duracion){
        this.nombre = nombre;
        this.duracion = duracion;
    }
    
    public final String getNombre(){
        return nombre;
    }
    
    public abstract String getDetalles();
}
