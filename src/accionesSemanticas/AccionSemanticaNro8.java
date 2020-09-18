package accionesSemanticas;

import analizadorLexico.AnalizadorLexico;

public class AccionSemanticaNro8 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.charAnterior();
		return null;
	}

}
