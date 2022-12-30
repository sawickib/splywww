/*
 * AOSgui.java
 *
 * Created on 10 czerwiec 2005, 13:35
 */
import javax.swing.*;
import java.util.*;
import java.io.*;
/**
 *
 * @author  
 */

public class AOSgui extends javax.swing.JFrame {
    
    private String wersja = "1.2beta";
    
    private DefaultListModel listModel;
    
    public Szlak szlak = new Szlak();
    public File plik;
    int zaznaczony_punkt;
    int zaznaczony_opisp;
    int zaznaczony_odcinek;
    boolean zmieniany_plik;
    
    /** Creates new form AOSgui */
    public AOSgui() {
        listModel = new DefaultListModel();
        zaznaczony_punkt = -1;
        zaznaczony_opisp = -1;
        zaznaczony_odcinek = -1;
        
        initComponents();
        
        informacja("Witamy w AOS SplyWWW.");
        zmiana_pliku(false);
        edytowalny_odcinek(false);
        edytowalny_punkt(false);

    }
    
    private void odswiez_punkt(){
        if(zaznaczony_punkt != -1) {
            jTF_etykieta.setText(szlak.punkty.get(zaznaczony_punkt).etykieta);
            jL_idp.setText(" id: "+szlak.punkty.get(zaznaczony_punkt).id);
            jTF_km.setText(number2text(szlak.punkty.get(zaznaczony_punkt).km));
            jTF_miejscowosc.setText(szlak.punkty.get(zaznaczony_punkt).miejscowosc);
            
            Double ns = szlak.punkty.get(zaznaczony_punkt).ns;
            Double we = szlak.punkty.get(zaznaczony_punkt).we;
            String tekst;
            if(ns==-1.0 || we==-1.0){
                tekst = "";
            } else {
                tekst = number2text(ns)+" "+number2text(we);
            }
            jTF_wsp.setText(tekst);
            
            Punkt p = szlak.punkty.get(zaznaczony_punkt);          
            int zop=zaznaczony_opisp;
            jL_opisyp.setListData(p.opisypunktu);
            zaznaczony_opisp = zop;
            odswiez_opisp();
        }
    }
    
    private void odswiez_opisp(){
        if(zaznaczony_opisp == -1) {
            zaznaczony_opisp = 0;
        }
        OpisPunktu op = szlak.punkty.get(zaznaczony_punkt).opisypunktu.get(zaznaczony_opisp);
        jCB_typp.setSelectedItem(op.typpunktu);
        jTP_opisp.setText(op.opis);
    }
    
    private void odswiez_odcinek(){
        if(zaznaczony_odcinek != -1) {
            Odcinek o = szlak.odcinki.get(zaznaczony_odcinek);
            jCB_typo.setSelectedItem(o.typ);
            jTF_nazwao.setText(o.nazwa);
            jTP_opiso.setText(o.opis);
            jCB_tru.setSelectedItem(o.tru);
            jCB_uci.setSelectedItem(o.uci);
            jCB_mal.setSelectedItem(o.mal);
            jCB_czy.setSelectedItem(o.czy);
            
            Vector<Integer> zazp;
            zazp = new Vector<Integer>();
            for(int i=0; i<szlak.punkty.size(); i++){
                if(szlak.punkty.get(i).idwewo == o.idwew){
                    zazp.add(i);
                }
            }
            int[] zazpi = new int[zazp.size()];
            for(int i=0; i<zazp.size(); i++){
                zazpi[i] = (int)zazp.get(i);
            }
            jL_punktyo.setSelectedIndices(zazpi);
            
        }
    }
    
    
    static Double text2number(String text){
        Double x;
        text = text.replace(',', '.');
        if(text.length() == 0) {
            x = -1.0;
        } else {
            try {
                x = Double.valueOf(text);
            } catch (NumberFormatException ex) {
                x = -1.0 ;
            }
        }
        return(x);
    }
    
    static String number2text(Double x){
        String text;
        if(x == -1.0) {
            text = "";
        } else {
            text = x.toString();
        }
        return(text);
    }
    
    
    void zapamietaj_punkt(){
        
        if(zaznaczony_punkt != -1){
            Punkt p = szlak.punkty.get(zaznaczony_punkt);
            p.etykieta = jTF_etykieta.getText();
            p.miejscowosc = jTF_miejscowosc.getText();
            p.km = text2number(jTF_km.getText());
            String[] wsp = { "", "" };
            wsp = jTF_wsp.getText().split(" ",2);
            if(!wsp[0].isEmpty() && !wsp[1].isEmpty()){
              p.ns = text2number( wsp[0] );
              p.we = text2number( wsp[1] );
            }
            
            informacja("Punkt zapamiętany");
            zmiana_pliku(true);
        }
   
    }
    
    
    void nowy_punkt(){
        Punkt p = new Punkt();
        p.kolejnosc = Double.valueOf(szlak.punkty.size());
        szlak.punkty.add(p);
        
        jL_punkty.setListData(szlak.punkty);
        jL_punkty.setSelectedIndex(szlak.punkty.size()-1);
        jL_punkty.ensureIndexIsVisible(szlak.punkty.size()-1);
    }
    
    
    void nowy_opisp(){
        if(zaznaczony_punkt != -1) {
            Punkt p = szlak.punkty.get(zaznaczony_punkt);
            OpisPunktu opkt = new OpisPunktu();
            p.opisypunktu.add(opkt);
                        
            jL_opisyp.setListData(p.opisypunktu);
            jL_opisyp.setListData(p.opisypunktu);
            jL_opisyp.setSelectedIndex(p.opisypunktu.size()-1);
            jL_opisyp.ensureIndexIsVisible(p.opisypunktu.size()-1);
        }
    }
    
    void zapamietaj_opisp(){
        if(zaznaczony_punkt != -1) {
            Punkt p = szlak.punkty.get(zaznaczony_punkt);
            if(zaznaczony_opisp != -1){
                OpisPunktu op = p.opisypunktu.get(zaznaczony_opisp);
                op.typpunktu = (String)jCB_typp.getSelectedItem();
                op.opis = jTP_opisp.getText();
                informacja("Opis punktu zapamiętany");
                zmiana_pliku(true);
            }
        }
    }
    
