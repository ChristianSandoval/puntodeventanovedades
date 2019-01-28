package com.openbravo.pos.panels;

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import java.awt.*;
import java.text.ParseException;
import javax.swing.*;
import java.util.Date;
import java.util.UUID;
import javax.swing.table.*;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.gui.TableRendererBasic;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author adrianromero
 */
public class JPanelCloseMoney extends JPanel implements JPanelView, BeanFactoryApp {

    private AppView m_App;
    private DataLogicSystem m_dlSystem;

    private PaymentsModel m_PaymentsToClose = null;

    private TicketParser m_TTP;

    /**
     * Creates new form JPanelCloseMoney
     */
    public JPanelCloseMoney() {

        initComponents();
    }

    public void init(AppView app) throws BeanFactoryException {

        m_App = app;
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);

        m_jTicketTable.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[]{new FormatsPayment(), Formats.CURRENCY}));

        m_jTicketTable1.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[]{Formats.STRING, Formats.STRING, Formats.STRING}));

        m_jTicketTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_jScrollTableTicket.getVerticalScrollBar().setPreferredSize(new Dimension(25, 25));
        m_jTicketTable.getTableHeader().setReorderingAllowed(false);
        m_jTicketTable.setRowHeight(25);
        m_jTicketTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        m_jTicketTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_jScrollTableTicket1.getVerticalScrollBar().setPreferredSize(new Dimension(25, 25));
        m_jTicketTable1.getTableHeader().setReorderingAllowed(false);
        m_jTicketTable1.setRowHeight(25);
        m_jTicketTable1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
