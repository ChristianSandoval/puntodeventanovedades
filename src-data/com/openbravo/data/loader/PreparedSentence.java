package com.openbravo.data.loader;

import java.sql.*;
import com.openbravo.basic.BasicException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  adrianromero
 */
public class PreparedSentence extends JDBCSentence {

    //private static Logger logger = Logger.getLogger("com.openbravo.data.loader.PreparedSentence");

    private String m_sentence;
    protected SerializerWrite m_SerWrite = null;
    protected SerializerRead m_SerRead = null;
    
    // Estado
    private PreparedStatement m_Stmt;
    
    public PreparedSentence(Session s, String sentence, SerializerWrite serwrite, SerializerRead serread) {         
        super(s);
        m_sentence = sentence;        
        m_SerWrite = serwrite;
        m_SerRead = serread;
        m_Stmt = null;
    }
    public PreparedSentence(Session s, String sentence, SerializerWrite serwrite) {         
        this(s, sentence, serwrite, null);
    }
    public PreparedSentence(Session s, String sentence) {         
        this(s, sentence, null, null);
    }
    
    private static final class PreparedSentencePars implements DataWrite {

        private PreparedStatement m_ps;

        /** Creates a new instance of SQLParameter */
        PreparedSentencePars(PreparedStatement ps) {
            m_ps = ps;
        }

        public void setInt(int paramIndex, Integer iValue) throws BasicException {
            try {
                m_ps.setObject(paramIndex, iValue, Types.INTEGER);
            } catch (SQLException eSQL) {
                throw new BasicException(eSQL);
            }
        }
        public void setString(int paramIndex, String sValue) throws BasicException {
            try {
                m_ps.setString(paramIndex, sValue);
            } catch (SQLException eSQL) {
                throw new BasicException(eSQL);
            }
        }
        public void setDouble(int paramIndex, Double dValue) throws BasicException {
            try {
                m_ps.setObject(paramIndex, dValue, Types.DOUBLE);
            } catch (SQLException eSQL) {
                throw new BasicException(eSQL);
            }
        }   
        public void setBoolean(int paramIndex, Boolean bValue) throws BasicException {
            try {
                if (bValue == null) {
                    m_ps.setObject(paramIndex, null);
                } else {
                    m_ps.setBoolean(paramIndex, bValue.booleanValue());
                }
                // m_ps.setObject(paramIndex, bValue, Types.BOOLEAN);
            } catch (SQLException eSQL) {
                throw new BasicException(eSQL);
            }
        }   
        public void setTimestamp(int paramIndex, java.util.Date dValue) throws BasicException {        
            try {
                m_ps.setObject(paramIndex, dValue == null ? null : new Timestamp(dValue.getTime()), Types.TIMESTAMP);
            } catch (SQLException eSQL) {
                throw new BasicException(eSQL);
            }
        }       
//        public void setBinaryStream(int paramIndex, java.io.InputStream in, int length) throws DataException {
//            try {
//                m_ps.setBinaryStream(paramIndex, in, length);
//            } catch (SQLException eSQL) {
//                throw new DataException(eSQL);
//            }
//        }
        public void setBytes(int paramIndex, byte[] value) throws BasicException {
            try {
                m_ps.setBytes(paramIndex, value);
            } catch (SQLException eSQL) {
                throw new BasicException(eSQL);
            }
        }
        public void setObject(int paramIndex, Object value) throws BasicException {
            try {
                m_ps.setObject(paramIndex, value);
            } catch (SQLException eSQL) {
                throw new BasicException(eSQL);
            }
        }
    } 
    
    public DataResultSet openExec(Object params) throws BasicException {
        // true -> un resultset
        // false -> un updatecount (si -1 entonces se acabo)
        
        closeExec();

        try {
            Statement stmt = m_s.getConnection().createStatement();
            stmt.executeQuery("SELECT 1");
        } catch (SQLException eSQL) {
        }
        /*try {
            new StaticSentence(m_s, "SELECT * FROM PEOPLE WHERE 1 = 0").exec();
        } catch (BasicException eSQL) {
        }*/
        try {

            //logger.info("Executing prepared SQL: " + m_sentence);
            
            m_Stmt = m_s.getConnection().prepareStatement(m_sentence);
 
            if (m_SerWrite != null) {
                // si m_SerWrite fuera null deberiamos cascar.
                m_SerWrite.writeValues(new PreparedSentencePars(m_Stmt), params);
            }

            if (m_Stmt.execute()) {
                return new JDBCDataResultSet(m_Stmt.getResultSet(), m_SerRead);
            } else { 
                int iUC = m_Stmt.getUpdateCount();
                if (iUC < 0) {
                    return null;
                } else {
                    return new SentenceUpdateResultSet(iUC);
                }
            }
        } catch (SQLException eSQL) {
            throw new BasicException(eSQL);
        }
    }
    
    public final DataResultSet moreResults() throws BasicException {
        // true -> un resultset
        // false -> un updatecount (si -1 entonces se acabo)
        
        try {
            if (m_Stmt.getMoreResults()){
                // tenemos resultset
                return new JDBCDataResultSet(m_Stmt.getResultSet(), m_SerRead);
            } else {
                // tenemos updatecount o si devuelve -1 ya no hay mas
                int iUC = m_Stmt.getUpdateCount();
                if (iUC < 0) {
                    return null;
                } else {
                    return new SentenceUpdateResultSet(iUC);
                }
            }
        } catch (SQLException eSQL) {
            throw new BasicException(eSQL);
        }
    }

    public final void closeExec() throws BasicException {
        
        if (m_Stmt != null) {
            try {
                m_Stmt.close();
           } catch (SQLException eSQL) {
                throw new BasicException(eSQL);
            } finally {
                m_Stmt = null;
            }
        }
     }      
}
