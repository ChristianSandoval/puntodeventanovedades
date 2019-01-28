package com.openbravo.pos.inventory;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.panels.JProductFinder;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.ticket.ProductInfoExt;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductsEditor extends JPanel implements EditorRecord {
       
    private SentenceList m_sentcat;
    private ComboBoxValModel m_CategoryModel;

    private SentenceList taxcatsent;
    private ComboBoxValModel taxcatmodel;  

    private SentenceList attsent;
    private ComboBoxValModel attmodel;
    private ProductInfoExt prod1;
    private ProductInfoExt prod2;
    private ProductInfoExt prod3;
    private ProductInfoExt prod4;
    private ProductInfoExt prod5;
    private ProductInfoExt prod6;
    private ProductInfoExt prod7;
    private ProductInfoExt prod8;
    private ProductInfoExt prod9;
    private ProductInfoExt prod10;
    private ProductInfoExt prod11;
    private ProductInfoExt prod12;
    
    private ProductInfoExt prod13;
    private ProductInfoExt prod14;
    private ProductInfoExt prod15;
    private ProductInfoExt prod16;
    private ProductInfoExt prod17;
    private ProductInfoExt prod18;
    private ProductInfoExt prod19;
    private ProductInfoExt prod20;
    private ProductInfoExt prod21;
    private ProductInfoExt prod22;
    private ProductInfoExt prod23;
    private ProductInfoExt prod24;
    private SentenceList taxsent;
    private TaxesLogic taxeslogic;
    
    private ComboBoxValModel m_CodetypeModel;
    
    private Object m_id;
    private Object pricesell;
    private boolean priceselllock = false;
    
    private boolean reportlock = false;
    private DataLogicSales dlSales;
    private AppView app;
    
    public ProductsEditor(DataLogicSales dlSales, DirtyManager dirty, AppView app) {
        initComponents();
        this.dlSales = dlSales;
        this.app = app;
        // The taxes sentence
        taxsent = dlSales.getTaxList();
             
        // The categories model
        m_sentcat = dlSales.getCategoriesList();
        m_CategoryModel = new ComboBoxValModel();
        
        // The taxes model
        taxcatsent = dlSales.getTaxCategoriesList();
        taxcatmodel = new ComboBoxValModel();

        // The attributes model
        attsent = dlSales.getAttributeSetList();
        attmodel = new ComboBoxValModel();
        
        m_CodetypeModel = new ComboBoxValModel();
        m_CodetypeModel.add(null);
        m_CodetypeModel.add(CodeType.EAN13);
        m_CodetypeModel.add(CodeType.CODE128);
        m_jCodetype.setModel(m_CodetypeModel);
        m_jCodetype.setVisible(false);
               
        m_jRef.getDocument().addDocumentListener(dirty);
        m_jCode.getDocument().addDocumentListener(dirty);
        m_jName.getDocument().addDocumentListener(dirty);
        m_jComment.addActionListener(dirty);
        m_jScale.addActionListener(dirty);
        m_jCategory.addActionListener(dirty);
        m_jTax.addActionListener(dirty);
        m_jAtt.addActionListener(dirty);
        m_jPriceBuy.getDocument().addDocumentListener(dirty);
        m_jPriceSell.getDocument().addDocumentListener(dirty);
        m_jPriceSell2.getDocument().addDocumentListener(dirty);
        m_jPriceSell3.getDocument().addDocumentListener(dirty);
        m_jPriceSell4.getDocument().addDocumentListener(dirty);
        m_jCantidad.getDocument().addDocumentListener(dirty);
        m_jCantidad2.getDocument().addDocumentListener(dirty);
        m_jCantidad3.getDocument().addDocumentListener(dirty);
        m_jCantidad4.getDocument().addDocumentListener(dirty);
        m_jImage.addPropertyChangeListener("image", dirty);
        m_jstockcost.getDocument().addDocumentListener(dirty);
        m_jstockvolume.getDocument().addDocumentListener(dirty);
        m_jInCatalog.addActionListener(dirty);
        m_jCatalogOrder.getDocument().addDocumentListener(dirty);
        txtAttributes.getDocument().addDocumentListener(dirty);
        /*
this.m_jCant1.getDocument().addDocumentListener(dirty);
    this.m_jCant2.getDocument().addDocumentListener(dirty);
    this.m_jCant3.getDocument().addDocumentListener(dirty);
    this.m_jCant4.getDocument().addDocumentListener(dirty);
    this.m_jCant5.getDocument().addDocumentListener(dirty);
    this.m_jCant6.getDocument().addDocumentListener(dirty);
    this.m_jCant7.getDocument().addDocumentListener(dirty);
    this.m_jCant8.getDocument().addDocumentListener(dirty);
    this.m_jCant9.getDocument().addDocumentListener(dirty);
    this.m_jCant10.getDocument().addDocumentListener(dirty);
    this.m_jCant11.getDocument().addDocumentListener(dirty);
    this.m_jCant12.getDocument().addDocumentListener(dirty);
    
    this.m_jCant13.getDocument().addDocumentListener(dirty);
    this.m_jCant14.getDocument().addDocumentListener(dirty);
    this.m_jCant15.getDocument().addDocumentListener(dirty);
    this.m_jCant16.getDocument().addDocumentListener(dirty);
    this.m_jCant17.getDocument().addDocumentListener(dirty);
    this.m_jCant18.getDocument().addDocumentListener(dirty);
    this.m_jCant19.getDocument().addDocumentListener(dirty);
    this.m_jCant20.getDocument().addDocumentListener(dirty);
    this.m_jCant21.getDocument().addDocumentListener(dirty);
    this.m_jCant22.getDocument().addDocumentListener(dirty);
    this.m_jCant23.getDocument().addDocumentListener(dirty);
    this.m_jCant24.getDocument().addDocumentListener(dirty);
        FieldsManager fm = new FieldsManager();
        m_jPriceBuy.getDocument().addDocumentListener(fm);
        m_jPriceSell.getDocument().addDocumentListener(fm);
        m_jPriceSell2.getDocument().addDocumentListener(fm);
        m_jPriceSell3.getDocument().addDocumentListener(fm);
        m_jPriceSell4.getDocument().addDocumentListener(fm);
        m_jTax.addActionListener(fm);
        */
        //m_jPriceSellTax.getDocument().addDocumentListener(new PriceTaxManager());
        //m_jmargin.getDocument().addDocumentListener(new MarginManager());
        
        writeValueEOF();
    }
    
    public void activate() throws BasicException {
        
        // Load the taxes logic
        taxeslogic = new TaxesLogic(taxsent.list());        
        
        m_CategoryModel = new ComboBoxValModel(m_sentcat.list());
        m_jCategory.setModel(m_CategoryModel);

        taxcatmodel = new ComboBoxValModel(taxcatsent.list());
        m_jTax.setModel(taxcatmodel);

        attmodel = new ComboBoxValModel(attsent.list());
        attmodel.add(0, null);
        m_jAtt.setModel(attmodel);
    }
    
    public void refresh() {
    }    
    
    public void writeValueEOF() {
        
        reportlock = true;
        // Los valores
        m_jTitle.setText(AppLocal.getIntString("label.recordeof"));
        m_id = null;
        m_jRef.setText(null);
        m_jCode.setText(null);
        m_jName.setText(null);
        m_jComment.setSelected(false);
        m_jScale.setSelected(false);
        m_CategoryModel.setSelectedKey(null);
        taxcatmodel.setSelectedKey(null);
        attmodel.setSelectedKey(null);
        m_jPriceBuy.setText(null);
        m_jPriceSell.setText(null);   
        m_jPriceSell2.setText(null);
        m_jPriceSell3.setText(null);
        m_jPriceSell4.setText(null);
        m_jCantidad.setText(null);   
        m_jCantidad2.setText(null);
        m_jCantidad3.setText(null);
        m_jCantidad4.setText(null);
        m_jImage.setImage(null);
        m_jstockcost.setText(null);
        m_jstockvolume.setText(null);
        m_jInCatalog.setSelected(false);
        m_jCatalogOrder.setText(null);
        txtAttributes.setText(null);
        reportlock = false;
        
        // Los habilitados
        m_jRef.setEnabled(false);
        m_jCode.setEnabled(false);
        m_jName.setEnabled(false);
        m_jComment.setEnabled(false);
        m_jScale.setEnabled(false);
        m_jCategory.setEnabled(false);
        m_jTax.setEnabled(false);
        m_jAtt.setEnabled(false);
        if(!app.getAppUserView().getUser().getRole().equals("0"))
            m_jPriceBuy.setVisible(false);
        else m_jPriceBuy.setVisible(true);
        
        m_jPriceBuy.setEnabled(false);
        m_jPriceSell.setEnabled(false);
        m_jPriceSell2.setEnabled(false);
        m_jPriceSell3.setEnabled(false);
        m_jPriceSell4.setEnabled(false);
        m_jPriceSellTax.setEnabled(false);
        m_jmargin.setEnabled(false);
        m_jImage.setEnabled(false);
        m_jstockcost.setEnabled(false);
        m_jstockvolume.setEnabled(false);
        m_jInCatalog.setEnabled(false);
        m_jCatalogOrder.setEnabled(false);
        txtAttributes.setEnabled(false);
    }
    public void writeValueInsert() {
       m_jPriceBuy.setVisible(true);
        reportlock = true;
        // Los valores
        m_jTitle.setText(AppLocal.getIntString("label.recordnew"));
        m_id = UUID.randomUUID().toString();
        m_jRef.setText(null);
        m_jCode.setText(null);
        m_jName.setText(null);
        m_jComment.setSelected(false);
        m_jScale.setSelected(false);
        m_CategoryModel.setSelectedKey("000");
        taxcatmodel.setSelectedKey(null);
        attmodel.setSelectedKey(null);
        m_jPriceBuy.setText("0.00");
        m_jPriceSell.setText(null);                    
        m_jPriceSell2.setText(null);
        m_jPriceSell3.setText(null);
        m_jPriceSell4.setText(null);
        m_jCantidad.setText("1");                    
        m_jCantidad2.setText("24");
        m_jCantidad3.setText("6");
        m_jCantidad4.setText("50");
        m_jImage.setImage(null);
        m_jstockcost.setText(null);
        m_jstockvolume.setText(null);
        m_jInCatalog.setSelected(true);
        m_jCatalogOrder.setText(null);
        txtAttributes.setText(null);
        reportlock = false;
        
        if(!app.getAppUserView().getUser().getRole().equals("0"))
            m_jPriceBuy.setVisible(false);
        else m_jPriceBuy.setVisible(true);
        
        // Los habilitados
        m_jRef.setEnabled(true);
        m_jCode.setEnabled(true);
        m_jName.setEnabled(true);
        m_jComment.setEnabled(true);
        m_jScale.setEnabled(true);
        m_jCategory.setEnabled(true);
        m_jTax.setEnabled(true);
        m_jAtt.setEnabled(true);
        m_jPriceBuy.setEnabled(true);
        m_jPriceSell.setEnabled(true); 
        m_jPriceSell2.setEnabled(true); 
        m_jPriceSell3.setEnabled(true); 
        m_jPriceSell4.setEnabled(true); 
        m_jCantidad.setEnabled(true); 
        m_jCantidad2.setEnabled(true); 
        m_jCantidad3.setEnabled(true); 
        m_jCantidad4.setEnabled(true); 
        m_jPriceSellTax.setEnabled(true);
        m_jmargin.setEnabled(true);
        m_jImage.setEnabled(true);
        m_jstockcost.setEnabled(true);
        m_jstockvolume.setEnabled(true);
        m_jInCatalog.setEnabled(true); 
        m_jCatalogOrder.setEnabled(false);
        txtAttributes.setEnabled(true);
   }
    
    public void writeValueDelete(Object value) {
        if(!app.getAppUserView().getUser().getRole().equals("0"))
            m_jPriceBuy.setVisible(false);
        else m_jPriceBuy.setVisible(true);
        reportlock = true;       
        Object[] myprod = (Object[]) value;
        m_jTitle.setText(Formats.STRING.formatValue(myprod[1]) + " - " + Formats.STRING.formatValue(myprod[3]) + " " + AppLocal.getIntString("label.recorddeleted"));
        m_id = myprod[0];
        m_jRef.setText(Formats.STRING.formatValue(myprod[1]));
        m_jCode.setText(Formats.STRING.formatValue(myprod[1]));
        m_jName.setText(Formats.STRING.formatValue(myprod[3]));
        m_jComment.setSelected(((Boolean)myprod[4]).booleanValue());
        m_jScale.setSelected(((Boolean)myprod[5]).booleanValue());
        m_jPriceBuy.setText(Formats.CURRENCY.formatValue(myprod[6]));
        
        m_jPriceSell.setText(Formats.CURRENCY.formatValue(myprod[7]));
        m_CategoryModel.setSelectedKey(myprod[8]);
        taxcatmodel.setSelectedKey("000");
        attmodel.setSelectedKey(myprod[10]);
        m_jImage.setImage((BufferedImage) myprod[11]);
        m_jstockcost.setText(Formats.CURRENCY.formatValue(myprod[12]));
        m_jstockvolume.setText(Formats.STRING.formatValue(myprod[13]));
        m_jInCatalog.setSelected(((Boolean)myprod[14]).booleanValue());
        m_jCatalogOrder.setText(Formats.INT.formatValue(myprod[15]));
        txtAttributes.setText(Formats.BYTEA.formatValue(myprod[16]));
        m_jPriceSell2.setText(Formats.CURRENCY.formatValue(myprod[17]));
        m_jPriceSell3.setText(Formats.CURRENCY.formatValue(myprod[18]));
        m_jPriceSell4.setText(Formats.CURRENCY.formatValue(myprod[19]));
        m_jCantidad.setText(Formats.INT.formatValue(myprod[20]));
        m_jCantidad2.setText(Formats.INT.formatValue(myprod[21]));
        m_jCantidad3.setText(Formats.INT.formatValue(myprod[22]));
        m_jCantidad4.setText(Formats.INT.formatValue(myprod[23]));
        txtAttributes.setCaretPosition(0);
        reportlock = false;
        
        // Los habilitados
        m_jRef.setEnabled(false);
        m_jCode.setEnabled(false);
        m_jName.setEnabled(false);
        m_jComment.setEnabled(false);
        m_jScale.setEnabled(false);
        m_jCategory.setEnabled(false);
        m_jTax.setEnabled(false);
        m_jAtt.setEnabled(false);
        m_jPriceBuy.setEnabled(false);
        m_jPriceSell.setEnabled(false);
        m_jPriceSell2.setEnabled(false);
        m_jPriceSell3.setEnabled(false);
        m_jPriceSell4.setEnabled(false);
        m_jCantidad.setEnabled(false);
        m_jCantidad2.setEnabled(false);
        m_jCantidad3.setEnabled(false);
        m_jCantidad4.setEnabled(false);
        m_jPriceSellTax.setEnabled(false);
        m_jmargin.setEnabled(false);
        m_jImage.setEnabled(false);
        m_jstockcost.setEnabled(false);
        m_jstockvolume.setEnabled(false);
        m_jInCatalog.setEnabled(false);
        m_jCatalogOrder.setEnabled(false);
        txtAttributes.setEnabled(false);
    }    
    
    public void writeValueEdit(Object value) {
        if(!app.getAppUserView().getUser().getRole().equals("0"))
            m_jPriceBuy.setVisible(false);
        else m_jPriceBuy.setVisible(true);
        reportlock = true;
        Object[] myprod = (Object[]) value;
        m_jTitle.setText(Formats.STRING.formatValue(myprod[1]) + " - " + Formats.STRING.formatValue(myprod[3]));
        m_id = myprod[0];
        m_jRef.setText(Formats.STRING.formatValue(myprod[1]));
        m_jCode.setText(Formats.STRING.formatValue(myprod[1]));
        m_jName.setText(Formats.STRING.formatValue(myprod[3]));
        m_jComment.setSelected(((Boolean)myprod[4]).booleanValue());
        m_jScale.setSelected(((Boolean)myprod[5]).booleanValue());
        m_jPriceBuy.setText(Formats.CURRENCY.formatValue(myprod[6]));
        m_jPriceSell.setText(Formats.CURRENCY.formatValue(myprod[7]));
                                 
        m_CategoryModel.setSelectedKey(myprod[8]);
        taxcatmodel.setSelectedKey("000");
        attmodel.setSelectedKey(myprod[10]);
        m_jImage.setImage((BufferedImage) myprod[11]);
        m_jstockcost.setText(Formats.CURRENCY.formatValue(myprod[12]));
        m_jstockvolume.setText(Formats.STRING.formatValue(myprod[13]));
        m_jInCatalog.setSelected(((Boolean)myprod[14]).booleanValue());
        m_jCatalogOrder.setText(Formats.INT.formatValue(myprod[15]));
   //     txtAttributes.setText(Formats.BYTEA.formatValue(myprod[16]));
        m_jPriceSell2.setText(Formats.CURRENCY.formatValue(myprod[17]));
        m_jPriceSell3.setText(Formats.CURRENCY.formatValue(myprod[18]));
        m_jPriceSell4.setText(Formats.CURRENCY.formatValue(myprod[19]));
        m_jCantidad.setText(Formats.INT.formatValue(myprod[20]));
        m_jCantidad2.setText(Formats.INT.formatValue(myprod[21]));
        m_jCantidad3.setText(Formats.INT.formatValue(myprod[22]));
        m_jCantidad4.setText(Formats.INT.formatValue(myprod[23]));
        txtAttributes.setCaretPosition(0);
        reportlock = false;
        
        // Los habilitados
        m_jRef.setEnabled(true);
        m_jCode.setEnabled(true);
        m_jName.setEnabled(true);
        m_jComment.setEnabled(true);
        m_jScale.setEnabled(true);
        m_jCategory.setEnabled(true);
        m_jTax.setEnabled(true);
        m_jAtt.setEnabled(true);
        m_jPriceBuy.setEnabled(true);
        m_jPriceSell.setEnabled(true); 
        m_jPriceSell2.setEnabled(true); 
        m_jPriceSell3.setEnabled(true); 
        m_jPriceSell4.setEnabled(true); 
        m_jCantidad.setEnabled(true); 
        m_jCantidad2.setEnabled(true); 
        m_jCantidad3.setEnabled(true); 
        m_jCantidad4.setEnabled(true); 
        m_jPriceSellTax.setEnabled(true);
        m_jmargin.setEnabled(true);
        m_jImage.setEnabled(true);
        m_jstockcost.setEnabled(true);
        m_jstockvolume.setEnabled(true);
        m_jInCatalog.setEnabled(true);
        m_jCatalogOrder.setEnabled(m_jInCatalog.isSelected());  
        txtAttributes.setEnabled(true);
    }   

    public Object createValue() throws BasicException {
        if(!app.getAppUserView().getUser().getRole().equals("0"))
            m_jPriceBuy.setVisible(false);
        else m_jPriceBuy.setVisible(true);
        Object[] myprod = new Object[24];
        myprod[0] = m_id;
        myprod[1] = m_jRef.getText();
        myprod[2] = m_jRef.getText();
        myprod[3] = m_jName.getText();
        myprod[4] = Boolean.valueOf(m_jComment.isSelected());
        myprod[5] = Boolean.valueOf(m_jScale.isSelected());
        myprod[6] = Formats.CURRENCY.parseValue(m_jPriceBuy.getText());
        myprod[7] = Formats.CURRENCY.parseValue(m_jPriceSell.getText());;
        myprod[8] = m_CategoryModel.getSelectedKey();
        myprod[9] = "000";
        myprod[10] = attmodel.getSelectedKey();
        myprod[11] = m_jImage.getImage();
        myprod[12] = Formats.CURRENCY.parseValue(m_jstockcost.getText());
        myprod[13] = Formats.STRING.parseValue(m_jstockvolume.getText());
        myprod[14] = Boolean.valueOf(m_jInCatalog.isSelected());
        myprod[15] = Formats.INT.parseValue(m_jCatalogOrder.getText());
        myprod[16] = Formats.BYTEA.parseValue(txtAttributes.getText());
        myprod[17] = Formats.CURRENCY.parseValue(m_jPriceSell2.getText());
        myprod[18] = Formats.CURRENCY.parseValue(m_jPriceSell3.getText());
        myprod[19] = Formats.CURRENCY.parseValue(m_jPriceSell4.getText());
        myprod[20] = Formats.INT.parseValue(m_jCantidad.getText());
        myprod[21] = Formats.INT.parseValue(m_jCantidad2.getText());
        myprod[22] = Formats.INT.parseValue(m_jCantidad3.getText());
        myprod[23] = Formats.INT.parseValue(m_jCantidad4.getText());
        return myprod;
    }
    
    public Component getComponent() {
        return this;
    }
    
    private void calculateMargin() {
        
        if (!reportlock) {
            reportlock = true;
            
            Double dPriceBuy = readCurrency(m_jPriceBuy.getText());
            Double dPriceSell = (Double) pricesell;

            if (dPriceBuy == null || dPriceSell == null) {
                m_jmargin.setText(null);
            } else {
                m_jmargin.setText(Formats.PERCENT.formatValue(new Double(dPriceSell.doubleValue() / dPriceBuy.doubleValue() - 1.0)));
            }    
            reportlock = false;
        }
    }
    
    private void calculatePriceSellTax() {
        
        if (!reportlock) {
            reportlock = true;
            
            Double dPriceSell = (Double) pricesell;
            
            if (dPriceSell == null) {
                m_jPriceSellTax.setText(null);
            } else {               
                double dTaxRate = taxeslogic.getTaxRate((TaxCategoryInfo) taxcatmodel.getSelectedItem(), new Date());
                m_jPriceSellTax.setText(Formats.CURRENCY.formatValue(new Double(dPriceSell.doubleValue() * (1.0 + dTaxRate))));
            }            
            reportlock = false;
        }
    }
    
    private void calculatePriceSellfromMargin() {
        
        if (!reportlock) {
            reportlock = true;
            
            Double dPriceBuy = readCurrency(m_jPriceBuy.getText());
            Double dMargin = readPercent(m_jmargin.getText());  
            
            if (dMargin == null || dPriceBuy == null) {
                setPriceSell(null);
            } else {
                setPriceSell(new Double(dPriceBuy.doubleValue() * (1.0 + dMargin.doubleValue())));
            }                        
            
            reportlock = false;
        }
      
    }
    
    private void calculatePriceSellfromPST() {
        
        if (!reportlock) {
            reportlock = true;
                    
            Double dPriceSellTax = readCurrency(m_jPriceSellTax.getText());  

            if (dPriceSellTax == null) {
                setPriceSell(null);
            } else {
                double dTaxRate = taxeslogic.getTaxRate((TaxCategoryInfo) taxcatmodel.getSelectedItem(), new Date());
                setPriceSell(new Double(dPriceSellTax.doubleValue() / (1.0 + dTaxRate)));
            }   
                        
            reportlock = false;
        }    
    }
    
    private void setPriceSell(Object value) {
        
        if (!priceselllock) {
            priceselllock = true;
            pricesell = value;
            m_jPriceSell.setText(Formats.CURRENCY.formatValue(pricesell));  
            priceselllock = false;
        }
    }
    
    private class PriceSellManager implements DocumentListener {
        public void changedUpdate(DocumentEvent e) {
            /*if (!priceselllock) {
                priceselllock = true;
                pricesell = readCurrency(m_jPriceSell.getText());
                priceselllock = false;
            }
            calculateMargin();
            calculatePriceSellTax();*/
        }
        public void insertUpdate(DocumentEvent e) {
            /*if (!priceselllock) {
                priceselllock = true;
                pricesell = readCurrency(m_jPriceSell.getText());
                priceselllock = false;
            }
            calculateMargin();
            calculatePriceSellTax();*/
        }    
        public void removeUpdate(DocumentEvent e) {
            /*if (!priceselllock) {
                priceselllock = true;
                pricesell = readCurrency(m_jPriceSell.getText());
                priceselllock = false;
            }
            calculateMargin();
            calculatePriceSellTax();*/
        }  
    }
    
    private class FieldsManager implements DocumentListener, ActionListener {
        public void changedUpdate(DocumentEvent e) {
            //calculateMargin();
            //calculatePriceSellTax();
        }
        public void insertUpdate(DocumentEvent e) {
            //calculateMargin();
            //calculatePriceSellTax();
        }    
        public void removeUpdate(DocumentEvent e) {
            //calculateMargin();
            //calculatePriceSellTax();
        }         
        public void actionPerformed(ActionEvent e) {
            //calculateMargin();
            //calculatePriceSellTax();
        }
    }

    private class PriceTaxManager implements DocumentListener {
        public void changedUpdate(DocumentEvent e) {
            //calculatePriceSellfromPST();
            //calculateMargin();
        }
        public void insertUpdate(DocumentEvent e) {
            //calculatePriceSellfromPST();
            //calculateMargin();
        }    
        public void removeUpdate(DocumentEvent e) {
            //calculatePriceSellfromPST();
            //calculateMargin();
        }         
    }
    
    private class MarginManager implements DocumentListener  {
        public void changedUpdate(DocumentEvent e) {
            //calculatePriceSellfromMargin();
            //calculatePriceSellTax();
        }
        public void insertUpdate(DocumentEvent e) {
            //calculatePriceSellfromMargin();
            //calculatePriceSellTax();
        }    
        public void removeUpdate(DocumentEvent e) {
            //calculatePriceSellfromMargin();
            //calculatePriceSellTax();
        }         
    }
    
    private final static Double readCurrency(String sValue) {
        try {
            return (Double) Formats.CURRENCY.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }
        
    private final static Double readPercent(String sValue) {
        try {
            return (Double) Formats.PERCENT.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jCode = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        m_jAtt = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        m_jstockcost = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        m_jComment = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        m_jCatalogOrder = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAttributes = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        m_jImage1 = new com.openbravo.data.gui.JImageEditor();
        jLabel14 = new javax.swing.JLabel();
        m_jPriceBuy1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        m_jPriceSell1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        m_jCategory1 = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        m_jInCatalog1 = new javax.swing.JCheckBox();
        m_jCodetype = new javax.swing.JComboBox();
        m_jstockvolume = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        m_jmargin = new javax.swing.JTextField();
        m_jTax = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        m_jScale = new javax.swing.JCheckBox();
        m_jPriceSellTax = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        m_jRef1 = new javax.swing.JTextField();
        m_jCant1 = new javax.swing.JTextField();
        m_jProd1 = new javax.swing.JTextField();
        m_jRef2 = new javax.swing.JTextField();
        m_jCant2 = new javax.swing.JTextField();
        m_jProd2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        m_jRef3 = new javax.swing.JTextField();
        m_jCant3 = new javax.swing.JTextField();
        m_jProd3 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        m_jRef4 = new javax.swing.JTextField();
        m_jCant4 = new javax.swing.JTextField();
        m_jProd4 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        m_jRef5 = new javax.swing.JTextField();
        m_jCant5 = new javax.swing.JTextField();
        m_jProd5 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        m_jRef6 = new javax.swing.JTextField();
        m_jCant6 = new javax.swing.JTextField();
        m_jProd6 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        m_jRef7 = new javax.swing.JTextField();
        m_jCant7 = new javax.swing.JTextField();
        m_jProd7 = new javax.swing.JTextField();
        m_jRef8 = new javax.swing.JTextField();
        m_jCant8 = new javax.swing.JTextField();
        m_jProd8 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        m_jRef9 = new javax.swing.JTextField();
        m_jCant9 = new javax.swing.JTextField();
        m_jProd9 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        m_jRef10 = new javax.swing.JTextField();
        m_jCant10 = new javax.swing.JTextField();
        m_jProd10 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        m_jRef11 = new javax.swing.JTextField();
        m_jCant11 = new javax.swing.JTextField();
        m_jProd11 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        m_jRef12 = new javax.swing.JTextField();
        m_jCant12 = new javax.swing.JTextField();
        m_jProd12 = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        m_jRef13 = new javax.swing.JTextField();
        m_jCant13 = new javax.swing.JTextField();
        m_jProd13 = new javax.swing.JTextField();
        m_jRef14 = new javax.swing.JTextField();
        m_jCant14 = new javax.swing.JTextField();
        m_jProd14 = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        m_jRef15 = new javax.swing.JTextField();
        m_jCant15 = new javax.swing.JTextField();
        m_jProd15 = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        m_jRef16 = new javax.swing.JTextField();
        m_jCant16 = new javax.swing.JTextField();
        m_jProd16 = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        m_jRef17 = new javax.swing.JTextField();
        m_jCant17 = new javax.swing.JTextField();
        m_jProd17 = new javax.swing.JTextField();
        jButton17 = new javax.swing.JButton();
        m_jRef18 = new javax.swing.JTextField();
        m_jCant18 = new javax.swing.JTextField();
        m_jProd18 = new javax.swing.JTextField();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        m_jRef19 = new javax.swing.JTextField();
        m_jCant19 = new javax.swing.JTextField();
        m_jProd19 = new javax.swing.JTextField();
        m_jRef20 = new javax.swing.JTextField();
        m_jCant20 = new javax.swing.JTextField();
        m_jProd20 = new javax.swing.JTextField();
        jButton20 = new javax.swing.JButton();
        m_jRef21 = new javax.swing.JTextField();
        m_jCant21 = new javax.swing.JTextField();
        m_jProd21 = new javax.swing.JTextField();
        jButton21 = new javax.swing.JButton();
        m_jRef22 = new javax.swing.JTextField();
        m_jCant22 = new javax.swing.JTextField();
        m_jProd22 = new javax.swing.JTextField();
        jButton22 = new javax.swing.JButton();
        m_jRef23 = new javax.swing.JTextField();
        m_jCant23 = new javax.swing.JTextField();
        m_jProd23 = new javax.swing.JTextField();
        jButton23 = new javax.swing.JButton();
        m_jRef24 = new javax.swing.JTextField();
        m_jCant24 = new javax.swing.JTextField();
        m_jProd24 = new javax.swing.JTextField();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        m_jRef = new javax.swing.JTextField();
        m_jName = new javax.swing.JTextField();
        m_jTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        m_jImage = new com.openbravo.data.gui.JImageEditor();
        jLabel3 = new javax.swing.JLabel();
        m_jPriceBuy = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        m_jPriceSell = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        m_jCategory = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        m_jInCatalog = new javax.swing.JCheckBox();
        jLabel24 = new javax.swing.JLabel();
        m_jPriceSell2 = new javax.swing.JTextField();
        m_jPriceSell3 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        m_jPriceSell4 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        m_jCantidad = new javax.swing.JTextField();
        m_jCantidad2 = new javax.swing.JTextField();
        m_jCantidad3 = new javax.swing.JTextField();
        m_jCantidad4 = new javax.swing.JTextField();

        jLabel6.setText(AppLocal.getIntString("label.prodbarcode")); // NOI18N

        jLabel16.setText(AppLocal.getIntString("label.prodpriceselltax")); // NOI18N

        jLabel13.setText(AppLocal.getIntString("label.attributes")); // NOI18N

        jLabel7.setText(AppLocal.getIntString("label.taxcategory")); // NOI18N

        jLabel12.setText("Es bebida?");

        jPanel2.setLayout(null);

        jLabel9.setText(AppLocal.getIntString("label.prodstockcost")); // NOI18N
        jPanel2.add(jLabel9);
        jLabel9.setBounds(10, 20, 150, 14);

        m_jstockcost.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(m_jstockcost);
        m_jstockcost.setBounds(160, 20, 80, 20);

        jLabel10.setText(AppLocal.getIntString("label.prodstockvol")); // NOI18N
        jPanel2.add(jLabel10);
        jLabel10.setBounds(10, 50, 150, 14);
        jPanel2.add(m_jComment);
        m_jComment.setBounds(160, 110, 80, 21);

        jLabel18.setText(AppLocal.getIntString("label.prodorder")); // NOI18N
        jPanel2.add(jLabel18);
        jLabel18.setBounds(250, 80, 60, 14);

        m_jCatalogOrder.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(m_jCatalogOrder);
        m_jCatalogOrder.setBounds(310, 80, 80, 20);

        jLabel11.setText(AppLocal.getIntString("label.prodaux")); // NOI18N
        jPanel2.add(jLabel11);
        jLabel11.setBounds(10, 110, 150, 14);

        txtAttributes.setFont(new java.awt.Font("DialogInput", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(txtAttributes);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel14.setText(AppLocal.getIntString("label.prodpricebuy")); // NOI18N

        m_jPriceBuy1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel15.setText(AppLocal.getIntString("label.prodpricesell")); // NOI18N

        m_jPriceSell1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel17.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N

        jLabel19.setText(AppLocal.getIntString("label.prodincatalog")); // NOI18N

        m_jInCatalog1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jInCatalog1ActionPerformed(evt);
            }
        });

        m_jstockvolume.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel20.setText("Unidad");

        m_jmargin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel21.setText("Tipo de impuesto");

        jLabel23.setText("Balanza");

        m_jPriceSellTax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel22.setText("Precio de venta + impuesto");

        jPanel4.setLayout(null);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1);
        jButton1.setBounds(10, 10, 30, 25);

        m_jRef1.setEnabled(false);
        jPanel4.add(m_jRef1);
        m_jRef1.setBounds(50, 10, 80, 20);
        jPanel4.add(m_jCant1);
        m_jCant1.setBounds(490, 10, 50, 20);

        m_jProd1.setEnabled(false);
        jPanel4.add(m_jProd1);
        m_jProd1.setBounds(140, 10, 340, 20);

        m_jRef2.setEnabled(false);
        jPanel4.add(m_jRef2);
        m_jRef2.setBounds(50, 50, 80, 20);
        jPanel4.add(m_jCant2);
        m_jCant2.setBounds(490, 50, 50, 20);

        m_jProd2.setEnabled(false);
        jPanel4.add(m_jProd2);
        m_jProd2.setBounds(140, 50, 340, 20);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton2);
        jButton2.setBounds(10, 50, 30, 25);

        m_jRef3.setEnabled(false);
        jPanel4.add(m_jRef3);
        m_jRef3.setBounds(50, 90, 80, 20);
        jPanel4.add(m_jCant3);
        m_jCant3.setBounds(490, 90, 50, 20);

        m_jProd3.setEnabled(false);
        jPanel4.add(m_jProd3);
        m_jProd3.setBounds(140, 90, 340, 20);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton4);
        jButton4.setBounds(10, 90, 30, 25);

        m_jRef4.setEnabled(false);
        jPanel4.add(m_jRef4);
        m_jRef4.setBounds(50, 130, 80, 20);
        jPanel4.add(m_jCant4);
        m_jCant4.setBounds(490, 130, 50, 20);

        m_jProd4.setEnabled(false);
        jPanel4.add(m_jProd4);
        m_jProd4.setBounds(140, 130, 340, 20);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton5);
        jButton5.setBounds(10, 130, 30, 25);

        m_jRef5.setEnabled(false);
        jPanel4.add(m_jRef5);
        m_jRef5.setBounds(50, 170, 80, 20);
        jPanel4.add(m_jCant5);
        m_jCant5.setBounds(490, 170, 50, 20);

        m_jProd5.setEnabled(false);
        jPanel4.add(m_jProd5);
        m_jProd5.setBounds(140, 170, 340, 20);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton6);
        jButton6.setBounds(10, 170, 30, 25);

        m_jRef6.setEnabled(false);
        jPanel4.add(m_jRef6);
        m_jRef6.setBounds(50, 210, 80, 20);
        jPanel4.add(m_jCant6);
        m_jCant6.setBounds(490, 210, 50, 20);

        m_jProd6.setEnabled(false);
        jPanel4.add(m_jProd6);
        m_jProd6.setBounds(140, 210, 340, 20);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton7);
        jButton7.setBounds(10, 210, 30, 25);

        jPanel5.setLayout(null);

        m_jRef7.setEnabled(false);
        jPanel5.add(m_jRef7);
        m_jRef7.setBounds(50, 10, 80, 20);
        jPanel5.add(m_jCant7);
        m_jCant7.setBounds(490, 10, 50, 20);

        m_jProd7.setEnabled(false);
        jPanel5.add(m_jProd7);
        m_jProd7.setBounds(140, 10, 340, 20);

        m_jRef8.setEnabled(false);
        jPanel5.add(m_jRef8);
        m_jRef8.setBounds(50, 50, 80, 20);
        jPanel5.add(m_jCant8);
        m_jCant8.setBounds(490, 50, 50, 20);

        m_jProd8.setEnabled(false);
        jPanel5.add(m_jProd8);
        m_jProd8.setBounds(140, 50, 340, 20);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton8);
        jButton8.setBounds(10, 10, 30, 25);

        m_jRef9.setEnabled(false);
        jPanel5.add(m_jRef9);
        m_jRef9.setBounds(50, 90, 80, 20);
        jPanel5.add(m_jCant9);
        m_jCant9.setBounds(490, 90, 50, 20);

        m_jProd9.setEnabled(false);
        jPanel5.add(m_jProd9);
        m_jProd9.setBounds(140, 90, 340, 20);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton9);
        jButton9.setBounds(10, 90, 30, 25);

        m_jRef10.setEnabled(false);
        jPanel5.add(m_jRef10);
        m_jRef10.setBounds(50, 130, 80, 20);
        jPanel5.add(m_jCant10);
        m_jCant10.setBounds(490, 130, 50, 20);

        m_jProd10.setEnabled(false);
        jPanel5.add(m_jProd10);
        m_jProd10.setBounds(140, 130, 340, 20);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton10);
        jButton10.setBounds(10, 130, 30, 25);

        m_jRef11.setEnabled(false);
        jPanel5.add(m_jRef11);
        m_jRef11.setBounds(50, 170, 80, 20);
        jPanel5.add(m_jCant11);
        m_jCant11.setBounds(490, 170, 50, 20);

        m_jProd11.setEnabled(false);
        jPanel5.add(m_jProd11);
        m_jProd11.setBounds(140, 170, 340, 20);

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton11);
        jButton11.setBounds(10, 170, 30, 25);

        m_jRef12.setEnabled(false);
        jPanel5.add(m_jRef12);
        m_jRef12.setBounds(50, 210, 80, 20);
        jPanel5.add(m_jCant12);
        m_jCant12.setBounds(490, 210, 50, 20);

        m_jProd12.setEnabled(false);
        jPanel5.add(m_jProd12);
        m_jProd12.setBounds(140, 210, 340, 20);

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton12);
        jButton12.setBounds(10, 210, 30, 25);

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton13);
        jButton13.setBounds(10, 50, 30, 25);

        jPanel6.setLayout(null);

        m_jRef13.setEnabled(false);
        jPanel6.add(m_jRef13);
        m_jRef13.setBounds(50, 10, 80, 20);
        jPanel6.add(m_jCant13);
        m_jCant13.setBounds(490, 10, 50, 20);

        m_jProd13.setEnabled(false);
        jPanel6.add(m_jProd13);
        m_jProd13.setBounds(140, 10, 340, 20);

        m_jRef14.setEnabled(false);
        jPanel6.add(m_jRef14);
        m_jRef14.setBounds(50, 50, 80, 20);
        jPanel6.add(m_jCant14);
        m_jCant14.setBounds(490, 50, 50, 20);

        m_jProd14.setEnabled(false);
        jPanel6.add(m_jProd14);
        m_jProd14.setBounds(140, 50, 340, 20);

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton14);
        jButton14.setBounds(10, 10, 30, 25);

        m_jRef15.setEnabled(false);
        jPanel6.add(m_jRef15);
        m_jRef15.setBounds(50, 90, 80, 20);
        jPanel6.add(m_jCant15);
        m_jCant15.setBounds(490, 90, 50, 20);

        m_jProd15.setEnabled(false);
        jPanel6.add(m_jProd15);
        m_jProd15.setBounds(140, 90, 340, 20);

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton15);
        jButton15.setBounds(10, 90, 30, 25);

        m_jRef16.setEnabled(false);
        jPanel6.add(m_jRef16);
        m_jRef16.setBounds(50, 130, 80, 20);
        jPanel6.add(m_jCant16);
        m_jCant16.setBounds(490, 130, 50, 20);

        m_jProd16.setEnabled(false);
        jPanel6.add(m_jProd16);
        m_jProd16.setBounds(140, 130, 340, 20);

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton16);
        jButton16.setBounds(10, 130, 30, 25);

        m_jRef17.setEnabled(false);
        jPanel6.add(m_jRef17);
        m_jRef17.setBounds(50, 170, 80, 20);
        jPanel6.add(m_jCant17);
        m_jCant17.setBounds(490, 170, 50, 20);

        m_jProd17.setEnabled(false);
        jPanel6.add(m_jProd17);
        m_jProd17.setBounds(140, 170, 340, 20);

        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton17);
        jButton17.setBounds(10, 170, 30, 25);

        m_jRef18.setEnabled(false);
        jPanel6.add(m_jRef18);
        m_jRef18.setBounds(50, 210, 80, 20);
        jPanel6.add(m_jCant18);
        m_jCant18.setBounds(490, 210, 50, 20);

        m_jProd18.setEnabled(false);
        jPanel6.add(m_jProd18);
        m_jProd18.setBounds(140, 210, 340, 20);

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton18);
        jButton18.setBounds(10, 210, 30, 25);

        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton19);
        jButton19.setBounds(10, 50, 30, 25);

        jPanel7.setLayout(null);

        m_jRef19.setEnabled(false);
        jPanel7.add(m_jRef19);
        m_jRef19.setBounds(50, 10, 80, 20);
        jPanel7.add(m_jCant19);
        m_jCant19.setBounds(490, 10, 50, 20);

        m_jProd19.setEnabled(false);
        jPanel7.add(m_jProd19);
        m_jProd19.setBounds(140, 10, 340, 20);

        m_jRef20.setEnabled(false);
        jPanel7.add(m_jRef20);
        m_jRef20.setBounds(50, 50, 80, 20);
        jPanel7.add(m_jCant20);
        m_jCant20.setBounds(490, 50, 50, 20);

        m_jProd20.setEnabled(false);
        jPanel7.add(m_jProd20);
        m_jProd20.setBounds(140, 50, 340, 20);

        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton20);
        jButton20.setBounds(10, 10, 30, 25);

        m_jRef21.setEnabled(false);
        jPanel7.add(m_jRef21);
        m_jRef21.setBounds(50, 90, 80, 20);
        jPanel7.add(m_jCant21);
        m_jCant21.setBounds(490, 90, 50, 20);

        m_jProd21.setEnabled(false);
        jPanel7.add(m_jProd21);
        m_jProd21.setBounds(140, 90, 340, 20);

        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton21);
        jButton21.setBounds(10, 90, 30, 25);

        m_jRef22.setEnabled(false);
        jPanel7.add(m_jRef22);
        m_jRef22.setBounds(50, 130, 80, 20);
        jPanel7.add(m_jCant22);
        m_jCant22.setBounds(490, 130, 50, 20);

        m_jProd22.setEnabled(false);
        jPanel7.add(m_jProd22);
        m_jProd22.setBounds(140, 130, 340, 20);

        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton22);
        jButton22.setBounds(10, 130, 30, 25);

        m_jRef23.setEnabled(false);
        jPanel7.add(m_jRef23);
        m_jRef23.setBounds(50, 170, 80, 20);
        jPanel7.add(m_jCant23);
        m_jCant23.setBounds(490, 170, 50, 20);

        m_jProd23.setEnabled(false);
        jPanel7.add(m_jProd23);
        m_jProd23.setBounds(140, 170, 340, 20);

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton23);
        jButton23.setBounds(10, 170, 30, 25);

        m_jRef24.setEnabled(false);
        jPanel7.add(m_jRef24);
        m_jRef24.setBounds(50, 210, 80, 20);
        jPanel7.add(m_jCant24);
        m_jCant24.setBounds(490, 210, 50, 20);

        m_jProd24.setEnabled(false);
        jPanel7.add(m_jProd24);
        m_jProd24.setBounds(140, 210, 340, 20);

        jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton24);
        jButton24.setBounds(10, 210, 30, 25);

        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton25);
        jButton25.setBounds(10, 50, 30, 25);

        setLayout(null);

        jLabel1.setText(AppLocal.getIntString("label.prodref")); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(10, 50, 80, 14);

        jLabel2.setText(AppLocal.getIntString("label.prodname")); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(180, 50, 70, 14);
        add(m_jRef);
        m_jRef.setBounds(90, 50, 80, 20);
        add(m_jName);
        m_jName.setBounds(250, 50, 320, 20);

        m_jTitle.setFont(new java.awt.Font("SansSerif", 3, 18)); // NOI18N
        add(m_jTitle);
        m_jTitle.setBounds(10, 10, 560, 30);

        jPanel1.setLayout(null);
        jPanel1.add(m_jImage);
        m_jImage.setBounds(340, 20, 200, 180);

        jLabel3.setText(AppLocal.getIntString("label.prodpricebuy")); // NOI18N
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 10, 110, 14);

        m_jPriceBuy.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceBuy);
        m_jPriceBuy.setBounds(160, 10, 80, 20);

        jLabel4.setText("Pieza");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 40, 150, 14);

        m_jPriceSell.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceSell);
        m_jPriceSell.setBounds(160, 40, 80, 20);

        jLabel5.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 160, 150, 14);
        jPanel1.add(m_jCategory);
        m_jCategory.setBounds(160, 160, 170, 20);

        jLabel8.setText(AppLocal.getIntString("label.prodincatalog")); // NOI18N
        jPanel1.add(jLabel8);
        jLabel8.setBounds(10, 190, 150, 14);

        m_jInCatalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jInCatalogActionPerformed(evt);
            }
        });
        jPanel1.add(m_jInCatalog);
        m_jInCatalog.setBounds(160, 190, 50, 21);

        jLabel24.setText("Serie");
        jPanel1.add(jLabel24);
        jLabel24.setBounds(10, 70, 150, 14);

        m_jPriceSell2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceSell2);
        m_jPriceSell2.setBounds(160, 70, 80, 20);

        m_jPriceSell3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceSell3);
        m_jPriceSell3.setBounds(160, 100, 80, 20);

        jLabel25.setText("Paquete");
        jPanel1.add(jLabel25);
        jLabel25.setBounds(10, 100, 150, 14);

        m_jPriceSell4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceSell4);
        m_jPriceSell4.setBounds(160, 130, 80, 20);

        jLabel26.setText("Caja");
        jPanel1.add(jLabel26);
        jLabel26.setBounds(10, 130, 150, 14);

        m_jCantidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jCantidad);
        m_jCantidad.setBounds(250, 40, 80, 20);

        m_jCantidad2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jCantidad2);
        m_jCantidad2.setBounds(250, 70, 80, 20);

        m_jCantidad3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jCantidad3);
        m_jCantidad3.setBounds(250, 100, 80, 20);

        m_jCantidad4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jCantidad4);
        m_jCantidad4.setBounds(250, 130, 80, 20);

        jTabbedPane1.addTab(AppLocal.getIntString("label.prodgeneral"), jPanel1); // NOI18N

        add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 80, 560, 280);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jInCatalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jInCatalogActionPerformed
 
        if (m_jInCatalog.isSelected()) {
            m_jCatalogOrder.setEnabled(true);   
        } else {
            m_jCatalogOrder.setEnabled(false);   
            m_jCatalogOrder.setText(null);   
        }

    }//GEN-LAST:event_m_jInCatalogActionPerformed

    private void m_jInCatalog1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jInCatalog1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jInCatalog1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    this.prod1 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod1 != null)
    {
      this.m_jRef1.setText(this.prod1.getReference());
      this.m_jProd1.setText(this.prod1.getName());
      this.m_jCant1.setText("");
      this.m_jCant1.setEnabled(true);
      this.m_jCant1.requestFocus();
    }
    else
    {
      this.m_jRef1.setText("");
      this.m_jProd1.setText("");
      this.m_jCant1.setText("");
      this.m_jCant1.setEnabled(false);
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
       this.prod2 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod2 != null)
    {
      this.m_jRef2.setText(this.prod2.getReference());
      this.m_jProd2.setText(this.prod2.getName());
      this.m_jCant2.setText("");
      this.m_jCant2.setEnabled(true);
      this.m_jCant2.requestFocus();
    }
    else
    {
      this.m_jRef2.setText("");
      this.m_jProd2.setText("");
      this.m_jCant2.setText("");
      this.m_jCant2.setEnabled(false);
    }
    }                                        

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    this.prod2 = JProductFinder.showMessage(this, this.dlSales);
        if (this.prod2 != null)
        {
          this.m_jRef2.setText(this.prod2.getReference());
          this.m_jProd2.setText(this.prod2.getName());
          this.m_jCant2.setText("");
          this.m_jCant2.setEnabled(true);
          this.m_jCant2.requestFocus();
        }
        else
        {
          this.m_jRef2.setText("");
          this.m_jProd2.setText("");
          this.m_jCant2.setText("");
          this.m_jCant2.setEnabled(false);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.prod3 = JProductFinder.showMessage(this, this.dlSales);
        if (this.prod3 != null)
        {
          this.m_jRef3.setText(this.prod3.getReference());
          this.m_jProd3.setText(this.prod3.getName());
          this.m_jCant3.setText("");
          this.m_jCant3.setEnabled(true);
          this.m_jCant3.requestFocus();
        }
        else
        {
          this.m_jRef3.setText("");
          this.m_jProd3.setText("");
          this.m_jCant3.setText("");
          this.m_jCant3.setEnabled(false);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.prod4 = JProductFinder.showMessage(this, this.dlSales);
        if (this.prod4 != null)
        {
          this.m_jRef4.setText(this.prod4.getReference());
          this.m_jProd4.setText(this.prod4.getName());
          this.m_jCant4.setText("");
          this.m_jCant4.setEnabled(true);
          this.m_jCant4.requestFocus();
        }
        else
        {
          this.m_jRef4.setText("");
          this.m_jProd4.setText("");
          this.m_jCant4.setText("");
          this.m_jCant4.setEnabled(false);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        this.prod5 = JProductFinder.showMessage(this, this.dlSales);
        if (this.prod5 != null)
        {
          this.m_jRef5.setText(this.prod5.getReference());
          this.m_jProd5.setText(this.prod5.getName());
          this.m_jCant5.setText("");
          this.m_jCant5.setEnabled(true);
          this.m_jCant5.requestFocus();
        }
        else
        {
          this.m_jRef5.setText("");
          this.m_jProd5.setText("");
          this.m_jCant5.setText("");
          this.m_jCant5.setEnabled(false);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
      this.prod6 = JProductFinder.showMessage(this, this.dlSales);
        if (this.prod6 != null)
        {
          this.m_jRef6.setText(this.prod6.getReference());
          this.m_jProd6.setText(this.prod6.getName());
          this.m_jCant6.setText("");
          this.m_jCant6.setEnabled(true);
          this.m_jCant6.requestFocus();
        }
        else
        {
          this.m_jRef6.setText("");
          this.m_jProd6.setText("");
          this.m_jCant6.setText("");
          this.m_jCant6.setEnabled(false);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
    this.prod7 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod7 != null)
    {
      this.m_jRef7.setText(this.prod7.getReference());
      this.m_jProd7.setText(this.prod7.getName());
      this.m_jCant7.setText("");
      this.m_jCant7.setEnabled(true);
      this.m_jCant7.requestFocus();
    }
    else
    {
      this.m_jRef7.setText("");
      this.m_jProd7.setText("");
      this.m_jCant7.setText("");
      this.m_jCant7.setEnabled(false);
    }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
this.prod9 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod9 != null)
    {
      this.m_jRef9.setText(this.prod9.getReference());
      this.m_jProd9.setText(this.prod9.getName());
      this.m_jCant9.setText("");
      this.m_jCant9.setEnabled(true);
      this.m_jCant9.requestFocus();
    }
    else
    {
      this.m_jRef9.setText("");
      this.m_jProd9.setText("");
      this.m_jCant9.setText("");
      this.m_jCant9.setEnabled(false);
    }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
this.prod10 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod10 != null)
    {
      this.m_jRef10.setText(this.prod10.getReference());
      this.m_jProd10.setText(this.prod10.getName());
      this.m_jCant10.setText("");
      this.m_jCant10.setEnabled(true);
      this.m_jCant10.requestFocus();
    }
    else
    {
      this.m_jRef10.setText("");
      this.m_jProd10.setText("");
      this.m_jCant10.setText("");
      this.m_jCant10.setEnabled(false);
    }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
this.prod11 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod11 != null)
    {
      this.m_jRef11.setText(this.prod11.getReference());
      this.m_jProd11.setText(this.prod11.getName());
      this.m_jCant11.setText("");
      this.m_jCant11.setEnabled(true);
      this.m_jCant11.requestFocus();
    }
    else
    {
      this.m_jRef11.setText("");
      this.m_jProd11.setText("");
      this.m_jCant11.setText("");
      this.m_jCant11.setEnabled(false);
    }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
this.prod12 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod12 != null)
    {
      this.m_jRef12.setText(this.prod12.getReference());
      this.m_jProd12.setText(this.prod12.getName());
      this.m_jCant12.setText("");
      this.m_jCant12.setEnabled(true);
      this.m_jCant12.requestFocus();
    }
    else
    {
      this.m_jRef12.setText("");
      this.m_jProd12.setText("");
      this.m_jCant12.setText("");
      this.m_jCant12.setEnabled(false);
    }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        this.prod8 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod8 != null)
    {
      this.m_jRef8.setText(this.prod8.getReference());
      this.m_jProd8.setText(this.prod8.getName());
      this.m_jCant8.setText("");
      this.m_jCant8.setEnabled(true);
      this.m_jCant8.requestFocus();
    }
    else
    {
      this.m_jRef8.setText("");
      this.m_jProd8.setText("");
      this.m_jCant8.setText("");
      this.m_jCant8.setEnabled(false);
    }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
