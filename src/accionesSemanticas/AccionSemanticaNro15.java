package accionesSemanticas;

import analizadorLexico.AnalizadorLexico;

public class AccionSemanticaNro15 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		if (al.fin_archivo()) {
			al.putError("Error: cadena invalida    "+ al.getLexema());
			return "ERROR";
		}
		return null;
	}

}
