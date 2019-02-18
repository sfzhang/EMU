package main.java.embl.rieslab.emu.ui.internalproperties;

public enum InternalPropertyType{
	INTEGER("Integer"), DOUBLE("Double"), STRING("String"); 

	private String value; 
	
	private InternalPropertyType(String value) { 
		this.value = value; 
	}
	
	public String getTypeValue() {
		return value;
	} 
}; 