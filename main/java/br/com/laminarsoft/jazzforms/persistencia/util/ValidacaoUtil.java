package br.com.laminarsoft.jazzforms.persistencia.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidacaoUtil {

	public static boolean validaDataNascimento(String dataNascimento) {
		boolean ret = true;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		if(dataNascimento == null || dataNascimento.trim().length() != 10) {
			ret = false;
		} else {
			try {
				Date dtNascimento = sdf.parse(dataNascimento);
				String dt1 = sdf2.format(dtNascimento);
				String dt2 = sdf2.format(new Date());
				if(dt1.compareTo(dt2) > 0) {
					ret = false;
				}
			} catch (ParseException e) {
				ret = false;
			}
		}
		return ret;
	}
	
	public static boolean validaEmail(String mail) {
		boolean ret = true;
		Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
		Matcher m = p.matcher(mail);
		if(!m.find()) {
			ret = false;
		}
		return ret;
	}
	
	public static boolean validaTelefone(String telefone) {
		boolean ret = true;
		if(telefone == null || telefone.trim().length() > 11 || telefone.trim().length() < 10) {
			ret = false;
		} else {
			ret = telefone.matches("10|[1-9][1-9]\\9?[6-9][0-9]{3}[0-9]{4}") || 
					telefone.matches("10|[1-9][1-9]\\[2-5][0-9]{3}[0-9]{4}");
		}
		return ret;
	}
	
	public static boolean validaCPF(String cpf) {
		boolean ret = true;
		if(cpf == null || cpf.trim().length() != 11) {
			ret = false;
		} else {
			String numDig = cpf.substring(0, 9); 
			ret = calcDigVerif(numDig).equals(cpf.substring(9, 11));
		}
		return ret;
	}
	
	private static String calcDigVerif(String num) {  
        Integer primDig, segDig;  
        int soma = 0, peso = 10;  
        for (int i = 0; i < num.length(); i++)  
                soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;  
  
        if (soma % 11 == 0 | soma % 11 == 1)  
            primDig = new Integer(0);  
        else  
            primDig = new Integer(11 - (soma % 11));  
  
        soma = 0;  
        peso = 11;  
        for (int i = 0; i < num.length(); i++)  
                soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;  
          
        soma += primDig.intValue() * 2;  
        if (soma % 11 == 0 | soma % 11 == 1)  
            segDig = new Integer(0);  
        else  
            segDig = new Integer(11 - (soma % 11));  
  
        return primDig.toString() + segDig.toString();  
    }  
  	
}
