package br.com.laminarsoft.jazzforms.persistencia.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {
	public static final int DIFERENCA_MINUTOS = 2;
	public static final int DIFERENCA_HORAS = 3;
	public static final int DIFERENCA_DIAS = 4;
	
	public static long diasDiferenca(Date dataInicial, Date dataFinal, int tipoDiferenca) {
		long diff = dataFinal.getTime() - dataInicial.getTime();
		diff = Math.abs(diff);
		
		long diffResult = diff;
		switch(tipoDiferenca) {
		case DIFERENCA_MINUTOS : 
			diffResult = TimeUnit.MILLISECONDS.toMinutes(diff);
			break;
		case DIFERENCA_HORAS :
			diffResult = TimeUnit.MILLISECONDS.toHours(diff);
			break;
		case DIFERENCA_DIAS :
			diffResult = TimeUnit.MICROSECONDS.toDays(diff);
			break;
		 
		}
		return diffResult;
	}
}
