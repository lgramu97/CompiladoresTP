package accionesSemanticas;

import analizadorLexico.AnalizadorLexico;

import java.math.BigDecimal;

public class AccionSemanticaNro4 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		//al.charAnterior();
		String lexema = al.getLexema().toString();
		float f ;
		if ( lexema.contains("f")) {
			String[] values = lexema.split("f");
			String value = values[0];
			String exponente = values[1];
			f = Float.parseFloat(value+"f");

		}else {
			f = Float.parseFloat(lexema);
		}		
		BigDecimal lexBig = new BigDecimal(f);
		float valorfinal = 3.40282347f;
		float valorbase = 1.17549435f;
		int exp = 38;
		int i0 = lexBig.compareTo(BigDecimal.valueOf(Math.pow(-valorfinal,+exp)));
		int i1 = lexBig.compareTo(BigDecimal.valueOf(Math.pow(-valorbase,-exp)));
		int i2 = lexBig.compareTo(BigDecimal.valueOf(0.0f));
		int i3 = lexBig.compareTo(BigDecimal.valueOf(Math.pow(valorbase,-exp)));
		int i4 = lexBig.compareTo(BigDecimal.valueOf(Math.pow(valorfinal,+exp)));
		
		if ((i3 > 0 && i4<0) || (i0> 0 && i1 < 0) || i2==0) {
			al.appendLexema();
			al.addTipo("Tipo", "Float");
			return "CTE";
		}else {
			al.putError("Float fuera de rango!");
			return "Error";
		}
		
		
	}

}