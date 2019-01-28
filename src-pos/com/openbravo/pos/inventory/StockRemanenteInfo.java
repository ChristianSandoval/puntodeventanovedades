package com.openbravo.pos.inventory;

import com.openbravo.data.loader.IKeyed;
import java.io.Serializable;

/**
 *
 * @author  adrianromero
 * @version 
 */
public class StockRemanenteInfo implements Serializable, IKeyed {
    
    private static final long serialVersionUID = 8959679342805L;
    private String m_sID;
    private String m_sFecha;
    private String m_sUnidades;
    private String m_sPrecio;
    private String m_sActuales;
    public StockRemanenteInfo(String sID, String sFecha, String sUnidades, String sPrecio, String sActuales) {
        m_sID = sID;  
        m_sFecha = sFecha;  
        m_sUnidades = sUnidades;  
        m_sPrecio = sPrecio;  
        m_sActuales = sActuales;  
    }
    
    public Object getKey() {
        return m_sID;
    }
    
    public void setID(String sID) {
        m_sID = sID;
    }
    
    public String getID() {
        return m_sID;
    }
   
    @Override
    public String toString(){
        return m_sID;
    }
}