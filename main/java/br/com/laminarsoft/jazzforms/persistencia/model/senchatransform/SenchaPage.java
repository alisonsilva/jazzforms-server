package br.com.laminarsoft.jazzforms.persistencia.model.senchatransform;

import java.util.ArrayList;
import java.util.List;

public class SenchaPage {

	public String extend;
	public String xtype;
	public String itemId;
	public boolean fullScreen;
	public SenchaImplementacao listeners = null;
	public String layout; 
	
	public List<String> requires = new ArrayList<String>();
	public List<SenchaItem> items = new ArrayList<SenchaItem>();
}
