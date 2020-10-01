package accionesSemanticas;

import componentes.AnalizadorLexico;

public class AccionSemanticaNro15 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.putError("Error: cadena invalida    "+ al.getLexema());
		al.inicializarBuffer();
		return "ERROR";
	}

}
