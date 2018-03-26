package main.embl.rieslab.htSMLM.threads.updaters;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JProgressBar;

import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import main.embl.rieslab.htSMLM.util.utils;

public class JProgressBarUpdater extends ComponentUpdater<JProgressBar>{

	
	public JProgressBarUpdater(JProgressBar component, UIProperty prop,
			int idletime) {
		super(component, prop, idletime);
	}

	@Override
	public boolean sanityCheck(UIProperty prop) {
		if(utils.isNumeric(prop.getPropertyValue())){
			return true;
		}
		return false;
	}

	@Override
	public void updateComponent(String val) {
		if(utils.isNumeric(val)){
			int value = (int) Math.round(Double.parseDouble(val));
			component_.setValue(value);
		}
	}

	public double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

}
