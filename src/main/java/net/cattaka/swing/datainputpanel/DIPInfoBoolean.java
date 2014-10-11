/*
 * 作成日: 2007/02/01
 */
package net.cattaka.swing.datainputpanel;

import javax.swing.JCheckBox;
import javax.swing.JComponent;


public class DIPInfoBoolean implements DIPInfo
{
    private String label;
    private boolean defaultData;
    private JCheckBox checkBox;
    
    public DIPInfoBoolean(String label, boolean defaultData) throws InvalidDataTypeException
    {
        super();
        if (label == null)
            throw new NullPointerException();
        
        this.label = "";
        this.defaultData = defaultData;
        this.checkBox = new JCheckBox(label);
        setValue(defaultData);
    }
    public Object getValue()
    {
        return new Boolean(checkBox.isSelected());
    }
    public void setValue(boolean value)
    {
        checkBox.setSelected(value);
    }
    public void makeDefault()
    {
        checkBox.setSelected(defaultData);
    }
    public String getLabel()
    {
        return label;
    }
    public JComponent getComponent()
    {
        return checkBox;
    }
    public boolean isEnable()
    {
        return checkBox.isEnabled();
    }
    public void setEnable(boolean enable)
    {
        checkBox.setEnabled(enable);
    }
}