    void nowy_odcinek(){
        Odcinek o = new Odcinek();
        o.kolejnosc = Double.valueOf(szlak.odcinki.size());
        szlak.odcinki.add(o);
        
        jL_odcinki.setListData(szlak.odcinki);
        jL_odcinki.setSelectedIndex(szlak.odcinki.size()-1);
        jL_odcinki.ensureIndexIsVisible(szlak.odcinki.size()-1);
    }
    
    void zapamietaj_odcinek(){
        
        if(zaznaczony_odcinek != -1){
            
            Odcinek o = szlak.odcinki.get(zaznaczony_odcinek);
            o.typ = (String)jCB_typo.getSelectedItem();
            o.nazwa = jTF_nazwao.getText();
            
            o.opis = jTP_opiso.getText();
            o.tru = (String)jCB_tru.getSelectedItem();
            o.uci = (String)jCB_uci.getSelectedItem();
            o.mal = (String)jCB_mal.getSelectedItem();
            o.czy = (String)jCB_czy.getSelectedItem();
            
            int[] zazp = jL_punktyo.getSelectedIndices();
            
            for(int i=0; i < szlak.punkty.size(); i++){
                if(szlak.punkty.get(i).idwewo == o.idwew){
                    szlak.punkty.get(i).idwewo = -1;
                }
            }
            
            for(int i=0; i < zazp.length; i++){
                szlak.punkty.get(zazp[i]).idwewo = o.idwew;
            }
            
            informacja("Odcinek zapamiętany");
            zmiana_pliku(true);
        }
    }
    
    void zapamietaj_szlak(){
        szlak.opis = jTP_opissz.getText();
        szlak.nazwa = jTF_nazwasz.getText();
        zmiana_pliku(true);
        informacja("Informacje o szlaku zapamiętane");
    }

    void zapisz_dane(){
        zapamietaj_szlak();

        int zp=zaznaczony_punkt;
        jL_punkty.setListData(szlak.punkty);
        zaznaczony_punkt = zp;
        odswiez_punkt();

        int zo=zaznaczony_odcinek;
        jL_odcinki.setListData(szlak.odcinki);
        jL_punktyo.setListData(szlak.punkty);
        zaznaczony_odcinek = zo;
        odswiez_odcinek();
    }

    void informacja(String tekst){
        informacjaLabel.setText(tekst);
    }
    
    void zmiana_pliku(boolean znacznik){
        String info = new String();
        zmieniany_plik = znacznik;
        if(znacznik){
            info = "( zmieniane )";
        } else {
            info = "";
        }
        info += " v." + wersja;
        zmienianyLabel.setText(info);
    }
    
    String sprawdz_puste(){
        String info="";
        int pustyo=0;
        int pustyp=0;
        for(int i=0; i<szlak.odcinki.size(); i++) {
            if(szlak.odcinki.get(i).typ.isEmpty() ||
                    szlak.odcinki.get(i).tru.isEmpty() ||
                    szlak.odcinki.get(i).uci.isEmpty() ||
                    szlak.odcinki.get(i).mal.isEmpty() ||
                    szlak.odcinki.get(i).czy.isEmpty() ) {
                pustyo++;
            }
        }
        if(pustyo > 0){
            info += "Liczba błędnych odcinków: "+pustyo+"\n";
        }
        for(int i=0; i<szlak.punkty.size(); i++) {
            if(szlak.punkty.get(i).opisypunktu.get(0).typpunktu.isEmpty()){
                pustyp++;
            }
        }
        if(pustyp > 0){
            info += "Liczba błędnych punktów: "+pustyp+"\n";
        }
        return info;
    }
    
