package amoeba;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import org.apache.log4j.Logger;

/**
 * @author Joti *
 */
public class Tabla {

  String[][] cellak;
  String[] sorindexek;
  String[] oszlopindexek;
  Jatekos[] jatekosok;
  int kimenetel;

  public Tabla() {
  }

  public void tablaGeneralas(int[] meret) {
    cellak = new String[meret[0]][meret[1]];
    sorindexek = new String[cellak.length];
    oszlopindexek = new String[cellak[0].length];

    for (int sor = 0; sor < cellak.length; sor++) {
      for (int oszlop = 0; oszlop < cellak[0].length; oszlop++) {
        cellak[sor][oszlop] = " ";
      }
      sorindexek[sor] = Amoeba.sorindexek.substring(sor * 2, sor * 2 + 2);
    }
    for (int oszlop = 0; oszlop < cellak[0].length; oszlop++) {
      oszlopindexek[oszlop] = Amoeba.oszlopindexek.substring(oszlop * 2, oszlop * 2 + 2);
    }
  }

  public void vonalRajzolas() {
    System.out.print("   +");
    for (int oszlop = 0; oszlop < oszlopindexek.length; oszlop++) {
      System.out.print("---+");
    }
    System.out.println("");
  }

  public void kiRajzolas(int[] utolsoLepes) {
    kiRajzolas(cellak, utolsoLepes, true);
  }

  public void kiRajzolas(String[][] cellak, int[] utolsoLepes, boolean nyeroJeloles) {
    System.out.println("");
    System.out.print("    ");
    for (int oszlop = 0; oszlop < oszlopindexek.length; oszlop++) {
      System.out.print(oszlopindexek[oszlop] + "  ");
    }
    System.out.println("");

    vonalRajzolas();

    for (int sor = 0; sor < sorindexek.length; sor++) {
      String[] sorcellak = cellak[sor];
      System.out.print(sorindexek[sor] + " |");
      for (int oszlop = 0; oszlop < oszlopindexek.length; oszlop++) {
        if (nyeroJeloles && jatekosok[0].getPozicio(sor, oszlop) > 4 && jatekosok[0].getPozicio(sor, oszlop) < 10) {
          System.out.print("(" + jatekosok[0].getJel() + ")|");
        } else if (nyeroJeloles && jatekosok[1].getPozicio(sor, oszlop) > 4 && jatekosok[1].getPozicio(sor, oszlop) < 10) {
          System.out.print("(" + jatekosok[1].getJel() + ")|");
        } else if (cellak[sor][oszlop].length() == 2) {
          if (sor == utolsoLepes[0] && oszlop == utolsoLepes[1])
            System.out.print(".");
          else
            System.out.print(" ");
          System.out.print(cellak[sor][oszlop] + "|");
        } else {
          if (sor == utolsoLepes[0] && oszlop == utolsoLepes[1])
            System.out.print(".");
          else
            System.out.print(" ");
          System.out.print(cellak[sor][oszlop] + " |");
        }
      }
      System.out.println("");
      vonalRajzolas();
    }
    System.out.println("");
  }

  private void tablaLetrehozas() {
    int[] tablaMeret = new int[2];
    Scanner sc = new Scanner(System.in);
    boolean helyes;

    for (int i = 0; i < 2; i++) {
      do {
        helyes = true;
        if (i == 0) {
          System.out.print("A játéktábla sorainak száma (5-26): ");
        } else {
          System.out.print("A játéktábla oszlopainak száma (5-26): ");
        }

        tablaMeret[i] = sc.nextInt();
        if (tablaMeret[i] < 5 || tablaMeret[i] > 26) {
          System.out.println("Csak 5 és 26 közötti szám adható meg!");
          helyes = false;
        }
      } while (!helyes);
    }
    sc.nextLine();
    tablaGeneralas(tablaMeret);
  }

