/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amoeba;

import java.util.Scanner;
import org.apache.log4j.Logger;

/**
 * @author Joti *
 */
public class Amoeba {

  static final String sorindexek    = " A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";
  static final String oszlopindexek = " 1 2 3 4 5 6 7 8 91011121314151617181920212223242526";
  static int nyeroSorozat = 5;
  
  public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);
    Logger logger = Logger.getLogger(Amoeba.class.getName());
    logger.debug("Program indul...");

    boolean kilep;
    boolean helyes;
    String valasz;

    do {
      Tabla tabla = new Tabla();
      tabla.inicializalas();
      logger.debug("Játék indul...");
      tabla.jatek();
      logger.debug("Játék vége");
      
      System.out.println("");
      kilep = false;
      do {
        helyes = true;
        System.out.print("Még egy parti (i/n)?");
        valasz = sc.nextLine();
        if (valasz.equalsIgnoreCase("i"))
          kilep = false;
        else if (valasz.equalsIgnoreCase("n"))
          kilep = true;
        else {
          System.out.println("A választ nem tudom értelmezni.");
          helyes = false;
        }
      } while (!helyes);
      System.out.println("");
    } while (!kilep);
    System.out.println("");
    logger.debug("Kilépés...");

  }

}
