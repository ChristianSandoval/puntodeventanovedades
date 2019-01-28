
package com.openbravo.pos.panels;

import java.util.*;
import javax.swing.table.AbstractTableModel;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.*;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.StringUtils;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PaymentsModel {

    private String m_sHost;
    private int m_iSeq;
    private Date m_dDateStart;
    private Date m_dDateEnd;       
    private java.util.List<ProductSalesLineR> m_lproductsalesR;        
    private Integer m_iPayments;
    private Double m_dPaymentsTotal;
    private java.util.List<PaymentsLine> m_lpayments;
    private java.util.List<PaymentMovements> m_lpaymentsMov;
    private final static String[] PAYMENTHEADERS = {"Label.Payment", "label.totalcash"};
    
    private Integer m_iSales;
    private Double m_dSalesBase;
    private Double m_dSalesTaxes;
    private java.util.List<SalesLine> m_lsales;
        private List<ProductSalesLine> m_lproductsales;
        private List<ProductSalesLine> m_lproductsales2;
    private final static String[] SALEHEADERS = {"", "",""};
    private List<Stock> m_lStock;
    private List<Stock2> m_lStock2;
    private List<Abonos> m_lAbonos;
    private List<Depositos> m_lDepositos;
    private PaymentsModel() {
    }    
    
    public static PaymentsModel emptyInstance() {
        
        PaymentsModel p = new PaymentsModel();
        
        p.m_iPayments = new Integer(0);
        p.m_dPaymentsTotal = new Double(0.0);
        p.m_lpayments = new ArrayList<PaymentsLine>();
        p.m_lpaymentsMov = new ArrayList<PaymentMovements>();
        p.m_lproductsales = new ArrayList<ProductSalesLine>();
        p.m_lproductsales2 = new ArrayList<ProductSalesLine>();
        p.m_iSales = null;
        p.m_dSalesBase = null;
        p.m_dSalesTaxes = null;
        p.m_lsales = new ArrayList<SalesLine>();
        p.m_lStock2 = new ArrayList();
        p.m_lStock = new ArrayList();
        p.m_lAbonos = new ArrayList();
        p.m_lDepositos = new ArrayList();
        return p;
    }
    public static PaymentsModel loadInstance(AppView app) throws BasicException {
        
        PaymentsModel p = new PaymentsModel();
        
        // Propiedades globales
        p.m_sHost = app.getProperties().getHost();
        p.m_iSeq = app.getActiveCashSequence();
        p.m_dDateStart = app.getActiveCashDateStart();
        p.m_dDateEnd = null;
        
        
        // Pagos
        Object[] valtickets = (Object []) new StaticSentence(app.getSession()
            , "SELECT COUNT(*), SUM(PAYMENTS.TOTAL) " +
              "FROM PAYMENTS, RECEIPTS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.INT, Datas.DOUBLE}))
            .find(app.getActiveCashIndex());
            
        if (valtickets == null) {
            p.m_iPayments = new Integer(0);
            p.m_dPaymentsTotal = new Double(0.0);
        } else {
            p.m_iPayments = (Integer) valtickets[0];
            p.m_dPaymentsTotal = (Double) valtickets[1];
        }  
        
        List l = new StaticSentence(app.getSession()            
            , "SELECT PAYMENTS.PAYMENT, SUM(PAYMENTS.TOTAL) " +
              "FROM PAYMENTS, RECEIPTS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ? " +
              "GROUP BY PAYMENTS.PAYMENT"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(PaymentsModel.PaymentsLine.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
            .list(app.getActiveCashIndex()); 
        
        if (l == null) {
            p.m_lpayments = new ArrayList();
        } else {
            p.m_lpayments = l;
        }        
        
        // Sales
        Object[] recsales = (Object []) new StaticSentence(app.getSession(),
            "SELECT COUNT(DISTINCT RECEIPTS.ID), SUM(TICKETLINES.UNITS * TICKETLINES.PRICE) " +
            "FROM RECEIPTS, TICKETLINES WHERE RECEIPTS.ID = TICKETLINES.TICKET AND RECEIPTS.MONEY = ?",
            SerializerWriteString.INSTANCE,
            new SerializerReadBasic(new Datas[] {Datas.INT, Datas.DOUBLE}))
            .find(app.getActiveCashIndex());
        if (recsales == null) {
            p.m_iSales = null;
            p.m_dSalesBase = null;
        } else {
            p.m_iSales = (Integer) recsales[0];
            p.m_dSalesBase = (Double) recsales[1];
        }             
        
        // Taxes
        Object[] rectaxes = (Object []) new StaticSentence(app.getSession(),
            "SELECT SUM(TAXLINES.AMOUNT) " +
            "FROM RECEIPTS, TAXLINES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND RECEIPTS.MONEY = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
            .find(app.getActiveCashIndex());            
        if (rectaxes == null) {
            p.m_dSalesTaxes = null;
        } else {
            p.m_dSalesTaxes = (Double) rectaxes[0];
        } 
                
        List<SalesLine> asales = new StaticSentence(app.getSession(),
                "SELECT TAXCATEGORIES.NAME, SUM(TAXLINES.AMOUNT) " +
                "FROM RECEIPTS, TAXLINES, TAXES, TAXCATEGORIES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND TAXLINES.TAXID = TAXES.ID AND TAXES.CATEGORY = TAXCATEGORIES.ID " +
                "AND RECEIPTS.MONEY = ? " +
                "GROUP BY TAXCATEGORIES.NAME"
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(PaymentsModel.SalesLine.class))
                .list(app.getActiveCashIndex());
        if (asales == null) {
            p.m_lsales = new ArrayList<SalesLine>();
        } else {
            p.m_lsales = asales;
        }
        
        List products = new StaticSentence(app.getSession(), 
"SELECT PRODUCTS.CODE AS REFERENCIA, PRODUCTS.NAME AS NOMBRE, "+
"CASE WHEN TICKETLINES.PRESENTACION=0 THEN 'PIEZA' WHEN TICKETLINES.PRESENTACION=1 THEN 'SERIE' WHEN TICKETLINES.PRESENTACION=2 THEN 'PAQUETE' WHEN TICKETLINES.PRESENTACION=3 THEN 'CAJA' ELSE '' END AS PRESENTACION, "+
"SUM(TICKETLINES.PRICE) AS PRECIO, SUM(TICKETLINES.UNITS) AS CANTIDAD, SUM(TICKETLINES.UNITS*TICKETLINES.PRICE) AS IMPORTE "+
"FROM TICKETLINES INNER JOIN TICKETS ON TICKETLINES.TICKET=TICKETS.ID INNER JOIN RECEIPTS ON RECEIPTS.ID=TICKETS.ID INNER JOIN PRODUCTS ON PRODUCTS.ID=TICKETLINES.PRODUCT INNER JOIN LOCATIONS ON LOCATIONS.ID=TICKETS.STATUS "+
"WHERE RECEIPTS.MONEY=? AND TICKETS.STATUS="+app.getInventoryLocation()+" GROUP BY PRODUCTS.CODE,TICKETLINES.PRESENTACION ORDER BY PRODUCTS.CODE"
, SerializerWriteString.INSTANCE
,new SerializerReadClass(ProductSalesLine.class)).list(app.getActiveCashIndex());
    if (products == null) {
      p.m_lproductsales = new ArrayList();
    } else {
      p.m_lproductsales = products;
    }
    
    List<Stock> stock = new StaticSentence(app.getSession(), "SELECT PRODUCTS.CODE,STOCKCURRENT.UNITS,PRODUCTS.NAME FROM STOCKCURRENT JOIN PRODUCTS ON STOCKCURRENT.PRODUCT=PRODUCTS.ID WHERE STOCKCURRENT.LOCATION=? ", SerializerWriteString.INSTANCE, new SerializerReadClass(Stock.class)).list(app.getInventoryLocation());
    if (stock == null) {
      p.m_lStock = new ArrayList();
    } else {
      p.m_lStock = stock;
    }
    
    List<Abonos> abonos = new StaticSentence(app.getSession(), 
            "SELECT CASE WHEN PAYMENTS.PAYMENT = 'debt' THEN 'DEUDA' WHEN PAYMENTS.PAYMENT='debtpaid' THEN 'ABONO' END,CUSTOMERS.NAME,PAYMENTS.TOTAL FROM PAYMENTS JOIN RECEIPTS ON PAYMENTS.RECEIPT=RECEIPTS.ID JOIN TICKETS ON TICKETS.ID=RECEIPTS.ID JOIN CUSTOMERS ON TICKETS.CUSTOMER=CUSTOMERS.ID WHERE RECEIPTS.MONEY=?", 
            SerializerWriteString.INSTANCE, new SerializerReadClass(Abonos.class)).list(app.getActiveCashIndex());
    if (abonos == null) {
      p.m_lAbonos = new ArrayList();
    } else {
      p.m_lAbonos = abonos;
    }
    
    List<Depositos> depositos = new StaticSentence(app.getSession(), 
            "SELECT IFNULL(C.SEARCHKEY,''),IFNULL(C.NAME,''), P.TOTAL, PE.NAME FROM PAYMENTS P INNER JOIN RECEIPTS R ON R.ID=P.RECEIPT "+
            " INNER JOIN TICKETS T ON T.ID=R.ID"+
            " INNER JOIN PEOPLE PE ON PE.ID=T.PERSON"+
            " LEFT JOIN CUSTOMERS C ON C.ID=T.CUSTOMER"+
            " WHERE P.PAYMENT='cheque' AND R.MONEY=? ORDER BY R.DATENEW DESC",
            SerializerWriteString.INSTANCE, new SerializerReadClass(Depositos.class)).list(app.getActiveCashIndex());
    if (depositos == null) {
      p.m_lDepositos = new ArrayList();
    } else {
      p.m_lDepositos = depositos;
    }
    
        List lMov = new StaticSentence(app.getSession()            
            , "SELECT PAYMENTS.PAYMENT,PAYMENTS.TOTAL, PAYMENTS.NOTES " +
              "FROM PAYMENTS, RECEIPTS " +
              "WHERE (PAYMENTS.PAYMENT='cashin' OR PAYMENTS.PAYMENT='cashout') AND PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ? " 
              //"GROUP BY PAYMENTS.PAYMENT"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(PaymentsModel.PaymentMovements.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
            .list(app.getActiveCashIndex()); 
        
        if (lMov == null) {
            p.m_lpaymentsMov = new ArrayList();
        } else {
            p.m_lpaymentsMov = lMov;
        }
        return p;
    }
    public List<Stock> getStockLines()
  {
    return this.m_lStock;
  }
    public List<Abonos> getAbonosLines()
  {
    return this.m_lAbonos;
  }
    public List<Depositos> getDepositosLines()
  {
    return this.m_lDepositos;
  }
      public List<PaymentMovements> getPaymentMovements()
  {
    return this.m_lpaymentsMov;
  }
    public List<Stock2> getStockLines2()
  {
    return this.m_lStock2;
  }
        public List<ProductSalesLineR> getProductSalesLinesR()
  {
    return this.m_lproductsalesR;
  }
    public static class Stock
    implements SerializableRead
  {
    private String productName;
    private String product;
    private Double units;
    
    public void readValues(DataRead dr)
      throws BasicException
    {
      this.product = dr.getString(1);
      this.units = dr.getDouble(2);
      this.productName = dr.getString(3);
    }
    
    public String printProduct()
    {
      return this.product;
    }
    
    public String printProductName()
    {
      return StringUtils.encodeXML(this.productName);
    }
    
    public Double printUnits()
    {
      return this.units;
    }
  }
    
  public static class Depositos
    implements SerializableRead
  {
    private String idcliente;
    private String nombre;
    private Double monto;
    private String usuario;
    public void readValues(DataRead dr)
      throws BasicException
    {
      this.idcliente = dr.getString(1);
      this.nombre = dr.getString(2);
      this.monto = dr.getDouble(3);
      this.usuario = dr.getString(2);
    }
    
    public String printIdCliente()
    {
      return this.idcliente;
    }
    public String printUsuario()
    {
      return this.usuario;
    }
    public String printCustomerName()
    {
      return StringUtils.encodeXML(this.nombre);
    }
    
    public String printMonto()
    {
      return Formats.CURRENCY.formatValue(this.monto);
    }
  }
    
    public static class Abonos
    implements SerializableRead
  {
    private String tipo;
    private String nombre;
    private Double monto;
    
    public void readValues(DataRead dr)
      throws BasicException
    {
      this.tipo = dr.getString(1);
      this.nombre = dr.getString(2);
      this.monto = dr.getDouble(3);
    }
    
    public String printTipo()
    {
      return this.tipo;
    }
    
    public String printCustomerName()
    {
      return StringUtils.encodeXML(this.nombre);
    }
    
    public String printMonto()
    {
      return Formats.CURRENCY.formatValue(this.monto);
    }
  }
    
    public static class Stock2
    implements SerializableRead
  {
    private String productName;
    private Double units;
    private Date fecha;
    private String tipo;
    public void readValues(DataRead dr)
      throws BasicException
    {
      this.productName = dr.getString(1);
      this.units = dr.getDouble(2);
      
      this.fecha = dr.getTimestamp(3);
      this.tipo = dr.getString(4);
    }
    public Date printDate()
    {
        return fecha;
    }
    public String printTipo()
    {
      return this.tipo;
    }
    public String printProductName()
    {
      return StringUtils.encodeXML(this.productName);
    }
    
    public Double printUnits()
    {
      return this.units;
    }
  }
    public static class ProductSalesLineR implements SerializableRead {

        private String name;
        private Double price;
        private String tipo;
        private String envase;
        private Integer ticketid;
        private Integer cant;
        private String persona;
        public void readValues(DataRead dr) throws BasicException {
            name = dr.getString(1);
            price = dr.getDouble(2);
            tipo= dr.getString(3);
            envase= dr.getString(4);
            ticketid=dr.getInt(5);
            cant=dr.getInt(6);
            persona=dr.getString(7);
        }

        public String printName() {
            return name;
        }

        public String printPrice() {
            return Formats.CURRENCY.formatValue(price);
        }
        public String printTipo() {
            return tipo;
        }
        public String printEnvase() {
            return envase;
        }
        public String printTicketId() {
            return ticketid.toString();
        }
        public String printCant() {
            return cant.toString();
        }
        public String printPersona() {
            return persona;
        }
    }
public List<ProductSalesLine> getProductSalesLines()
  {
    return this.m_lproductsales;
  }
public List<ProductSalesLine> getProductSalesLines2()
  {
    return this.m_lproductsales2;
  }
public static class ProductSalesLine
    implements SerializableRead
  {
    private String m_Referencia;
    private String m_ProductName;
    private String m_Presentacion;
    private Double m_ProductPrice;
    private Double m_ProductUnits;
    private Double m_ProductImporte;
    
    public void readValues(DataRead dr)
      throws BasicException
    {
      this.m_Referencia = dr.getString(1);
      this.m_ProductName = dr.getString(2);
      this.m_Presentacion = dr.getString(3);
      this.m_ProductPrice = dr.getDouble(4);
      this.m_ProductUnits = dr.getDouble(5);
      this.m_ProductImporte = dr.getDouble(6);
    }
    public String getReferencia()
    {
      return this.m_Referencia;
    }
    public String getProduct()
    {
      return this.m_ProductName;
    }
    public String getPresentacion()
    {
      return this.m_Presentacion;
    }
    public String getUnits()
    {
      return Formats.INT.formatValue(this.m_ProductUnits);
    }
    
    public String getPrecio()
    {
      return Formats.CURRENCY.formatValue(this.m_ProductPrice);
    }
    
    public String getImporte()
    {
      return Formats.CURRENCY.formatValue(this.m_ProductImporte);
    }
  }
    public int getPayments() {
        return m_iPayments.intValue();
    }
    public double getTotal() {
        return m_dPaymentsTotal.doubleValue();
    }
    public String getHost() {
        return m_sHost;
    }
    public int getSequence() {
        return m_iSeq;
    }
    public Date getDateStart() {
        return m_dDateStart;
    }
    public void setDateEnd(Date dValue) {
        m_dDateEnd = dValue;
    }
    public Date getDateEnd() {
        return m_dDateEnd;
    }
    
    public String printHost() {
        return StringUtils.encodeXML(m_sHost);
    }
    public String printSequence() {
        return Formats.INT.formatValue(m_iSeq);
    }
    public String printDateStart() {
        return Formats.TIMESTAMP.formatValue(m_dDateStart);
    }
    public String printDateEnd() {
        return Formats.TIMESTAMP.formatValue(m_dDateEnd);
    }  
    
    public String printPayments() {
        return Formats.INT.formatValue(m_iPayments);
    }

    public String printPaymentsTotal() {
        return Formats.CURRENCY.formatValue(m_dPaymentsTotal);
    }     
    
    public List<PaymentsLine> getPaymentLines() {
        return m_lpayments;
    }
    
    public int getSales() {
        return m_iSales == null ? 0 : m_iSales.intValue();
    }    
    public String printSales() {
        return Formats.INT.formatValue(m_iSales);
    }
    public String printSalesBase() {
        return Formats.CURRENCY.formatValue(m_dSalesBase);
    }     
    public String printSalesTaxes() {
        return Formats.CURRENCY.formatValue(m_dSalesTaxes);
    }     
    public String printSalesTotal() {            
        return Formats.CURRENCY.formatValue((m_dSalesBase == null || m_dSalesTaxes == null)
                ? null
                : m_dSalesBase + m_dSalesTaxes);
    }     
    public List<SalesLine> getSaleLines() {
        return m_lsales;
    }
    
    public AbstractTableModel getPaymentsModel() {
        return new AbstractTableModel() {
            public String getColumnName(int column) {
                return AppLocal.getIntString(PAYMENTHEADERS[column]);
            }
            public int getRowCount() {
                return m_lpayments.size();
            }
            public int getColumnCount() {
                return PAYMENTHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                PaymentsLine l = m_lpayments.get(row);
                switch (column) {
                case 0: return l.getType();
                case 1: return l.getValue();
                default: return null;
                }
            }  
        };
    }
    public AbstractTableModel getPaymentsModel1() {
        return new AbstractTableModel() {
            public String getColumnName(int column) {
                switch (column) {
                case 0: return "TIPO";//l.printProductName();
                case 1: return "TOTAL";//l.printUnits();
                    //case 2: return "FECHA";//l.printDate();
                        case 2: return "DESC";//l.printTipo();
                default: return null;
                }
                //return AppLocal.getIntString(PAYMENTHEADERS[column]);
            }
            public int getRowCount() {
                return m_lpaymentsMov.size();
            }
            public int getColumnCount() {
                return 3;//PAYMENTHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                PaymentMovements l = m_lpaymentsMov.get(row);
                switch (column) {
                case 0: return l.printType();
                case 1: return l.printValue();
                    case 2: return l.printType2();
                default: return null;
                }
            }  
        };
    }
    public static class SalesLine implements SerializableRead {
        
        private String m_SalesTaxName;
        private Double m_SalesTaxes;
        
        public void readValues(DataRead dr) throws BasicException {
            m_SalesTaxName = dr.getString(1);
            m_SalesTaxes = dr.getDouble(2);
        }
        public String printTaxName() {
            return m_SalesTaxName;
        }      
        public String printTaxes() {
            return Formats.CURRENCY.formatValue(m_SalesTaxes);
        }
        public String getTaxName() {
            return m_SalesTaxName;
        }
        public Double getTaxes() {
            return m_SalesTaxes;
        }        
    }

    public AbstractTableModel getSalesModel() {
        return new AbstractTableModel() {
            public String getColumnName(int column) {
                return AppLocal.getIntString(SALEHEADERS[column]);
            }
            public int getRowCount() {
                return m_lpaymentsMov.size();
            }
            public int getColumnCount() {
                return SALEHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                PaymentMovements l = m_lpaymentsMov.get(row);
                switch (column) {
                case 0: return l.printType();
                case 1: return l.printValue();
                case 2: return l.printType2();
                default: return null;
                }
            }  
        };
    }
    
    public static class PaymentsLine implements SerializableRead {
        
        private String m_PaymentType;
        private Double m_PaymentValue;
        
        public void readValues(DataRead dr) throws BasicException {
            m_PaymentType = dr.getString(1);
            m_PaymentValue = dr.getDouble(2);
        }
        
        public String printType() {
            return AppLocal.getIntString("transpayment." + m_PaymentType);
        }
        public String getType() {
            return m_PaymentType;
        }
        public String printValue() {
            return Formats.CURRENCY.formatValue(m_PaymentValue);
        }
        public Double getValue() {
            return m_PaymentValue;
        }        
    }
    
    public static class PaymentMovements implements SerializableRead {
        
        private String m_PaymentType;
        private Double m_PaymentValue;
        private String m_PaymentType2;
        public void readValues(DataRead dr) throws BasicException {
            m_PaymentType = dr.getString(1);
            m_PaymentValue = dr.getDouble(2);
            m_PaymentType2 = dr.getString(3);
        }
        
        public String printType() {
            return AppLocal.getIntString("transpayment." + m_PaymentType);
        }
        public String printType2() {
            return m_PaymentType2;
        }
        public String getType() {
            return m_PaymentType;
        }
        public String printValue() {
            return Formats.CURRENCY.formatValue(m_PaymentValue);
        }
        public Double getValue() {
            return m_PaymentValue;
        }        
    }
}    