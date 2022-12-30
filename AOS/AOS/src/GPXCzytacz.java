 /*
 * GPXCzytacz.java
 *
 * Created on 27 wrzesien 2007, 21:33
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


public class GPXCzytacz extends DefaultHandler {

  boolean inName;
  boolean inDesc;
  boolean inSym;
  int inWpt;
  StringBuffer data = new StringBuffer();

  Szlak szlak;
  
  public void startDocument() {
    System.out.println("start GPX document");
    inName = false;
    inDesc = false;
    inSym = false;
    inWpt = -1;
  }

  public void endDocument() {
    System.out.println("end GPX document");
  }

  public void startElement(String uri, String localName,
                           String qName, Attributes atts) {

    data.delete(0, data.length());
    
    if (qName.equals("wpt")) {
        Punkt p = new Punkt();
        p.kolejnosc = Double.valueOf(szlak.punkty.size());
        p.ns = text2number(atts.getValue("lat"));
        p.we = text2number(atts.getValue("lon"));
        szlak.punkty.add(p);
        inWpt = szlak.punkty.size()-1;
    }
      
    if (qName.equals("name")) {
        inName = true;
    }       
    if (qName.equals("desc")) {
        inDesc = true;
    }       
    if (qName.equals("sym")) {
        inSym = true;
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
        
        if (qName.equals("name") && inName) {
            if(inWpt != -1){
                szlak.punkty.get(inWpt).etykieta = content;
            }
            inName = false;
        }
        if (qName.equals("desc") && inDesc) {
            if(inWpt != -1){
                Punkt p = szlak.punkty.get(inWpt);
                p.opisypunktu.get(0).opis = content;
            }
            inDesc = false;
        }
        if (qName.equals("sym") && inSym) {
            if(inWpt != -1){
                System.out.println("Zignorowano symbol: "+content);
            }
            inName = false;
        }
               
    }
    data.delete(0, data.length());
 
    if (qName.equals("wpt")) {
        inWpt = -1;
    }    
  
  }

  
  public void characters(char[] ch, int start, int length){
    data.append(ch, start, length);
  }

  public GPXCzytacz(File plik, Szlak sz){
      szlak = sz;
  //    DefaultHandler handler = new GPXCzytacz(plik, szlak);
      SAXParserFactory factory = SAXParserFactory.newInstance();
      try {SAXParser saxParser = factory.newSAXParser();
            saxParser.parse( plik, this);

        } catch (Throwable t) {
            t.printStackTrace();
        }
      
  }
  
  public GPXCzytacz(){
      Szlak szlak = new Szlak();
  }
  
  public static void main(String[] args) {

      // Use an instance of ourselves as the SAX event handler
        DefaultHandler handler = new GPXCzytacz();
      
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
