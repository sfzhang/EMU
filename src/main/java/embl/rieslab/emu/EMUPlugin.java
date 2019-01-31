package main.java.embl.rieslab.emu;

import java.io.File;

import javax.swing.SwingUtilities;

import org.micromanager.MenuPlugin;
import org.micromanager.Studio;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.SciJavaPlugin;

import main.java.embl.rieslab.emu.controller.SystemConstants;
import main.java.embl.rieslab.emu.controller.SystemController;


@Plugin(type = MenuPlugin.class)
public class EMUPlugin implements MenuPlugin, SciJavaPlugin {

	private SystemController controller_;
	private static Studio mmAPI_;
	
	private static String name = "EMU";
	private static String description = "Easier Micro-manager User interface: loads its own plugins and interface their UI with Micro-manager device properties.";
	private static String copyright = "Joran Deschamps, EMBL, 2016-2019.";
	private static String version = "0.1";

	@Override
	public String getCopyright() {
		return copyright;
	}

	@Override
	public String getHelpText() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setContext(Studio mmAPI) {
		mmAPI_ = mmAPI;
	}

	@Override
	public String getSubMenu() {
		return "Interface";
	}

	@Override
	public void onPluginSelected() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// make sure the directory EMU exist
				if(!(new File(SystemConstants.HOME)).exists()){
					new File(SystemConstants.HOME).mkdirs();
				}
				
				controller_ = new SystemController(mmAPI_);
				controller_.start();
			}
		});
	}

	@Override
	public String getVersion() {
		return version;
	}

}