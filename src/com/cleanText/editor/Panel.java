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
  //------------------- Variables para el menu -------------------
  private JMenuBar menu;
  private JMenu archivo, editar, seleccion, ver, aparariencia;
  private JMenuItem item;

  public Panel (){
    // se incicializan las variables para el area de texto
    tPane = new JTabbedPane();
    listaTexto = new ArrayList<JTextPane>();
    listaScroll = new ArrayList<JScrollPane>();

    // se inicializan las variables para el menu
    menu = new JMenuBar();
    archivo = new JMenu("Archivo");
    editar = new JMenu("Editar");
    seleccion = new JMenu("Seleccion");
    ver = new JMenu("Ver");
    aparariencia = new JMenu("Apariencia");

    menu.add(archivo);
    menu.add(editar);
    menu.add(seleccion);
    ver.add(aparariencia);
    menu.add(ver);



    add(menu);
    add(tPane); // se agrega al panel principal, que seria el que se crea con el constructor, el contenedor que tiene las areas de texto y se ejecuta la funcion crearPanel
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
