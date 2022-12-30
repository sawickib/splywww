/*
 * Odcinek.java
 *
 * Created on 12 czerwiec 2005, 21:48
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

/**
 *
 * @author kaczka
 */
public class Odcinek implements Comparable{
    String typ;
    String nazwa;
    String opis;
    String tru;
    String uci;
    String mal;
    String czy;
    Double kolejnosc; 
    int idwew;
    String id;
    
    /** Creates a new instance of Odcinek */
    public Odcinek() {
        idwew = (int) (Integer.MAX_VALUE * Math.random());
        id = "";
        kolejnosc = -1.0;
        opis = "";
        nazwa = "";
        typ = "";
        tru = "";
        uci = "";
        mal = "";
        czy = "";
    }  
    
//    public String getXML() {
//        String xml;
//        xml = "<odcinek nazwa=\""+this.nazwa+ "\" ";
//        xml += "trudnosc=\"" + this.tru + "\" ";
//        xml += "uciazliwosc=\"" + this.uci + "\" ";
//        xml += "malowniczosc=\"" + this.mal + "\" ";
//        xml += "czystosc=\"" + this.czy + "\" >";
//        xml += "<opis>" + this.opis + "</opis>\n";
//        xml += "</odcinek>";
//        return(xml);
//    }
     
    public String toString() {
        String label;
        if(this.nazwa.length()>0){
            label = this.nazwa;
        } else {
            label = "( puste )";
        }
        return(label);
    }

    public int compareTo(Object o) {
        Odcinek odc = (Odcinek) o;
        return this.kolejnosc.compareTo(odc.kolejnosc);
    }

}
