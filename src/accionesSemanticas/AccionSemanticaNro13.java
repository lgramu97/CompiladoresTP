package accionesSemanticas;

import componentes.AnalizadorLexico;

public class AccionSemanticaNro13 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.putError("Error: caracter invalido    "+ c);
		return "ERROR";
	}

}
