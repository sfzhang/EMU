package main.java.de.embl.rieslab.emu.ui.uiproperties;

import main.java.de.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.de.embl.rieslab.emu.ui.uiproperties.UIProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.flag.PropertyFlag;

/**
 * A UIProperty with multiple allowed states, whose values are unknown at compilation time. Upon instantiation
 * the number of states is set, while the user can change the values of each state in the configuration wizard.
 * 
 * @author Joran Deschamps
 *
 */
public class MultiStateUIProperty extends UIProperty{

	public final static String STATE = " state ";
	
	private String[] states_;
	private String[] statenames_;
	
	/**
	 * Constructor with a PropertyFlag.
	 * 
	 * @param owner ConfigurablePanel that instantiated the UIProperty
	 * @param label Name of the UIProperty
	 * @param description Description of the UIProperty
	 * @param flag Flag of the UIProperty
	 * @param size Number of allowed states
	 */
	public MultiStateUIProperty(ConfigurablePanel owner, String label, String description, PropertyFlag flag, int size) {
		super(owner, label, description, flag);

		states_ = new String[size];
		statenames_ = new String[size];
		for(int i=0;i<size;i++){
			states_[i] = "";
			statenames_[i] = "State"+i;
		}
	}	
	/**
	 * Constructor without a PropertyFlag.
	 * 
	 * @param owner ConfigurablePanel that instantiated the UIProperty
	 * @param label Name of the UIProperty
	 * @param description Description of the UIProperty
	 * @param size Number of allowed states
	 */
	public MultiStateUIProperty(ConfigurablePanel owner, String label, String description, int size) {
		super(owner, label, description);

		states_ = new String[size];
		statenames_ = new String[size];
		for(int i=0;i<size;i++){
			states_[i] = "";
			statenames_[i] = "State"+i;
		}
	}

	/**
	 * Sets values for the states of the UI property. If the array of values is too long, then the supernumerary values are ignored. 
	 * If the array is too short, then the corresponding states are modified while the other ones are left unchanged.
	 * 
	 * @param vals Array of values.
	 */
	public boolean setStateValues(String[] vals){
		if(vals == null){
			return false;
		}
		
		for(int i=0;i<vals.length;i++){
			if(vals[i] == null || !isValueAllowed(vals[i])){
				return false;
			}
		}
		
		
		if(vals.length == states_.length){
			states_ = vals;
		} else if (vals.length > states_.length){
			for(int i=0; i<states_.length;i++){
				states_[i] = vals[i];
			}
		} else {
			for(int i=0; i<vals.length;i++){
				states_[i] = vals[i];
			}
		}
		return true;
	}
	
	/**
	 * Gives names to the states.
	 * 
	 * @param stateNames State names
	 */
	public void setStatesName(String[] stateNames){
		if(stateNames.length == statenames_.length){
			statenames_ = stateNames;
		} else if (stateNames.length > statenames_.length){
			for(int i=0; i<statenames_.length;i++){
				statenames_[i] = stateNames[i];
			}
		} else {
			for(int i=0; i<stateNames.length;i++){
				statenames_[i] = stateNames[i];
			}
		}
	}
	
	/**
	 * Returns the number of states.
	 * 
	 * @return Number of states.
	 */
	public int getNumberOfStates(){
		return states_.length;
	}
	
	/**
	 * Returns the position of the states corresponding to the value val. If the value is not amongst 
	 * the states, returns 0.
	 * 
	 * @param val The state value.
	 * @return The position of value among the states, or 0 if not found.
	 */
	public int getStatePositionNumber(String val){
		for(int i=0;i<states_.length;i++){
			if(states_[i].equals(val)){
				return i;
			}
		}
		return 0;
	}
	
	/**
	 * Returns the value of the state in position pos.
	 * 
	 * @param pos Position of the state.
	 * @return Value of the state.
	 */
	public String getStateValue(int pos){
		if(pos >= 0 && pos<states_.length){
			return states_[pos];
		}
		return "";
	}
	
	/**
	 * Returns the name of the state in position pos.
	 * 
	 * @param pos Position of the state.
	 * @return Name of the state.
	 */
	public String getStateName(int pos){
		if(pos >= 0 && pos<states_.length){
			return statenames_[pos];
		}
		return "";
	}
	
	/**
	 * Returns the name of the state corresponding to value. The first
	 * state will be returned if the value does not correspond to any state.
	 * 
	 * @param value Value
	 * @return Name of the state, or the first state if value does not correspond to any state.
	 */
	public String getStateNameFromValue(String value){
		for(int i=0;i<states_.length;i++){
			if(states_[i].equals(value)){
				return statenames_[i];
			}
		}	
		return statenames_[0];
	}
	
	
	/**
	 * Sets the value of the MMProperty to {@code val} if {@code val}
	 * equals either one of the state values or one of the state names.
	 * 
	 */
	@Override
	public void setPropertyValue(String val) {
		if(isAssigned()) {
			for(int i=0;i<states_.length;i++){
				if(states_[i].equals(val)){
					getMMProperty().setStringValue(val, this);
					return;
				}
			}
			for(int i=0;i<statenames_.length;i++){
				if(statenames_[i].equals(val)){
					getMMProperty().setStringValue(states_[i], this);
					return;
				}
			}
		}
	}
	
	/**
	 * Returns the generic name for state i.
	 * 
	 * @param i State number
	 * @return Generic name
	 */
	public static String getConfigurationStateName(int i){
		return STATE+i;
	}
	
	/**
	 * Returns the generic state name for String search and comparison: ".*"+STATE+"\\d+".
	 * 
	 * @return generic state name
	 */
	public static String getGenericStateName(){
		return ".*"+STATE+"\\d+";
	}
		
	/**
	 * Returns the names of the states.
	 * 
	 * @return State names
	 */
	public String[] getStatesName(){
		return statenames_;
	}
}
