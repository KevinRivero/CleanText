package com.cleanText.editor;

import javax.swing.*;
import java.util.ArrayList;

public class Panel extends JPanel {
  // ---------- Variables para el area de texto -----------
  private JTabbedPane tPane;
  private JPanel ventana;
  // se crean ArrayList porque vamos a poder tener varias pestanias y archivos
  private ArrayList<JTextPane> listaTexto;
  private ArrayList<JScrollPane> listaScroll;
  private int contadorPanel = 0; // se crea un contador para sumar cuantas "ventanas" vamos creando

  public Panel (){

    tPane = new JTabbedPane();
    listaTexto = new ArrayList<JTextPane>();
    listaScroll = new ArrayList<JScrollPane>();

    add(tPane);
    crearPanel();

  }

  public void crearPanel (){
    ventana = new JPanel();
    listaTexto.add(new JTextPane());
    listaScroll.add(new JScrollPane(listaTexto.get(contadorPanel)));

    ventana.add(listaScroll.get(contadorPanel));
    tPane.addTab("Pestaña " +(contadorPanel+1),ventana);

  }

}
