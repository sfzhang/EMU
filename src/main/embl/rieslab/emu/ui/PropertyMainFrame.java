package main.embl.rieslab.emu.ui;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import main.embl.rieslab.emu.controller.SystemController;
import main.embl.rieslab.emu.controller.SystemDialogs;
import main.embl.rieslab.emu.tasks.TaskHolder;
import main.embl.rieslab.emu.ui.internalproperty.IntInternalProperty;
import main.embl.rieslab.emu.ui.internalproperty.IntInternalPropertyValue;
import main.embl.rieslab.emu.ui.internalproperty.InternalProperty;
import main.embl.rieslab.emu.ui.internalproperty.InternalPropertyType;
import main.embl.rieslab.emu.ui.uiparameters.UIParameter;
import main.embl.rieslab.emu.ui.uiproperties.UIProperty;
import mmcorej.CMMCore;

/**
 * 
 * 
 * @author Joran Deschamps
 *
 */
public abstract class PropertyMainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6852560487523093601L;

	private ArrayList<PropertyPanel> panels_;
	private SystemController controller_;
	private PropertyMainFrameInterface interface_;
    private JMenu switch_plugin, switch_configuration;
	
	/**
	 * 
	 * @param title
	 * @param controller
	 */
	public PropertyMainFrame(String title, SystemController controller){
		controller_ = controller;
		
		panels_ = new ArrayList<PropertyPanel>();
		
        this.setTitle(title);
		
    	this.addWindowListener(new WindowAdapter() {
    	    @Override
    	    public void windowClosing(WindowEvent e) {
    	    	shutDownAllPropertyPanels();
    	    }
    	});

        setUpMenu();
		initComponents();
		linkInternalProperties();
		interface_ = generateInterface();
	}

	private void setUpMenu() {

		JMenuBar mb=new JMenuBar(); 
        
		JMenu menu = new JMenu("Menu");
		JMenuItem wiz = new JMenuItem(new AbstractAction("Settings Wizard") {
			private static final long serialVersionUID = -8992610502306964249L;

			public void actionPerformed(ActionEvent e) {
				boolean b = controller_.launchWizard();
				if (!b) {
					JOptionPane.showMessageDialog(null,
							"Configuration wizard already running.",
							"Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		switch_plugin = new JMenu("Switch plugin");
		switch_configuration = new JMenu("Switch configuration");
		
		JMenuItem about = new JMenuItem(new AbstractAction("About") {
			private static final long serialVersionUID = -5519431309736542210L;

			public void actionPerformed(ActionEvent e) {
				SystemDialogs.showAboutUIDevInt();
			}
		});
		
		menu.add(wiz);
		menu.add(switch_plugin);
		menu.add(switch_configuration);
		menu.add(about);
		mb.add(menu);
        
        this.setJMenuBar(mb); 
	}

	public PropertyMainFrameInterface getInterface(){
		return interface_;
	}
	
	@SuppressWarnings("rawtypes")
	private PropertyMainFrameInterface generateInterface() {
		Iterator<PropertyPanel> it = this.getPropertyPanels().iterator();
		PropertyPanel pan;
		HashMap<String, UIProperty> uiprops = new HashMap<String,UIProperty>();
		HashMap<String, UIParameter> uiparams = new HashMap<String,UIParameter>();
		HashMap<String, TaskHolder> tasks = new HashMap<String,TaskHolder>();
		
		while (it.hasNext()) { // loops over the PropertyPanel contained in the MainFrame
			pan = it.next();
			
			// adds all the UIProperties, since their name contains their parent PropertyPanel name
			// there is no collision
			uiprops.putAll(pan.getUIProperties()); 
			
			// adds all the UIParameters, in case of collision the first UIParameter has priority
			// and substituted to the second UIParameter in its owner PropertyPanel: "same name" = "same parameter"
			HashMap<String,UIParameter> panparam = pan.getUIParameters();
			Iterator<String> paramit = panparam.keySet().iterator();
			ArrayList<String> subst = new ArrayList<String>();
			while(paramit.hasNext()){
				String param = paramit.next();
				
				if(!uiparams.containsKey(param)){ // if param doesn't exist already, adds it
					uiparams.put(param, panparam.get(param));
				} else if(uiparams.get(param).getType().equals(panparam.get(param).getType())){
					// if it already exists and the new parameter is of the same type than the one
					// previously added to the HashMap, then add to array subst
					subst.add(param);
				} 
				// if it is not of the same type, it is then ignored
			}
			// avoid concurrent modification of the hashmap, by substituting the UIParameter in the 
			// second PropertyPanel
			for(int i=0;i<subst.size();i++){
				pan.substituteParameter(subst.get(i), uiparams.get(subst.get(i)));
			}

			// gets tasks
			if(pan instanceof TaskHolder){
				tasks.put(((TaskHolder) pan).getTaskName(), (TaskHolder) pan);
			}
		}	

		return new PropertyMainFrameInterface(panels_, uiprops, uiparams, tasks);
	}
	
	protected void registerPropertyPanel(ArrayList<PropertyPanel> panels){
		panels_.addAll(panels);
	}
	
	protected void registerPropertyPanel(PropertyPanel pan){
		panels_.add(pan);
	}
	
	public ArrayList<PropertyPanel> getPropertyPanels(){
		return panels_;
	}
	
	public void updateAllPropertyPanels(){
		Iterator<PropertyPanel> it = panels_.iterator();
		PropertyPanel pan;
		while(it.hasNext()){
			pan = it.next();

			pan.updateAllProperties();
			pan.updateAllParameters();
		}
	}
	
	public void shutDownAllPropertyPanels(){
		Iterator<PropertyPanel> it = panels_.iterator();
		PropertyPanel pan;
		while(it.hasNext()){
			pan = it.next();
			pan.shutDown();
		}
		this.dispose();
	}

	protected CMMCore getCore(){
		return controller_.getCore();
	}
	
	protected SystemController getController(){
		return controller_;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void linkInternalProperties(){
		Iterator<PropertyPanel> panelsIt = panels_.iterator();
		HashMap<String, InternalProperty> internalproperties = new HashMap<String, InternalProperty>();
		HashMap<String, InternalProperty> internalpropertiesCopy;
		while(panelsIt.hasNext()){
			internalproperties.putAll(panelsIt.next().getInternalProperties());
		}
		
		Iterator<String> propIt1 = internalproperties.keySet().iterator();
		Iterator<String> propIt2;
		String firstproperty, secondproperty;
		while(propIt1.hasNext()){
			firstproperty = propIt1.next();
			if(!internalproperties.get(firstproperty).isAllocated()){
				internalpropertiesCopy = (HashMap<String, InternalProperty>) internalproperties.clone();
				internalpropertiesCopy.remove(firstproperty);
				propIt2 = internalpropertiesCopy.keySet().iterator();
				while(propIt2.hasNext()){
					secondproperty = propIt2.next();
					if(internalproperties.get(secondproperty).getName().equals(internalproperties.get(firstproperty).getName())){
						String firstType = internalproperties.get(firstproperty).getType();
						String secondType = internalproperties.get(secondproperty).getType();
						if(firstType.equals(secondType)){
							if(firstType.equals(InternalPropertyType.INTEGER.getTypeValue())){
								IntInternalPropertyValue val = new IntInternalPropertyValue(((IntInternalProperty) internalproperties.get(firstproperty)).getDefaultValue());
								((IntInternalProperty) internalproperties.get(firstproperty)).linkValue(val);
								((IntInternalProperty) internalproperties.get(secondproperty)).linkValue(val);
							} else if(firstType.equals(InternalPropertyType.DOUBLE)){
								// TODO
							} else if(firstType.equals(InternalPropertyType.STRING)){
								// TODO
							}
						}
					}
				}
			}
		}
		
	}
	
	public void updateMenu() {
		switch_plugin.removeAll();
		final String[] plugins = controller_.getOtherPluginsList();
		for (int i = 0; i < plugins.length; i++) {
			final int index = i;
			JMenuItem item = new JMenuItem(new AbstractAction(plugins[index]) {
				private static final long serialVersionUID = -4996932088885254278L;

				public void actionPerformed(ActionEvent e) {
					controller_.loadPlugin(plugins[index]);
				}
			});
			switch_plugin.add(item);
		}
		
		switch_configuration.removeAll();
		final String[] confs = controller_.getOtherCompatibleConfigurationList();
		for (int i = 0; i < confs.length; i++) {
			final int index = i;
			JMenuItem item = new JMenuItem(new AbstractAction(confs[index]) {
				private static final long serialVersionUID = -4996932088885254278L;

				public void actionPerformed(ActionEvent e) {
					controller_.loadConfiguration(confs[index]);
				}
			});
			switch_configuration.add(item);
		}
	}

	
	protected abstract void initComponents();

}
