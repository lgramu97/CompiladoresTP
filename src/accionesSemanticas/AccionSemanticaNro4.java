package accionesSemanticas;

import analizadorLexico.AnalizadorLexico;

import java.math.BigDecimal;

public class AccionSemanticaNro4 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.charAnterior();
		String lexema = al.getLexema().toString();
		BigDecimal lexBig = new BigDecimal(lexema);
		int i0 = lexBig.compareTo(BigDecimal.valueOf(-3.40282347f+38));
		int i1 = lexBig.compareTo(BigDecimal.valueOf(-1.17549435f-38));
		int i2 = lexBig.compareTo(BigDecimal.valueOf(0.0f));
		int i3 = lexBig.compareTo(BigDecimal.valueOf(1.17549435f-38));
		int i4 = lexBig.compareTo(BigDecimal.valueOf(3.40282347f+38));
		if (i0 <= 0 || (i1 >= 0 && i2 < 0.0f) || (i2 > 0 && i3 <= 0) || (i4 >= 0)) {
			al.putError("Float fuera de rango!");
			return "Error";
		}
		al.appendLexema();
		al.addTipo("Tipo", "Float");
		return "CTE";
	}

}