  private void jatekosLetrehozas() {
    Scanner sc = new Scanner(System.in);

    System.out.println("Kérem, adja meg a játékosok nevét és jelét!");
    System.out.println("(Ha a számítógép ellen kíván játszani, akkor a gépi játékos nevét felkiáltójellel zárja.)");

    jatekosok = new Jatekos[2];

    for (int i = 0; i < jatekosok.length; i++) {
      String nev;
      String jel;
      boolean gep;
      boolean helyes;
      int jatekEro = 0;
      System.out.print((i + 1) + ". játékos neve: ");
      do {
        nev = sc.nextLine();
      } while (nev.isEmpty());
      gep = (nev.substring(nev.length() - 1, nev.length()).equals("!"));
      if (gep) {
        nev = nev.substring(0, nev.length() - 1);
        do {
          helyes = true;
          System.out.print(nev + " játékereje (1-10): ");
          try {
            jatekEro = sc.nextInt();
          } catch (InputMismatchException e) {
            jatekEro = 0;
          }
          sc.nextLine();
          if (jatekEro < 1 || jatekEro > 10) {
            System.out.println("Csak 1 és 10 közötti érték adható meg.");
            helyes = false;
          }
        } while (!helyes);
      }

      do {
        System.out.print(nev + " jele: ");
        helyes = false;
        jel = sc.nextLine();
        if (jel.length() != 1) {
          System.out.println("A jel csak 1 db karakterből állhat.");
        } else if (i == 1 && jel.equals(jatekosok[0].getJel())) {
          System.out.println("A két játékos jele nem egyezhet meg.");
        } else {
          helyes = true;
        }
      } while (!helyes);

      jatekosok[i] = new Jatekos(nev, gep, jatekEro, jel, cellak.length, cellak[0].length);
    }

  }

  public int[] lepesbolCella(String lepes) {
    int[] cella = {-1, -1};
    String[] indexek;
    String lepesIndex;

    if (lepes.length() == 2) {
      lepes = " " + lepes.substring(0, 1) + " " + lepes.substring(1, 2);
    } else if (lepes.length() == 3) {
      lepes = " " + lepes.substring(0, 1) + lepes.substring(1, 3);
    }

    if (lepes.length() == 4) {
      for (int j = 0; j < 2; j++) {
        if (j == 0) {
          indexek = sorindexek;
        } else {
          indexek = oszlopindexek;
        }
        lepesIndex = lepes.substring(2 * j, 2 * j + 2);
        for (int i = 0; i < indexek.length && cella[j] == -1; i++) {
          if (lepesIndex.equalsIgnoreCase(indexek[i])) {
            cella[j] = i;
          }
        }
      }
    }
    return cella;
  }
  
  private int[][] vonalvizsgalat(int[] cella, String jel, String ellenjel, int sorVektor, int oszlopVektor, boolean[] zart, boolean[] lyuk, boolean lehetLyuk) {

    int[] maxSorozat = new int[2];
    int[] sorozat = new int[2];
    lyuk[0] = false;
    lyuk[1] = false;
    for (int i = 0; i < 2; i++) {
      int sor = cella[0];
      int oszlop = cella[1];
      int irany = 2 * i - 1;
      sorozat[i] = 0;
      maxSorozat[i] = 0;
      zart[i] = false;
      boolean tovabb = true;
      while (tovabb) {
        oszlop += irany * oszlopVektor;
        sor += irany * sorVektor;
        if (sor >= 0 && sor < cellak.length && oszlop >= 0 && oszlop < cellak[0].length) {
          //System.out.println("[" + sor + "][" + oszlop + "]: " + cellak[sor][oszlop]);
          if (cellak[sor][oszlop].equals(jel)) {
            if (sorozat[i] == maxSorozat[i] || (lehetLyuk && !lyuk[(i+1)%2] && sorozat[i]+1 == maxSorozat[i])){
              if (sorozat[i]+1 == maxSorozat[i]){
                lyuk[i] = true;
              }
              sorozat[i]++;
            }  
            maxSorozat[i]++;
          } else if (cellak[sor][oszlop].equals(ellenjel)) {
            if (sorozat[i] == maxSorozat[i] || (lyuk[i] && sorozat[i]+1 == maxSorozat[i]))
              zart[i] = true;
            tovabb = false;
          } else { // Üres hely
            maxSorozat[i]++;
          }
        } else {
          if (sorozat[i] == maxSorozat[i] || (lyuk[i] && sorozat[i]+1 == maxSorozat[i]))
            zart[i] = true;
          tovabb = false;
        }
      }
    }

    int[][] sorozatCellak = new int[sorozat[0] + sorozat[1] + 1][2];
//    System.out.println("vektor: " + sorVektor + "," + oszlopVektor + ", sorozat: " + sorozat[0] + "-" + sorozat[1] + ", teljes hossza: " + sorozatCellak.length);
    for (int i = -sorozat[0]; i <= sorozat[1]; i++) {
      sorozatCellak[i + sorozat[0]][0] = cella[0] + sorVektor * i;
      sorozatCellak[i + sorozat[0]][1] = cella[1] + oszlopVektor * i;
//      System.out.println("sorozatcella: [" + sorozatCellak[i + sorozat[0]][0] + "," + sorozatCellak[i + sorozat[0]][1] + "]");
    }

    // Ha már nem jöhet össze ezen a vonalon az 5, akkor mindkét oldalról zártnak tekintjük 
    if (maxSorozat[0] + maxSorozat[1] + 1 < 5) {
      zart[0] = true;
      zart[1] = true;
    }

    return sorozatCellak;
  }
  
