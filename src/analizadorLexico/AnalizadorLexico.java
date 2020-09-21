package analizadorLexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


import accionesSemanticas.AccionSemantica;
import accionesSemanticas.AccionSemanticaNro1;
import accionesSemanticas.AccionSemanticaNro10;
import accionesSemanticas.AccionSemanticaNro11;
import accionesSemanticas.AccionSemanticaNro12;
import accionesSemanticas.AccionSemanticaNro13;
import accionesSemanticas.AccionSemanticaNro2;
import accionesSemanticas.AccionSemanticaNro3;
import accionesSemanticas.AccionSemanticaNro4;
import accionesSemanticas.AccionSemanticaNro5;
import accionesSemanticas.AccionSemanticaNro6;
import accionesSemanticas.AccionSemanticaNro7;
import accionesSemanticas.AccionSemanticaNro8;
import accionesSemanticas.AccionSemanticaNro9;

public class AnalizadorLexico {

    private ArrayList<String> lineas;
    private ArrayList<String> errores = new ArrayList<>();
    private StringBuilder lexema;
    
    private int columna; //Ultimo caracter leido.
    private int fila_leida; // Linea de codigo actual.
    private int ultimo_estado;

    private final static String ERROR = "ERROR";


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
            initializeTokens();
            mapColumnToChar();
            fila_leida = 0;
            tabla_simbolos = new HashMap<>();
            initializeMatrizTransicionEstados();
            initializeMatrizAccionesSemanticas();
        }else{
        	JOptionPane.showMessageDialog(null,"No selecciono ningun archivo");
        	System.exit(100);
        }
    }

    private void initializeMatrizTransicionEstados() {
        transicion_estados = new int[][] {
		{16, 1, 1, 1, 2, F, F, F, F, F, F, 19, F, 10, 14, 13, 5, 7, -1, 11, 0, 0, F, -1},
		{F, 1, 1, 1, 1, F, F, F, F, F, F, F, F, F, F, F, F, F, 1, F, F, F, -1, -1},
		{-1, -1, -1, -1, 2, -1, -1, -1, -1, -1, -1, 3, -1, -1, -1, -1, -1, -1, 12, -1, -1, -1, -1, -1},
		{F, F, 15, F, 3, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -1, -1},
		{F, F, F, F, 4, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -1, -1},
		{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 6, -1, -1, -1, -1, -1, -1, -1},
		{6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 6, -1, -1},
		{7, 7, 7, 7, 7, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, F, 7, 7, -1, 7, -1, -1},
		{7, 7, 7, 7, 7, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 9, 7, -1, -1},
		{7, 7, 7, 7, 7, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, F, 7, 7, -1, 7, -1, -1},
		{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -1, -1},
		{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, F, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
		{-1, -1, -1, F, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
		{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -1, -1},
		{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -1, -1},
		{-1, -1, -1, -1, -1, 17, 17, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
		{16, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, 16, F, F, F, -1, -1},
		{-1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
		{F, F, 15, F, 18, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -1, -1},
		{-1, -1, -1, -1, 18, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}
        };
    }

    private void initializeMatrizAccionesSemanticas() {
        AccionSemantica as1 = new AccionSemanticaNro1();
        AccionSemantica as2 = new AccionSemanticaNro2();
        AccionSemantica as3 = new AccionSemanticaNro3();
        AccionSemantica as4 = new AccionSemanticaNro4();
        AccionSemantica as5 = new AccionSemanticaNro5();
        AccionSemantica as6 = new AccionSemanticaNro6();
        AccionSemantica as7 = new AccionSemanticaNro7();
        AccionSemantica as8 = new AccionSemanticaNro8();
        AccionSemantica as9 = new AccionSemanticaNro9();
        AccionSemantica as10 = new AccionSemanticaNro10();
        AccionSemantica as11 = new AccionSemanticaNro11();
        AccionSemantica as12 = new AccionSemanticaNro12();
        AccionSemantica as13 = new AccionSemanticaNro13();
        acciones_semanticas = new AccionSemantica[][] {
        	{as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, null, as2, as12, as2, as6, null, as2, as13},
			{as1, as3, as3, as3, as3, as1, as1, as1, as1, as1, as1, as1, as1, as1, as1, as1, as1, as1, as1, as1, as1, as1, as1, as1}, 
			{as9, as9, as9, as9, as3, as9, as9, as9, as9, as9, as9, as3, as9, as9, as9, as9, as9, as9, as3, as9, as9, as9, as9, as9},
			{as4, as4, as3, as4, as3, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4},
			{as4, as4, as4, as4, as3, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4},
			{as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, null, as9, as9, as9, as9, as9, as9, as9},
			{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, as6, null,null/*Vacio*/ , as13},
			{as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as10, as3, as3, as9, as3, null/*Vacio*/, as3},
			{as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as12, as3, as3, as11, as3,null/*Vacio*/ , as3},
			{as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as10, as3, as3, as9, as3,null/*Vacio*/ , as3},
			{as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as3, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8},
			{as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as3, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9},
			{as9, as9, as9, as5, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9},
			{as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as3, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8},
			{as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as3, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8},
			{as9, as9, as9, as9, as9, as3, as3, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9},
			{as3, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as3, as7, as7, as7, as7, as7},
			{as9, as9, as9, as9, as3, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9},
			{as4, as4, as3, as4, as3, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4},
			{as9, as9, as9, as9, as3, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9}  
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
        columnas.put(' ', 21);
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
        tokens.put("ERROR",119);
        tokens.put("{", 123);
        tokens.put("}", 125);
    }
    
    public void charAnterior() {
    	//Devolver el ultimo caracter leido.
		if(columna==0) {
			fila_leida--;
			columna=lineas.get(fila_leida).length();
		}else
			columna--;	
    }
    
    public void nuevaLinea() {
    	//Siguiente linea de codigo.
    	fila_leida++;
    	columna = 0;
    }
    
    public void inicializarBuffer() {
    	//Inicializar el lexema.
    	lexema = new StringBuilder();
    }
    
    public void appendChar(char c) {
    	//Agregar char al lexema.
    	lexema.append(c);
    }
    
    public void appendLexema() {
    	//Agregar lexema a la tabla de simbolos.
    	if(!tabla_simbolos.containsKey(lexema.toString())){
    		tabla_simbolos.put(lexema.toString(), new HashMap<String, Object>());
    	}else {
    		//nada
    	}
    }

    public void addTipo(String tipo, Object obj) {
        tabla_simbolos.get(lexema.toString()).put(tipo, obj);
    }

    public boolean isPalabraReservada(String lexema) {
        return Arrays.asList(palabras_reservadas).contains(lexema);
    }

    public void deleteLastChar() {
    	this.lexema.deleteCharAt(lexema.length()-1);
    }


    public void putError(String error) {
    	//Agregar error.
    	errores.add("Linea numero " + (fila_leida+1) + " :	"+ error);
    }
    
    public StringBuilder getLexema() {
		return lexema;
	}
    
    public ArrayList<String> getErrores(){
    	return this.errores;
    }
 
    public int getFilaActual() {
    	return this.fila_leida;
    }
    
	public HashMap<String, HashMap<String, Object>> getTabla_simbolos() {
		//Retornar tabla de simbolos.
		return this.tabla_simbolos;
	}
	
	public String getDatosTabla_simbolos() {
		StringBuilder datos = new StringBuilder();
		for (String key: tabla_simbolos.keySet()) {
			datos.append("Lexema: ").append(key).append("\n");
			for(String att: tabla_simbolos.get(key).keySet()) {
				datos.append("Atributo: ").append(att).append("   ").append("Valor: ").append(tabla_simbolos.get(key).get(att)).append("\n");
			}
		}
		return datos.toString();	
	}
	
	public void setTablaSimbolos(HashMap<String, HashMap<String, Object>> ts) {
		//Usado para test.
		this.tabla_simbolos = ts;
	}
	
	public void setLexema(StringBuilder l) {
		//Usado para test.
		this.lexema = l;
	}
		
	
	public HashMap<String, Integer> getTokens() {
		//Usado para test.
		return tokens;
	}
	
	public int getLineasTotales() {
		return this.lineas.size();
	}

	public int yylex() {
		//Retorna codigo de token para el parser yacc.
        ultimo_estado = 0;
        String tipo = null;
        while (ultimo_estado != F && ultimo_estado != -1) {
            char proximo_char = 'z';
            System.out.println("Linnea : " + fila_leida + " de " + lineas.size());
            if (columna == lineas.get(fila_leida).length()) {
                proximo_char = '\n';
            } else {
                proximo_char = lineas.get(fila_leida).charAt(columna);
            }
            columna++;
            int columna_caracter = 0;
            columna_caracter = columnas.getOrDefault(proximo_char, 23);
            System.out.println("Proximo char: " + proximo_char + "  " + "Columna Caracter: " + columna_caracter +
            		"   " + "Accion semantica: " + ultimo_estado + " , " + columna_caracter);
            if (acciones_semanticas[ultimo_estado][columna_caracter] != null) {
                tipo = acciones_semanticas[ultimo_estado][columna_caracter].ejecutar(proximo_char,this);
            }
            ultimo_estado = transicion_estados[ultimo_estado][columna_caracter];
        }
        if (tipo == null) {
            return tokens.get(lexema.toString());
        }
        return tokens.get(tipo);
	}

	public static void main(String[] args) {
    	AnalizadorLexico an = new AnalizadorLexico();
	}


}



