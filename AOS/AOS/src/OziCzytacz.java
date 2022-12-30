/*
 * OziCzytacz.java
 *
 * Created on 27 wrzesień 2007, 23:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;

public class OziCzytacz {
    
    Szlak szlak;
    File plik;
    
    public OziCzytacz(File p, Szlak sz){
        szlak = sz;
        plik = p;
    }
    
    public void wczytaj(){
        try {
            Scanner scanner = new Scanner(plik);
            while ( scanner.hasNextLine() ){
                String line = scanner.nextLine();
                if(line.contains(",")) {
                    processLine( line );
                }
            }
            scanner.close();
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        }
        
    }
    
    void processLine(String aLine){
        Scanner scanner = new Scanner(aLine);
        scanner.useDelimiter(",");
        if ( scanner.hasNext() ){
            scanner.next();
            String etykieta = scanner.next();
            String ns = scanner.next();
            String we = scanner.next();
            scanner.next();
            String typ = scanner.next();
            scanner.next();
            scanner.next();
            scanner.next();
            scanner.next();
            String opis = scanner.next();
            Punkt p = new Punkt();
            p.kolejnosc = Double.valueOf(szlak.punkty.size());
            p.etykieta = etykieta;
            p.ns = text2number(ns);
            p.we = text2number(we);
            p.opisypunktu.get(0).typpunktu = ozi2typp(Integer.valueOf(typ.trim()));
            p.opisypunktu.get(0).opis = typ+" : "+opis;
            szlak.punkty.add(p);
        }
        scanner.close();
        
    }
    
    void processLine1(String aLine){
        String[] pola = new String[11];
        pola = aLine.split(",",11);
        String etykieta = pola[1];
        String ns = pola[2];
        String we = pola[3];
        String typ = pola[5];
        String opis = pola[10];
        Punkt p = new Punkt();
        p.kolejnosc = Double.valueOf(szlak.punkty.size());
        p.etykieta = etykieta;
        p.ns = text2number(ns);
        p.we = text2number(we);
        p.opisypunktu.get(0).typpunktu = ozi2typp(Integer.valueOf(typ.trim()));
        p.opisypunktu.get(0).opis = typ+" : "+opis;
        szlak.punkty.add(p);
    }
    
    Double text2number(String text){
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
    
    String ozi2typp(int ozi){
        Hashtable<Integer, String> konw = new Hashtable<Integer, String>();
        konw.put(30, "most");
        konw.put(18, "uwaga");
        konw.put(6, "stanica");
        
        String typp;
        typp = konw.get(ozi);
        if(typp==null){
            typp = "biwak";
        }
        
        return typp;
    }
}




