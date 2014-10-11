/*
 * 作成日: 2007/02/01
 */
package net.cattaka.swing.datainputpanel;

import javax.swing.JComponent;
import javax.swing.JTextField;


public class DIPInfoString implements DIPInfo
{
    private String label;
    private String defaultData;
    private JTextField field;
    
    public DIPInfoString(String label, String defaultData) throws InvalidDataTypeException
    {
        super();
        if (label == null || defaultData == null)
            throw new NullPointerException();
        
        this.label = label;
        this.defaultData = String.valueOf(defaultData);
        this.field = new JTextField(defaultData, 0);
    }
    public String getStringValue()
    {
        String arg = field.getText();
        return arg;
    }
    public Object getValue()
    {
        String arg = field.getText();
        return arg;
    }
    public void setValue(String value)
    {
        if (value == null)
            throw new NullPointerException();
        field.setText(value);
    }
    public void makeDefault()
    {
        field.setText(defaultData);
    }
    public String getLabel()
    {
        return label;
    }
    public JComponent getComponent()
    {
        return field;
    }
    public boolean isEnable()
    {
        return field.isEnabled();
    }
    public void setEnable(boolean enable)
    {
        field.setEnabled(enable);
    }
}
