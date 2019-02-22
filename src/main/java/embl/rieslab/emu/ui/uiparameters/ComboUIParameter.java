package main.java.embl.rieslab.emu.ui.uiparameters;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.UIParameter;

/**
 * UIParameter represented by a String value that can only take a finite number of values.
 * The possible values are passed to the constructor and only values contained in the array 
 * will be accepted, otherwise the first value will be chosen.
 * <pre>
 * This UIParameter can be used to define a set of possible values.
 * 
 * @author Joran Deschamps
 *
 */
public class ComboUIParameter extends UIParameter<String> {

	private String[] combovalues_;
	
	/**
	 * Constructor, a String array of allowed values must be passed as well as the index of the default
	 * value in the array. If the index is not valid, then the first entry of the array is used as 
	 * default.
	 * 
	 * @param owner ConfigurablePanel that instantiated the UIParameter
	 * @param name Name of the UIParameter
	 * @param description Description of the UIParameter
	 * @param allowedValues Array of allowed values the UIParameter can take
	 * @param ind Index of the UIParameter default value
	 */
	public ComboUIParameter(ConfigurablePanel owner, String name, String description, String[] allowedValues, int ind) {
		super(owner, name, description);
		
		combovalues_ = allowedValues;
		if(ind >= 0 && ind<combovalues_.length){
			setValue(combovalues_[ind]);
		} else {
			setValue(combovalues_[0]);
		}
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public UIParameterType getType() {
		return UIParameterType.COMBO;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public boolean isSuitable(String val) {
		for(int i=0;i<combovalues_.length;i++){
			if(combovalues_[i].equals(val)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public String convertValue(String val) {
		return val;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public String getStringValue() {
		return getValue();
	}
	
	/**
	 * @inheritDoc
	 */
	public String[] getComboValues(){
		return combovalues_;
	}
	
}
