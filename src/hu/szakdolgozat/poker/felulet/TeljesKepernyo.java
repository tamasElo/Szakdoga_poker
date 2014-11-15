package hu.szakdolgozat.poker.felulet;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.*;

public class TeljesKepernyo {
    HashSet<DisplayMode> hashset = new HashSet<>();
    GraphicsDevice vc;

    //Initialize the vc with the Screen Device
    public TeljesKepernyo() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();       
       
        vc = ge.getDefaultScreenDevice();
        
  //   Kilistázza az adott megjelenítő lehetséges felbontásait
        DisplayMode[] d = vc.getDisplayModes();
        for (int i = 0; i < d.length; i++) {
            hashset.add(d[i]);
        }
        
        Iterator itr = hashset.iterator();
        while (itr.hasNext()) {
            DisplayMode disp = (DisplayMode) itr.next();
            System.out.println(disp.getWidth() + "*" + disp.getHeight() + " " + disp.getBitDepth() + "bit " + disp.getRefreshRate() + "Hz");
        }
        
        
        /*Monitor felbontás ez:
         System.out.println(vc.getDisplayMode().getWidth()); 
         vagy ez:
         Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
         double width = screenSize.getWidth();
         double height = screenSize.getHeight();
         System.out.println(width);*/
    }

    //Set the Frame to Full Screen
    public void setFullScreen(DisplayMode dm, JFrame win) {

        //Remove the Title Bar, Maximization , Minimization Button...
        win.setUndecorated(true);

        //Can not be resized
        win.setResizable(false);

        //Make the win(JFrame) Full Screen
        vc.setFullScreenWindow(win);
       

        //check low-level display changes are supported for this graphics device.
        if (dm != null && vc.isDisplayChangeSupported()) {
            try {
                vc.setDisplayMode(dm);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    //To Exit From Full Screen
    public void CloseFullScreen(JFrame win) {
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            w.dispose();
        }
        win.setUndecorated(false);
        vc.setFullScreenWindow(null);
    }
}
