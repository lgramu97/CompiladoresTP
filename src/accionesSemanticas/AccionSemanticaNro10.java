package accionesSemanticas;

import componentes.AnalizadorLexico;

public class AccionSemanticaNro10 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.appendChar(c);
		al.appendLexema();
		al.addTipo("Tipo", "CADENA");
		return "CADENA";
	}

}
