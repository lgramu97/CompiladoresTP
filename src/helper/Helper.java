package helper;

import java.util.HashMap;

import accionesSemanticas.AccionSemantica;
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
		StringBuilder lexema = new StringBuilder("125_");
		al.setLexema(lexema);
		AccionSemantica as5 = new AccionSemanticaNro5();
		as5.ejecutar('l', al);
		System.out.println(al.getLexema() + "  Size " + al.getLexema().length());
	}
	
	public static void main(String[] args) {
		Helper h = new Helper();
		h.imprimir_tabla_simbolos();
		h.check_as5();
	}
}
