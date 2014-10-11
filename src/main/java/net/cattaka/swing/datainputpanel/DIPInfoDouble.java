/*
 * 作成日: 2007/02/01
 */
package net.cattaka.swing.datainputpanel;

import javax.swing.JComponent;
import javax.swing.JTextField;


public class DIPInfoDouble implements DIPInfo
{
    private String label;
    private String defaultData;
    private JTextField field;
    
    public DIPInfoDouble(String label, String defaultData) throws InvalidDataTypeException
    {
        super();
        if (label == null || defaultData == null)
            throw new NullPointerException();
        
        this.label = label;
        this.defaultData = defaultData;
        this.field = new JTextField(defaultData, 0);
    }
    public Object getValue()
    {
        String arg = field.getText();
        try
        {
            return Double.valueOf(arg);
        }
        catch(NumberFormatException e)
        {
            return null;
        }
    }
    public void setValue(double value)
    {
        field.setText(String.valueOf(value));
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
