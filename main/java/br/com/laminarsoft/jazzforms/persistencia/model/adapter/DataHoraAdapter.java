package br.com.laminarsoft.jazzforms.persistencia.model.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DataHoraAdapter extends XmlAdapter<String, Date> {


	@Override
	public Date unmarshal(String strdt) throws Exception {
		Date dt = null;
		if(!strdt.isEmpty()) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dt = df.parse(strdt);
		}
		return dt;
	}

	@Override
	public String marshal(Date dt) throws Exception {
		String strdt = "";
		if(dt != null) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			strdt = df.format(dt);
		}
		return strdt;
	}
}
