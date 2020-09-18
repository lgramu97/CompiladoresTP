package accionesSemanticas;

import analizadorLexico.AnalizadorLexico;

public class AccionSemanticaNro7 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.charAnterior();
		String lexema = al.getLexema().toString();
		if (!al.isPalabraReservada(lexema)) {
			al.putError("No es una palabra reservada!");
		}
		return null;
	}

}
