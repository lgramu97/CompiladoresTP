package accionesSemanticas;

import componentes.AnalizadorLexico;

public class AccionSemanticaNro14 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.putError("Error: caracter en comentario invalido    "+ c);
		al.nuevaLinea();
		al.inicializarBuffer();
		return "ERROR";
	}

}