  public boolean elemzes(int[] cella, boolean lepesCellaja) {
    // az utolsó lépés pozíciójából indulunk négy irányban
    String jel = cellak[cella[0]][cella[1]];
//    System.out.println("jel: " + jel);
    Jatekos jatekos, ellenfel;
    int aktualisJatekos = 0;
    int sor, oszlop, irany, sorozathossz;
    int[][] sorozatCellak = new int[10][2];
    boolean[] zart = new boolean[2];
    boolean[] lyuk = new boolean[2];
    boolean tovabb;
    int[] ujCella = new int[2];
    int[] poziciok = new int[4];
    int maxPozicio = 0;

    if (jatekosok[0].getJel().equals(jel)) {
      jatekos = jatekosok[0];
      ellenfel = jatekosok[1];
    } else {
      jatekos = jatekosok[1];
      ellenfel = jatekosok[0];
    }
    String ellenjel = ellenfel.getJel();

    // Először a sort nézzük balra és jobbra
    sorozatCellak = vonalvizsgalat(cella, jel, ellenjel, 0, 1, zart, lyuk, !lepesCellaja);
    // A pozíciós táblába beírjuk a meglévő sorozathosszakat
    if (lepesCellaja) {
      jatekos.setSorozatPozicio(sorozatCellak, true);
      ellenfel.setSorozatPozicio(sorozatCellak, false);
      if (sorozatCellak.length >= 5) {
        return true;
      }
    } else {
      // A sorozat hossza és a lezártság alapján pozíciós értéket rendelünk a cellához
      poziciok[0] = jatekos.calcPozicio(cella[0], cella[1], sorozatCellak.length, zart, lyuk[0] || lyuk[1]);
    }

    // Jöhet az oszlopvizsgálat
    sorozatCellak = vonalvizsgalat(cella, jel, ellenjel, 1, 0, zart, lyuk, !lepesCellaja);
    // A pozíciós táblába beírjuk a meglévő sorozathosszakat
    if (lepesCellaja) {
      jatekos.setSorozatPozicio(sorozatCellak, true);
      ellenfel.setSorozatPozicio(sorozatCellak, false);
      if (sorozatCellak.length >= 5) {
        return true;
      }
    } else {
      // A sorozat hossza és a lezártság alapján pozíciós értéket rendelünk a cellához
      poziciok[1] = jatekos.calcPozicio(cella[0], cella[1], sorozatCellak.length, zart, lyuk[0] || lyuk[1]);
    }

    // Most jön az ÉNy-DK átló
    sorozatCellak = vonalvizsgalat(cella, jel, ellenjel, 1, 1, zart, lyuk, !lepesCellaja);
    // A pozíciós táblába beírjuk a meglévő sorozathosszakat
    if (lepesCellaja) {
      jatekos.setSorozatPozicio(sorozatCellak, true);
      ellenfel.setSorozatPozicio(sorozatCellak, false);
      if (sorozatCellak.length >= 5) {
        return true;
      }
    } else {
      // A sorozat hossza és a lezártság alapján pozíciós értéket rendelünk a cellához
      poziciok[2] = jatekos.calcPozicio(cella[0], cella[1], sorozatCellak.length, zart, lyuk[0] || lyuk[1]);
    }

    // Végül pedig az DNy-ÉK átló
    sorozatCellak = vonalvizsgalat(cella, jel, ellenjel, -1, 1, zart, lyuk, !lepesCellaja);
    // A pozíciós táblába beírjuk a meglévő sorozathosszakat
    if (lepesCellaja) {
      jatekos.setSorozatPozicio(sorozatCellak, true);
      ellenfel.setSorozatPozicio(sorozatCellak, false);
      if (sorozatCellak.length >= 5) {
        return true;
      }
    } else {
      // A sorozat hossza és a lezártság alapján pozíciós értéket rendelünk a cellához
      poziciok[3] = jatekos.calcPozicio(cella[0], cella[1], sorozatCellak.length, zart, lyuk[0] || lyuk[1]);
//      System.out.println("cella max: " + cella[0] + "," + cella[1]);
      maxPozicio = jatekos.calcMaxPozicio(poziciok);
    }

    if (lepesCellaja) {

      // Megjelöljük az aktuális lépés körüli mezőket
      for (sor = cella[0] - 1; sor <= cella[0] + 1; sor++) {
        for (oszlop = cella[1] - 1; oszlop <= cella[1] + 1; oszlop++) {
          if (sor >= 0 && sor < cellak.length && oszlop >= 0 && oszlop < cellak[0].length) {
            if (jatekos.getPozicio(sor, oszlop) == 10 || jatekos.getPozicio(sor, oszlop) == 0) {
              jatekos.setPozicio(sor, oszlop, 13);
            }
            if (ellenfel.getPozicio(sor, oszlop) == 10 || ellenfel.getPozicio(sor, oszlop) == 0) {
              ellenfel.setPozicio(sor, oszlop, 13);
            }
          }
        }
      }
      for (sor = cella[0] - 2; sor <= cella[0] + 2; sor++) {
        for (oszlop = cella[1] - 2; oszlop <= cella[1] + 2; oszlop++) {
          if (sor >= 0 && sor < cellak.length && oszlop >= 0 && oszlop < cellak[0].length) {
            if (jatekos.getPozicio(sor, oszlop) == 0) {
              jatekos.setPozicio(sor, oszlop, 10);
            }
            if (ellenfel.getPozicio(sor, oszlop) == 0) {
              ellenfel.setPozicio(sor, oszlop, 10);
            }
          }
        }
      }

      // A környező cellákra is megfuttatjuk az elemzést
      // Minden irányban 4 cellát kell elemezni, de csak azokat, ahol még nincs jel, illetve amelyek "elég közel" vannak egy meglévő jelhez
      //
      // Először a sort nézzük balra és jobbra
      sor = cella[0];
      for (int i = 0; i < 2; i++) {
        irany = 2 * i - 1;
        for (oszlop = cella[1] + irany; oszlop >= 0 && oszlop < cellak[0].length; oszlop += irany) {
          if (cellak[sor][oszlop].equals(" ") && jatekos.getPozicio(sor, oszlop) > 0) {
            // Mindkét játékos pozícióját megnézzük
            for (int j = 0; j < 2; j++) {
//              System.out.println("NY-K, sor: " + sor + " oszlop: " + oszlop + ", " + jatekosok[j].getNev());
              cellak[sor][oszlop] = jatekosok[j].getJel();
              ujCella[0] = sor;
              ujCella[1] = oszlop;
              elemzes(ujCella, false);
            }
            cellak[sor][oszlop] = " ";
          }
        }
      }
      //
      // Aztán az oszlopot felfelé és lefelé
      oszlop = cella[1];
      for (int i = 0; i < 2; i++) {
        irany = 2 * i - 1;
        for (sor = cella[0] + irany; sor >= 0 && sor < cellak.length; sor += irany) {
          if (cellak[sor][oszlop].equals(" ") && jatekos.getPozicio(sor, oszlop) > 0) {
            // Mindkét játékos pozícióját megnézzük
            for (int j = 0; j < 2; j++) {
//              System.out.println("É-D, sor: " + sor + " oszlop: " + oszlop);
              cellak[sor][oszlop] = jatekosok[j].getJel();
              ujCella[0] = sor;
              ujCella[1] = oszlop;
              elemzes(ujCella, false);
            }
            cellak[sor][oszlop] = " ";
          }
        }
      }
      //
      // ÉNy-DK átló
      for (int i = 0; i < 2; i++) {
        irany = 2 * i - 1;
        oszlop = cella[1] + irany;
        for (sor = cella[0] + irany; sor >= 0 && sor < cellak.length && oszlop >= 0 && oszlop < cellak[0].length; sor += irany) {
          if (cellak[sor][oszlop].equals(" ") && jatekos.getPozicio(sor, oszlop) > 0) {
            // Mindkét játékos pozícióját megnézzük
            for (int j = 0; j < 2; j++) {
              cellak[sor][oszlop] = jatekosok[j].getJel();
              ujCella[0] = sor;
              ujCella[1] = oszlop;
              elemzes(ujCella, false);
            }
            cellak[sor][oszlop] = " ";
          }
          oszlop += irany;
        }
      }
      //
      // DNy-ÉK átló
      for (int i = 0; i < 2; i++) {
        irany = 2 * i - 1;
        oszlop = cella[1] + irany;
        for (sor = cella[0] - irany; sor >= 0 && sor < cellak.length && oszlop >= 0 && oszlop < cellak[0].length; sor -= irany) {
          if (cellak[sor][oszlop].equals(" ") && jatekos.getPozicio(sor, oszlop) > 0) {
            // Mindkét játékos pozícióját megnézzük
            for (int j = 0; j < 2; j++) {
              cellak[sor][oszlop] = jatekosok[j].getJel();
              ujCella[0] = sor;
              ujCella[1] = oszlop;
              elemzes(ujCella, false);
            }
            cellak[sor][oszlop] = " ";
          }
          oszlop += irany;
        }
      }

    } else {
//      System.out.println("max pozíció: " + maxPozicio);
      if (jatekos.getPozicio(cella[0], cella[1]) == 13)
        maxPozicio++;
      jatekos.setPozicio(cella[0], cella[1], maxPozicio);
    }

    return false;
  }

