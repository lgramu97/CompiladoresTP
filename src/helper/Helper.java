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
	
	public void imprimir_tabla_simbolos() {
		 HashMap<String, HashMap<String, Object>> ts = new  HashMap<String, HashMap<String, Object>>();
		 HashMap<String,Object> att = new HashMap<String, Object>();
		 att.put("Tipo","Integer");
		 att.put("Contador",2);
		 att.put("Algo mas?",false);
		 ts.put("123",new HashMap<String,Object>(att));
		 ts.put("-123",new HashMap<String,Object>(att));
		 att.remove("Tipo");
		 att.put("Tipo","Float");
		 ts.put("12.f+5", new HashMap<String,Object>(att));
		 this.al.setTablaSimbolos(ts);
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
	
	public void prueba_al() {
		ArrayList<Integer> tokens = new ArrayList<>();
		while(al.getFilaActual() < al.getLineasTotales()-1) {
			int token = al.yylex();
			tokens.add(token);
			System.out.println("Token nro:   " +  token);			
		}
		System.out.println(al.getDatosTabla_simbolos());
		System.out.println();
		System.out.println();
		for(int i = 0; i< tokens.size();i++) {
			System.out.print(tokens.get(i) + "  ");
		}
	}
	
	public static void main(String[] args) {
		Helper h = new Helper();
		//h.imprimir_tabla_simbolos();
		//h.check_as5();
		//h.check_as4();
		h.prueba_al();
	}
}
