package com.code.ui.converters;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("com.code.ui.converters.CSNumberConverter")
public class CSNumberConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		try{
			if(value == null || value.equals(""))
				return 0.00D;
			
			return Double.parseDouble(value.replaceAll(",", ""));
		}
		catch(NumberFormatException e){
			if(value.contains("-")){
				return (Double.parseDouble(value.replaceAll(",", "").replaceAll("-", "")) * -1);
			}		
			
			throw new ConverterException();
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		if(value == null)
			return "0.00";
		
		NumberFormat formatter = new DecimalFormat();
		formatter.setGroupingUsed(true);
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		
		return formatter.format(value);
	}

}