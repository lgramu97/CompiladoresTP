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
    private final String[] palabras_reservadas = {"IF", "THEN", "END_IF", "OUT",
            "FUNC", "RETURN", "ELSE", "LONGINT", "FLOAT", "WHILE", "LOOP"};
    private final static int F = Integer.MAX_VALUE;

    public AnalizadorLexico(){

        //inicializo variables


        //cargo archivo
        JFileChooser jchooser=new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        jchooser.setFileFilter(filter);
        jchooser.showOpenDialog(null);

        File open = jchooser.getSelectedFile();
        if (open != null){
            readFile(open);
            System.out.print(this.lineas.size());
            initializeTokens();
            mapColumnToChar();
            tabla_simbolos = new HashMap<>();
            initializeMatrizTransicionEstados();
        }else{
        	JOptionPane.showMessageDialog(null,"No selecciono ningun archivo");
        	System.exit(100);
        }
    }

    private void initializeMatrizTransicionEstados() {
        transicion_estados = new int[][] {
                { 17, 1, 1, 1, 2, F, F, F, F, F, F, 3, F, 11, 15, 14, 5, 7, -1, 12, 0, 0, F, -1},
                { -1, 1, 1, 1, 1, F, F, F, F, F, F, F, F, F, F, F, F, F, F, 1, F, F, F, -1, -1},
                { -1, -1, -1, -1, 2, -1, -1, -1, -1, -1, -1, 3, -1, -1, -1, -1, -1, -1, 13, -1, -1, -1, -1, -1},
                { -1, F, 16, F, 3, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -1, -1 },
                { -1, F, F, F, 4, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 6, -1, -1, -1, -1, -1, -1, -1},
                { -1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 6, -1, -1},
                { -1, 8, 8, 8, 8, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, F, 8, 8, -1, 8, -1, -1},
                { -1, 8, 8, 8, 8, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, F, 8, 8, -1, 8, -1, -1},
                { -1, 8, 8, 8, 8, -1, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 10, 8, -1, -1},
                { -1, 8, 8, 8, 8, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, F, 8, 8, -1, 8, -1, -1},
                { -1, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, F, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, F, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                { -1, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -1, -1},
                { -1, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -1, -1},
                { -1, -1, -1, -1, -1, 4, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                { 17, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, 17, F, F, F, -1, -1}
        };
    }

    private void readFile(File abre) {
        // Leer el archivo
        lineas = new ArrayList<>();
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
    }

    private void mapColumnToChar() {
        // Mapear columnas a caracter
        columnas = new HashMap<>();
        columnas.put('L', 0);
        columnas.put('F', 0);
        char[] abecedario = {'a', 'b', 'c', 'd', 'e', 'g', 'h', 'i',
                'j', 'k', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        for (char c : abecedario) {
            columnas.put(Character.toUpperCase(c), 0);
            columnas.put(c, 1);
        }
        columnas.put('f', 2);
        columnas.put('l', 3);
        for (int c = 0; c < 10; c++) {
            columnas.put(Character.forDigit(c,10), 4);
        }
        columnas.put('-', 5);
        columnas.put('+', 6);
        columnas.put('*', 7);
        columnas.put('/', 8);
        columnas.put('(', 9);
        columnas.put(')', 9);
        columnas.put('{', 10);
        columnas.put('}', 10);
        columnas.put('.', 11);
        columnas.put(',', 12);
        columnas.put(';', 12);
        columnas.put('=', 13);
        columnas.put('<', 14);
        columnas.put('>', 15);
        columnas.put('%', 16);
        columnas.put('"', 17);
        columnas.put('_', 18);
        columnas.put('!', 19);
        columnas.put('\n', 20);
        columnas.put('\t', 21);
        columnas.put('\s', 21);
        columnas.put('$', 22);
    }

    private void initializeTokens() {
        // Inicializar tokens
        tokens = new HashMap<>();
        tokens.put("\"", 34);
        tokens.put("$", 36);
        tokens.put("%", 37);
        tokens.put("(", 40);
        tokens.put(")", 41);
        tokens.put("*", 42);
        tokens.put("+", 43);
        tokens.put(",", 44);
        tokens.put("-", 45);
        tokens.put(".", 46);
        tokens.put("/", 47);
        tokens.put(";", 59);
        tokens.put("<", 60);
        tokens.put("=", 61);
        tokens.put(">", 62);
        tokens.put("_", 95);
        tokens.put("!=", 101);
        tokens.put(">=", 102);
        tokens.put("<=", 103);
        tokens.put("==", 104);
        tokens.put("ID", 105);
        tokens.put("CTE", 106);
        tokens.put("CADENA", 107);
        tokens.put("IF", 108);
        tokens.put("THEN", 109);
        tokens.put("END_IF", 110);
        tokens.put("OUT", 111);
        tokens.put("FUNC", 112);
        tokens.put("RETURN", 113);
        tokens.put("ELSE", 114);
        tokens.put("LONGINT", 115);
        tokens.put("FLOAT", 116);
        tokens.put("WHILE", 117);
        tokens.put("LOOP", 118);
        tokens.put("{", 123);
        tokens.put("}", 125);
    }


    public static void main(String[] args) {
    	AnalizadorLexico an = new AnalizadorLexico();
	}

}



