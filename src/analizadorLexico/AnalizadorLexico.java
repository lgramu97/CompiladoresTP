package analizadorLexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


import accionesSemanticas.AccionSemantica;

public class AnalizadorLexico {
 
    private ArrayList<String> lineas;  
    private ArrayList<String> errores;
    private StringBuilder lexema;
    
    private int columna;
    private int fila_leida;
    private int ultimo_estado;


    //Estructuras
    private HashMap<Character,Integer> columnas;
    private HashMap<String,Integer> tokens;
    private int [][] transicion_estados;
    private AccionSemantica[][] acciones_semanticas;
    private HashMap<String , HashMap< String, Object > > tabla_simbolos;
    private String[] palabras_reservadas = new String[11]; 

    public AnalizadorLexico(){

        //inicializo variablesa



        //cargo archivo
        JFileChooser jchooser=new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        jchooser.setFileFilter(filter);
        jchooser.showOpenDialog(null);

        File abre= jchooser.getSelectedFile();
        if (abre !=null){
            
            lineas = new ArrayList<String>();
            try {
                FileReader archivo=new FileReader(abre);
                BufferedReader buffer =new BufferedReader(archivo);
                String linea = buffer.readLine();
                while (linea != null) {
                    lineas.add(linea);
                    linea = buffer.readLine();
                }
                buffer.close();
            } catch (Exception e) {
                //TODO: handle exception
            }
        	System.out.print(this.lineas.size());

            
        }else{
        	JOptionPane.showMessageDialog(null,"No selecciono ningun archivo");
        	System.exit(100);
        }
    }


    
    
    public static void main(String args[]) {
    	AnalizadorLexico an = new AnalizadorLexico();
    			
	}

}



