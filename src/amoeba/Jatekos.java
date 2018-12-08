package amoeba;

import java.util.Random;

/**
 * @author Joti *
 */
public class Jatekos {

  private String nev;
  private boolean gep;
  private int jatekEro;
  private String jel;
  private int[][] poziciok;

  public Jatekos() {
  }

  public Jatekos(String nev, boolean gep, int jatekEro, String jel, int sorokSzama, int oszlopokSzama) {
    this.nev = nev;
    this.gep = gep;
    this.jel = jel;
    this.jatekEro = jatekEro;
    this.poziciok = new int[sorokSzama][oszlopokSzama];
  }

  public String[][] pozicioTabla() {
    String[][] tabla = new String[poziciok.length][poziciok[0].length];
    for (int sor = 0; sor < tabla.length; sor++) {
      for (int oszlop = 0; oszlop < tabla[0].length; oszlop++) {
        tabla[sor][oszlop] = Integer.toString(poziciok[sor][oszlop]);
      }
    }
    return tabla;
  }

  public int getPozicio(int sor, int oszlop) {
    return poziciok[sor][oszlop];
  }

  public void setPozicio(int sor, int oszlop, int ertek) {
    poziciok[sor][oszlop] = ertek;
  }

  public int calcPozicio(int sor, int oszlop, int sorozathossz, boolean[] zart, boolean lyukas) {
    int pozicio;
    if (zart[0] && zart[1] && sorozathossz < 5)
      pozicio = 10;
    else {
      if (sorozathossz > 5)
        sorozathossz = 5;

      switch (sorozathossz) {
        case 5:
          if (lyukas)
            pozicio = 42;
          else  
            pozicio = 50;
          break;
        case 4:
          if (zart[0] || zart[1] || lyukas)
            // egyik oldalt lezárt négyes kb. ugyanannyit ér, mint egy nyitott hármas
            pozicio = 34;
          else
            pozicio = 42;
          break;
        case 3:
          if (zart[0] || zart[1])
            pozicio = 31;
          else if (lyukas)
            pozicio = 32;
          else  
            pozicio = 33;
          break;
        case 2:
          if (zart[0] || zart[1])
            pozicio = 21;
          else
            pozicio = 22;
          break;
        case 1:
          // a tábla szélét próbáljuk elkerülni
          if (sor > 0 && sor < poziciok.length - 1 && oszlop > 0 && oszlop < poziciok.length - 1) {
            if (zart[0] || zart[1])
              pozicio = 11;
            else
              pozicio = 12;
          } else
            pozicio = 10;
          break;
        default:
          pozicio = 10;
      }
    }
//    System.out.println(" calc s: " + sor + " o: " + oszlop + " hossz: " + sorozathossz + " lyuk: " + lyukas + " p: " + pozicio);

    return pozicio;
  }

  public int calcMaxPozicio(int[] poziciok) {
    int maxPozicio = 0;
    int szamlalo = 0;
    int bonusz = 0;

    for (int pozicio : poziciok) {
      if (pozicio > 40)
        return pozicio;
      if (pozicio > 31)
        szamlalo++;
      if (pozicio > maxPozicio)
        maxPozicio = pozicio;
    }
//    System.out.println("max: " + maxPozicio);
    
    //nyerő pozíció a két nyitott hármas is
    if (szamlalo > 1)
      return 41;

    szamlalo = 0;
    if (maxPozicio >= 20) {
      for (int pozicio : poziciok) {
        if (pozicio == maxPozicio)
          szamlalo++;
      }
      bonusz = (szamlalo - 1) * 2;
    }  
    
//    System.out.println("bónusz: " + bonusz);
    
    return maxPozicio + bonusz;
  }

  public void setSorozatPozicio(int[][] sorozatCellak, boolean sajatSorozat) {
    int pozicio = sorozatCellak.length;
    if (!sajatSorozat)
      pozicio *= -1;

    for (int i = 0; i < sorozatCellak.length; i++) {
      if (sorozatCellak.length > Math.abs(poziciok[sorozatCellak[i][0]][sorozatCellak[i][1]])
              || poziciok[sorozatCellak[i][0]][sorozatCellak[i][1]] > 9) {
        setPozicio(sorozatCellak[i][0], sorozatCellak[i][1], pozicio);
      }
    }
  }

  public int[][] getPoziciok() {
    return poziciok;
  }

  public void setPoziciok(int[][] poziciok) {
    this.poziciok = poziciok;
  }

  public String getNev() {
    return nev;
  }

  public int probalkozasokSzama(){
    Random rnd = new Random();
    switch (jatekEro) {
      case 1:
        return 1;
      case 2:
        // 1-2
        return rnd.nextInt(2) + 1;
      case 3:
        return 2;
      case 4:
        // 1-3
        return rnd.nextInt(3) + 1;
      case 5:
        // 2-3
        return rnd.nextInt(2) + 2;
      case 6:
        // 2-4
        return rnd.nextInt(3) + 2;
      case 7:
        // 3-4
        return rnd.nextInt(2) + 3;
      case 8:
        // 4-6
        return rnd.nextInt(3) + 4;
      case 9:
        // 5-8
        return rnd.nextInt(4) + 5;
      case 10:
        //8-10
        return rnd.nextInt(2) + 8;
      default:  
        return 1;
    }
  }
  
  public void setNev(String nev) {
    this.nev = nev;
  }

  public boolean isGep() {
    return gep;
  }

  public void setGep(boolean gep) {
    this.gep = gep;
  }

  public int getJatekEro() {
    return jatekEro;
  }

  public void setJatekEro(int jatekEro) {
    this.jatekEro = jatekEro;
  }

  public String getJel() {
    return jel;
  }

  public void setJel(String jel) {
    this.jel = jel;
  }

}
