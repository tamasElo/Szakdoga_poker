package vezerloOsztalyok;
import felulet.JatekterPanel;
import felulet.TeljesKepernyo;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import javax.swing.JFrame;

public class FeluletKezelo {
    
    static DisplayMode dm;
    
    public static void main(String[] args){
        int szelesseg = 1024, magassag = 768;
        dm = new DisplayMode(szelesseg, magassag, 32, 60);
        JFrame pokerFrame = new JFrame();
        SzalVezerlo szalVezerlo = new SzalVezerlo();
        JatekterPanel jatekterPanel = new JatekterPanel();
        jatekterPanel.setBackground(Color.black);
        szalVezerlo.setJatekTerPanel(jatekterPanel);
        jatekterPanel.setSzalVezerlo(szalVezerlo);
        
        pokerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pokerFrame.setTitle("Póker");
        pokerFrame.getContentPane().setPreferredSize(new Dimension(szelesseg, magassag));
        pokerFrame.getContentPane().add(BorderLayout.CENTER, jatekterPanel);        
        FeluletKezelo.run(dm, pokerFrame);
        pokerFrame.pack();
        pokerFrame.setResizable(false);
        pokerFrame.setVisible(true);
    }
    
    public static void run(DisplayMode dm, JFrame frame){

          //Create an Object of FullScreen class
		TeljesKepernyo fs=new TeljesKepernyo();

            //Call method setFullScreen to make the frame Full Screen
			fs.setFullScreen(dm, frame);
                       // fs.CloseFullScreen(frame);
	}
}
