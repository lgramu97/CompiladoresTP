package helper;

import java.util.HashMap;

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
	
	public static void main(String[] args) {
		Helper h = new Helper();
		h.imprimir_tabla_simbolos();
	}
}