/*
        m_jsalestable.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[]{Formats.STRING, Formats.CURRENCY, Formats.STRING}));
        m_jsalestable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_jScrollSales.getVerticalScrollBar().setPreferredSize(new Dimension(25, 25));
        m_jsalestable.getTableHeader().setReorderingAllowed(false);
        m_jsalestable.setRowHeight(25);
        m_jsalestable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);*/
    }

    public Object getBean() {
        return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.CloseTPV");
    }

    public void activate() throws BasicException {
        loadData();
    }

    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate   
        return true;
    }

    private void loadData() throws BasicException {

        // Reset
        m_jSequence.setText(null);
        m_jMinDate.setText(null);
        m_jMaxDate.setText(null);
        m_jPrintCash.setEnabled(false);
        m_jCloseCash.setEnabled(false);
        m_jCount.setText(null); // AppLocal.getIntString("label.noticketstoclose");
        m_jCash.setText(null);

        m_jSales.setText(null);
        m_jSalesSubtotal.setText(null);
        m_jSalesTaxes.setText(null);
        m_jSalesTotal.setText(null);

        m_jTicketTable.setModel(new DefaultTableModel());
        m_jTicketTable1.setModel(new DefaultTableModel());
        m_jsalestable.setModel(new DefaultTableModel());

        // LoadData
        m_PaymentsToClose = PaymentsModel.loadInstance(m_App);

        // Populate Data
        m_jSequence.setText(m_PaymentsToClose.printSequence());
        m_jMinDate.setText(m_PaymentsToClose.printDateStart());
        m_jMaxDate.setText(m_PaymentsToClose.printDateEnd());

        if (m_PaymentsToClose.getPayments() != 0 || m_PaymentsToClose.getSales() != 0) {

            m_jPrintCash.setEnabled(true);
            m_jCloseCash.setEnabled(true);

            m_jCount.setText(m_PaymentsToClose.printPayments());
            m_jCash.setText(m_PaymentsToClose.printPaymentsTotal());

            m_jSales.setText(m_PaymentsToClose.printSales());
            m_jSalesSubtotal.setText(m_PaymentsToClose.printSalesBase());
            m_jSalesTaxes.setText(m_PaymentsToClose.printSalesTaxes());
            m_jSalesTotal.setText(m_PaymentsToClose.printSalesTotal());
        }

        m_jTicketTable.setModel(m_PaymentsToClose.getPaymentsModel());
        m_jTicketTable1.setModel(m_PaymentsToClose.getPaymentsModel1());

        TableColumnModel jColumns = m_jTicketTable.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(200);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(100);
        jColumns.getColumn(1).setResizable(false);

        jColumns = m_jTicketTable1.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(100);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(50);
        jColumns.getColumn(1).setResizable(false);
        jColumns.getColumn(2).setPreferredWidth(300);
        jColumns.getColumn(2).setResizable(false);
/*
        m_jsalestable.setModel(m_PaymentsToClose.getPaymentsModel1());

        jColumns = m_jsalestable.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(200);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(100);
        jColumns.getColumn(1).setResizable(false);        
        jColumns.getColumn(2).setPreferredWidth(300);
        jColumns.getColumn(2).setResizable(false);*/
    }

    private void printPayments(String report) {

        String sresource = m_dlSystem.getResourceAsXML(report);
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("payments", m_PaymentsToClose);
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            }
        }
    }

    private class FormatsPayment extends Formats {

        protected String formatValueInt(Object value) {
            return AppLocal.getIntString("transpayment." + (String) value);
        }

        protected Object parseValueInt(String value) throws ParseException {
            return value;
        }

        public int getAlignment() {
            return javax.swing.SwingConstants.LEFT;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        m_jSalesTotal = new javax.swing.JTextField();
        m_jSalesTaxes = new javax.swing.JTextField();
        m_jSalesSubtotal = new javax.swing.JTextField();
        m_jSales = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        m_jScrollSales = new javax.swing.JScrollPane();
        m_jsalestable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        m_jSequence = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        m_jMinDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        m_jMaxDate = new javax.swing.JTextField();
        m_jCount = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        m_jCash = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        m_jScrollTableTicket = new javax.swing.JScrollPane();
        m_jTicketTable = new javax.swing.JTable();
        m_jCloseCash = new javax.swing.JButton();
        m_jPrintCash = new javax.swing.JButton();
        m_jScrollTableTicket1 = new javax.swing.JScrollPane();
        m_jTicketTable1 = new javax.swing.JTable();

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.salestitle"))); // NOI18N

        m_jSalesTotal.setEditable(false);
        m_jSalesTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        m_jSalesTaxes.setEditable(false);
        m_jSalesTaxes.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        m_jSalesSubtotal.setEditable(false);
        m_jSalesSubtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        m_jSales.setEditable(false);
        m_jSales.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel5.setText(AppLocal.getIntString("label.sales")); // NOI18N

        jLabel6.setText(AppLocal.getIntString("label.subtotalcash")); // NOI18N

        jLabel12.setText(AppLocal.getIntString("label.taxcash")); // NOI18N

        jLabel7.setText(AppLocal.getIntString("label.totalcash")); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(m_jSales, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(m_jSalesSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(m_jSalesTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(m_jSalesTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(m_jSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(m_jSalesSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(m_jSalesTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(m_jSalesTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jButton1.setText("Enviar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel8.setText("No. Corte");

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.paymentstitle"))); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        m_jsalestable.setFocusable(false);
        m_jsalestable.setIntercellSpacing(new java.awt.Dimension(0, 1));
        m_jsalestable.setRequestFocusEnabled(false);
        m_jsalestable.setShowVerticalLines(false);
        m_jScrollSales.setViewportView(m_jsalestable);

        setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.datestitle"))); // NOI18N

        jLabel11.setText(AppLocal.getIntString("label.sequence")); // NOI18N

        m_jSequence.setEditable(false);
        m_jSequence.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel2.setText(AppLocal.getIntString("Label.StartDate")); // NOI18N

        m_jMinDate.setEditable(false);
        m_jMinDate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel3.setText(AppLocal.getIntString("Label.EndDate")); // NOI18N

        m_jMaxDate.setEditable(false);
        m_jMaxDate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        m_jCount.setEditable(false);
        m_jCount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel1.setText(AppLocal.getIntString("Label.Tickets")); // NOI18N

        jLabel4.setText(AppLocal.getIntString("Label.Cash")); // NOI18N

        m_jCash.setEditable(false);
        m_jCash.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(m_jSequence, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(m_jCount, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(m_jMinDate, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(m_jMaxDate, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(m_jCash, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(631, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(m_jCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(m_jSequence, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(m_jCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(m_jMinDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(m_jMaxDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.paymentstitle"))); // NOI18N

        m_jScrollTableTicket.setMinimumSize(new java.awt.Dimension(350, 140));
        m_jScrollTableTicket.setPreferredSize(new java.awt.Dimension(350, 140));

        m_jTicketTable.setFocusable(false);
        m_jTicketTable.setIntercellSpacing(new java.awt.Dimension(0, 1));
        m_jTicketTable.setRequestFocusEnabled(false);
        m_jTicketTable.setShowVerticalLines(false);
        m_jScrollTableTicket.setViewportView(m_jTicketTable);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(m_jScrollTableTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(198, 198, 198))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(m_jScrollTableTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(202, Short.MAX_VALUE))
        );

        m_jCloseCash.setText(AppLocal.getIntString("Button.CloseCash")); // NOI18N
        m_jCloseCash.setPreferredSize(new java.awt.Dimension(83, 43));
        m_jCloseCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCloseCashActionPerformed(evt);
            }
        });

        m_jPrintCash.setText(AppLocal.getIntString("Button.PrintCash")); // NOI18N
        m_jPrintCash.setPreferredSize(new java.awt.Dimension(55, 43));
        m_jPrintCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jPrintCashActionPerformed(evt);
            }
        });

        m_jScrollTableTicket1.setMinimumSize(new java.awt.Dimension(550, 140));
        m_jScrollTableTicket1.setPreferredSize(new java.awt.Dimension(550, 140));

        m_jTicketTable1.setFocusable(false);
        m_jTicketTable1.setIntercellSpacing(new java.awt.Dimension(0, 1));
        m_jTicketTable1.setRequestFocusEnabled(false);
        m_jTicketTable1.setShowVerticalLines(false);
        m_jScrollTableTicket1.setViewportView(m_jTicketTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(m_jPrintCash, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(m_jCloseCash, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(m_jScrollTableTicket1, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
                        .addGap(22, 22, 22)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(m_jCloseCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jPrintCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(m_jScrollTableTicket1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void m_jPrintCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jPrintCashActionPerformed
        printPayments("Printer.PartialCash");
    }//GEN-LAST:event_m_jPrintCashActionPerformed

    private void m_jCloseCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCloseCashActionPerformed
        int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannaclosecash"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {

            Date dNow = new Date();

            try {
                if (m_App.getActiveCashDateEnd() == null) {
                    new StaticSentence(m_App.getSession(), "UPDATE CLOSEDCASH SET DATEEND = ?, ACTIVECASH = ? WHERE HOST = ? AND MONEY = ?", new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.STRING}))
                    .exec(new Object[]{dNow, m_App.getInventoryLocation(), m_App.getProperties().getHost(), m_App.getActiveCashIndex()});

                    AppConfig config = new AppConfig(new File(System.getProperty("user.home") + File.separator + "openbravopos.properties"));
                    config.load();
                    if(config.getProperty("enviarCorreo").equalsIgnoreCase("SI"))
                    {
                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.user", config.getProperty("correoEmisor"));
                    props.put("mail.smtp.password", config.getProperty("claveCorreoEmisor"));
                    props.put("mail.smtp.port", "587");

                    Session session = Session.getInstance(props, new GMailAuthenticator(config.getProperty("correoEmisor"), config.getProperty("claveCorreoEmisor")));
                    try {
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(config.getProperty("correoEmisor")));
                        message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(config.getProperty("correoReceptor1")));
                        message.setRecipients(Message.RecipientType.CC,
                            InternetAddress.parse(config.getProperty("correoReceptor2")));
                        
                        message.setRecipients(Message.RecipientType.BCC,
                            InternetAddress.parse(config.getProperty("correoReceptor3")));
                        message.setRecipients(Message.RecipientType.BCC,
                            InternetAddress.parse(config.getProperty("correoReceptor4")));
                        message.setSubject("Cierre de caja "+config.getProperty("machine.hostname")+". Fecha: " + dNow);

                        String body = "<html><table><tr><td colspan=\"8\">*REPORTE DE CIERRE DE CAJA " + config.getProperty("machine.hostname") + ".</td></tr><tr><td colspan=\"8\"><hr/></td></tr><tr><td colspan=\"2\">Secuencia:</td><td colspan=\"6\">" + this.m_PaymentsToClose.printSequence() + "</td></tr>" + "<tr><td colspan=\"2\">Fecha de inicio:</td><td colspan=\"6\">" + this.m_PaymentsToClose.printDateStart() + "</td></tr>" + "<tr><td colspan=\"2\">Fecha de fin:</td><td colspan=\"6\">" + Formats.TIMESTAMP.formatValue(dNow) + "</td></tr>" + "<tr><td colspan=\"8\"><hr/></td></tr><tr></tr>" + "<tr><td colspan=\"8\">*REPORTE DE PAGOS.</td></tr>" + "<tr><td colspan=\"8\"><hr/></td></tr>";

                        java.util.List<PaymentsModel.PaymentsLine> payments = this.m_PaymentsToClose.getPaymentLines();
                        for (int i = 0; i < payments.size(); i++) {
                            body = body + "<tr><td colspan=\"2\">" + ((PaymentsModel.PaymentsLine) payments.get(i)).printType() + "</td><td colspan=\"6\">" + ((PaymentsModel.PaymentsLine) payments.get(i)).printValue() + "</td></tr>";
                        }
                        
                        body = body + "<tr><td colspan=\"8\"><hr/></td></tr><tr></tr><tr><td colspan=\"8\">*REPORTE DE PRODUCTOS VENDIDOS.</td></tr><tr><td colspan=\"8\"><hr/></td></tr>";
                        body = body + "<tr><td>REFERENCIA</td><td colspan=\"3\">PRODUCTO</td><td>PRESENTACION</td><td>PRECIO</td><td>CANTIDAD</td><td>IMPORTE</td></tr>";
                        java.util.List<PaymentsModel.ProductSalesLine> productSales = this.m_PaymentsToClose.getProductSalesLines();
                        for (int i = 0; i < productSales.size(); i++) {
                            body = body + "<tr><td>" + ((PaymentsModel.ProductSalesLine) productSales.get(i)).getReferencia()+ "</td><td colspan=\"3\">" + ((PaymentsModel.ProductSalesLine) productSales.get(i)).getProduct() + "</td><td>" + ((PaymentsModel.ProductSalesLine) productSales.get(i)).getPresentacion()+ "</td><td>" + ((PaymentsModel.ProductSalesLine) productSales.get(i)).getPrecio() +"</td><td>" + ((PaymentsModel.ProductSalesLine) productSales.get(i)).getUnits()+"</td><td>" + ((PaymentsModel.ProductSalesLine) productSales.get(i)).getImporte() + "</td></tr>";
                        }
                        
                        java.util.List<PaymentsModel.PaymentMovements> pm = this.m_PaymentsToClose.getPaymentMovements();
                        if(pm.size()>0)
                        {
                            body = body + "<tr><td colspan=\"8\"><hr/></td></tr><tr></tr><tr><td colspan=\"8\">REPORTE DE ENTRADAS/SALIDAS.</td></tr><tr><td colspan=\"8\"><hr/></td></tr>";
                            for (int i = 0; i < pm.size(); i++) {
                                body = body + "<tr><td colspan=\"1\">" + ((PaymentsModel.PaymentMovements) pm.get(i)).printType()+ "</td><td colspan=\"1\">" + ((PaymentsModel.PaymentMovements) pm.get(i)).printValue() + "</td><td colspan=\"6\">" + ((PaymentsModel.PaymentMovements) pm.get(i)).printType2() + "</td></tr>";
                            }
                        }
                        
                        java.util.List<PaymentsModel.Abonos> abonos = this.m_PaymentsToClose.getAbonosLines();
                        if(abonos.size()>0)
                        {
                            body = body + "<tr><td colspan=\"8\"><hr/></td></tr><tr></tr><tr><td colspan=\"8\">REPORTE DE DEUDAS/ABONOS.</td></tr><tr><td colspan=\"8\"><hr/></td></tr>";
                            for (int i = 0; i < abonos.size(); i++) {
                                body = body + "<tr><td colspan=\"2\">" + ((PaymentsModel.Abonos) abonos.get(i)).printTipo()+ "</td><td colspan=\"4\">" + ((PaymentsModel.Abonos) abonos.get(i)).printCustomerName() + "</td><td colspan=\"2\">" + ((PaymentsModel.Abonos) abonos.get(i)).printMonto().toString() + "</td></tr>";
                            }
                        }
                        
                        java.util.List<PaymentsModel.Depositos> depositos = this.m_PaymentsToClose.getDepositosLines();
                        if(depositos.size()>0)
                        {
                            body = body + "<tr><td colspan=\"8\"><hr/></td></tr><tr></tr><tr><td colspan=\"8\">REPORTE DE DEPOSITOS.</td></tr><tr><td colspan=\"8\"><hr/></td></tr>";
                            for (int i = 0; i < depositos.size(); i++) {
                                body = body + "<tr><td colspan=\"2\">" + ((PaymentsModel.Depositos) depositos.get(i)).printIdCliente()+ "</td><td colspan=\"2\">" + ((PaymentsModel.Depositos) depositos.get(i)).printCustomerName() + "</td><td colspan=\"2\">" + ((PaymentsModel.Depositos) depositos.get(i)).printMonto().toString() + "</td><td colspan=\"2\">" + ((PaymentsModel.Depositos) depositos.get(i)).printUsuario().toString() + "</td></tr>";
                            }
                        }
                        
                        body = body + "<tr><td colspan=\"8\"><hr/></td></tr><tr></tr><tr><td colspan=\"8\">REPORTE DE INVENTARIO ACTUAL.</td></tr><tr><td colspan=\"8\"><hr/></td></tr>";

                        java.util.List<PaymentsModel.Stock> stock = this.m_PaymentsToClose.getStockLines();
                        for (int i = 0; i < stock.size(); i++) {
                            body = body + "<tr><td colspan=\"2\">" + ((PaymentsModel.Stock) stock.get(i)).printProduct()+ "</td><td colspan=\"4\">" + ((PaymentsModel.Stock) stock.get(i)).printProductName() + "</td><td colspan=\"2\">" + ((PaymentsModel.Stock) stock.get(i)).printUnits().toString() + "</td></tr>";
                        }
                        message.setContent(body, "text/html");
                        Transport transport = session.getTransport("smtp");
                        transport.connect("smtp.gmail.com", 587, config.getProperty("correoEmisor"), config.getProperty("claveCorreoEmisor"));
                        message.saveChanges();
                        transport.sendMessage(message, message.getAllRecipients());
                        transport.close();
                        System.out.println("ENVIO EXITOSO");
                    } catch (MessagingException ex) {
                        System.out.println("ERROR:" + ex.toString());
                        Logger.getLogger(JPanelCloseMoney.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                }
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotclosecash"), e);
                msg.show(this);
            }

            try {
                
                // Creamos una nueva caja
                m_App.setActiveCash(UUID.randomUUID().toString(), m_App.getActiveCashSequence() + 1, dNow, null);

                // creamos la caja activa
                m_dlSystem.execInsertCash(
                    new Object[]{m_App.getActiveCashIndex(), m_App.getProperties().getHost(), m_App.getActiveCashSequence(), m_App.getActiveCashDateStart(), m_App.getActiveCashDateEnd()});

                // ponemos la fecha de fin
                m_PaymentsToClose.setDateEnd(dNow);

                // print report
                printPayments("Printer.CloseCash");

                // Mostramos el mensaje
                JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.closecashok"), AppLocal.getIntString("message.title"), JOptionPane.INFORMATION_MESSAGE);
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotclosecash"), e);
                msg.show(this);
            }

            try {
                loadData();
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("label.noticketstoclose"), e);
                msg.show(this);
            }
        }
    }//GEN-LAST:event_m_jCloseCashActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField m_jCash;
    private javax.swing.JButton m_jCloseCash;
    private javax.swing.JTextField m_jCount;
    private javax.swing.JTextField m_jMaxDate;
    private javax.swing.JTextField m_jMinDate;
    private javax.swing.JButton m_jPrintCash;
    private javax.swing.JTextField m_jSales;
    private javax.swing.JTextField m_jSalesSubtotal;
    private javax.swing.JTextField m_jSalesTaxes;
    private javax.swing.JTextField m_jSalesTotal;
    private javax.swing.JScrollPane m_jScrollSales;
    private javax.swing.JScrollPane m_jScrollTableTicket;
    private javax.swing.JScrollPane m_jScrollTableTicket1;
    private javax.swing.JTextField m_jSequence;
    private javax.swing.JTable m_jTicketTable;
    private javax.swing.JTable m_jTicketTable1;
    private javax.swing.JTable m_jsalestable;
    // End of variables declaration//GEN-END:variables

}
