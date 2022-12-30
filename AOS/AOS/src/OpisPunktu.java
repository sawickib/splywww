/*
 * OpisPunktu.java
 *
 * Created on 12 czerwiec 2005, 21:54
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

/**
 *
 * @author kaczka
 */
public class OpisPunktu implements Comparable{
    
    public String typpunktu;
    public String opis;
    public Double kolejnosc;
    
    
    /** Creates a new instance of OpisPunktu */
    public OpisPunktu() {
        typpunktu = "";
        opis = "";
        kolejnosc = -1.0;
    }
  
    public String getXML() {
        String xml;
        xml = "<opispunktu typ=\""+typpunktu+"\" kolejnosc=\""+kolejnosc.intValue()+"\">";
        String safe_opis = opis;
        safe_opis = safe_opis.replaceAll("<", "&lt;");
        safe_opis = safe_opis.replaceAll(">", "&gt;");
        safe_opis = safe_opis.replaceAll("\"", "&quot;");
        xml += safe_opis;
        xml += "</opispunktu>\n"; 
        return(xml);
    }
    
@Override public String toString() {
        if(typpunktu.isEmpty()){
            return("( puste )");
        } else {
            return(typpunktu);
        }
    }
    
@Override public int compareTo(Object o) {
        OpisPunktu op = (OpisPunktu) o;
        return this.kolejnosc.compareTo(op.kolejnosc);
    }
}
