package main.embl.rieslab.mm.uidevint.uiexamples.htsmlm.others;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

public class ToggleSlider extends JToggleButton {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2208822483619747706L;

	public ToggleSlider(){
		this.setPreferredSize(new Dimension(65,28));
		this.setFocusPainted(false);

		this.setIcon(new ImageIcon(getClass().getResource("/images/ToggleSlider-off.png")));
		this.setSelectedIcon(new ImageIcon(getClass().getResource("/images/ToggleSlider-on.png")));
		//this.setRolloverIcon(new ImageIcon("images/ToggleSlider-rollover.png"));
		this.setDisabledIcon(new ImageIcon(getClass().getResource("/images/ToggleSlider-disabled.png")));
		
		this.setBorderPainted(false);
		this.setBorder(null);
		this.setFocusable(false);
		this.setContentAreaFilled(false);
	}
	
}
