package com.openbravo.pos.panels;

import java.util.*;
import javax.swing.table.AbstractTableModel;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.*;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.StringUtils;

public class PaymentsModel2 {

    private String m_sHost;
    private int m_iSeq;
    private Date m_dDateStart;
    private Date m_dDateEnd;       
            
    private Integer m_iPayments;
    private Double m_dPaymentsTotal;
    private java.util.List<PaymentsLine> m_lpayments;
    private java.util.List<TicketsDefaultLine> m_ldefault;
    private java.util.List<TicketsNoDefaultLine> m_lNodefault;
    private java.util.List<ProductSalesLine> m_lproductsales;
    private java.util.List<ProductSalesLineR> m_lproductsalesR;
    private final static String[] PAYMENTHEADERS = {"Label.Payment", "label.totalcash"};
    private final static String[] DEFAULTHEADERS = {"Label.noTicket", "Label.Fecha","Label.Total"};
    private Integer m_iSales;
    private Double m_dSalesBase;
    private Double m_dSalesTaxes;
    private String s_ActiveCash;
    private java.util.List<SalesLine> m_lsales;
    private List<Stock> m_lStock;
    private final static String[] SALEHEADERS = {"label.taxcash", "label.totalcash"};

    private PaymentsModel2() {
    }    
    
    public static PaymentsModel2 emptyInstance() {
        
        PaymentsModel2 p = new PaymentsModel2();
        
        p.m_iPayments = new Integer(0);
        p.m_dPaymentsTotal = new Double(0.0);
        p.m_ldefault= new ArrayList<TicketsDefaultLine>();
        p.m_lNodefault= new ArrayList<TicketsNoDefaultLine>();
        p.m_lpayments = new ArrayList<PaymentsLine>();
        p.m_lproductsales = new ArrayList<ProductSalesLine>();
        p.m_iSales = null;
        p.m_dSalesBase = null;
        p.m_dSalesTaxes = null;
        p.m_lsales = new ArrayList<SalesLine>();
        p.m_lproductsalesR = new ArrayList<ProductSalesLineR>();
        p.m_lStock = new ArrayList();
        p.s_ActiveCash="";
        return p;
    }
    
