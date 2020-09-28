package helper;

import java.util.ArrayList;
import java.util.HashMap;

import accionesSemanticas.AccionSemantica;
import accionesSemanticas.AccionSemanticaNro4;
import accionesSemanticas.AccionSemanticaNro5;
import accionesSemanticas.AccionSemanticaNro6;
import analizadorLexico.AnalizadorLexico;

public class Helper {

	private AnalizadorLexico al = null;

	public Helper() {
		al = new AnalizadorLexico();
	}
	
	public void set_tabla_simbolos() {
		 HashMap<String, HashMap<String, Object>> ts = new  HashMap<String, HashMap<String, Object>>();
		 HashMap<String,Object> att = new HashMap<String, Object>();
		 att.put("Tipo","LONGINT");
		 att.put("Cantidad",2);
		 att.put("Algo mas?",false);
		 ts.put("2147483647",new HashMap<String,Object>(att));
		 ts.put("2147483648",new HashMap<String,Object>(att));
		 att.remove("Tipo");
		 att.put("Tipo","Float");
		 ts.put("12.f+5", new HashMap<String,Object>(att));
		 this.al.setTablaSimbolos(ts);
	}
	
	public void imprimir_tabla_simbolos() {
		this.set_tabla_simbolos();
		System.out.println(this.al.getDatosTabla_simbolos());
	}
	
	public void check_as5() {
		Double db = Math.pow(2, 32) -1 ;
		StringBuilder lexema = new StringBuilder("0_");
		al.setLexema(lexema);
		AccionSemantica as5 = new AccionSemanticaNro5();
		as5.ejecutar('l', al);
		System.out.println(al.getLexema() + "  Size " + al.getLexema().length());
		System.out.println(al.getDatosTabla_simbolos());
		System.out.println("ERRORES: " + al.getErrores().size());
	}
	
	public void check_as4() {
		StringBuilder lexema = new StringBuilder("0.84");
		al.setLexema(lexema);
		AccionSemantica as4 = new AccionSemanticaNro4();
		as4.ejecutar('j', al);
		System.out.println(al.getLexema());
		System.out.println(al.getDatosTabla_simbolos());
		System.out.println("ERRORES: " + al.getErrores().size());

	}
	
	public void check_as15() {
		StringBuilder lexema = new StringBuilder();
	}
	
	public void prueba_al() {
		ArrayList<Integer> tokens = new ArrayList<>();
		while(al.getFilaActual() < al.getLineasTotales()) {
			int token = al.yylex();
			tokens.add(token);
			System.out.println("Token nro:   " +  token);			
		}

		for(int i = 0; i< tokens.size();i++) {
			System.out.print(tokens.get(i) + "  ");
		}
		System.out.println(al.getErrores());
	}
	
	public void check_longint_rango(String lexema) {
		this.set_tabla_simbolos();
		if (al.check_rango_longint(lexema)) {
			System.out.println("LONGINT dentro de rango");
		}else
			System.out.println("LONGINT fuera de rango");

	}
	
	public static void main(String[] args) {
		Helper h = new Helper();
		//h.imprimir_tabla_simbolos();
		//h.check_as5();
		//h.check_as4();
		h.prueba_al();
		//h.check_longint_rango("2147483647");
		//h.check_longint_rango("2147483648");

	}
}
