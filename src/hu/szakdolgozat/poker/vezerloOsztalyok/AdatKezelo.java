package hu.szakdolgozat.poker.vezerloOsztalyok;

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
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class AdatKezelo {

    private static Document doc;
    public static final File GRAFIKA = new File("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/grafika.xml");
    public static final File AUDIO = new File("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/audio.xml");
    public static final File JATEKMENET = new File("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/jatekmenet.xml");

    private AdatKezelo() {
    }

    /**
     * Létre hoz egy úgy Document példányt.
     */
    private static void dokumentumLetrehozas() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch (ParserConfigurationException pce) {
            JOptionPane.showMessageDialog(null, pce.getMessage());
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
        dokumentumLetrehozas();

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
        dokumentumLetrehozas();

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
        dokumentumLetrehozas();

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
        List<String> adatok = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(eleresiUt);
        } catch (ParserConfigurationException | SAXException | IOException pce) {
            JOptionPane.showMessageDialog(null, pce.getMessage());
        }

        Element docEle = doc.getDocumentElement();
        NodeList nl = docEle.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            adatok.add(nl.item(i).getTextContent());
        }

        return adatok;
    }
    
    public static List<String> szelessegBetolt(File eleresiUt) {
        List<String> adatok = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(eleresiUt);
        } catch (ParserConfigurationException | SAXException | IOException pce) {
            JOptionPane.showMessageDialog(null, pce.getMessage());
        }

        Element docEle = doc.getDocumentElement();
        NodeList nl = docEle.getChildNodes();
        NodeList nl2;
        for (int i = 0; i < nl.getLength(); i++) {
            nl2 =  nl.item(i).getChildNodes();
            
            for (int j = 0; j < nl2.getLength(); j++) {
                adatok.add(nl2.item(j).getTextContent());
            }
        }

        return adatok;
    }
    
    public static void ideiglenesCucc(List<JComponent> komponensek, double szelesseg){
         dokumentumLetrehozas();

        Element rootEle = doc.createElement("Komponensek");
        doc.appendChild(rootEle);
        Element elem, elem2;
        Text text;
        for (JComponent comp : komponensek) {
            elem = doc.createElement(comp.getName());         
            elem2 = doc.createElement("X");
            text = doc.createTextNode(String.valueOf(szelesseg / comp.getBounds().getX()));
            elem2.appendChild(text);
            elem.appendChild(elem2);
            elem2 = doc.createElement("Y");
            text = doc.createTextNode(String.valueOf(szelesseg / comp.getBounds().getY()));
            elem2.appendChild(text);
            elem.appendChild(elem2);
            elem2 = doc.createElement("Szelesseg");
            text = doc.createTextNode(String.valueOf(szelesseg / comp.getBounds().getWidth()));
            elem2.appendChild(text);
            elem.appendChild(elem2);
            elem2 = doc.createElement("Magassag");
            text = doc.createTextNode(String.valueOf(szelesseg / comp.getBounds().getHeight()));
            elem2.appendChild(text);
            elem.appendChild(elem2);
            rootEle.appendChild(elem);
        }
        fajlbaKiiras(new File("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/proba.xml"));
    }
}
