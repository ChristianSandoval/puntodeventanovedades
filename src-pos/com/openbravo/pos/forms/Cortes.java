
package com.openbravo.pos.forms;

import com.openbravo.data.loader.LocalRes;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import javax.swing.Icon;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import com.openbravo.pos.ticket.UserInfo;
import com.openbravo.pos.util.Hashcypher;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author adrianromero
 */
public class Cortes {
    private String id;
    public Cortes(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }      
}
