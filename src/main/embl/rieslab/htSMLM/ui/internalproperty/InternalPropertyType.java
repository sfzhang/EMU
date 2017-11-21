package main.embl.rieslab.htSMLM.ui.internalproperty;

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