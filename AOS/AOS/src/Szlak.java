/*
 * Szlak.java
 *
 * Created on 12 czerwiec 2005, 12:08
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
import java.util.*;
import java.io.*;
/**
 *
 * @author kaczka
 */
public class Szlak {
    public String id;
    public String wersja;
    public String nazwa;
    public String opis;
    public Vector<Odcinek> odcinki;
    public Vector<Punkt> punkty;
    
    
    /** Creates a new instance of Szlak */
    public Szlak() {
        id = "";
        wersja = "1";
        nazwa = "";
        opis = "";
        punkty = new Vector<Punkt>();
        odcinki = new Vector<Odcinek>();
        
     }
    
    
    public String zapiszXML(){
        String xml;
        
        boolean[] zapispkt = new boolean[punkty.size()];
        for ( int i=0; i< zapispkt.length; i++ ){
          zapispkt[i] = false;
        }
        
        xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        xml += "<splywww wersja=\"3\">\n";
        String safe_nazwa = nazwa;
        safe_nazwa = safe_nazwa.replaceAll("\"", "&quot;");
        xml += "<szlak nazwa=\""+safe_nazwa+"\" id=\""+id+"\" wersja=\""+wersja+"\">\n";
        xml += "<opis>";
        String safe_opis = opis;
        safe_opis = safe_opis.replaceAll("<", "&lt;");
        safe_opis = safe_opis.replaceAll(">", "&gt;");
        safe_opis = safe_opis.replaceAll("\"", "&quot;");
        xml += safe_opis;
        xml += "</opis>\n";
        
        for(int i=0; i<odcinki.size(); i++) {
            xml += "<odcinek id=\""+odcinki.get(i).id+"\" ";
            xml += "typ=\""+odcinki.get(i).typ+"\" ";
            xml += "nazwa=\""+odcinki.get(i).nazwa+ "\" ";
            xml += "trudnosc=\"" + odcinki.get(i).tru + "\" ";
            xml += "uciazliwosc=\"" + odcinki.get(i).uci + "\" ";
            xml += "malowniczosc=\"" + odcinki.get(i).mal + "\" ";
            xml += "czystosc=\"" + odcinki.get(i).czy + "\" ";
            xml += "kolejnosc=\"" + odcinki.get(i).kolejnosc.intValue() + "\" >\n";
            xml += "<opis>";
            safe_opis = odcinki.get(i).opis;
            safe_opis = safe_opis.replaceAll("<", "&lt;");
            safe_opis = safe_opis.replaceAll(">", "&gt;");
            safe_opis = safe_opis.replaceAll("\"", "&quot;");
            xml += safe_opis;
            xml += "</opis>\n";
            for(int j=0; j<punkty.size(); j++) {
                if(punkty.get(j).idwewo == odcinki.get(i).idwew){
                    xml += punkty.get(j).getXML();
                    zapispkt[j] = true;
                }
            }
        xml += "</odcinek>\n";
        }
        
        for ( int i=0; i< zapispkt.length; i++ ){
          if( zapispkt[i] == false ){
              xml += punkty.get(i).getXML();
          }
        }
        
        xml += "</szlak>\n";
        xml += "</splywww>\n";
        return xml;       
    }
}

