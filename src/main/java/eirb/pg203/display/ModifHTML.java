package eirb.pg203.display;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModifHTML {
    //remplace dans un fichier html situé à filepath l'element ciblé par target par newContent
    public ModifHTML(String filePath, String newContent, String target) {
        String htmlContent = readHtmlFile(filePath);
        if (htmlContent != null) {
            if(target.equals("nbjours-selector")){
                htmlContent = htmlContent.replace(target+"' value='1'/>", target+"' value='"+newContent+"'/>"); 
            }
            else {
                htmlContent = htmlContent.replace(target+"'>", target+ "'>" + newContent ); 
            }
            writeHtmlFile(filePath, htmlContent);
        }
    }


    private static String readHtmlFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            System.out.println("Une erreur est survenue lors de la lecture du fichier HTML : " + e.getMessage());
            return null;
        }
    }

    private static void writeHtmlFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content); 
        } catch (IOException e) {
            System.out.println("Une erreur est survenue lors de l'écriture du fichier HTML : " + e.getMessage());
        }
    }

    public static void resetHtmlFile(String filePath, String fileTemplate) {
        String initialContent = readHtmlFile(fileTemplate); 
        if (initialContent != null) {
            writeHtmlFile(filePath, initialContent);
            //System.out.println("\nLe fichier HTML a été réinitialisé pour actualisation");
        }
    }
}
