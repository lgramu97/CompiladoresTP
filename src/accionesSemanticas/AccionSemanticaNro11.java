package accionesSemanticas;

import analizadorLexico.AnalizadorLexico;

public class AccionSemanticaNro11 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.deleteLastChar();
		return null;
	}

}
