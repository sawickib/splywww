 /*
 * XMLCzytacz.java
 *
 * Created on 13 czerwiec 2005, 21:33
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;


public class XMLCzytacz extends DefaultHandler {

  boolean inSzlak;
  boolean inOpis;
  int inOdcinek;
  int inPunkt;
  int inOpisPunktu;
  boolean pierwszyop;
          
  StringBuffer data = new StringBuffer();

  Szlak szlak;
  
  public void startDocument() {
    // System.out.println("start document");
    inSzlak = false;
    inOpis = false;
    inOdcinek = -1;
    inPunkt = -1;
    inOpisPunktu = -1;
  }

  public void endDocument() {
    // System.out.println("end document");
  }

  public void startElement(String uri, String localName,
                           String qName, Attributes atts) {

    data.delete(0, data.length());
    
    if (qName.equals("szlak")) {
        //System.out.println("Szlak :"+ atts.getValue("nazwa"));
        szlak.nazwa = atts.getValue("nazwa");
        szlak.id  = atts.getValue("id");
        szlak.wersja  = atts.getValue("wersja");
        inSzlak = true;
    }            
    
    if (qName.equals("odcinek")) {
        Odcinek o = new Odcinek();
        o.id = atts.getValue("id");
        o.typ = atts.getValue("typ");
        o.nazwa = atts.getValue("nazwa");
        o.tru = atts.getValue("trudnosc");
        o.uci = atts.getValue("uciazliwosc");
        o.mal = atts.getValue("malowniczosc");
        o.czy = atts.getValue("czystosc");
        o.kolejnosc = text2number(atts.getValue("kolejnosc"));
        
        szlak.odcinki.add(o);
        inOdcinek = szlak.odcinki.size()-1;
    }
    
    if (qName.equals("punkt")) {
        Punkt p = new Punkt();
        p.etykieta = atts.getValue("etykieta");
        p.km = text2number(atts.getValue("km"));
        p.miejscowosc = atts.getValue("miejscowosc");
        p.id = atts.getValue("id");
        p.ns = text2number(atts.getValue("ns"));
        p.we = text2number(atts.getValue("we"));
        p.kolejnosc = text2number(atts.getValue("kolejnosc"));
        if(inOdcinek != -1){
          p.idwewo = szlak.odcinki.get(inOdcinek).idwew;
        }
        szlak.punkty.add(p);
        inPunkt = szlak.punkty.size()-1;
        pierwszyop = true;
    }
    
    if (qName.equals("opispunktu")) {
        OpisPunktu op = new OpisPunktu();
        op.typpunktu = atts.getValue("typ");
        op.kolejnosc = text2number(atts.getValue("kolejnosc"));
        
        if (pierwszyop) {
            szlak.punkty.get(inPunkt).opisypunktu.set(0,op);
            pierwszyop = false;
        } else {
            szlak.punkty.get(inPunkt).opisypunktu.add(op);
        }
        inOpisPunktu = szlak.punkty.get(inPunkt).opisypunktu.size()-1;
    }       
    
    
    if (qName.equals("opis")) {
        inOpis = true;
    }       

        
    
  }

  public void endElement(String uri, String localName, 
                         String qName) {
    // Check Whitespaces
    boolean allwhite = true;
    int j=0;
    while ((j<data.length())&&(allwhite)) {
        allwhite = Character.isWhitespace(data.charAt(j));
	++j;
    }
    if (!allwhite) {
        String content = data.toString();
        
        if (qName.equals("opis") && inOpis) {
            if(inOdcinek != -1){
                szlak.odcinki.get(inOdcinek).opis = content;
            }
            else if (inSzlak) {
                szlak.opis = content;
            }
        inOpis = false;
        }
        
        if( qName.equals("opispunktu")) {
            Punkt p = szlak.punkty.get(inPunkt);
            p.opisypunktu.get(inOpisPunktu).opis = content;
        }
        
    }
    data.delete(0, data.length());
 
    if (qName.equals("obszar")) {
        inOdcinek = -1;
    }    
    if (qName.equals("szlak")) {
        inSzlak = false;
    }    

  
  }

  
  public void characters(char[] ch, int start, int length){
    data.append(ch, start, length);
  }

  public XMLCzytacz(File plik, Szlak sz){
      szlak = sz;
  //    DefaultHandler handler = new XMLCzytacz(plik, szlak);
      SAXParserFactory factory = SAXParserFactory.newInstance();
      try {SAXParser saxParser = factory.newSAXParser();
            saxParser.parse( plik, this);

        } catch (Throwable t) {
            t.printStackTrace();
        }
      
  }
  
  public XMLCzytacz(){
      Szlak szlak = new Szlak();
  }
  
  public static void main(String[] args) {

      // Use an instance of ourselves as the SAX event handler
        DefaultHandler handler = new XMLCzytacz();
      
        // Use the default (non-validating) parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            // Set up output stream
            //out = new OutputStreamWriter(System.out, "UTF8");
            // Parse the input
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse( new File("test.xml"), handler);

        } catch (Throwable t) {
            t.printStackTrace();
        }

  }
  
   public Double text2number(String text){
        Double x;
        try {
        if(text.length() == 0) {
             x = -1.0;
        } else {
            try {
                x = Double.valueOf(text);
            } catch (NumberFormatException ex) {
                x = -1.0 ;
            }
        }
        } catch (NullPointerException ex) {
            x = -1.0;
        }
        return(x);
    }

 
}