  public int[] gepLepes(int aktualisJatekos) {
    int[] cella = new int[2];
    Jatekos jatekos = jatekosok[aktualisJatekos];
    Jatekos ellenfel = jatekosok[(aktualisJatekos + 1) % 2];
    Random rnd = new Random();
    int szamlalo = 0;
    int maximum = 0;
    int ellenMaximum = 0;
    int elozoMax = 100;
    int pozicio;
    int lepesPozicio = 0;
    boolean kivalasztva;

    for (int sor = 0; sor < cellak.length; sor++)
      for (int oszlop = 0; oszlop < cellak[0].length; oszlop++) {
        pozicio = jatekos.getPozicio(sor, oszlop);
        if (pozicio > maximum) {
          maximum = pozicio;
        }
      }

    for (int sor = 0; sor < cellak.length; sor++)
      for (int oszlop = 0; oszlop < cellak[0].length; oszlop++) {
        pozicio = ellenfel.getPozicio(sor, oszlop);
        if (pozicio > ellenMaximum) {
          ellenMaximum = pozicio;
        }
      }

    // veszély felismerése
    if (ellenMaximum >= 50 && maximum < 50) {
      // ezt a gyengébb gép is észreveszi
      lepesPozicio = -ellenMaximum;
    } else if (ellenMaximum >= 40 && maximum < 40) {
      // ezt még eltéveszthetik
      szamlalo = 0;
      do {
        szamlalo++;
        kivalasztva = rnd.nextBoolean();
      } while (szamlalo < jatekos.probalkozasokSzama() && (!kivalasztva));
//      System.out.println("1: " + szamlalo);
      if (kivalasztva)
        lepesPozicio = -ellenMaximum;
    }

    if (lepesPozicio == 0) {
      do {
        maximum = 9;
        for (int sor = 0; sor < cellak.length; sor++) {
          for (int oszlop = 0; oszlop < cellak[0].length; oszlop++) {
            pozicio = jatekos.getPozicio(sor, oszlop);
            if (pozicio > maximum && pozicio < elozoMax) {
              maximum = pozicio;
            }
          }
        }

//        System.out.println("maximum:" + maximum);
        if (maximum > 9) {
          if (maximum >= 50)
            // a győzelmet a gyengébb gép is vegye észre
            kivalasztva = true;
          else {
            szamlalo = 0;
            do {
              szamlalo++;
              kivalasztva = rnd.nextBoolean();
            } while (szamlalo < jatekos.probalkozasokSzama() && (!kivalasztva));
//            System.out.println("2: " + szamlalo);
          }

          //        System.out.println(kivalasztva);
          if (kivalasztva) {
            lepesPozicio = maximum;
          } else {
            elozoMax = maximum;
          }
        } else if (elozoMax > 0 && elozoMax < 100) {
          lepesPozicio = elozoMax;
        } else {
          maximum = 0;
        }
      } while (lepesPozicio == 0 && maximum > 0);
//      System.out.println("lépéspozíció: " + lepesPozicio);
    }

    if (lepesPozicio != 0) {
      if (lepesPozicio < 0){
        jatekos = ellenfel;
        lepesPozicio *= -1;
      }  
        
      szamlalo = 0;
      for (int sor = 0; sor < cellak.length; sor++) {
        for (int oszlop = 0; oszlop < cellak[0].length; oszlop++) {
          if (jatekos.getPozicio(sor, oszlop) == lepesPozicio) {
            szamlalo++;
          }
        }
      }
      int sorszam = rnd.nextInt(szamlalo) + 1;

      szamlalo = 0;
      for (int sor = 0; sor < cellak.length && szamlalo < sorszam; sor++) {
        for (int oszlop = 0; oszlop < cellak[0].length && szamlalo < sorszam; oszlop++) {
          if (jatekos.getPozicio(sor, oszlop) == lepesPozicio) {
            szamlalo++;
            if (szamlalo == sorszam) {
              cella[0] = sor;
              cella[1] = oszlop;
            }
          }
        }
      }
    } else {
      cella[0] = cellak.length / 2;
      cella[1] = cellak[0].length / 2;
    }

    return cella;
  }

