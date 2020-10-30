package accionesSemanticas;

import componentes.AnalizadorLexico;

public class AccionSemanticaNro3 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.appendChar(c);
		return null;
	}

}
