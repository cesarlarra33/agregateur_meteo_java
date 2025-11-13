package eirb.pg203.display;

import java.io.File;
import java.io.IOException;
import java.awt.Desktop;


public class OpenHTML {
    public static void openHtmlFile(String filePath) {
        try {
            File htmlFile = new File(filePath);
            if (htmlFile.exists()) {
                Desktop.getDesktop().browse(htmlFile.toURI());
                System.out.println("Fichier HTML ouvert dans le navigateur.");
            } else {
                System.out.println("Le fichier HTML n'existe pas.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

    
}
