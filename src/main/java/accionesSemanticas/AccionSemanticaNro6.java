package accionesSemanticas;

import componentes.AnalizadorLexico;

public class AccionSemanticaNro6 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.nuevaLinea();
		return null;
	}

}
