package de.embl.rieslab.emu.micromanager.mmproperties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.embl.rieslab.emu.exceptions.AlreadyAssignedUIPropertyException;
import de.embl.rieslab.emu.micromanager.mmproperties.IntegerMMProperty;
import de.embl.rieslab.emu.micromanager.mmproperties.StringMMProperty;
import de.embl.rieslab.emu.ui.ConfigurablePanel;
import de.embl.rieslab.emu.ui.uiproperties.PropertyPair;
import de.embl.rieslab.emu.ui.uiproperties.UIProperty;
import de.embl.rieslab.emu.ui.uiproperties.flag.PropertyFlag;

public class AssignedUIPropertyTest {

	@Test
	public void testUIPropertyPairing() throws AlreadyAssignedUIPropertyException {
		UIPropertyTestPanel cp = new UIPropertyTestPanel("MyPanel");
		final StringMMProperty mmprop = new StringMMProperty(null,"","","") {
			@Override
			public String getValue() { // avoids NullPointerException
				return "";
			}
		};

		cp.property.assignProperty(mmprop);		
		assertTrue(cp.property.isAssigned());
	}
		
	@Test
	public void testUIPropertyReadOnly() {
		UIPropertyTestPanel cp = new UIPropertyTestPanel("MyPanel");
		
		final IntegerMMProperty mmprop = new IntegerMMProperty(null, "", "", true) {
			@Override
			public Integer getValue() { // avoids NullPointerException
				return 0;
			}
		};
		

		PropertyPair.pair(cp.property, mmprop);	
		assertTrue(cp.property.isMMPropertyReadOnly());
	}

	@Test
	public void testUIPropertySetValue() { 
		UIPropertyTestPanel cp = new UIPropertyTestPanel("MyPanel");
		
		final IntegerMMProperty mmprop = new IntegerMMProperty(null, "", "", false) {
			@Override
			public Integer getValue() { // avoids NullPointerException
				return 0;
			}
			
			@Override
			public void setValue(String stringval, UIProperty source){ // bypassing communication with MM core
				value = convertToValue(stringval);
			}
			
			@Override
			public String getStringValue() { // same here
				return convertToString(value);
			}

		};
		
		PropertyPair.pair(cp.property, mmprop);	
		
		String val = "15";
		cp.property.setPropertyValue(String.valueOf(val));
		assertEquals(val, cp.property.getPropertyValue());
	}

	@Test
	public void testUIPropertyAllowedValues() { 
		UIPropertyTestPanel cp = new UIPropertyTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				property = new UIProperty(this, PROP, DESC);
			}
		};
		
		final String[] str = {"1", "2", "3"};
		final IntegerMMProperty mmprop = new IntegerMMProperty(null, "", "", str) {
			@Override
			public Integer getValue() { // avoids NullPointerException
				return 0;
			}
		};
		
		PropertyPair.pair(cp.property, mmprop);	
		assertTrue(cp.property.hasMMPropertyAllowedValues());
		assertFalse(cp.property.hasMMPropertyLimits());
		
		String[] s = cp.property.getAllowedValues();
		assertEquals(str.length,s.length);
		for(int i = 0; i<str.length; i++) {
			assertEquals(str[i], s[i]);
			assertTrue(cp.property.isValueAllowed(str[i]));
		}

		assertFalse(cp.property.isValueAllowed(null));
		assertFalse(cp.property.isValueAllowed(""));
		assertFalse(cp.property.isValueAllowed("dfsdfsdf"));
		assertFalse(cp.property.isValueAllowed("4"));
	}
	
	@Test
	public void testUIPropertyBoundedValues() { 
		UIPropertyTestPanel cp = new UIPropertyTestPanel("MyPanel");
		
		final double min = -1.56;
		final double max = 5.48;
		final IntegerMMProperty mmprop = new IntegerMMProperty(null, "", "", max, min) {
			@Override
			public Integer getValue() { // avoids NullPointerException
				return 0;
			}
		};
		
		PropertyPair.pair(cp.property, mmprop);	
		assertFalse(cp.property.hasMMPropertyAllowedValues());
		assertTrue(cp.property.hasMMPropertyLimits());
		
		String[] s = cp.property.getLimits();
		assertEquals(2, s.length);
		assertEquals(String.valueOf((int) min), s[0]);
		assertEquals(String.valueOf((int) max), s[1]);

		assertTrue(cp.property.isValueAllowed("-1.05"));
		assertTrue(cp.property.isValueAllowed("5.354"));
		assertTrue(cp.property.isValueAllowed("4"));

		assertFalse(cp.property.isValueAllowed(null));
		assertFalse(cp.property.isValueAllowed(""));
		assertFalse(cp.property.isValueAllowed("dfsdfsdf"));
		assertFalse(cp.property.isValueAllowed("8"));
	}
	
	@Test (expected = Exception.class)
	public void testUIPropertyAlreadyAssigned() throws AlreadyAssignedUIPropertyException {
		UIPropertyTestPanel cp = new UIPropertyTestPanel("MyPanel");

		final IntegerMMProperty mmprop1 = new IntegerMMProperty(null, "", "", false) {
			@Override
			public Integer getValue() { // avoids NullPointerException
				return 0;
			}
		};

		final IntegerMMProperty mmprop2 = new IntegerMMProperty(null, "", "", false) {
			@Override
			public Integer getValue() { // avoids NullPointerException
				return 0;
			}
		};

		cp.property.assignProperty(mmprop1);
		cp.property.assignProperty(mmprop2);
	}
	
	private class UIPropertyTestPanel extends ConfigurablePanel{

		private static final long serialVersionUID = 1L;
		public UIProperty property;
		public final String PROP = "MyProp"; 
		public final String DESC = "MyDescription"; 
		public PropertyFlag flag;
		
		public UIPropertyTestPanel(String label) {
			super(label);
		}

		@Override
		protected void initializeProperties() {
			property = new UIProperty(this, PROP, DESC);
		}
		
		@Override
		protected void initializeParameters() {}
		
		@Override
		protected void parameterhasChanged(String parameterName) {}
		
		@Override
		protected void initializeInternalProperties() {}

		@Override
		public void internalpropertyhasChanged(String propertyName) {}

		@Override
		protected void propertyhasChanged(String propertyName, String newvalue) {}

		@Override
		public void shutDown() {}

		@Override
		protected void addComponentListeners() {}
		
		@Override
		public String getDescription() {return "";}
	}
}
