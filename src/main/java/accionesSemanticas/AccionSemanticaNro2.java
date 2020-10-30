package accionesSemanticas;

import componentes.AnalizadorLexico;

public class AccionSemanticaNro2 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.inicializarBuffer();
		al.appendChar(c);
		return null;
	}

}
