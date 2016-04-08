package br.com.laminarsoft.jazzforms.persistencia.model.senchatransform;

import java.util.ArrayList;
import java.util.List;

public class SenchaPicker {

	public String doneButton;
	public String cancelButton;
	
	public List<String> slotOrder = new ArrayList<String>();
	
	public SenchaPicker(){};
	public SenchaPicker(String doneButton, String cancelButton){
		this.doneButton = doneButton;
		this.cancelButton = cancelButton;
	}
	
	public SenchaPicker addSlotOrder(String order) {
		slotOrder.add(order);
		return this;
	}
}
