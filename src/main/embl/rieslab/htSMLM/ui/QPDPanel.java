package main.embl.rieslab.htSMLM.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import main.embl.rieslab.htSMLM.threads.ChartUpdater;
import main.embl.rieslab.htSMLM.threads.JProgressBarUpdater;
import main.embl.rieslab.htSMLM.ui.graph.Chart;
import main.embl.rieslab.htSMLM.ui.uiparameters.IntUIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

public class QPDPanel extends PropertyPanel {

	//////// Thread
	private ChartUpdater chartupdater_;
	private JProgressBarUpdater progressbarupdater_;
	
	//////// Properties
	public static String QPD_X = "QPD Z";
	public static String QPD_Y = "QPD Y";
	public static String QPD_Z = "QPD Z";
	
	//////// Parameters
	public static String PARAM_XYMAX = "XY max";
	public static String PARAM_ZMAX = "Z max";
	public static String PARAM_IDLE = "Idle time";
	
	//////// Default parameters
	private int idle_, xymax_, zmax_; 
	
	//////// Components
	private JProgressBar progressBar_;
	private JToggleButton togglebuttonMonitor_;
	private Chart graph_;
	private JPanel graphpanel_;
	
	public QPDPanel(String label) {
		super(label);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7082332561417231746L;

	@Override
	public void setupPanel() {
		this.setLayout(new GridBagLayout());
		
		graph_ = new Chart("QPD","X","Y",1,270,270, xymax_);
		chartupdater_ = new ChartUpdater(graph_,getUIProperty(QPD_X),getUIProperty(QPD_Y),idle_);
		graphpanel_ = new JPanel();
		graphpanel_.add(graph_.getChart());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
//		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2,2,2,2);
		c.gridwidth = 3;
		c.gridheight = 3;
		
		this.add(graphpanel_,c);
		
		c.gridx = 3;
		c.gridy = 0;
		c.fill = GridBagConstraints.VERTICAL;
		c.insets = new Insets(40,2,2,2);
		c.gridwidth = 1;
		c.gridheight = 2;

		progressBar_ = new javax.swing.JProgressBar();
		progressBar_.setOrientation(SwingConstants.VERTICAL);
		progressBar_.setMaximum(zmax_);
		progressBar_.setMinimum(0);
		progressbarupdater_ = new JProgressBarUpdater(progressBar_, getUIProperty(QPD_Z), idle_);
		this.add(progressBar_,c);
		
		c.gridx = 3;
		c.gridy = 2;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(2,10,2,10);
		c.gridwidth = 1;
		c.gridheight = 1;
		togglebuttonMonitor_ = new JToggleButton("Monitor");
		togglebuttonMonitor_.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					monitorQPD(true);
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					monitorQPD(false);
				}
			}
        });
		this.add(togglebuttonMonitor_,c);

	}
	
	protected void monitorQPD(boolean b) {
		if(b){
			chartupdater_.startUpdater();
			progressbarupdater_.startUpdater();
		} else {
			chartupdater_.stopUpdater();
			progressbarupdater_.stopUpdater();
		}
	}


	@Override
	protected void initializeProperties() {
		addUIProperty(new UIProperty(this, QPD_X,"Read-out property of the QPD x signal."));
		addUIProperty(new UIProperty(this, QPD_Y,"Read-out property of the QPD y signal."));
		addUIProperty(new UIProperty(this, QPD_Z,"Read-out property of the QPD z signal."));
	}

	@Override
	protected void initializeParameters() {
		xymax_ = 700;
		zmax_ = 700;
		idle_ = 100;
		
		addUIParameter(new IntUIParameter(this, PARAM_XYMAX,"Default for the maximum X and Y signals value to display.",xymax_));
		addUIParameter(new IntUIParameter(this, PARAM_ZMAX,"Default for the maximum Z signal value to display.",zmax_));
		addUIParameter(new IntUIParameter(this, PARAM_IDLE,"Idle time of the QPD signals monitoring.",idle_)); // thread idle time
	}

	@Override
	protected void changeProperty(String name, String value) {
		// do nothing
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		// do nothing
	}

	@Override
	public void parameterhasChanged(String label) {
		if(label.equals(PARAM_XYMAX)){
			if(((IntUIParameter) getUIParameter(PARAM_XYMAX)).getValue() != xymax_){
				xymax_ = ((IntUIParameter) getUIParameter(PARAM_XYMAX)).getValue();
				graphpanel_.remove(graph_.getChart());
				graph_ =  new Chart("QPD","X","Y",1,270,270, xymax_);
				graphpanel_.add(graph_.getChart());
				graphpanel_.updateUI();
				chartupdater_.changeChart(graph_);
			}
		} else if(label.equals(PARAM_ZMAX)){
			if(((IntUIParameter) getUIParameter(PARAM_ZMAX)).getValue() != zmax_){
				zmax_ = ((IntUIParameter) getUIParameter(PARAM_ZMAX)).getValue();
				progressBar_.setMaximum(zmax_);
			}
		}else if(label.equals(PARAM_IDLE)){
			if(((IntUIParameter) getUIParameter(PARAM_IDLE)).getValue() != idle_){
				idle_ = ((IntUIParameter) getUIParameter(PARAM_IDLE)).getValue();
				chartupdater_.changeIdleTime(idle_);
				progressbarupdater_.changeIdleTime(idle_);
			}
		}
	}

	@Override
	public void shutDown() {
		chartupdater_.stopUpdater();
		progressbarupdater_.stopUpdater();
	}
}