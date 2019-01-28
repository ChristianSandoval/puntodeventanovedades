
package com.openbravo.pos.scale;

import com.openbravo.beans.JNumberDialog;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.ticket.ProductInfoExt;
import java.awt.Component;
import javax.swing.ImageIcon;

/**
 *
 * @author adrian
 */
public class ScaleDialog implements Scale {

    private Component parent;
    private ProductInfoExt prod;

    public ScaleDialog(Component parent, ProductInfoExt p) {
        this.parent = parent;
        this.prod = p;
    }
    public ScaleDialog(Component parent) {
        this.parent = parent;
    }

    public Double readWeight() throws ScaleException {
        //return this.m_value =  / this.prod.getPriceSell();
        // Set title for grams Kilos, ounzes, pounds, ...
        return JNumberDialog.showEditNumber(parent, AppLocal.getIntString("label.scale"), AppLocal.getIntString("label.scaleinput"), new ImageIcon(ScaleDialog.class.getResource("/com/openbravo/images/ark2.png")));
    }
    public Double readWeight2(double value) throws ScaleException {
        return value / this.prod.getPriceSell();
    }
}
