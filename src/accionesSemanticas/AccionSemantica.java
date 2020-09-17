package accionesSemanticas;

import analizadorLexico.AnalizadorLexico;

public abstract class AccionSemantica {
	
	public abstract String ejecutar(char c, AnalizadorLexico al);

}
