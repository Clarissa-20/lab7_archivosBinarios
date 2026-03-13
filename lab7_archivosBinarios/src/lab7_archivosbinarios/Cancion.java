/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_archivosbinarios;

/**
 *
 * @author HP
 */
public class Cancion extends ElementoMultimedia{
    private String artista;
    private Genero genero;
    private String rutaImagen;
    private String rutaArchivo;
    
    public Cancion(String nombre, String artista, double duracion, Genero genero, String rutaImagen, String rutaArchivo){
        super(nombre, duracion);
        this.artista = artista;
        this.genero = genero;
        this.rutaImagen = rutaImagen;
        this.rutaArchivo = rutaArchivo;
    }
    
    public String getArtista(){
        return artista;
    }
    
    public Genero getGenero(){
        return genero;
    }
    
    public String getRutaImagen(){
        return rutaImagen;
    }
    
    public String getRutaArchivo(){
        return rutaArchivo;
    }
    
    @Override
    public String getDetalles(){
        return nombre+" - "+artista+" ["+genero+"]";
    }
}
