package accionesSemanticas;

import componentes.AnalizadorLexico;

public class AccionSemanticaNro1 extends AccionSemantica{

	public AccionSemanticaNro1() {
		
	}
	
	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.charAnterior();
		StringBuilder lexema = al.getLexema();
		if (lexema.length() > 20) {
			lexema.delete(20, lexema.length());
			al.putError("Warning: Identificador reducido a 20 caracteres.");
		}
		if (!al.getTabla_simbolos().containsKey(lexema.toString())) {
			al.appendLexema();
			al.addTipo("Tipo","ID");
		}
		return "ID";
	}

}
