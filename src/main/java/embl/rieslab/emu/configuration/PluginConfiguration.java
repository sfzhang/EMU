package main.java.embl.rieslab.emu.configuration;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class representing a plugin configuration. It holds the name of an EMU plugin
 * and maps of the plugin properties and parameters names and values. The Class
 * is written and read using jackson ObjectMapper by the ConfigurationIO class.
 * 
 * @author Joran Deschamps
 *
 */
public class PluginConfiguration implements Comparable<PluginConfiguration>{
	
	private String configurationName;
	private String pluginName;
	private TreeMap<String,String> properties;
	private TreeMap<String,String> parameters;
	
	public PluginConfiguration(){
		// do nothing
	}
	
	public void configure(String configurationName, String pluginName, Map<String,String> props, Map<String,String> params){
		this.configurationName = configurationName;
		this.pluginName = pluginName;

		properties = new TreeMap<String,String>(props);
		parameters = new TreeMap<String,String>(params);	
	}

	public String getConfigurationName(){
		return configurationName;
	}
	
	public void setConfigurationName(String configurationName){
		this.configurationName = configurationName;
	}

	public String getPluginName(){
		return pluginName;
	}
	
	public void getPluginName(String pluginName){
		this.pluginName = pluginName;
	}
	
	public TreeMap<String,String> getProperties(){
		return properties;
	}

	public void setProperties(Map<String,String> properties){
		this.properties = new TreeMap<String,String>(properties);	
	}
	
	public void setParameters(Map<String,String> parameters){
		this.parameters = new TreeMap<String,String>(parameters);	
	}

	public TreeMap<String,String> getParameters(){
		return parameters;
	}
	
	@Override
	public int compareTo(PluginConfiguration OtherUIPlugin) {
		// TODO should also check if they are related to the same plugin otherwise we might have inconsistencies
		return configurationName.compareTo(OtherUIPlugin.getConfigurationName());
	}
	
}
