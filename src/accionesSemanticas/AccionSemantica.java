package accionesSemanticas;

import componentes.AnalizadorLexico;

public abstract class AccionSemantica {
	
	public abstract String ejecutar(char c, AnalizadorLexico al);

}
