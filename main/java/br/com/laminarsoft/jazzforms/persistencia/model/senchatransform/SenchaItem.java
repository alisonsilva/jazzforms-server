package br.com.laminarsoft.jazzforms.persistencia.model.senchatransform;

import java.util.ArrayList;
import java.util.List;

public class SenchaItem {
	public String docked;
	public String align;
	public String xtype;
	public String ui;
	
	public String baseCls;
	public String cls;
	public String itemId;
	public String style;
	
	public List<SenchaItem> items = new ArrayList<SenchaItem>();
	
	/********* button *******/
	public String badgeText;
	public String iconCls;
	public String text;
	
	/********* field *******/
	public String label;
	public String labelAlign;
	public String labelWidth;
	public String name;
	public String value;
	public Boolean required;
	public Boolean labelWrap;	
	
	/********* text *******/
	public String placeHolder;
	public Integer maxLength;
	public Boolean autoCapitalize;
	public Boolean autoComplete;
	public Boolean autoCorrect;
	public Boolean readOnly;
	
	/****** text area ******/
	public Integer maxRows;
	
	/****** slider ********/
	public Integer increment;

	/****** select ********/
	public String displayField;
	public String hiddenName;
	public String store;
	public SenchaPicker defaultPhonePickerConfig;
	public SenchaPicker defaultTabletPickerConfig;
	public List<SenchaOption> options;
	
	/****** checkbox ******/
	public Boolean checked;
	
	/****** datepicker *****/
	public Integer yearFrom;
	public Integer yearTo;	
	public String picker;
	
	
	/***** number *******/
	public Double maxValue;
	public Double minValue;
	public Double stepValue;
	
	/***** spinner *******/
	public Boolean cycle;
	public Double defaultValue;
	public Boolean accelerateOnTapHold;
	
	/******* container *******/
	public String itemTpl;
	public Boolean modal;
	public Boolean vscrollable;
	public Boolean hscrollable;
	public String maxWidth;
	public String maxHeight;
	
	/******* form panel *******/
	public String method;
	public String record;
	public String url;
	public Boolean scrollable;
	public Boolean submitOnAction;
	
	/****** field set ********/
	public String instructions;
	public String title;
	
	/****** sheet *******/
	public String enter;
	public String exit;
	public String hideAnimation;
	public String showAnimation;
	public Boolean stretchX;
	public Boolean stretchY;
	
	/***** data view *****/
	public String emptyText;
	public String data;
	public Boolean disableSelection;
	public Boolean useComponents = false;
	public String defaultType;
	public String loadingText;
	public String mode;
	public Integer maxItemCache;
	public String triggerEvent;
	public String implementacaoCustomizada;
	public String itemCls;
	public String pressedCls;
	public String selectedCls;
	
	/***** carousel ****/
	public String direction;
	public Boolean indicator;
	
	/**** tab panel *****/
	public String tabBarPosition;
	
	/****** container ********/
	public String layout;
	public String margin;
	public String html;
	public Boolean fullscreen;
	
	/**** chart **************/
	public String series;
	public String axes;
	public String legend;
	public String interactions;
	public String theme;
	public Integer insetPadding;
	public boolean shadow;
	public boolean animate;
	public boolean flipXY;
	public String colors;
	

	public SenchaImplementacao.Impl[] listenersarray;
	public SenchaImplementacao listeners;
	
}
