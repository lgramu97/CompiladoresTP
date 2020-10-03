package componentes;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
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
import accionesSemanticas.AccionSemanticaNro14;
import accionesSemanticas.AccionSemanticaNro15;
import accionesSemanticas.AccionSemanticaNro2;
import accionesSemanticas.AccionSemanticaNro3;
import accionesSemanticas.AccionSemanticaNro4;
import accionesSemanticas.AccionSemanticaNro5;
import accionesSemanticas.AccionSemanticaNro6;
import accionesSemanticas.AccionSemanticaNro7;
import accionesSemanticas.AccionSemanticaNro8;
import accionesSemanticas.AccionSemanticaNro9;

import componentes.Parser;

public class AnalizadorLexico {

    private ArrayList<String> lineas;
    private ArrayList<String> errores = new ArrayList<>();
    private StringBuilder lexema;
    
    private int columna; //Ultimo caracter leido.
    private int fila_leida; // Linea de codigo actual.
    private int ultimo_estado;

    //Estructuras
    private HashMap<Character,Integer> columnas;
    private HashMap<String,Integer> tokens;
    private int [][] transicion_estados;
    private AccionSemantica[][] acciones_semanticas;
    private HashMap<String , HashMap< String, Object > > tabla_simbolos;
    private final String[] palabras_reservadas = {"IF", "THEN", "END_IF", "OUT",
            "FUNC", "RETURN", "ELSE", "LONGINT", "FLOAT", "WHILE", "LOOP", "PROC", "NI", "REF"};
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
            columna = 0;
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
            {16, 1, 1, 1, 2, F, F, F, F, F, 19, F, 10, 14, 13, 5, 7, -F, 11, 0, 0, F, -F},
            {F, 1, 1, 1, 1, F, F, F, F, F, F, F, F, F, F, F, F, 1, F, F, F, -F, -F},
            {-F, -F, -F, -F, 2, -F, -F, -F, -F, -F, 3, -F, -F, -F, -F, -F, -F, 12, -F, -F, -F, -F, -F},
            {F, F, 15, F, 3, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -F, -F},
            {F, F, F, F, 4, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -F, -F},
            {-F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, 6, -F, -F, -F, -F, -F, -F, -F},
            {6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 6, -F, -F},
            {7, 7, 7, 7, 7, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, F, 7, 7, -F, 7, -F, -F},
            {7, 7, 7, 7, 7, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 9, 7, -F, -F},
            {7, 7, 7, 7, 7, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, F, 7, 7, -F, 7, -F, -F},
            {F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -F, -F},
            {-F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F},
            {-F, -F, -F, F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F},
            {F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -F, -F},
            {F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -F, -F},
            {-F, -F, -F, -F, -F, 17, 17, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F},
            {16, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, 16, F, F, F, -F, -F},
            {-F, -F, -F, -F, 4, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F},
            {F, F, 15, F, 18, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, -F, -F},
            {-F, -F, -F, -F, 18, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F, -F}
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
        AccionSemantica as14 = new AccionSemanticaNro14();
        AccionSemantica as15 = new AccionSemanticaNro15();

