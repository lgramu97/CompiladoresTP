package accionesSemanticas;

import java.math.BigDecimal;

import analizadorLexico.AnalizadorLexico;

public class AccionSemanticaNro5 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		StringBuilder lexema = al.getLexema();
		lexema.deleteCharAt(lexema.length()-1);
		BigDecimal lexBig = new BigDecimal(lexema.toString());
		Double db = Math.pow(2, 32) - 1;
		int i0 = lexBig.compareTo(BigDecimal.valueOf(db));
		int i1 = lexBig.compareTo(BigDecimal.valueOf(0));
		if (i0 < 0 && i1 >= 0) {
			al.appendLexema();
			al.addTipo("Tipo","LONG INT");
			return "CTE";
		}
		al.putError("Long Int fuera de rango.");
		return "ERROR";
	}

}