    void zapisz_plik(){

        zapisz_dane();

        if(plik != null) {
            String info=sprawdz_puste();
            if(!info.isEmpty()){
                String ostrzezenie =
                    "Uwaga! \n"+
                    "Są punkty lub odcinki, dla których nie określono typu.\n"+
                    info+
                    "\nZapisany plik XML nie będzie poprawny. Uzupełnij opis.\n"+
                    "Czy mimo tego chcesz go zapisać?";
                int odp = JOptionPane.showConfirmDialog(this, ostrzezenie);
                if( odp != JOptionPane.OK_OPTION) {
                    informacja("Operacja anulowana");
                    return;
                }
            }

            try {
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(plik), "UTF-8");
                out.write(szlak.zapiszXML());
                out.close();
                zmiana_pliku(false);
                informacja("Zapisano do pliku");
            } catch (IOException e) {
                informacja("Błąd zapisu do pliku");
            }
        }
    }
    
    void wybierz_plik(){
        JFileChooser fc=new JFileChooser(System.getProperty("user.dir"));
        
        ExampleFileFilter filter = new ExampleFileFilter("xml", "Pliki XML");
        fc.addChoosableFileFilter(filter);
        
        int fd = fc.showSaveDialog(this);
        if(fd!=JFileChooser.APPROVE_OPTION) return;
        plik = fc.getSelectedFile().getAbsoluteFile();

        String name = plik.getName();
        if (name.indexOf('.')==-1) {
            name += ".xml";
            plik = new File(plik.getParentFile(), name);
        }

        if(plik.exists()){
            int odp = JOptionPane.showConfirmDialog(this,
                    "Istnieje już taki plik. Chcesz go nadpisać?");
            if( odp != JOptionPane.OK_OPTION) {
                plik = null;
                informacja("Operacja anulowana");
                return;
            }
        }
        
    }
     
    void wyczysc_wszystko(){
            jTP_opissz.setText("");
            jTF_nazwasz.setText("");
            zaznaczony_punkt = -1;
            zaznaczony_opisp = -1;
            zaznaczony_odcinek = -1;
            jL_punkty.setListData(new Vector());
            jL_opisyp.setListData(new Vector());
            jL_odcinki.setListData(new Vector());
            jTF_etykieta.setText("");
            jTF_km.setText("");
            jTF_miejscowosc.setText("");
            jTF_wsp.setText("");
            jCB_typp.setSelectedItem("");
            jTP_opisp.setText("");
            jCB_typo.setSelectedItem("");
            jTF_nazwao.setText("");
            jTP_opiso.setText("");
            jCB_tru.setSelectedItem("");
            jCB_uci.setSelectedItem("");
            jCB_mal.setSelectedItem("");
            jCB_czy.setSelectedItem("");
    }
    
    void edytowalny_punkt(boolean enable){
        jB_pwgore.setEnabled(enable);
        jB_pwdol.setEnabled(enable);
        jTF_miejscowosc.setEnabled(enable);
        jTF_etykieta.setEnabled(enable);
        jTF_km.setEnabled(enable);
        jTF_wsp.setEnabled(enable);
        jL_opisyp.setEnabled(enable);
        jCB_typp.setEnabled(enable);
        jTP_opisp.setEnabled(enable);
        jB_usunp.setEnabled(enable);
        jB_nowyop.setEnabled(enable);
        jB_usunop.setEnabled(enable);
    }
    
    void edytowalny_odcinek(boolean enable){
        jB_owgore.setEnabled(enable);
        jB_owdol.setEnabled(enable);
        jCB_typo.setEnabled(enable);
        jTF_nazwao.setEnabled(enable);
        jTP_opiso.setEnabled(enable);
        jCB_tru.setEnabled(enable);
        jCB_uci.setEnabled(enable);
        jCB_mal.setEnabled(enable);
        jCB_czy.setEnabled(enable);
        jB_usuno.setEnabled(enable);
        jL_punktyo.setEnabled(enable);
    }
    
    void koniec_programu(){
        boolean ok = false;
        if(zmieniany_plik){
            int odp = JOptionPane.showConfirmDialog(this, "Chcesz stracić zmiany?");
            if( odp == JOptionPane.OK_OPTION) {
                ok = true;
            }
        } else {
            ok = true;
        }
        if(ok){
            System.exit(0);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        szlakPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jTF_nazwasz = new javax.swing.JTextField();
        jPanel36 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTP_opissz = new javax.swing.JTextPane();
        punktyPanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jL_punkty = new javax.swing.JList(listModel);
        jPanel4 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel12 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jB_pwgore = new javax.swing.JButton();
        jB_pwdol = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jL_etykieta = new javax.swing.JLabel();
        jTF_etykieta = new javax.swing.JTextField();
        jL_idp = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jL_km = new javax.swing.JLabel();
        jTF_km = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jL_miejscowosc = new javax.swing.JLabel();
        jTF_miejscowosc = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jL_wsp = new javax.swing.JLabel();
        jTF_wsp = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jL_opisyp = new javax.swing.JList();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jCB_typp = new javax.swing.JComboBox();
        jPanel31 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTP_opisp = new javax.swing.JTextPane();
        jPanel6 = new javax.swing.JPanel();
        jB_nowyp = new javax.swing.JButton();
        jB_usunp = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jB_nowyop = new javax.swing.JButton();
        jB_usunop = new javax.swing.JButton();
        odcinkiPanel = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        jL_punktyo = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        jSplitPane5 = new javax.swing.JSplitPane();
        jPanel16 = new javax.swing.JPanel();
        jSplitPane6 = new javax.swing.JSplitPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        jL_odcinki = new javax.swing.JList();
        jPanel18 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jB_owgore = new javax.swing.JButton();
        jB_owdol = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jCB_typo = new javax.swing.JComboBox();
        jPanel19 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTF_nazwao = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jCB_tru = new javax.swing.JComboBox();
        jPanel22 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jCB_uci = new javax.swing.JComboBox();
        jPanel23 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jCB_mal = new javax.swing.JComboBox();
        jPanel24 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jCB_czy = new javax.swing.JComboBox();
        jPanel32 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTP_opiso = new javax.swing.JTextPane();
        jPanel33 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jB_nowyo = new javax.swing.JButton();
        jB_usuno = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        informacjaLabel = new javax.swing.JLabel();
        zmienianyLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        importGPXMenuItem = new javax.swing.JMenuItem();
        importOziMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMI_sort = new javax.swing.JMenuItem();
        jMI_initkol = new javax.swing.JMenuItem();
        jMI_stat = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        jMI_minihelp = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("AOS - SplyWWW");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                zamkniecie_okna(evt);
            }
        });
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        szlakPanel.setLayout(new javax.swing.BoxLayout(szlakPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel7.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel7.setPreferredSize(new java.awt.Dimension(204, 30));
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("Nazwa szlaku");
        jPanel7.add(jLabel1);

        szlakPanel.add(jPanel7);

        jPanel8.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jTF_nazwasz.setColumns(20);
        jTF_nazwasz.setFont(jTF_nazwasz.getFont().deriveFont(jTF_nazwasz.getFont().getSize()+12f));
        jPanel8.add(jTF_nazwasz, java.awt.BorderLayout.WEST);

        szlakPanel.add(jPanel8);
        szlakPanel.add(jPanel36);

        jPanel9.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel2.setText("Opis szlaku");
        jPanel9.add(jLabel2);

        szlakPanel.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(500, 200));

        jTP_opissz.setBackground(java.awt.Color.white);
        jScrollPane2.setViewportView(jTP_opissz);

        jPanel10.add(jScrollPane2);

        szlakPanel.add(jPanel10);

        jTabbedPane1.addTab("Szlak", szlakPanel);

        punktyPanel.setLayout(new javax.swing.BoxLayout(punktyPanel, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane5.setMinimumSize(new java.awt.Dimension(200, 22));
        jScrollPane5.setPreferredSize(new java.awt.Dimension(200, 131));

        jL_punkty.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jL_punktyValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(jL_punkty);

        jSplitPane1.setLeftComponent(jScrollPane5);

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.Y_AXIS));

        jLabel3.setText("Przesuń punkt: ");
        jPanel27.add(jLabel3);

        jB_pwgore.setText("w górę");
        jB_pwgore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_pwgoreActionPerformed(evt);
            }
        });
        jPanel27.add(jB_pwgore);

        jB_pwdol.setText("w dół");
        jB_pwdol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_pwdolActionPerformed(evt);
            }
        });
        jPanel27.add(jB_pwdol);

        jPanel12.add(jPanel27);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jL_etykieta.setText("Etykieta");
        jL_etykieta.setPreferredSize(new java.awt.Dimension(100, 15));
        jPanel1.add(jL_etykieta);

        jTF_etykieta.setColumns(5);
        jPanel1.add(jTF_etykieta);
        jPanel1.add(jL_idp);

        jPanel12.add(jPanel1);

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jL_km.setText("Km");
        jL_km.setPreferredSize(new java.awt.Dimension(100, 15));
        jPanel26.add(jL_km);

        jTF_km.setColumns(5);
        jPanel26.add(jTF_km);

        jPanel12.add(jPanel26);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jL_miejscowosc.setText("Miejscowość");
        jL_miejscowosc.setPreferredSize(new java.awt.Dimension(100, 15));
        jPanel2.add(jL_miejscowosc);

        jTF_miejscowosc.setColumns(20);
        jPanel2.add(jTF_miejscowosc);

        jPanel12.add(jPanel2);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jL_wsp.setText("NS WE");
        jL_wsp.setPreferredSize(new java.awt.Dimension(100, 15));
        jPanel3.add(jL_wsp);

        jTF_wsp.setColumns(10);
        jPanel3.add(jTF_wsp);

        jPanel12.add(jPanel3);

        jSplitPane3.setLeftComponent(jPanel12);

        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane4.setMinimumSize(new java.awt.Dimension(100, 22));
        jScrollPane4.setPreferredSize(new java.awt.Dimension(100, 3));

        jL_opisyp.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jL_opisypValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(jL_opisyp);

        jSplitPane2.setLeftComponent(jScrollPane4);

        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.Y_AXIS));

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel9.setText("Typ punktu");
        jPanel15.add(jLabel9);

        jCB_typp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "ujście", "przenoska", "most", "jaz", "biwak", "sklep", "lekarz", "stanica", "most", "wypożyczalnia", "wypływ", "uwaga", "bar", "niebezpieczeństwo" }));
        jCB_typp.setPreferredSize(new java.awt.Dimension(140, 27));
        jPanel15.add(jCB_typp);

        jPanel14.add(jPanel15);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel4.setText("Opis punktu");
        jPanel31.add(jLabel4);

        jPanel14.add(jPanel31);

        jScrollPane3.setMinimumSize(new java.awt.Dimension(200, 100));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(200, 100));

        jTP_opisp.setBackground(java.awt.Color.white);
        jScrollPane3.setViewportView(jTP_opisp);

        jPanel14.add(jScrollPane3);

        jB_nowyp.setText("nowy punkt");
        jB_nowyp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_nowypActionPerformed(evt);
            }
        });
        jPanel6.add(jB_nowyp);

        jB_usunp.setText("usuń punkt");
        jB_usunp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_usunpActionPerformed(evt);
            }
        });
        jPanel6.add(jB_usunp);

        jPanel14.add(jPanel6);

        jB_nowyop.setText("nowy typ");
        jB_nowyop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_nowyopActionPerformed(evt);
            }
        });
        jPanel17.add(jB_nowyop);

        jB_usunop.setText("usuń typ");
        jB_usunop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_usunopActionPerformed(evt);
            }
        });
        jPanel17.add(jB_usunop);

        jPanel14.add(jPanel17);

        jSplitPane2.setRightComponent(jPanel14);

        jPanel13.add(jSplitPane2);

        jSplitPane3.setRightComponent(jPanel13);

        jPanel4.add(jSplitPane3);

        jSplitPane1.setRightComponent(jPanel4);

        punktyPanel.add(jSplitPane1);

        jTabbedPane1.addTab("Punkty", punktyPanel);

        odcinkiPanel.setLayout(new javax.swing.BoxLayout(odcinkiPanel, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane6.setMinimumSize(new java.awt.Dimension(200, 22));
        jScrollPane6.setPreferredSize(new java.awt.Dimension(200, 131));
        jScrollPane6.setViewportView(jL_punktyo);

        jSplitPane4.setRightComponent(jScrollPane6);

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jSplitPane5.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel16.setLayout(new javax.swing.BoxLayout(jPanel16, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane7.setMinimumSize(new java.awt.Dimension(200, 22));
        jScrollPane7.setPreferredSize(new java.awt.Dimension(200, 131));

        jL_odcinki.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jL_odcinkiValueChanged(evt);
            }
        });
        jScrollPane7.setViewportView(jL_odcinki);

        jSplitPane6.setLeftComponent(jScrollPane7);

        jPanel18.setLayout(new javax.swing.BoxLayout(jPanel18, javax.swing.BoxLayout.Y_AXIS));

        jLabel6.setText("Przesuń odcinek: ");
        jPanel34.add(jLabel6);

        jB_owgore.setText("w górę");
        jB_owgore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_owgoreActionPerformed(evt);
            }
        });
        jPanel34.add(jB_owgore);

        jB_owdol.setText("w dół");
        jB_owdol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_owdolActionPerformed(evt);
            }
        });
        jPanel34.add(jB_owdol);

        jPanel18.add(jPanel34);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel10.setText("Typ odcinka");
        jLabel10.setPreferredSize(new java.awt.Dimension(100, 15));
        jPanel25.add(jLabel10);

        jCB_typo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "rzeka", "jezioro", "kanał", "mieszany", "zalew" }));
        jPanel25.add(jCB_typo);

        jPanel18.add(jPanel25);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel5.setText("Nazwa");
        jPanel19.add(jLabel5);

        jTF_nazwao.setColumns(15);
        jPanel19.add(jTF_nazwao);

        jPanel18.add(jPanel19);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel11.setText("Trudność");
        jLabel11.setPreferredSize(new java.awt.Dimension(100, 15));
        jPanel21.add(jLabel11);

        jCB_tru.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "ZWA", "ZWB", "ZWC", "WW1", "WW2", "WW3", "WW4", "WW5", "WW6" }));
        jCB_tru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCB_truActionPerformed(evt);
            }
        });
        jPanel21.add(jCB_tru);

        jPanel18.add(jPanel21);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel12.setText("Uciążliwość");
        jLabel12.setPreferredSize(new java.awt.Dimension(100, 15));
        jPanel22.add(jLabel12);

        jCB_uci.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "U1", "U2", "U3", "U4", "U5", "U6" }));
        jPanel22.add(jCB_uci);

        jPanel18.add(jPanel22);

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel13.setText("Malowniczość");
        jLabel13.setPreferredSize(new java.awt.Dimension(100, 15));
        jPanel23.add(jLabel13);

        jCB_mal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "-", "*", "**", "***" }));
        jPanel23.add(jCB_mal);

        jPanel18.add(jPanel23);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel14.setText("Czystość");
        jLabel14.setPreferredSize(new java.awt.Dimension(100, 15));
        jPanel24.add(jLabel14);

        jCB_czy.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "pozaKL", "KL3", "KL2", "KL1" }));
        jPanel24.add(jCB_czy);

        jPanel18.add(jPanel24);

        jSplitPane6.setRightComponent(jPanel18);

        jPanel16.add(jSplitPane6);

        jSplitPane5.setLeftComponent(jPanel16);

        jPanel32.setLayout(new javax.swing.BoxLayout(jPanel32, javax.swing.BoxLayout.Y_AXIS));

        jPanel35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel7.setText("Opis odcinka");
        jPanel35.add(jLabel7);

        jPanel32.add(jPanel35);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(10, 100));

        jTP_opiso.setBackground(java.awt.Color.white);
        jScrollPane1.setViewportView(jTP_opiso);

        jPanel32.add(jScrollPane1);
        jPanel32.add(jPanel33);

        jPanel20.setLayout(new javax.swing.BoxLayout(jPanel20, javax.swing.BoxLayout.LINE_AXIS));

        jB_nowyo.setText("nowy");
        jB_nowyo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_nowyoActionPerformed(evt);
            }
        });
        jPanel20.add(jB_nowyo);

        jB_usuno.setText("usuń");
        jB_usuno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_usunoActionPerformed(evt);
            }
        });
        jPanel20.add(jB_usuno);

        jPanel32.add(jPanel20);

        jSplitPane5.setRightComponent(jPanel32);

        jPanel5.add(jSplitPane5);

        jSplitPane4.setLeftComponent(jPanel5);

        odcinkiPanel.add(jSplitPane4);

        jTabbedPane1.addTab("Odcinki", odcinkiPanel);

        getContentPane().add(jTabbedPane1);

        jPanel28.setMaximumSize(new java.awt.Dimension(2147483647, 13));
        jPanel28.setLayout(new java.awt.BorderLayout(10, 0));

        informacjaLabel.setFont(informacjaLabel.getFont().deriveFont(informacjaLabel.getFont().getSize()-3f));
        informacjaLabel.setText("start");
        jPanel28.add(informacjaLabel, java.awt.BorderLayout.WEST);

        zmienianyLabel.setFont(zmienianyLabel.getFont().deriveFont(zmienianyLabel.getFont().getSize()-3f));
        zmienianyLabel.setText("start");
        jPanel28.add(zmienianyLabel, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel28);

        fileMenu.setText("Plik");

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setText("Nowy");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setText("Otwórz ...");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setText("Zapisz");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setText("Zapisz jako ...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(jSeparator2);

        importGPXMenuItem.setText("Import GPX");
        importGPXMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importGPXMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(importGPXMenuItem);

        importOziMenuItem.setText("Import Ozi WPT");
        importOziMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importOziMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(importOziMenuItem);
        fileMenu.add(jSeparator1);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setText("Zakończ");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        toolsMenu.setText("Narzędzia");

        jMenu1.setText("Zaawansowane");

        jMI_sort.setText("Sortowanie punktów");
        jMI_sort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_sortActionPerformed(evt);
            }
        });
        jMenu1.add(jMI_sort);

        jMI_initkol.setText("Inicjalizowanie kolejności");
        jMI_initkol.setActionCommand("Inicjowanie kolejności");
        jMI_initkol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_initkolActionPerformed(evt);
            }
        });
        jMenu1.add(jMI_initkol);

        toolsMenu.add(jMenu1);

        jMI_stat.setText("Statystyka");
        jMI_stat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_statActionPerformed(evt);
            }
        });
        toolsMenu.add(jMI_stat);

        menuBar.add(toolsMenu);

        helpMenu.setText("Pomoc");

        jMI_minihelp.setText("Minipomoc");
        jMI_minihelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_minihelpActionPerformed(evt);
            }
        });
        helpMenu.add(jMI_minihelp);

        aboutMenuItem.setText("O AOS");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void importOziMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importOziMenuItemActionPerformed
        JFileChooser fc=new JFileChooser(System.getProperty("user.dir"));
        ExampleFileFilter filter = new ExampleFileFilter("wpt", "Pliki Ozi WPT");
        fc.addChoosableFileFilter(filter);
        int fd = fc.showOpenDialog(this);
        if(fd!=JFileChooser.APPROVE_OPTION) return;
        plik = fc.getSelectedFile().getAbsoluteFile();
        
        OziCzytacz oziczytacz = new OziCzytacz(plik, szlak);
        oziczytacz.wczytaj();
        
        jTF_nazwasz.setText(szlak.nazwa);
        jTP_opissz.setText(szlak.opis);        
        Collections.sort(szlak.punkty);
        jL_punkty.setListData(szlak.punkty);
        Collections.sort(szlak.odcinki);
        jL_odcinki.setListData(szlak.odcinki);
        
        informacja("Zaimportowano punkty z pliku Ozi WPT");
        zmiana_pliku(true);
        
    }//GEN-LAST:event_importOziMenuItemActionPerformed
    
    private void importGPXMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importGPXMenuItemActionPerformed
        JFileChooser fc=new JFileChooser(System.getProperty("user.dir"));
        ExampleFileFilter filter = new ExampleFileFilter("gpx", "Pliki PGX");
        fc.addChoosableFileFilter(filter);
        int fd = fc.showOpenDialog(this);
        if(fd!=JFileChooser.APPROVE_OPTION) return;
        plik = fc.getSelectedFile().getAbsoluteFile();

        GPXCzytacz gpxczytacz = new GPXCzytacz(plik, szlak);

        jTF_nazwasz.setText(szlak.nazwa);
        jTP_opissz.setText(szlak.opis);

        Collections.sort(szlak.punkty);
        jL_punkty.setListData(szlak.punkty);
        Collections.sort(szlak.odcinki);
        jL_odcinki.setListData(szlak.odcinki);

        informacja("Zaimportowano punkty z pliku GPX");
        zmiana_pliku(true);

    }//GEN-LAST:event_importGPXMenuItemActionPerformed
    
    private void jMI_minihelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_minihelpActionPerformed
        String minihelp =
                "Mamy nadzieję, że korzystanie z aplikacji jest dosyć intuicyjne.\n"+
                "W tej minipomocy chcemy zwrócić uwagę tylko na najważniejsze \n"+
                "zasady programu. Pełną dokumentacje można zaleźć na stronie \n"+
                "http://kajak.org.pl/splywww/ \n\n"+
                ":: W jednym punkcie może być kilka typów obiektów, np. jaz i przenoska.\n"+
                ":: Etykieta punktu ma tylko znaczenie pomocnicze.\n"+
                ":: Współrzędne wprowadzamy w WGS84, np. 51.86364 20.28682\n"+
                ":: Punkty będą wyświetlane zgodnie z ustaloną tutaj kolejnością.\n"+
                ":: Przypisywanie punktów do odcinka odbywa się poprzez zaznaczenie\n"+
                "    punktów w liście po prawej stronie. Do zaznaczenia wielu punktów\n"+
                "    używaj klawiszy <Shift> i <Ctrl> razem z kursorem myszy.\n"+
                ":: Punkt może należeć tylko do jednego odcinka jednocześnie.\n"+
                ":: Nie ma opcji 'Cofnij zmiany', pracuj uważnie i rób kopie zapasowe.\n\n"+
                "Miłej pracy życzą twórcy AOS.";
        JOptionPane.showMessageDialog(this, minihelp,
                "Minipomoc AOS", JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_jMI_minihelpActionPerformed
    
    private void zamkniecie_okna(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_zamkniecie_okna
        koniec_programu();
    }//GEN-LAST:event_zamkniecie_okna
    
    private void jMI_initkolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_initkolActionPerformed
        
        for ( int i=0; i< szlak.punkty.size(); i++ ){
            szlak.punkty.get(i).kolejnosc = Double.valueOf(i);
        }
        Collections.sort(szlak.punkty);
        jL_punkty.setListData(szlak.punkty);
        
        for ( int i=0; i< szlak.odcinki.size(); i++ ){
            szlak.odcinki.get(i).kolejnosc = Double.valueOf(i);
        }
        Collections.sort(szlak.odcinki);
        jL_odcinki.setListData(szlak.odcinki);
        
        informacja("Kolejność zainicjowana");
        zmiana_pliku(true);
        
    }//GEN-LAST:event_jMI_initkolActionPerformed
    
    private void jMI_statActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_statActionPerformed
        JOptionPane.showMessageDialog(this,
                "Statystyka szlaku "+szlak.nazwa+" ("+szlak.wersja+" wersja)\n\n"+
                "Liczba punktów:  "+szlak.punkty.size()+"\n"+
                "Liczba odcinków: "+szlak.odcinki.size(),
                "Statystyka", JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_jMI_statActionPerformed
    
    private void jB_owdolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_owdolActionPerformed
        zapamietaj_odcinek();
        if(zaznaczony_odcinek < szlak.odcinki.size()-1){
            Double tmp = szlak.odcinki.get(zaznaczony_odcinek).kolejnosc;
            szlak.odcinki.get(zaznaczony_odcinek).kolejnosc =
                    szlak.odcinki.get(zaznaczony_odcinek+1).kolejnosc;
            szlak.odcinki.get(zaznaczony_odcinek+1).kolejnosc = tmp;
            
            int zaznacz_o = zaznaczony_odcinek+1;
            zaznaczony_odcinek = -1;
            Collections.sort(szlak.odcinki);
            jL_odcinki.setListData(szlak.odcinki);
            jL_odcinki.setSelectedIndex(zaznacz_o);
            informacja("");
            zmiana_pliku(true);
        }
    }//GEN-LAST:event_jB_owdolActionPerformed
    
    private void jB_pwdolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_pwdolActionPerformed
        zapamietaj_opisp();
        zapamietaj_punkt();
        if(zaznaczony_punkt < szlak.punkty.size()-1){
            Double tmp = szlak.punkty.get(zaznaczony_punkt).kolejnosc;
            szlak.punkty.get(zaznaczony_punkt).kolejnosc =
                    szlak.punkty.get(zaznaczony_punkt+1).kolejnosc;
            szlak.punkty.get(zaznaczony_punkt+1).kolejnosc = tmp;
            
            int zaznacz_p = zaznaczony_punkt+1;
            zaznaczony_punkt = -1;
            Collections.sort(szlak.punkty);
            jL_punkty.setListData(szlak.punkty);
            jL_punkty.setSelectedIndex(zaznacz_p);
            
            zmiana_pliku(true);
            informacja("");
        }
    }//GEN-LAST:event_jB_pwdolActionPerformed
    
    private void jB_pwgoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_pwgoreActionPerformed
        zapamietaj_opisp();
        zapamietaj_punkt();
        if(zaznaczony_punkt > 0){
            Double tmp = szlak.punkty.get(zaznaczony_punkt).kolejnosc;
            szlak.punkty.get(zaznaczony_punkt).kolejnosc =
                    szlak.punkty.get(zaznaczony_punkt-1).kolejnosc;
            szlak.punkty.get(zaznaczony_punkt-1).kolejnosc = tmp;
            
            int zaznacz_p = zaznaczony_punkt-1;
            zaznaczony_punkt = -1;
            Collections.sort(szlak.punkty);
            jL_punkty.setListData(szlak.punkty);
            jL_punkty.setSelectedIndex(zaznacz_p);
            
            zmiana_pliku(true);
            informacja("");
            
        }
    }//GEN-LAST:event_jB_pwgoreActionPerformed
    
    private void jB_owgoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_owgoreActionPerformed
        zapamietaj_odcinek();
        if(zaznaczony_odcinek > 0){
            Double tmp = szlak.odcinki.get(zaznaczony_odcinek).kolejnosc;
            szlak.odcinki.get(zaznaczony_odcinek).kolejnosc =
                    szlak.odcinki.get(zaznaczony_odcinek-1).kolejnosc;
            szlak.odcinki.get(zaznaczony_odcinek-1).kolejnosc = tmp;
            
            int zaznacz_o = zaznaczony_odcinek-1;
            zaznaczony_odcinek = -1;
            Collections.sort(szlak.odcinki);
            jL_odcinki.setListData(szlak.odcinki);
            jL_odcinki.setSelectedIndex(zaznacz_o);
            
            zmiana_pliku(true);
            informacja("");
        }
    }//GEN-LAST:event_jB_owgoreActionPerformed
    
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        JOptionPane.showMessageDialog(this,
                "AOS, czyli Aplikacja Opiekuna Szlaku to program \n"+
                "mający na celu ułatwić wprowadzanie i aktualizacje \n"+
                "opisów szlaków kajakowych w bazie SplyWWW. \n\n" +
                "AOS wersja "+wersja+".\n\n"+
                "Więcej infomacji na stronie:\n  http://kajak.org.pl/splywww",
                "O AOS", JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_aboutMenuItemActionPerformed
    
    private void jMI_sortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_sortActionPerformed
        int odp = JOptionPane.showConfirmDialog(this,
                "Sortowanie po ID, może wprowadzić bałagan. Jesteś pewien?");
        if( odp == JOptionPane.OK_OPTION) {
            Vector<Integer> lp = new Vector<Integer>();
            for ( int i=0; i< szlak.punkty.size(); i++ ){
                lp.add( Integer.parseInt(szlak.punkty.get(i).id) );
            }
            
            Collections.sort(lp);
            for ( int i=0; i< lp.size(); i++ ){
                for ( int j=0; j< szlak.punkty.size(); j++ ){
                    if(Integer.parseInt(szlak.punkty.get(j).id) == lp.get(i)){
                        szlak.punkty.get(j).kolejnosc = Double.valueOf(i);
                    }
                }
            }
            
            Collections.sort(szlak.punkty);
            jL_punkty.setListData(szlak.punkty);
            
            informacja("Punkty posortowane po ID");
            zmiana_pliku(true);
        }
    }//GEN-LAST:event_jMI_sortActionPerformed
    
    private void jCB_truActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCB_truActionPerformed
        String typ = (String)jCB_tru.getSelectedItem();
        if(zaznaczony_odcinek != -1){
            jL_opisyp.setSelectedIndex(0);
        }
        jCB_typp.setSelectedItem(typ);
    }//GEN-LAST:event_jCB_truActionPerformed
    
    private void jB_usunoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_usunoActionPerformed
        
        if( zaznaczony_odcinek == -1) {
            JOptionPane.showMessageDialog(this, "Nie wybrałeś odcinka!");
            return ;
        }
        
        int odp = JOptionPane.showConfirmDialog(this, "Czy jesteś pewień, że chcesz skasować?");
        if( odp == JOptionPane.OK_OPTION) {
            szlak.odcinki.remove(zaznaczony_odcinek);
            zaznaczony_odcinek = -1;
            jL_odcinki.setListData(szlak.odcinki);
            informacja("Odcinek skasowany");
            zmiana_pliku(true);
        }
        
    }//GEN-LAST:event_jB_usunoActionPerformed
    
    private void jL_odcinkiValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jL_odcinkiValueChanged
        
        if(zaznaczony_odcinek != -1){
            zapamietaj_odcinek();
        }
        
        if(jL_odcinki.isSelectionEmpty()){
            zaznaczony_odcinek = -1;
            //wyczysc_odcinek();
            edytowalny_odcinek(false);
        } else {
            zaznaczony_odcinek = jL_odcinki.getSelectedIndex();
            odswiez_odcinek();
            edytowalny_odcinek(true);
            
        }
        
