package accionesSemanticas;

import java.math.BigDecimal;

import componentes.AnalizadorLexico;

public class AccionSemanticaNro4 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		al.charAnterior();
		String lexema = al.getLexema().toString();
		BigDecimal lexBig = new BigDecimal("1.17549435e-38");
		if ( lexema.contains("f")) {
			String[] values = lexema.split("f");
			String value = values[0];
			String exponente = values[1];
			lexBig = new BigDecimal(value+"e"+exponente);
		}else {
			lexBig = new BigDecimal(lexema);
		}		
		String valorfinal = "3.40282347e";
		String valorbase = "1.17549435e";
		String exp = "38";
		int i0 = lexBig.compareTo(new BigDecimal("-"+valorfinal+"+"+exp));
		int i1 = lexBig.compareTo(new BigDecimal("-"+valorbase+"-"+exp));
		int i2 = lexBig.compareTo(new BigDecimal("0.0"));
		int i3 = lexBig.compareTo(new BigDecimal(valorbase+"-"+exp));
		int i4 = lexBig.compareTo(new BigDecimal(valorfinal+"+"+exp));
		if ((i3 > 0 && i4<0) || (i0> 0 && i1 < 0) || i2==0) {
			al.appendLexema();
			al.addTipo("Tipo", "FLOAT");
			return "CTE";
		}else {
			al.putError("Float fuera de rango!.");
			al.inicializarBuffer();
			return "ERROR";
		}
		
		
	}

}