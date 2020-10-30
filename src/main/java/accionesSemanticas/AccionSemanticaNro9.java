package accionesSemanticas;

import componentes.AnalizadorLexico;

public class AccionSemanticaNro9 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.charAnterior();
		al.putError("Error: token invalido!");
		al.inicializarBuffer();
		return "ERROR";
	}

}
