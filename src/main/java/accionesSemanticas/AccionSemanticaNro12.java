package accionesSemanticas;

import componentes.AnalizadorLexico;

public class AccionSemanticaNro12 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.putError("Error: " + c +  " token invalido. Construccion anterior: "+ al.getLexema());
		return "ERROR";
	}

}