    public static PaymentsModel2 loadInstance(AppView app, int cashSequence) throws BasicException {
        
        PaymentsModel2 p = new PaymentsModel2();
        
        // Propiedades globales
        p.m_sHost = app.getProperties().getHost();
        p.m_iSeq = cashSequence;
        //p.m_dDateStart = app.getActiveCashDateStart();
        //p.m_dDateEnd = null;
        p.s_ActiveCash = "";
        
        // Obtiene caja activa corte de caja
        Object[] valtickets = (Object []) new StaticSentence(app.getSession()
            , "SELECT MONEY,DATEEND,DATESTART " +
              "FROM CLOSEDCASH " +
              "WHERE HOSTSEQUENCE = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.STRING,Datas.TIMESTAMP,Datas.TIMESTAMP}))
            .find(String.valueOf(p.m_iSeq));
        if (valtickets == null) {
            p.s_ActiveCash = "";
        } else {
            p.s_ActiveCash = (String)valtickets[0];
            p.m_dDateEnd = (Date)valtickets[1];
            p.m_dDateStart=(Date)valtickets[2];
        }  
        
        // Pagos
        valtickets = (Object []) new StaticSentence(app.getSession()
            , "SELECT COUNT(*), SUM(PAYMENTS.TOTAL) " +
              "FROM PAYMENTS, RECEIPTS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.INT, Datas.DOUBLE}))
            .find(p.s_ActiveCash );
            
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
            , new SerializerReadClass(PaymentsModel2.PaymentsLine.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
            .list(p.s_ActiveCash ); 
        
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
            .find(p.s_ActiveCash);
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
            .find(p.s_ActiveCash );            
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
                , new SerializerReadClass(PaymentsModel2.SalesLine.class))
                .list(p.s_ActiveCash );
        if (asales == null) {
            p.m_lsales = new ArrayList<SalesLine>();
        } else {
            p.m_lsales = asales;
        }
        
        
        List<TicketsDefaultLine> ticketsDefaultLines = new StaticSentence(app.getSession(),
                "SELECT T.TICKETID,R.DATENEW,SUM(PM.TOTAL) " +
                "FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID JOIN PAYMENTS PM ON R.ID = PM.RECEIPT " +
                "WHERE  R.MONEY = ? AND T.TICKETTYPE=0 GROUP BY T.TICKETID,R.DATENEW " +
                "ORDER BY T.TICKETID"
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(PaymentsModel2.TicketsDefaultLine.class))
                .list(p.s_ActiveCash );
        if (ticketsDefaultLines == null) {
            p.m_ldefault = new ArrayList<TicketsDefaultLine>();
        } else {
            p.m_ldefault = ticketsDefaultLines;
        }
        
        List<TicketsNoDefaultLine> ticketsNoDefaultLines = new StaticSentence(app.getSession(),
                "SELECT T.TICKETID,R.DATENEW,SUM(PM.TOTAL) " +
                "FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID JOIN PAYMENTS PM ON R.ID = PM.RECEIPT " +
                "WHERE R.MONEY = ? AND T.TICKETTYPE=0 GROUP BY T.TICKETID,R.DATENEW " +
                "ORDER BY SUM(PM.TOTAL) DESC"
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(PaymentsModel2.TicketsNoDefaultLine.class))
                .list(p.s_ActiveCash);
        if (ticketsNoDefaultLines == null) {
            p.m_lNodefault = new ArrayList<TicketsNoDefaultLine>();
        } else {
            p.m_lNodefault = ticketsNoDefaultLines;
        }
        /*
        List products = new StaticSentence(app.getSession()
            , "SELECT PRODUCTS.NAME,TICKETLINES.TIPO, SUM(TICKETLINES.UNITS), TICKETLINES.ENVASE, SUM(TICKETLINES.UNITS)*TICKETLINES.PRICE,PRODUCTS.PRICEBUY,(SUM(TICKETLINES.UNITS)*TICKETLINES.PRICE)-(SUM(TICKETLINES.UNITS)*PRODUCTS.PRICEBUY) " +
              "FROM TICKETLINES, TICKETS, RECEIPTS, PRODUCTS " +
              "WHERE TICKETLINES.PRODUCT = PRODUCTS.ID AND TICKETLINES.TICKET = TICKETS.ID AND TICKETS.ID = RECEIPTS.ID AND RECEIPTS.MONEY = ? " +
              "GROUP BY PRODUCTS.NAME,TICKETLINES.TIPO,TICKETLINES.ENVASE"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(PaymentsModel.ProductSalesLine.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
            .list(app.getActiveCashIndex());

        if (products == null) {
            p.m_lproductsales = new ArrayList();
        } else {
            p.m_lproductsales = products;
        }
        List productsR = new StaticSentence(app.getSession()
            , "SELECT PRODUCTS.NAME AS NAME, TICKETLINES.PRICE AS PRICE,TICKETLINES.TIPO AS TIPO,TICKETLINES.ENVASE AS ENVASE, TICKETS.TICKETID AS TICKETID, TICKETLINES.UNITS, PEOPLE.NAME AS PERSONA " +
              "FROM CLOSEDCASH,PEOPLE,RECEIPTS, TICKETS, TICKETLINES LEFT OUTER JOIN PRODUCTS ON TICKETLINES.PRODUCT = PRODUCTS.ID " +
              "WHERE CLOSEDCASH.MONEY=RECEIPTS.MONEY AND PEOPLE.ID=TICKETS.PERSON AND RECEIPTS.ID = TICKETS.ID AND TICKETS.ID = TICKETLINES.TICKET AND RECEIPTS.MONEY = ? " +
              "ORDER BY TICKETS.TICKETID"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(PaymentsModel.ProductSalesLineR.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
            .list(app.getActiveCashIndex());

        if (productsR == null) {
            p.m_lproductsalesR = new ArrayList();
        } else {
            p.m_lproductsalesR = productsR;
        }
        
        List<Stock> stock = new StaticSentence(app.getSession(), "SELECT STOCKCURRENT.PRODUCT,STOCKCURRENT.UNITS,PRODUCTS.NAME FROM STOCKCURRENT JOIN PRODUCTS ON STOCKCURRENT.PRODUCT=PRODUCTS.ID WHERE '1'=? ", SerializerWriteString.INSTANCE, new SerializerReadClass(Stock.class)).list("1");
        if (stock == null) {
          p.m_lStock = new ArrayList();
        } else {
          p.m_lStock = stock;
        }*/
        return p;
    }
    public List<Stock> getStockLines()
  {
    return this.m_lStock;
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
    public List<ProductSalesLineR> getProductSalesLinesR()
  {
    return this.m_lproductsalesR;
  }
    public static class ProductSalesLine implements SerializableRead {
        private String m_ProductName;
        private String m_Presentacion;
        private Double m_ProductUnits;
        private String m_Envase;
        private Double m_Total;
        private Double m_PriceBuy;
        private Double m_Ganancia;
//SELECT PRODUCTS.NAME,TICKETLINES.TIPO, SUM(TICKETLINES.UNITS), TICKETLINES.ENVASE, SUM(TICKETLINES.UNITS)*TICKETLINES.PRICE,PRODUCTS.PRICEBUY,(SUM(TICKETLINES.UNITS)*TICKETLINES.PRICE)-(SUM(TICKETLINES.UNITS)*PRODUCTS.PRICEBUY) 
        public void readValues(DataRead dr) throws BasicException {
            m_ProductName = dr.getString(1);
            m_Presentacion = dr.getString(2);
            m_ProductUnits = dr.getDouble(3);
            m_Envase = dr.getString(4);
            m_Total = dr.getDouble(5);
            m_PriceBuy= dr.getDouble(6);
            m_Ganancia= dr.getDouble(7);
        }

        public String printProductName() {
            return StringUtils.encodeXML(m_ProductName);
        }
        public String printPresentacion() {
            return m_Presentacion;
        }

        public String printProductUnits() {
            return Formats.DOUBLE.formatValue(m_ProductUnits);
        }

        public Double getProductUnits() {
            return m_ProductUnits;
        }

        public String printEnvase() {
            return m_Envase;
        }

        public String getEnvase() {
            return m_Envase;
        }

        public String printTotal() {
            return Formats.CURRENCY.formatValue(m_Total);
        }
        public String printGanancia() {
            return Formats.CURRENCY.formatValue(m_Ganancia);
        }
        public String printPriceBuy() {
            return Formats.CURRENCY.formatValue(m_PriceBuy);
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
    
    public List<TicketsDefaultLine> getTicketsDefault() {
        return m_ldefault;
    }
    
    public List<TicketsNoDefaultLine> getTicketsNoDefault() {
        return m_lNodefault;
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
    
    public AbstractTableModel getPaymentsModelTicketsDefault() {
        return new AbstractTableModel() {
            public String getColumnName(int column) {
                return AppLocal.getIntString(DEFAULTHEADERS[column]);
            }
            public int getRowCount() {
                return m_ldefault.size();
            }
            public int getColumnCount() {
                return DEFAULTHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                TicketsDefaultLine l = m_ldefault.get(row);
                switch (column) {
                case 0: return l.printNoTicket();
                case 1: return l.printFecha();
                    case 2: return l.printTotal();
                default: return null;
                }
            }  
        };
    }
    
    public AbstractTableModel getPaymentsModelTicketsNoDefault() {
        return new AbstractTableModel() {
            public String getColumnName(int column) {
                return AppLocal.getIntString(DEFAULTHEADERS[column]);
            }
            public int getRowCount() {
                return m_lNodefault.size();
            }
            public int getColumnCount() {
                return DEFAULTHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                TicketsNoDefaultLine l = m_lNodefault.get(row);
                switch (column) {
                case 0: return l.printNoTicket();
                case 1: return l.printFecha();
                    case 2: return l.printTotal();
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
                return m_lsales.size();
            }
            public int getColumnCount() {
                return SALEHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                SalesLine l = m_lsales.get(row);
                switch (column) {
                case 0: return l.getTaxName();
                case 1: return l.getTaxes();
                default: return null;
                }
            }  
        };
    }
    
    public static class TicketsDefaultLine implements SerializableRead {
        private String noTicket;
        private String fecha;
        private String total;
        
        public void readValues(DataRead dr) throws BasicException {
            noTicket = dr.getString(1);
            fecha = dr.getString(2);
            total = dr.getString(3);
        }
        
        public String printNoTicket() {
            return noTicket;
        }
        public String printFecha() {
            return fecha;
        }
        public String printTotal() {
            return total;
        }
    }
    
    public static class TicketsNoDefaultLine implements SerializableRead {
        private String noTicket;
        private String fecha;
        private String total;
        
        public void readValues(DataRead dr) throws BasicException {
            noTicket = dr.getString(1);
            fecha = dr.getString(2);
            total = dr.getString(3);
        }
        
        public String printNoTicket() {
            return noTicket;
        }
        public String printFecha() {
            return fecha;
        }
        public String printTotal() {
            return total;
        }
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
}    