/*
 * 作成日: 2007/02/01
 */
package net.cattaka.swing.datainputpanel;

import javax.swing.JComponent;


public interface DIPInfo
{
    public Object getValue();
    public String getLabel();
    public JComponent getComponent();
    public void makeDefault();
    public boolean isEnable();
    public void setEnable(boolean enable);
}
