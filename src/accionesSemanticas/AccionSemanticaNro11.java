package accionesSemanticas;

import componentes.AnalizadorLexico;

public class AccionSemanticaNro11 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.deleteLastChar();
		al.appendChar(c);
		al.nuevaLinea();
		return null;
	}

}
