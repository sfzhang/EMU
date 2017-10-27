package main.embl.rieslab.htSMLM.controller.uiwizard;

import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import main.embl.rieslab.htSMLM.ui.uiparameters.UIParameter;
import main.embl.rieslab.htSMLM.ui.uiparameters.UIParameterType;
import main.embl.rieslab.htSMLM.util.ColorRepository;

public class ParameterComboTable extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1094849697965112381L;
	
	private JTable table;
	private JComboBox color;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, UIParameter> uiparameterSet_;
	private String[] uiparamkeys_;
	private HelpWindow help_;
		
	@SuppressWarnings("rawtypes")
	public ParameterComboTable(HashMap<String, UIParameter> uiparameterSet, HelpWindow help) {
		
		uiparameterSet_ = uiparameterSet; 
		
		// Color combobox
		Map<Object, ColorIcon> icons = new HashMap<Object, ColorIcon>();
		color = new JComboBox();
		String[] colors = ColorRepository.getColors();
		for(int k=0; k<colors.length;k++){
			color.addItem(colors[k]);
			icons.put(colors[k], new ColorIcon(ColorRepository.getColor(colors[k])));
		}
		color.setRenderer(new IconListRenderer(icons));
		        		
		// Extract uiparameters names
		uiparamkeys_ = new String[uiparameterSet_.size()];
		String[] temp = new String[uiparameterSet_.size()]; 
		uiparamkeys_ = StringSorting.sort(uiparameterSet_.keySet().toArray(temp));
		
		// Define table
		DefaultTableModel model = new DefaultTableModel(new Object[] {"UI parameter", "Value" }, 0);
		for(int i=0;i<uiparamkeys_.length;i++){
			model.addRow(new Object[] {uiparamkeys_[i], uiparameterSet_.get(uiparamkeys_[i]).getStringValue()});
		}


		table = new JTable(model) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7528102943663023952L;

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				switch (column) {
				case 0:
					return new BoldTableCellRenderer();
				case 1:
					String s = (String) table.getValueAt(row, 0);
					if(uiparameterSet_.get(s).getType().getTypeValue().equals(UIParameterType.COLOUR.getTypeValue())){
						return new IconTableRenderer();
					} else {
						return new DefaultTableCellRenderer(); 
					}
				default:
					return super.getCellRenderer(row, column);
				}
			}

			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				switch (column) {
				case 0:
					return super.getCellEditor(row, column);
				case 1:
					String s = (String) table.getValueAt(row, 0);
					if(uiparameterSet_.get(s).getType().getTypeValue().equals(UIParameterType.COLOUR.getTypeValue())){
						return new DefaultCellEditor(color);
					} else {
						return new DefaultCellEditor(new JTextField()); 
					}
				default:
					return super.getCellEditor(row, column);
				}
			}
			
			@Override
	        public boolean isCellEditable(int row, int col) { // only second column is editable
	            if (col < 1 ) {
	                return false;
	            } else {
	                return true;
	            }
	        }
		};
		table.setAutoCreateRowSorter(false);
		table.setRowHeight(23); 
		//table.getColumnModel().getColumn(0).setMaxWidth(210);
		//table.getColumnModel().getColumn(1).setMaxWidth(70);
		
		table.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int row = table.rowAtPoint(evt.getPoint());
		        int col = table.columnAtPoint(evt.getPoint());
		        if (col==0) {
		            updateHelper(row);
		        }
		    }
		});
		
		help_ = help;

		JScrollPane sc = new JScrollPane(table);
		//sc.setPreferredSize(new Dimension(280,590));
		this.add(sc);
	}
	
	public void showHelp(boolean b){
		help_.showHelp(b);
		updateHelper(table.getSelectedRow());
	}

	private void updateHelper(int row){
		String s = (String) table.getValueAt(row, 0);
		help_.update(s+":\n\n"+uiparameterSet_.get(s).getDescription());
	}
	
	public void disposeHelp(){
		help_.disposeHelp();
	}
	
	public HashMap<String,String> getSettings(){
		HashMap<String,String> settings = new HashMap<String,String>();
		
		TableModel model = table.getModel();
		int nrow = model.getRowCount();
		
		for(int i=0;i<nrow;i++){
			settings.put((String) model.getValueAt(i, 0), (String) model.getValueAt(i, 1));
		}
		
		return settings;
	}
	
	/**
	 * Renders cell text with a bold font. Adapted from: https://stackoverflow.com/questions/22325138/cellrenderer-making-text-bold
	 */
	class BoldTableCellRenderer extends DefaultTableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7284712630858433079L;

		public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
             Component compo = super.getTableCellRendererComponent(table, 
                   value, isSelected, hasFocus, row, column);
             if (column == 0) {
            	 compo.setFont(compo.getFont().deriveFont(Font.BOLD));
            } else {  
            	compo.setFont(compo.getFont().deriveFont(Font.PLAIN));
            }

             return compo;
          }
	}
	
}