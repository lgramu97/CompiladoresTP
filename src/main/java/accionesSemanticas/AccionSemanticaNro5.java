package accionesSemanticas;

import java.math.BigDecimal;

import componentes.AnalizadorLexico;

public class AccionSemanticaNro5 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.deleteLastChar();
		BigDecimal lexBig = new BigDecimal(al.getLexema().toString());
		Double db = Math.pow(2, 31);//Comparo contra el 2147483648.
		int i0 = lexBig.compareTo(BigDecimal.valueOf(db)); // Si el valor es -1 o 0 lo acepto porque es lexicograficamente anterior.
		if (i0 <= 0) {
			al.appendLexema();
			al.addTipo("Tipo","LONGINT");
			return "CTE";
		}
		al.putError("Long Int fuera de rango.");
		al.inicializarBuffer();
		return "ERROR";
	}

}
