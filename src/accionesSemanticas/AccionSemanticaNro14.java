package accionesSemanticas;

import analizadorLexico.AnalizadorLexico;

public class AccionSemanticaNro14 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.nuevaLinea();
		al.putError("Error: caracter en comentario invalido    "+ c);
		return "ERROR";
	}

}
