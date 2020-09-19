package accionesSemanticas;

import analizadorLexico.AnalizadorLexico;

public class AccionSemanticaNro12 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.putError("Error: token invalido    "+ c);
		return "ERROR";
	}

}
