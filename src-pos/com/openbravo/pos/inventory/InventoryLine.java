package com.openbravo.pos.inventory;

import com.openbravo.format.Formats;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.util.StringUtils;

/**
 *
 * @author adrianromero
 */
public class InventoryLine {
    
    private double m_dMultiply;    
    private double m_dPrice;
    private int unidades;
    private int presentacion;
    private String nombrePresentacion;
    private String m_sProdID;
    private String m_sProdName;
    private String m_sCodigo;
    private String attsetid;
    private String attsetinstid;
    private String attsetinstdesc;
 
    /** Creates a new instance of InventoryLine */
    public InventoryLine(ProductInfoExt oProduct) {
        m_sProdID = oProduct.getID();
        m_sProdName = oProduct.getName();
        m_dMultiply = 1.0;
        unidades = 1;
        presentacion=0;
        nombrePresentacion="";
        m_dPrice = oProduct.getPriceBuy();
        attsetid = oProduct.getAttributeSetID();
        attsetinstid = null;
        attsetinstdesc = null;
        m_sCodigo=oProduct.getReference();
    }
    
    public InventoryLine(ProductInfoExt oProduct, double dpor, double dprice) {
        m_sProdID = oProduct.getID();
        m_sProdName = oProduct.getName();
        m_dMultiply = dpor;
        m_dPrice = dprice;
        attsetid = oProduct.getAttributeSetID();
        attsetinstid = null;
        unidades = 1;
        presentacion=0;
        nombrePresentacion="";
        attsetinstdesc = null;
        m_sCodigo=oProduct.getReference();
    }
    public String getProductReference() {
        return m_sCodigo;
    }
    public String getProductID() {
        return m_sProdID;
    } 
    public String getNombrePresentacion() {
        return nombrePresentacion;
    } 
    public void setNombrePresentacion(String sValue) {
            nombrePresentacion = sValue;
    }
    
    public String getProductName() {
        return m_sProdName;
    } 
    public void setProductName(String sValue) {
        if (m_sProdID == null) {
            m_sProdName = sValue;
        }
    }
    public double getMultiply() {
        return m_dMultiply;
    }
    
    public void setMultiply(double dValue) {
        m_dMultiply = dValue;
    }
    
    public int getUnidades() {
        return unidades;
    }
    
    public void setUnidades(int dValue) {
        unidades = dValue;
    }
    
    public int getPresentacion() {
        return presentacion;
    }
    
    public void setPresentacion(int dValue) {
        presentacion = dValue;
    }
    
    public double getPrice() {
        return m_dPrice;
    }
    
    public void setPrice(double dValue) {
        m_dPrice = dValue;
    }    
    
    public double getSubValue() {
        return m_dMultiply * m_dPrice;
    }
    
    public String getProductAttSetInstId() {
        return attsetinstid;
    }

    public void setProductAttSetInstId(String value) {
        attsetinstid = value;
    }    
    
    public String getProductAttSetId() {
        return attsetid;
    }

    public String getProductAttSetInstDesc() {
        return attsetinstdesc;
    }

    public void setProductAttSetInstDesc(String value) {
        attsetinstdesc = value;
    }
    
    public String printName() {
        return StringUtils.encodeXML(m_sProdName);
    }
    
    public String printPrice() {
        if (m_dMultiply == 1.0) {
            return "";
        } else {
            return Formats.CURRENCY.formatValue(new Double(getPrice()));
        }
    }
    
    public String printMultiply() {
        return Formats.INT.formatValue(new Double(m_dMultiply));
    }
    
    public String printSubValue() {
        return Formats.CURRENCY.formatValue(new Double(getSubValue()));
    }    
}
