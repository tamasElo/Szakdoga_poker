package hu.szakdolgozat.poker.vezerloOsztalyok;

import hu.szakdolgozat.poker.alapOsztalyok.Menupont;
import hu.szakdolgozat.poker.felulet.KepernyoKezelo;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.ls.LSOutput;
import java.awt.DisplayMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class AdatKezelo {

    private static Document doc;
    private static int i;
    public static final File GRAFIKA = new File("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/grafika.xml");
    public static final File AUDIO = new File("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/audio.xml");
    public static final File JATEKMENET = new File("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/jatekmenet.xml");
    public static final File JATEK_MENU_PANEL = new File("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/jatekmenu_panel.xml");
    public static final File Proba = new File("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/menupontok.xml");

    private AdatKezelo() {
    }

    /**
     * Létre hoz egy úgy Document példányt.
     */
    private static void dokumentumLetrehozas(File eleresiUt) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            if (eleresiUt == null) {
                doc = db.newDocument();
            } else {
                doc = db.parse(eleresiUt);
            }            
        } catch (ParserConfigurationException pce) {
            JOptionPane.showMessageDialog(null, pce.getMessage());
        } catch (SAXException | IOException ex) {
            Logger.getLogger(AdatKezelo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Kiírja fájlba a grafikus beállítások paramétereit.
     *
     * @param kepernyoMod
     * @param kepernyoAllapot
     * @param elsimitas
     */
    public static void grafikaBeallitasMent(DisplayMode kepernyoMod, byte kepernyoAllapot, boolean elsimitas) {
        dokumentumLetrehozas(null);

        Element rootEle = doc.createElement("Grafika");
        doc.appendChild(rootEle);
        Element elem;
        Text text;

        elem = doc.createElement("Vertikalis");
        text = doc.createTextNode(String.valueOf(kepernyoMod.getWidth()));
        elem.appendChild(text);
        rootEle.appendChild(elem);
        elem = doc.createElement("Horizontalis");
        text = doc.createTextNode(String.valueOf(kepernyoMod.getHeight()));
        elem.appendChild(text);
        rootEle.appendChild(elem);
        elem = doc.createElement("Szinmelyseg");
        text = doc.createTextNode(String.valueOf(kepernyoMod.getBitDepth()));
        elem.appendChild(text);
        rootEle.appendChild(elem);
        elem = doc.createElement("Kepfrissites");
        text = doc.createTextNode(String.valueOf(kepernyoMod.getRefreshRate()));
        elem.appendChild(text);
        rootEle.appendChild(elem);
        elem = doc.createElement("KepernyoAllapot");
        text = doc.createTextNode(String.valueOf(kepernyoAllapot));
        elem.appendChild(text);
        rootEle.appendChild(elem);
        elem = doc.createElement("Elsimitas");
        text = doc.createTextNode(String.valueOf(elsimitas));
        elem.appendChild(text);
        rootEle.appendChild(elem);

        fajlbaKiiras(GRAFIKA);
    }

    /**
     * Kiírja fájlba az audió beállítások paramétereit
     *
     * @param menuZene
     * @param hangok
     */
    public static void audioBeallitasMent(boolean menuZene, boolean hangok) {
        dokumentumLetrehozas(null);

        Element rootEle = doc.createElement("Audio");
        doc.appendChild(rootEle);
        Element elem;
        Text text;

        elem = doc.createElement("MenuZene");
        text = doc.createTextNode(String.valueOf(menuZene));
        elem.appendChild(text);
        rootEle.appendChild(elem);
        elem = doc.createElement("Hangok");
        text = doc.createTextNode(String.valueOf(hangok));
        elem.appendChild(text);
        rootEle.appendChild(elem);

        fajlbaKiiras(AUDIO);
    }

    /**
     * Kiírja fájlba a játékmenet beállítások paramétereit.
     *
     * @param emberJatekosNev
     * @param jatekosokSzama
     * @param zsetonOsszeg
     * @param nagyVakErtek
     * @param vakErtekEmeles
     */
    public static void jatekmenetBeallitasMent(String emberJatekosNev, byte jatekosokSzama,
            int zsetonOsszeg, byte nagyVakErtek, byte vakErtekEmeles) {
        dokumentumLetrehozas(null);

        Element rootEle = doc.createElement("Jatekmenet");
        doc.appendChild(rootEle);
        Element elem;
        Text text;

        elem = doc.createElement("nev");
        text = doc.createTextNode(String.valueOf(emberJatekosNev));
        elem.appendChild(text);
        rootEle.appendChild(elem);
        elem = doc.createElement("jatekosokSzama");
        text = doc.createTextNode(String.valueOf(jatekosokSzama));
        elem.appendChild(text);
        rootEle.appendChild(elem);
        elem = doc.createElement("osszeg");
        text = doc.createTextNode(String.valueOf(zsetonOsszeg));
        elem.appendChild(text);
        rootEle.appendChild(elem);
        elem = doc.createElement("nagyvakErtek");
        text = doc.createTextNode(String.valueOf(nagyVakErtek));
        elem.appendChild(text);
        rootEle.appendChild(elem);
        elem = doc.createElement("vakErtekEmeles");
        text = doc.createTextNode(String.valueOf(vakErtekEmeles));
        elem.appendChild(text);
        rootEle.appendChild(elem);

        fajlbaKiiras(JATEKMENET);
    }

    /**
     * Kiírja a paraméterként átadott xml fájlba az adatokat.
     * 
     * @param file 
     */
    private static void fajlbaKiiras(File file) {
        try {
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();

            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");

            LSSerializer writer = impl.createLSSerializer();
            LSOutput output = impl.createLSOutput();

            output.setByteStream(new FileOutputStream(file));
            writer.write(doc, output);
        } catch (IOException | IllegalAccessException | InstantiationException | ClassNotFoundException ie) {
            JOptionPane.showMessageDialog(null, ie.getMessage());
        }
    }

    /**
     * Beolvassa fájlból a elérési útként megadott beállítások paramétereit.
     *
     * @param eleresiUt
     * @return
     */
    public static List<String> beallitasBetolt(File eleresiUt) {
        dokumentumLetrehozas(eleresiUt);
        
        ArrayList<String> adatok = new ArrayList<>();
        Element docEle = doc.getDocumentElement();
        NodeList nl = docEle.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            adatok.add(nl.item(i).getTextContent());
        }

        return adatok;
    }
    
    public static HashMap<String, List<Double>> aranyErtekekBetolt(File eleresiUt, String elemNev, Dimension felbontas) {
        dokumentumLetrehozas(eleresiUt);
        List<Double> ertekek;
        HashMap<String,List<Double>> adatok = new HashMap<>();
        double szelesseg = felbontas.getWidth(), magassag = felbontas.getHeight();
        double ertek, aranyErtek;
        Element docEle = doc.getDocumentElement();         
        Element elem;
        NodeList nl = docEle.getElementsByTagName(elemNev);
        NodeList nl2;
        nl = nl.item(0).getChildNodes();
        nl = nl.item(KepernyoKezelo.keparanySzamit(felbontas)).getChildNodes();

        for (int i = 0; i < nl.getLength(); i++) {
            ertekek = new ArrayList<>();
            nl2 =  nl.item(i).getChildNodes();
            elemNev = nl.item(i).getNodeName();
            
            for (int j = 0; j < nl2.getLength(); j++) {
               elem = (Element) nl2.item(j);
               ertek = Double.parseDouble(nl2.item(j).getTextContent());
               
                if (elem.getAttribute("Arany").equals("szelesseg")) {
                    aranyErtek = szelesseg / ertek;
                } else {
                    aranyErtek = magassag / ertek;
                }
                
                ertekek.add(aranyErtek);
            }
            
            adatok.put(elemNev, ertekek);
        }

        return adatok;
    }

    public static void ideiglenesCucc(List<JComponent> komponensek, double szelesseg, double magassag) {
        dokumentumLetrehozas(null);

        Element rootEle = doc.createElement("JatekMenuPanel");
        Element elem, elem2, elem3, elem4;
        Text text;

        doc.appendChild(rootEle);
        elem = doc.createElement("Menupontok");
        elem2 = doc.createElement("Normal");
        rootEle.appendChild(elem);
        elem.appendChild(elem2);

        for (int i = 0; i < komponensek.size(); i++) {
            JComponent comp = komponensek.get(i);
            elem3 = doc.createElement(comp.getName());
            elem4 = doc.createElement("X");
            elem4.setAttribute("Arany", "szelesseg");
            text = doc.createTextNode(String.valueOf(szelesseg / comp.getX()));
            elem4.appendChild(text);
            elem3.appendChild(elem4);
            elem4 = doc.createElement("Y");
            elem4.setAttribute("Arany", "magassag");
            text = doc.createTextNode(String.valueOf(magassag / comp.getY()));
            elem4.appendChild(text);
            elem3.appendChild(elem4);
            elem4 = doc.createElement("betuMeret");
            elem4.setAttribute("Arany", "magassag");
            //text = doc.createTextNode(String.valueOf(magassag / comp.getSzovegMagassag()));
            elem4.appendChild(text);
            elem3.appendChild(elem4);
            elem2.appendChild(elem3);
        }
        
        fajlbaKiiras(new File("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/proba"+ i++ +".xml"));
    }
}