this.prod13 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod13 != null)
    {
      this.m_jRef13.setText(this.prod13.getReference());
      this.m_jProd13.setText(this.prod13.getName());
      this.m_jCant13.setText("");
      this.m_jCant13.setEnabled(true);
      this.m_jCant13.requestFocus();
    }
    else
    {
      this.m_jRef13.setText("");
      this.m_jProd13.setText("");
      this.m_jCant13.setText("");
      this.m_jCant13.setEnabled(false);
    }      
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
    this.prod15 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod15 != null)
    {
      this.m_jRef15.setText(this.prod15.getReference());
      this.m_jProd15.setText(this.prod15.getName());
      this.m_jCant15.setText("");
      this.m_jCant15.setEnabled(true);
      this.m_jCant15.requestFocus();
    }
    else
    {
      this.m_jRef15.setText("");
      this.m_jProd15.setText("");
      this.m_jCant15.setText("");
      this.m_jCant15.setEnabled(false);
    }   
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        this.prod16 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod16 != null)
    {
      this.m_jRef16.setText(this.prod16.getReference());
      this.m_jProd16.setText(this.prod16.getName());
      this.m_jCant16.setText("");
      this.m_jCant16.setEnabled(true);
      this.m_jCant16.requestFocus();
    }
    else
    {
      this.m_jRef16.setText("");
      this.m_jProd16.setText("");
      this.m_jCant16.setText("");
      this.m_jCant16.setEnabled(false);
    }  // TODO add your handling code here:
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
 this.prod17 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod17 != null)
    {
      this.m_jRef17.setText(this.prod17.getReference());
      this.m_jProd17.setText(this.prod17.getName());
      this.m_jCant17.setText("");
      this.m_jCant17.setEnabled(true);
      this.m_jCant17.requestFocus();
    }
    else
    {
      this.m_jRef17.setText("");
      this.m_jProd17.setText("");
      this.m_jCant17.setText("");
      this.m_jCant17.setEnabled(false);
    }         // TODO add your handling code here:
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
         this.prod18 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod18 != null)
    {
      this.m_jRef18.setText(this.prod18.getReference());
      this.m_jProd18.setText(this.prod18.getName());
      this.m_jCant18.setText("");
      this.m_jCant18.setEnabled(true);
      this.m_jCant18.requestFocus();
    }
    else
    {
      this.m_jRef18.setText("");
      this.m_jProd18.setText("");
      this.m_jCant18.setText("");
      this.m_jCant18.setEnabled(false);
    } 
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
this.prod14 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod14 != null)
    {
      this.m_jRef14.setText(this.prod14.getReference());
      this.m_jProd14.setText(this.prod14.getName());
      this.m_jCant14.setText("");
      this.m_jCant14.setEnabled(true);
      this.m_jCant14.requestFocus();
    }
    else
    {
      this.m_jRef14.setText("");
      this.m_jProd14.setText("");
      this.m_jCant14.setText("");
      this.m_jCant14.setEnabled(false);
    }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
 this.prod19 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod19 != null)
    {
      this.m_jRef19.setText(this.prod19.getReference());
      this.m_jProd19.setText(this.prod19.getName());
      this.m_jCant19.setText("");
      this.m_jCant19.setEnabled(true);
      this.m_jCant19.requestFocus();
    }
    else
    {
      this.m_jRef19.setText("");
      this.m_jProd19.setText("");
      this.m_jCant19.setText("");
      this.m_jCant19.setEnabled(false);
    } 
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
 this.prod21 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod21 != null)
    {
      this.m_jRef21.setText(this.prod21.getReference());
      this.m_jProd21.setText(this.prod21.getName());
      this.m_jCant21.setText("");
      this.m_jCant21.setEnabled(true);
      this.m_jCant21.requestFocus();
    }
    else
    {
      this.m_jRef21.setText("");
      this.m_jProd21.setText("");
      this.m_jCant21.setText("");
      this.m_jCant21.setEnabled(false);
    }         // TODO add your handling code here:
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        this.prod22 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod22 != null)
    {
      this.m_jRef22.setText(this.prod22.getReference());
      this.m_jProd22.setText(this.prod22.getName());
      this.m_jCant22.setText("");
      this.m_jCant22.setEnabled(true);
      this.m_jCant22.requestFocus();
    }
    else
    {
      this.m_jRef22.setText("");
      this.m_jProd22.setText("");
      this.m_jCant22.setText("");
      this.m_jCant22.setEnabled(false);
    }  // TODO add your handling code here:
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        this.prod23 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod23 != null)
    {
      this.m_jRef23.setText(this.prod23.getReference());
      this.m_jProd23.setText(this.prod23.getName());
      this.m_jCant23.setText("");
      this.m_jCant23.setEnabled(true);
      this.m_jCant23.requestFocus();
    }
    else
    {
      this.m_jRef23.setText("");
      this.m_jProd23.setText("");
      this.m_jCant23.setText("");
      this.m_jCant23.setEnabled(false);
    }  // TODO add your handling code here:
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
         this.prod24 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod24 != null)
    {
      this.m_jRef24.setText(this.prod24.getReference());
      this.m_jProd24.setText(this.prod24.getName());
      this.m_jCant24.setText("");
      this.m_jCant24.setEnabled(true);
      this.m_jCant24.requestFocus();
    }
    else
    {
      this.m_jRef24.setText("");
      this.m_jProd24.setText("");
      this.m_jCant24.setText("");
      this.m_jCant24.setEnabled(false);
    } // TODO add your handling code here:
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
 this.prod20 = JProductFinder.showMessage(this, this.dlSales);
    if (this.prod20 != null)
    {
      this.m_jRef20.setText(this.prod20.getReference());
      this.m_jProd20.setText(this.prod20.getName());
      this.m_jCant20.setText("");
      this.m_jCant20.setEnabled(true);
      this.m_jCant20.requestFocus();
    }
    else
    {
      this.m_jRef20.setText("");
      this.m_jProd20.setText("");
      this.m_jCant20.setText("");
      this.m_jCant20.setEnabled(false);
    }         // TODO add your handling code here:
    }//GEN-LAST:event_jButton25ActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox m_jAtt;
    private javax.swing.JTextField m_jCant1;
    private javax.swing.JTextField m_jCant10;
    private javax.swing.JTextField m_jCant11;
    private javax.swing.JTextField m_jCant12;
    private javax.swing.JTextField m_jCant13;
    private javax.swing.JTextField m_jCant14;
    private javax.swing.JTextField m_jCant15;
    private javax.swing.JTextField m_jCant16;
    private javax.swing.JTextField m_jCant17;
    private javax.swing.JTextField m_jCant18;
    private javax.swing.JTextField m_jCant19;
    private javax.swing.JTextField m_jCant2;
    private javax.swing.JTextField m_jCant20;
    private javax.swing.JTextField m_jCant21;
    private javax.swing.JTextField m_jCant22;
    private javax.swing.JTextField m_jCant23;
    private javax.swing.JTextField m_jCant24;
    private javax.swing.JTextField m_jCant3;
    private javax.swing.JTextField m_jCant4;
    private javax.swing.JTextField m_jCant5;
    private javax.swing.JTextField m_jCant6;
    private javax.swing.JTextField m_jCant7;
    private javax.swing.JTextField m_jCant8;
    private javax.swing.JTextField m_jCant9;
    private javax.swing.JTextField m_jCantidad;
    private javax.swing.JTextField m_jCantidad2;
    private javax.swing.JTextField m_jCantidad3;
    private javax.swing.JTextField m_jCantidad4;
    private javax.swing.JTextField m_jCatalogOrder;
    private javax.swing.JComboBox m_jCategory;
    private javax.swing.JComboBox m_jCategory1;
    private javax.swing.JTextField m_jCode;
    private javax.swing.JComboBox m_jCodetype;
    private javax.swing.JCheckBox m_jComment;
    private com.openbravo.data.gui.JImageEditor m_jImage;
    private com.openbravo.data.gui.JImageEditor m_jImage1;
    private javax.swing.JCheckBox m_jInCatalog;
    private javax.swing.JCheckBox m_jInCatalog1;
    private javax.swing.JTextField m_jName;
    private javax.swing.JTextField m_jPriceBuy;
    private javax.swing.JTextField m_jPriceBuy1;
    private javax.swing.JTextField m_jPriceSell;
    private javax.swing.JTextField m_jPriceSell1;
    private javax.swing.JTextField m_jPriceSell2;
    private javax.swing.JTextField m_jPriceSell3;
    private javax.swing.JTextField m_jPriceSell4;
    private javax.swing.JTextField m_jPriceSellTax;
    private javax.swing.JTextField m_jProd1;
    private javax.swing.JTextField m_jProd10;
    private javax.swing.JTextField m_jProd11;
    private javax.swing.JTextField m_jProd12;
    private javax.swing.JTextField m_jProd13;
    private javax.swing.JTextField m_jProd14;
    private javax.swing.JTextField m_jProd15;
    private javax.swing.JTextField m_jProd16;
    private javax.swing.JTextField m_jProd17;
    private javax.swing.JTextField m_jProd18;
    private javax.swing.JTextField m_jProd19;
    private javax.swing.JTextField m_jProd2;
    private javax.swing.JTextField m_jProd20;
    private javax.swing.JTextField m_jProd21;
    private javax.swing.JTextField m_jProd22;
    private javax.swing.JTextField m_jProd23;
    private javax.swing.JTextField m_jProd24;
    private javax.swing.JTextField m_jProd3;
    private javax.swing.JTextField m_jProd4;
    private javax.swing.JTextField m_jProd5;
    private javax.swing.JTextField m_jProd6;
    private javax.swing.JTextField m_jProd7;
    private javax.swing.JTextField m_jProd8;
    private javax.swing.JTextField m_jProd9;
    private javax.swing.JTextField m_jRef;
    private javax.swing.JTextField m_jRef1;
    private javax.swing.JTextField m_jRef10;
    private javax.swing.JTextField m_jRef11;
    private javax.swing.JTextField m_jRef12;
    private javax.swing.JTextField m_jRef13;
    private javax.swing.JTextField m_jRef14;
    private javax.swing.JTextField m_jRef15;
    private javax.swing.JTextField m_jRef16;
    private javax.swing.JTextField m_jRef17;
    private javax.swing.JTextField m_jRef18;
    private javax.swing.JTextField m_jRef19;
    private javax.swing.JTextField m_jRef2;
    private javax.swing.JTextField m_jRef20;
    private javax.swing.JTextField m_jRef21;
    private javax.swing.JTextField m_jRef22;
    private javax.swing.JTextField m_jRef23;
    private javax.swing.JTextField m_jRef24;
    private javax.swing.JTextField m_jRef3;
    private javax.swing.JTextField m_jRef4;
    private javax.swing.JTextField m_jRef5;
    private javax.swing.JTextField m_jRef6;
    private javax.swing.JTextField m_jRef7;
    private javax.swing.JTextField m_jRef8;
    private javax.swing.JTextField m_jRef9;
    private javax.swing.JCheckBox m_jScale;
    private javax.swing.JComboBox m_jTax;
    private javax.swing.JLabel m_jTitle;
    private javax.swing.JTextField m_jmargin;
    private javax.swing.JTextField m_jstockcost;
    private javax.swing.JTextField m_jstockvolume;
    private javax.swing.JTextArea txtAttributes;
    // End of variables declaration//GEN-END:variables
    
}
