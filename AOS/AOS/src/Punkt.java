/*
 * Punkt.java
 *
 * Created on 12 czerwiec 2005, 21:51
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
import java.util.*;
/**
 *
 * @author kaczka
 */
public class Punkt implements Comparable{
    
    public String id;
    public String etykieta;
    public Double km;
    public String miejscowosc;
    public Double ns;
    public Double we;
    public Double kolejnosc;
    
    public Vector<OpisPunktu> opisypunktu;
    
    public int idwewo;
    
    /** Creates a new instance of Punkt */
   // public Punkt() {
   // }
    
    public Punkt(){
        this.etykieta = "";
        this.miejscowosc = "";
        this.id = "";
        this.km = -1.0;
        this.ns = -1.0;
        this.we = -1.0;
        this.kolejnosc = -1.0;
        this.opisypunktu = new Vector<OpisPunktu>();
        OpisPunktu opkt = new OpisPunktu();
        this.opisypunktu.add(opkt);
        idwewo = 0;
    }
    
    public String getXML() {
        String xml;
        
        xml = "<punkt ";
        xml += "etykieta=\""+etykieta+"\" ";
        xml += "id=\""+id+"\" ";
        xml += "km=\""+AOSgui.number2text(km)+"\" ";
        xml += "ns=\""+AOSgui.number2text(ns)+"\" ";
        xml += "we=\""+AOSgui.number2text(we)+"\" ";
        xml += "miejscowosc=\""+miejscowosc+"\" ";
        xml += "kolejnosc=\""+kolejnosc.intValue()+"\" ";
        
        xml += ">\n";  
        
        for(int i=0; i<opisypunktu.size(); i++){
            xml += opisypunktu.get(i).getXML();
        }
        
        xml += "</punkt>\n";
        
        return(xml);
    }
    

@Override public String toString() {
        String label;
        label = etykieta + " : ";
        label += AOSgui.number2text(km) + " : ";
        if(opisypunktu.size()>0){
            label += opisypunktu.get(0).typpunktu;
        }
        label += " : ";
        label += miejscowosc;
        return(label);
    }

@Override public int compareTo(Object o) {
        Punkt p = (Punkt) o;
        return this.kolejnosc.compareTo(p.kolejnosc);
    }

}
