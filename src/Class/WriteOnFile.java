package Class;

import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.FileWriter;
import java.io.IOException; 
public class WriteOnFile
{ 
    private File fichero; 
    private FileWriter pw; 
   public  WriteOnFile(String rfichero,String cadena) throws IOException 
   { 
     try { 
         fichero = new File(rfichero); 
         pw = new FileWriter(fichero,true); 
         println(cadena);
         cierra();
    } catch (FileNotFoundException e) {
        e.printStackTrace(); 
    } 
   } 
   public synchronized void println(String cadena) throws IOException 
   { 
       pw.write(cadena); 
   } 
    
   public synchronized void cierra() throws IOException 
   { 
       pw.close(); 
   } 
    
} 