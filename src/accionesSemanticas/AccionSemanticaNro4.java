package accionesSemanticas;

import java.math.BigDecimal;

import componentes.AnalizadorLexico;

public class AccionSemanticaNro4 extends AccionSemantica{

	@Override
	public String ejecutar(char c, AnalizadorLexico al) {
		/*al.charAnterior();
		String lexema = al.getLexema().toString();
		double d;
		if ( lexema.contains("f")) {
			String[] values = lexema.split("f");
			String value = values[0];
			String exponente = values[1];
			d = Math.pow(Double.parseDouble(value), Double.parseDouble(exponente));
			System.out.println("Value " + value + "  " + "EXP " + exponente);

		}else {
			d = Double.parseDouble(lexema);
		}		
		System.out.println("LEXEMA: " + d);
		BigDecimal lexBig = new BigDecimal(d);
		System.out.println("LEXBIG " + lexBig);
		double valorfinal = 3.40282347;
		double valorbase = 1.17549435;
		System.out.println("Fin " + valorfinal + "  BASE " + valorbase);
		int exp = 38;
		int i0 = lexBig.compareTo(BigDecimal.valueOf(Math.pow(-valorfinal,+exp)));
		int i1 = lexBig.compareTo(BigDecimal.valueOf(Math.pow(-valorbase,-exp)));
		int i2 = lexBig.compareTo(BigDecimal.valueOf(0.0f));
		System.out.println("DOuble " + (BigDecimal.valueOf(Math.pow(valorbase,-exp))));
		int i3 = lexBig.compareTo(BigDecimal.valueOf(Math.pow(valorbase,-exp)));
		int i4 = lexBig.compareTo(BigDecimal.valueOf(Math.pow(valorfinal,+exp)));
		
		if ((i3 > 0 && i4<0) || (i0> 0 && i1 < 0) || i2==0) {
			System.out.println("CONDICION: i0 " + (i0>0));
			System.out.println("CONDICION: i1 " + (i1<0));
			System.out.println("CONDICION: i2 " + (i2==0));
			System.out.println("CONDICION: i3 " + (i3>0));			
			System.out.println("CONDICION: i4 " + (i4<0));
			al.appendLexema();
			al.addTipo("Tipo", "Float");
			return "CTE";
		}else {
			al.putError("Float fuera de rango!");
			return "Error";
		}*/
		
		al.charAnterior();
		String lexema = al.getLexema().toString();
		BigDecimal lexBig = new BigDecimal("1.17549435e-38");
		if ( lexema.contains("f")) {
			String[] values = lexema.split("f");
			String value = values[0];
			String exponente = values[1];
			lexBig = new BigDecimal(value+"e"+exponente);
			System.out.println("Value " + value + "  " + "EXP " + exponente);
		}else {
			lexBig = new BigDecimal(lexema);
		}		
		System.out.println("LEXBIG " + lexBig);
		String valorfinal = "3.40282347e";
		String valorbase = "1.17549435e";
		System.out.println("Fin " + valorfinal + "  BASE " + valorbase);
		String exp = "38";
		int i0 = lexBig.compareTo(new BigDecimal("-"+valorfinal+"+"+exp));
		int i1 = lexBig.compareTo(new BigDecimal("-"+valorbase+"-"+exp));
		int i2 = lexBig.compareTo(new BigDecimal("0.0"));
		int i3 = lexBig.compareTo(new BigDecimal(valorbase+"-"+exp));
		int i4 = lexBig.compareTo(new BigDecimal(valorfinal+"+"+exp));
		
		if ((i3 > 0 && i4<0) || (i0> 0 && i1 < 0) || i2==0) {
			System.out.println("CONDICION: i0 " + (i0>0));
			System.out.println("CONDICION: i1 " + (i1<0));
			System.out.println("CONDICION: i2 " + (i2==0));
			System.out.println("CONDICION: i3 " + (i3>0));			
			System.out.println("CONDICION: i4 " + (i4<0));
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