        acciones_semanticas = new AccionSemantica[][] {
                {as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, null, as2, as12, as2, as6, null, as2, as13},
                {as1, as3, as3, as3, as3, as1, as1, as1, as1, as1, as1, as1, as1, as1, as1, as1, as1, as3, as1, as1, as1, as1, as1 },
                {as9, as9, as9, as9, as3, as9, as9, as9, as9, as9, as3, as9, as9, as9, as9, as9, as9, as3, as9, as9, as9, as9, as9},
                {as4, as4, as3, as4, as3, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4},
                {as4, as4, as4, as4, as3, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4},
                {as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, null, as9, as9, as9, as9, as9, as9, as9},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, as6, null, as14, as13},
                {as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as10, as3, as3, as9, as3, as15, as3},
                {as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as12, as3, as3, as11, as3, as15, as3},
                {as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as10, as3, as3, as9, as3, as15, as3},
                {as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as3, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8},
                {as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as3, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9},
                {as9, as9, as9, as5, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9},
                {as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as3, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8},
                {as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8, as3, as8, as8, as8, as8, as8, as8, as8, as8, as8, as8},
                {as9, as9, as9, as9, as9, as3, as3, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9},
                {as3, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as3, as7, as7, as7, as7, as7},
                {as9, as9, as9, as9, as3, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9},
                {as4, as4, as3, as4, as3, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4, as4},
                {as9, as9, as9, as9, as3, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9}
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
        columnas.put('/', 7);
        columnas.put('(', 8);
        columnas.put(')', 8);
        columnas.put('{', 9);
        columnas.put('}', 9);
        columnas.put('.', 10);
        columnas.put(',', 11);
        columnas.put(';', 11);
        columnas.put(':',11);
        columnas.put('=', 12);
        columnas.put('<', 13);
        columnas.put('>', 14);
        columnas.put('%', 15);
        columnas.put('"', 16);
        columnas.put('_', 17);
        columnas.put('!', 18);
        columnas.put('\n', 19);
        columnas.put('\t', 20);
        columnas.put(' ', 20);
        columnas.put('$', 21);
    }

    private void initializeTokens() {
        // Inicializar tokens
        tokens = new HashMap<>();
        tokens.put("$", 36);
        //tokens.put("%", 37); // creo que no va como token, se usan en los comentarios o cadenas pero no es necesario informarlo.
        tokens.put("(", 40);
        tokens.put(")", 41);
        tokens.put("*", 42);
        tokens.put("+", 43);
        tokens.put(",", 44);
        tokens.put("-", 45);
        //tokens.put(".", 46);//creo q no va como token, se usa en float pero el token en si es la CTE.
        tokens.put("/", 47);
        tokens.put(":", 58);
        tokens.put(";", 59);
        tokens.put("<", 60);
        tokens.put("=", 61);
        tokens.put(">", 62);
        //tokens.put("_", 95);//creo q no va como token, se usa en longint pero el token en si es la CTE.
        tokens.put("!=", (int) Parser.DISTINTO);
        tokens.put(">=", (int) Parser.MAYOR_IGUAL);
        tokens.put("<=", (int) Parser.MENOR_IGUAL);
        tokens.put("==", (int) Parser.IGUAL);
        tokens.put("ID", (int) Parser.ID);
        tokens.put("CTE", (int) Parser.CTE);
        tokens.put("CADENA", (int) Parser.CADENA);
        tokens.put("IF",(int) Parser.IF);
        tokens.put("THEN", (int) Parser.THEN);
        tokens.put("END_IF", (int) Parser.END_IF);
        tokens.put("OUT", (int) Parser.OUT);
        tokens.put("FUNC", (int) Parser.FUNC);
        tokens.put("RETURN", (int) Parser.RETURN);
        tokens.put("ELSE", (int) Parser.ELSE);
        tokens.put("LONGINT", (int) Parser.LONGINT);
        tokens.put("FLOAT", (int) Parser.FLOAT);
        tokens.put("WHILE", (int) Parser.WHILE);
        tokens.put("LOOP", (int) Parser.LOOP);
        tokens.put("ERROR",(int) Parser.ERROR);
        tokens.put("PROC",(int) Parser.PROC);
        tokens.put("NI",(int) Parser.NI);
        tokens.put("REF",(int) Parser.REF);
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
    		tabla_simbolos.get(lexema.toString()).put("Cantidad", 1);
    	}else {
    		//aumento en 1 la cantidad de veces que aparecio el lexema (para el caso de negativos).
    		tabla_simbolos.get(lexema.toString()).put("Cantidad",((Integer)tabla_simbolos.get(lexema.toString()).get("Cantidad"))+1);
    	}
    }
    
    public void updateTablaSimbolos(String lexema) {
    	//Utilizado en el Parser, cuando se lea una constante negativa.
    	String lexema_negativo = "-"+lexema;
    	if (!tabla_simbolos.containsKey(lexema_negativo)) {
    		//Si no esta el lexema negativo en la tabla de simbolos, agregarlo.
    		tabla_simbolos.put(lexema_negativo, new HashMap<String, Object>());
    		tabla_simbolos.get(lexema_negativo).put("Cantidad", 1);
    		tabla_simbolos.get(lexema_negativo).put("Tipo",tabla_simbolos.get(lexema).get("Tipo"));
    	}else {
    		//Si esta en la tabla de simbolos, agregar +1;
    		tabla_simbolos.get(lexema_negativo).put("Cantidad",(Integer)tabla_simbolos.get(lexema_negativo).get("Cantidad")+1);
    	}
    	// Por cualquiera de los 2 casos, debo restar en 1 la cantidad a +lexema;
    	// Si la cantidad al restarle 1 es 0, eliminarlo, nunca debio existir en la TS.
    	tabla_simbolos.get(lexema).put("Cantidad", (Integer) tabla_simbolos.get(lexema).get("Cantidad")-1); 
    	if ((Integer) tabla_simbolos.get(lexema).get("Cantidad") == 0)
    		tabla_simbolos.remove(lexema);
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
				datos.append("\t" + "Atributo: ").append(att).append("   ").append("Valor: ").append(tabla_simbolos.get(key).get(att)).append("\n");
			}
		}
		return datos.toString();	
	}
	
	public boolean fin_archivo() {
		return (this.lineas.size() == (this.fila_leida+1) && this.lineas.get(fila_leida).length() == this.columna);
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
        while (ultimo_estado != F && ultimo_estado != -F) {
            char proximo_char = 'z';
            //System.out.println("Linnea : " + fila_leida + " de " + lineas.size());
            //System.out.println("Col " + columna + " de " + lineas.get(fila_leida).length());
            if (fila_leida == this.getLineasTotales()) // usado para test.
            	return 0;
            if (columna == lineas.get(fila_leida).length()) {
                proximo_char = '\n';
            } else {
                proximo_char = lineas.get(fila_leida).charAt(columna);
            }
            columna++;
            int columna_caracter = 0;
            columna_caracter = columnas.getOrDefault(proximo_char, 22);
            //System.out.println("Proximo char: " + (proximo_char == '\n' ? "SALTO_LINEA" : proximo_char == ' ' ? "BLANCO" : proximo_char) + "  " + "Columna Caracter: " + columna_caracter +
            //		"   " + "Accion semantica: " + ultimo_estado + " , " + columna_caracter);
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
	
	public boolean check_rango_longint(String lexema) {
		// Chequeo rango longint positivo < 2147483647.
		if(tabla_simbolos.get(lexema).get("Tipo") == "LONGINT") {
			BigDecimal lexBig = new BigDecimal(lexema.toString());
			Double db = Math.pow(2, 31)-1;
			int i0 = lexBig.compareTo(BigDecimal.valueOf(db));
			if (i0 <= 0) {
				return true;
			}
		}
		return true;
	}
	
	public ParserVal yylval() {
		// Devuelvo el lexema por medio de ParserVal.
		return new ParserVal(lexema.toString());
	}
	
	public String tokenToString(int nroToken) {
		switch (nroToken) {
		case 36:
			return "$";
		case 40:
			return "(";
		case 41:
			return ")";
		case 42:
			return "*";
		case 43:
			return "+";
		case 44:
			return ",";
		case 45:
			return "-";
		case 47:
			return "/";
		case 58:
			return ":";
		case 59:
			return ";";
		case 60:
			return "<";
		case 61:
			return "=";
		case 62:
			return ">";
		case Parser.CADENA:
			return "CADENA";
		case Parser.CTE:
			return "CTE";
		case Parser.DISTINTO:
			return "!=";
		case Parser.ELSE:
			return "ELSE";
		case Parser.END_IF:
			return "END_IF";
		case Parser.ERROR:
			return "ERROR";
		case Parser.FLOAT:
			return "FLOAT";
		case Parser.ID:
			return "ID";
		case Parser.FUNC:
			return "FUNC";
		case Parser.IF:
			return "IF";
		case Parser.IGUAL:
			return "==";
		case Parser.LONGINT:
			return "LONGINT";
		case Parser.LOOP:
			return "LOOP";
		case Parser.MAYOR_IGUAL:
			return ">=";
		case Parser.MENOR_IGUAL:
			return "<=";
		case Parser.NI:
			return "NI";
		case Parser.OUT:
			return "OUT";
		case Parser.PROC:
			return "PROC";
		case Parser.REF:
			return "REF";
		case Parser.RETURN:
			return "RETURN";
		case Parser.THEN:
			return "THEN";
		case Parser.WHILE:
			return "WHILE";
		case 0:
			return "FIN DE ARCHIVO";
		default:
			return "";
			
		}
	}

}