//        jL_odcinki.setListData(szlak.odcinki);
//        jL_odcinki.setSelectedIndex(zaznaczony_odcinek);
//        informacja("");
    }//GEN-LAST:event_jL_odcinkiValueChanged
    
    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        
        zapisz_dane();
        informacja("");
        
    }//GEN-LAST:event_jTabbedPane1StateChanged
        
    private void jB_usunopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_usunopActionPerformed
        if( zaznaczony_opisp == -1 ) {
            JOptionPane.showMessageDialog(this, "Nie wybrano opisu!");
            return ;
        }
        
        if(szlak.punkty.get(zaznaczony_punkt).opisypunktu.size() == 1){
            JOptionPane.showMessageDialog(this, "Punkt musi mieć conajmniej jeden typ!");
            return ;
        }
        
        int odp = JOptionPane.showConfirmDialog(this, "Czy jesteś pewień, że chcesz skasować?");
        if( odp == JOptionPane.OK_OPTION) {
            szlak.punkty.get(zaznaczony_punkt).opisypunktu.remove(zaznaczony_opisp);
            zaznaczony_opisp = -1;
            jL_opisyp.setListData(szlak.punkty.get(zaznaczony_punkt).opisypunktu);
            informacja("Opis punktu skasowany");
            zmiana_pliku(true);
        }
    }//GEN-LAST:event_jB_usunopActionPerformed
    
    private void jL_punktyValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jL_punktyValueChanged

        zapamietaj_opisp();
        zaznaczony_opisp = -1;
        zapamietaj_punkt();
        
        if(jL_punkty.isSelectionEmpty()){
            // wyczysc_punkt();
            zaznaczony_punkt = -1;
            edytowalny_punkt(false);
        } else {
            zaznaczony_punkt = jL_punkty.getSelectedIndex();
            odswiez_punkt();
            jL_opisyp.setSelectedIndex(0);
            edytowalny_punkt(true);
        }
        informacja("");
    }//GEN-LAST:event_jL_punktyValueChanged
    
    private void jL_opisypValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jL_opisypValueChanged
        
        if(zaznaczony_opisp != -1){
            zapamietaj_opisp();
        }
        
        if(jL_opisyp.isSelectionEmpty()){
            zaznaczony_opisp = -1;
        } else {
            zaznaczony_opisp = jL_opisyp.getSelectedIndex();
            odswiez_opisp();
        }
        informacja("");
    }//GEN-LAST:event_jL_opisypValueChanged
    
    private void jB_usunpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_usunpActionPerformed
        
        if( zaznaczony_punkt == -1) {
            JOptionPane.showMessageDialog(this, "Nie wybrałeś punktu!");
            return ;
        }
        
        int odp = JOptionPane.showConfirmDialog(this, "Czy jesteś pewień, że chcesz skasować?");
        if( odp == JOptionPane.OK_OPTION) {
            szlak.punkty.remove(zaznaczony_punkt);
            zaznaczony_punkt = -1;
            jL_punkty.setListData(szlak.punkty);
            informacja("Punkt skasowany");
            zmiana_pliku(true);
            
        }
        
    }//GEN-LAST:event_jB_usunpActionPerformed
    
    private void jB_nowyoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_nowyoActionPerformed
        nowy_odcinek();
