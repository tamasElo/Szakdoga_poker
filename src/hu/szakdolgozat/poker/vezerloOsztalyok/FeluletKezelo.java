package hu.szakdolgozat.poker.vezerloOsztalyok;
import hu.szakdolgozat.poker.felulet.JatekMenuPanel;
import hu.szakdolgozat.poker.felulet.JatekterPanel;
import hu.szakdolgozat.poker.felulet.TeljesKepernyo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import javax.swing.JFrame;

public class FeluletKezelo {
    
    private static DisplayMode dm;
    private static JatekMenuPanel jatekMenuPanel;
    private static JatekterPanel jatekterPanel;
    private static SzalVezerlo szalVezerlo;
    private static JFrame pokerFrame;
    private static int szelesseg;
    private static int magassag;
    
    public static void main(String[] args){
        szelesseg = 1600;
        magassag = 1200;
        dm = new DisplayMode(szelesseg, magassag, 32, 60);
        pokerFrame = new JFrame();                
        pokerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pokerFrame.setTitle("Póker");        
        pokerFrame.getContentPane().setPreferredSize(new Dimension(szelesseg, magassag));      
        FeluletKezelo.run(dm, pokerFrame);         
        pokerFrame.setResizable(false);     
        jatekMenuPanelBetolt(); 
        pokerFrame.setLocationRelativeTo(null);   
        pokerFrame.setVisible(true); 
    }

    public static void run(DisplayMode dm, JFrame frame) {

        //Create an Object of FullScreen class
        TeljesKepernyo fs = new TeljesKepernyo();

        //Call method setFullScreen to make the frame Full Screen
        fs.setFullScreen(dm, frame);
        // fs.CloseFullScreen(frame);
    }
    
    public static void jatekMenuPanelBetolt(){
        szalVezerlo = new SzalVezerlo();
        jatekMenuPanel = new JatekMenuPanel();
        jatekMenuPanel.setBackground(Color.black);    
        jatekMenuPanel.setSize(new Dimension(szelesseg, magassag));     
        jatekMenuPanel.setSzalVezerlo(szalVezerlo);
        szalVezerlo.setJatekMenuPanel(jatekMenuPanel);
        
        if (jatekterPanel != null) {
            pokerFrame.remove(jatekterPanel);
        }
        
        pokerFrame.getContentPane().add(BorderLayout.CENTER, jatekMenuPanel);
        pokerFrame.pack();
    }
    
    public static void jatekTerPanelBetolt(){ 
        szalVezerlo = new SzalVezerlo();
        jatekterPanel = new JatekterPanel();
        jatekterPanel.setBackground(Color.black);
        jatekterPanel.setSize(new Dimension(szelesseg, magassag));
        jatekterPanel.setSzalVezerlo(szalVezerlo);
        szalVezerlo.setJatekTerPanel(jatekterPanel);
        pokerFrame.remove(jatekMenuPanel);
        pokerFrame.getContentPane().add(BorderLayout.CENTER, jatekterPanel);
        pokerFrame.pack();
    }
}