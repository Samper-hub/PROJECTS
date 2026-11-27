package sami.adakouj;

import java.io.File;
import java.util.ArrayList;

public class Gestor {
    public static void main(String[] args){
        if (args.length != 2) {
            System.err.println("Error: Se necesitan 2 argumentos palabra y ruta");
            return;
        }

        String clase ="sami.adakouj.Buscador";
        String palabra=args[0];
        String rutaBuscar=args[1];
        File carpeta=new File(rutaBuscar);

        if (!carpeta.exists()){
            throw new IllegalArgumentException("Error. La ruta no existe");
        }

        File[] archivosTxt = carpeta.listFiles((dir, nombre) -> nombre.toLowerCase().endsWith(".txt"));

        if (archivosTxt==null || archivosTxt.length==0){
            System.out.println("No se encontraron archivos .txt en la carpeta especificada.");
            return;
        }

        File informe =new File("informe.txt");
        File errores=new File("errores.log");

        ArrayList<Process> procesos=new ArrayList<>();

        System.out.println("Búsqueda de la palabra "+palabra+" en "+archivosTxt.length+" archivos.");

        for (File archivo : archivosTxt){
            try {
                ProcessBuilder pb=new ProcessBuilder("java","-cp",".",clase,
                        palabra,archivo.getAbsolutePath());

                pb.redirectOutput(ProcessBuilder.Redirect.appendTo(informe));
                pb.redirectError(ProcessBuilder.Redirect.appendTo(errores));

                Process process= pb.start();
                procesos.add(process);
            } catch (Exception e) {
                System.out.println("Error al lanzar proceso para el archivo "+archivo.getName());
            }
        }

        for (Process p:procesos){
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                System.out.println("El proceso fue interrumpido.");
                e.printStackTrace();
            }
        }
        System.out.println("Busqueda hecha. Resultados guardados en informe.txt y posibles errores en errores.log.");
    }
}