//      wyczysc_odcinek();
        informacja("Dodano nowy odcinek");
    }//GEN-LAST:event_jB_nowyoActionPerformed
    
    private void jB_nowyopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_nowyopActionPerformed
        nowy_opisp();
        informacja("Dodano nowy opis punktu");
    }//GEN-LAST:event_jB_nowyopActionPerformed
    
    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        wybierz_plik();
        zapisz_plik();
    }//GEN-LAST:event_saveAsMenuItemActionPerformed
    
    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        boolean ok = false;
        
        if(zmieniany_plik){
            int odp = JOptionPane.showConfirmDialog(this, "Chcesz stracić zmiany?");
            if( odp == JOptionPane.OK_OPTION) {
                ok = true;
            }
        } else {
            ok = true;
        }
        
        if(ok){
            szlak = new Szlak();
            wyczysc_wszystko();
            
            informacja("Nowy szlak");
            zmiana_pliku(false);
        }
    }//GEN-LAST:event_newMenuItemActionPerformed
    
    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        if( plik == null) {
            wybierz_plik();
        }
        zapisz_plik();
    }//GEN-LAST:event_saveMenuItemActionPerformed
    
    private void jB_nowypActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_nowypActionPerformed
        nowy_punkt();
        informacja("Dodano nowy punkt");
    }//GEN-LAST:event_jB_nowypActionPerformed
    
    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        boolean ok = false;
        
        if(zmieniany_plik){
            int odp = JOptionPane.showConfirmDialog(this, "Chcesz stracić zmiany?");
            if( odp == JOptionPane.OK_OPTION) {
                ok = true;
            }
        } else {
            ok = true;
        }
        
        if(ok){
            JFileChooser fc=new JFileChooser(System.getProperty("user.dir"));
            ExampleFileFilter filter = new ExampleFileFilter("xml", "Pliki XML");
            fc.addChoosableFileFilter(filter);
            int fd = fc.showOpenDialog(this);
            if(fd!=JFileChooser.APPROVE_OPTION) return;
            plik = fc.getSelectedFile().getAbsoluteFile();
            
            szlak = new Szlak();
            wyczysc_wszystko();
            
            XMLCzytacz xmlczytacz = new XMLCzytacz(plik, szlak);
            
            jTF_nazwasz.setText(szlak.nazwa);
            jTP_opissz.setText(szlak.opis);
            
            Collections.sort(szlak.punkty);
            jL_punkty.setListData(szlak.punkty);
            Collections.sort(szlak.odcinki);
            jL_odcinki.setListData(szlak.odcinki);
            
            informacja("Plik został wczytany");
            zmiana_pliku(false);
            
        }
    }//GEN-LAST:event_openMenuItemActionPerformed
    
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        koniec_programu();
    }//GEN-LAST:event_exitMenuItemActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public @Override void run() {
                try {
                    String systemlaf = new String(UIManager.getSystemLookAndFeelClassName());
//                    System.out.println(systemlaf);
//                    if( systemlaf.indexOf("windows") > -1){
                        UIManager.setLookAndFeel(systemlaf);
//                    }
                } catch (Exception e) {}
                
                new AOSgui().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem importGPXMenuItem;
    private javax.swing.JMenuItem importOziMenuItem;
    private javax.swing.JLabel informacjaLabel;
    private javax.swing.JButton jB_nowyo;
    private javax.swing.JButton jB_nowyop;
    private javax.swing.JButton jB_nowyp;
    private javax.swing.JButton jB_owdol;
    private javax.swing.JButton jB_owgore;
    private javax.swing.JButton jB_pwdol;
    private javax.swing.JButton jB_pwgore;
    private javax.swing.JButton jB_usuno;
    private javax.swing.JButton jB_usunop;
    private javax.swing.JButton jB_usunp;
    private javax.swing.JComboBox jCB_czy;
    private javax.swing.JComboBox jCB_mal;
    private javax.swing.JComboBox jCB_tru;
    private javax.swing.JComboBox jCB_typo;
    private javax.swing.JComboBox jCB_typp;
    private javax.swing.JComboBox jCB_uci;
    private javax.swing.JLabel jL_etykieta;
    private javax.swing.JLabel jL_idp;
    private javax.swing.JLabel jL_km;
    private javax.swing.JLabel jL_miejscowosc;
    private javax.swing.JList jL_odcinki;
    private javax.swing.JList jL_opisyp;
    private javax.swing.JList jL_punkty;
    private javax.swing.JList jL_punktyo;
    private javax.swing.JLabel jL_wsp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMI_initkol;
    private javax.swing.JMenuItem jMI_minihelp;
    private javax.swing.JMenuItem jMI_sort;
    private javax.swing.JMenuItem jMI_stat;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JSplitPane jSplitPane6;
    private javax.swing.JTextField jTF_etykieta;
    private javax.swing.JTextField jTF_km;
    private javax.swing.JTextField jTF_miejscowosc;
    private javax.swing.JTextField jTF_nazwao;
    private javax.swing.JTextField jTF_nazwasz;
    private javax.swing.JTextField jTF_wsp;
    private javax.swing.JTextPane jTP_opiso;
    private javax.swing.JTextPane jTP_opisp;
    private javax.swing.JTextPane jTP_opissz;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JPanel odcinkiPanel;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JPanel punktyPanel;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JPanel szlakPanel;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JLabel zmienianyLabel;
    // End of variables declaration//GEN-END:variables
    
}
