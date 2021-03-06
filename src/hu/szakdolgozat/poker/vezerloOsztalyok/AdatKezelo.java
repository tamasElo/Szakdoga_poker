package hu.szakdolgozat.poker.vezerloOsztalyok;

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
import java.awt.DisplayMode;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class AdatKezelo {

    private static Document doc;
    public static final File GRAFIKA = new File("src/hu/szakdolgozat/poker/adatFajlok/mentesek/beallitasok/grafika.xml");
    public static final File AUDIO = new File("src/hu/szakdolgozat/poker/adatFajlok/mentesek/beallitasok/audio.xml");
    public static final File JATEKMENET = new File("src/hu/szakdolgozat/poker/adatFajlok/mentesek/beallitasok/jatekmenet.xml"); 
    private static final File ARANY_ERTEKEK = new File("src/hu/szakdolgozat/poker/adatFajlok/mentesek/beallitasok/arany_ertekek.xml");   
    private static final File JATEK_ALLAS = new File("src/hu/szakdolgozat/poker/adatFajlok/mentesek/jatek/jatek_allas.ser");   

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
            JOptionPane.showMessageDialog(null, ex.getMessage());
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

        xmlFajlbaKiiras(GRAFIKA);
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

        xmlFajlbaKiiras(AUDIO);
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

        xmlFajlbaKiiras(JATEKMENET);
    }

    /**
     * Kiírja a paraméterként átadott xml fájlba az adatokat.
     *
     * @param file
     */
    private static void xmlFajlbaKiiras(File file) {
        try {
            DOMSource domSource = new DOMSource(doc);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult sr = new StreamResult(file);

            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, sr);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(AdatKezelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(AdatKezelo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Beolvassa fájlból a elérési útként megadott beállítások paramétereit.
     *
     * @param eleresiUt
     * @return
     */
    public synchronized static List<String> beallitasBetolt(File eleresiUt) {
        dokumentumLetrehozas(eleresiUt);
        doc.getDocumentElement().normalize();
        
        ArrayList<String> adatok = new ArrayList<>();
        Element docEle = doc.getDocumentElement();
        NodeList nl = docEle.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                adatok.add(nl.item(i).getTextContent());
            }
        }
        
        return adatok;
    }
    
    /**
     * Betölti az arány értékeket tartalmazó xml dokumentumot.
     * 
     * @param elemNev
     * @param felbontas
     * @return 
     */
    public synchronized static Map<String, List<Double>> aranyErtekekBetolt(String elemNev, Dimension felbontas) {
        dokumentumLetrehozas(ARANY_ERTEKEK);
        doc.getDocumentElement().normalize();
        
        List<Double> ertekek;
        Map<String,List<Double>> adatok = new HashMap<>();
        double szelesseg = felbontas.getWidth(), magassag = felbontas.getHeight();
        double ertek, aranyErtek;
        Element docEle = doc.getDocumentElement();         
        Element elem = (Element) docEle.getElementsByTagName(elemNev).item(0);
        NodeList nl = KepernyoKezelo.keparanySzamit(felbontas) == KepernyoKezelo.NORMAL_KEPERNYO
                ? elem.getElementsByTagName("Normal").item(0).getChildNodes() 
                : elem.getElementsByTagName("Szeles").item(0).getChildNodes();
        NodeList nl2;

        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                ertekek = new ArrayList<>();
                nl2 = nl.item(i).getChildNodes();
                elemNev = nl.item(i).getNodeName();

                for (int j = 0; j < nl2.getLength(); j++) {
                    if (nl2.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        elem = (Element) nl2.item(j);
                        ertek = Double.parseDouble(elem.getTextContent());

                        if (elem.getAttribute("Arany").equals("szelesseg")) {
                            aranyErtek = szelesseg / ertek;
                        } else {
                            aranyErtek = magassag / ertek;
                        }

                        ertekek.add(aranyErtek);
                    }
                }

                adatok.put(elemNev, ertekek);
            }
        }

        return adatok;
    }
    
    public static void jatekAllasMent(SzalVezerlo szalVezerlo){        
 
        try {
            FileOutputStream fs;
            fs = new FileOutputStream(JATEK_ALLAS);
            ObjectOutputStream os;

            os = new ObjectOutputStream(fs);
            os.writeObject(szalVezerlo);
            os.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public static SzalVezerlo jatekAllasBetolt(){
        SzalVezerlo szalVezerlo = null;

        try {
            FileInputStream fs = new FileInputStream(JATEK_ALLAS);
            ObjectInputStream os = new ObjectInputStream(fs);
            szalVezerlo = (SzalVezerlo)os.readObject();
            os.close();
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return szalVezerlo;
    }
    
    public static void jatekAllasTorol(){
        JATEK_ALLAS.delete();
    }
    
    public static boolean jatekAllasEllenorzes(){
        return JATEK_ALLAS.exists();
    }
}
