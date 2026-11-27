package sami.adakouj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Buscador {
    public static void main(String[] args){
        long tiempoinicio=System.currentTimeMillis();

        if (args.length != 2) { return; }

        String palabra=args[0];
        String rutaArchivo=args[1];
        File archivo=new File(rutaArchivo);

        if (!archivo.exists()){
            throw new IllegalArgumentException("Error. El archivo no existe "+rutaArchivo);
        }

        int contador=0;
        int numlineas=0;

        String regex = "\\b" + Pattern.quote(palabra) + "\\b";
        Pattern patron = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        try (BufferedReader bf=new BufferedReader(new FileReader(archivo))){
            String linea;

            while ((linea=bf.readLine()) !=null){
                numlineas++;

                Matcher matcher=patron.matcher(linea);
                while (matcher.find()){
                    contador++;
                }
            }

            long pid=ProcessHandle.current().pid();

            long tiempoFin=System.currentTimeMillis();
            long tiempototal=tiempoFin-tiempoinicio;

            System.out.println(archivo.getName() + ";" +contador+";"+numlineas+"lineas;"+pid+" process id;"+tiempototal+" ms;");
        } catch (Exception e) {
            System.err.println(archivo.getName()+ "Error al leer");
        }
    }
}