  public void inicializalas() {
    tablaLetrehozas();
    jatekosLetrehozas();
  }

  public void jatek() {
    int lepesSzam = 0;
    int aktualisJatekos = 1;
    int gyoztes = -1;
    int[] cella = {-1, -1};
    String lepes;
    Scanner sc = new Scanner(System.in);
    Logger logger = Logger.getLogger(Amoeba.class.getName());

    System.out.println("Kezdődhet a játék!");
    System.out.println("Megszakítás '.' érték megadásával lehetséges.");

    kiRajzolas(cella);

    do {
      lepesSzam++;
      aktualisJatekos = (aktualisJatekos + 1) % 2;
      boolean helyes;

      do {
        helyes = true;
        System.out.print(jatekosok[aktualisJatekos].getNev() + " (" + jatekosok[aktualisJatekos].getJel() + ") lépése: ");

        if (jatekosok[aktualisJatekos].isGep()) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            continue;
          }
          cella = gepLepes(aktualisJatekos);
//          System.out.println(cella[0] + " " + cella[1]);
          if (sorindexek[cella[0]].substring(0, 1).equals(" ")) {
            lepes = sorindexek[cella[0]].substring(1, 2);
          } else {
            lepes = sorindexek[cella[0]];
          }
          if (oszlopindexek[cella[1]].substring(0, 1).equals(" ")) {
            lepes = lepes + oszlopindexek[cella[1]].substring(1, 2);
          } else {
            lepes = lepes + oszlopindexek[cella[1]];
          }
          System.out.println(lepes);
          cellak[cella[0]][cella[1]] = jatekosok[aktualisJatekos].getJel();
        } else {
          lepes = sc.nextLine();
          if (lepes.equals("#1")) {
            kiRajzolas(jatekosok[aktualisJatekos].pozicioTabla(), cella, false);
            helyes = false;
          } else if (lepes.equals("#2")) {
            kiRajzolas(jatekosok[(aktualisJatekos + 1) % 2].pozicioTabla(), cella, false);
            helyes = false;
          } else if (lepes.equals(".")) {
            gyoztes = 3;
          } else {
            cella = lepesbolCella(lepes);
            if (cella[0] < 0 || cella[1] < 0) {
              helyes = false;
              System.out.println("A megadott cella nem létezik.");
            } else if (cellak[cella[0]][cella[1]].equals(jatekosok[0].getJel())
                    || cellak[cella[0]][cella[1]].equals(jatekosok[1].getJel())) {
              helyes = false;
              System.out.println("A megadott cella már foglalt.");
            } else {
              cellak[cella[0]][cella[1]] = jatekosok[aktualisJatekos].getJel();
            }
          }
        }
      } while (!helyes);

      if (gyoztes < 0) {
        if (elemzes(cella, true)) {
          gyoztes = aktualisJatekos;
        } else if (lepesSzam == cellak.length * cellak[0].length) {
          gyoztes = 2;
        }
        kiRajzolas(cella);
      }
    } while (gyoztes < 0);

    switch (gyoztes) {
      case 2:
        System.out.println("A tábla betelt, a játéknak vége.");
        break;
      case 3:
        System.out.println("A játékot " + jatekosok[aktualisJatekos].getNev() + " megszakította.");
        break;
      default:
        System.out.println("A győztes: " + jatekosok[aktualisJatekos].getNev());
        logger.debug("Nyer: " + jatekosok[aktualisJatekos].getNev() );
    }

  }

}
