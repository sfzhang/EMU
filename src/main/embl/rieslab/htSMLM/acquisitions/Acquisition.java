package main.embl.rieslab.htSMLM.acquisitions;

import java.util.HashMap;

import main.embl.rieslab.htSMLM.micromanager.properties.ConfigurationGroup;

import org.micromanager.api.SequenceSettings;

public abstract class Acquisition {

	private SequenceSettings settings_;
	private AcquisitionType type_;
	private String path_, name_;
	private int numframes_, intervalframes_;
	private boolean activate_ = false, useconfig_ = false;
	private ConfigurationGroup group_;
	private String configname_; 
	private HashMap<String,String> propvalues_;
	
	public Acquisition(AcquisitionType type, String path, String name, ConfigurationGroup group, String configname, 
			int numframes, int intervalframes, boolean activate, HashMap<String,String> propvalues){
		type_ = type;
		path_ = path;
		numframes_ = numframes;
		intervalframes_ = intervalframes;
		activate_ = activate;
		name_ = name;
		propvalues_ = propvalues;
		
		if(group != null && group.hasConfiguration(configname)){
			group_ = group;
			configname_  = configname;
			useconfig_ = true;
		}
		
		setSequenceSettings();
	}
	
	private void setSequenceSettings(){
		settings_ = new SequenceSettings();
		
		settings_.numFrames = numframes_;
		settings_.intervalMs = intervalframes_;
		settings_.root = path_;
		settings_.save = true;
		settings_.timeFirst = true;
		settings_.usePositionList = false;
	}
	
	public SequenceSettings getSettings(){
		return settings_;
	}
	
	public String getType(){
		return type_.getTypeValue();
	}
	
	public boolean useActivation(){
		return activate_;
	}

	public String getName(){
		return name_;
	}
	
	public String getPath(){
		return path_;
	}
	
	public boolean useConfig(){
		return useconfig_;
	}
	
	public String getConfigurationGroup(){
		if(useconfig_){
			return group_.getName();
		}
		return null;
	}
	
	public String getConfigurationName(){
		if(useconfig_){
			return configname_;
		}
		return null;
	}
	
	public HashMap<String,String> getPropertyValues(){
		return propvalues_;
	}
}
