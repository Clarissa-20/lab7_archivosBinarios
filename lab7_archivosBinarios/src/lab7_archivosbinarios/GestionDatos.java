/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_archivosbinarios;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class GestionDatos {
    private static final String ARCHIVO_LISTA = "playlist.dat";
    
    public static void guardarPlayList(ArrayList<Cancion> lista){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_LISTA))){
            oos.writeObject(lista);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static ArrayList<Cancion> cargarPlayList(){
        File file = new File(ARCHIVO_LISTA);
        if(!file.exists()){
            return new ArrayList<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            return (ArrayList<Cancion>) ois.readObject();
        }catch(Exception e){
            return new ArrayList<>();
        }
    }
    
    public static File buscarArchivo(File carpeta, String nombre){
        File[] archivos = carpeta.listFiles();
        if(archivos!=null){
            for(File f : archivos){
                if(f.isDirectory()){
                    File encontrado = buscarArchivo(f, nombre);
                    if(encontrado!=null){
                        return encontrado;
                    }
                }else if(f.getName().equalsIgnoreCase(nombre)){
                    return f;
                }
            }
        }
        return null;
    }
}
