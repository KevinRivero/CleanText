package com.cleanText.editor;

import jdk.jshell.execution.Util;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;


public class Panel extends JPanel {
  // ---------- Variables para el area de texto -----------
  private JTabbedPane tPane;
  private JPanel ventana;
  private ArrayList<NumeroLinea> enumeraciones;
  private boolean numeracion = true;
  // se crean ArrayList porque vamos a poder tener varias pestanias y archivos
  private ArrayList<JTextPane> listaTexto;
  private ArrayList<JScrollPane> listaScroll;
  private ArrayList<UndoManager> listaManager; // este array contiene clases de seguimiento que se van a acoplar con las areas de texto, para que tengan registrados los cambios que vamos haciendo y asi poder
  // utilizar comandos como deshacer y rehacer, entre otros.
  private int contadorPanel = 0; // se crea un contador para sumar cuantas "ventanas" vamos creando
  //------------------- Variables para el menu -------------------
  private JMenuBar menu;
  private JMenu archivo, editar, seleccion, ver, aparariencia;
  private JMenuItem item;

  // ---------------- Variables para archivos
  private ArrayList<File> listaArchivos;

  public Panel() {
    // se incicializan las variables para el area de texto
    tPane = new JTabbedPane();
    listaTexto = new ArrayList<JTextPane>();
    listaScroll = new ArrayList<JScrollPane>();
    listaManager = new ArrayList<UndoManager>();
    enumeraciones = new ArrayList<NumeroLinea>();

    // se inicializan las variables para el menu
    menu = new JMenuBar();
    archivo = new JMenu("Archivo");
    editar = new JMenu("Editar");
    seleccion = new JMenu("Seleccion");
    ver = new JMenu("Ver");
    aparariencia = new JMenu("Apariencia");

    // se inicializa las variables de archivo
    listaArchivos = new ArrayList<File>();

    // se agrega cada opcion al menu
    menu.add(archivo);
    menu.add(editar);
    menu.add(seleccion);
    ver.add(aparariencia);
    menu.add(ver);

    // se ejecuta la funcion creaItem para crear cada submenu dentro del menu principal
    crearItem("Nuevo archivo", "archivo", "nuevo");
    crearItem("Abrir archivo", "archivo", "abrir");
    crearItem("Guardar", "archivo", "guardar");
    crearItem("Guardar como...", "archivo", "guardar como");
    crearItem("Deshacer", "editar", "deshacer");
    crearItem("Rehacer", "editar", "rehacer");
    crearItem("Copiar", "editar", "copiar");
    crearItem("Cortar", "editar", "cortar");
    crearItem("Pegar", "editar", "pegar");
    crearItem("Seleccionar todo", "seleccion", "seleccionar todo");
    crearItem("Numeracion", "ver", "numeracion");
    crearItem("Normal", "apariencia", "normal");
    crearItem("Oscuro", "apariencia", "oscuro");

    add(menu);
    add(tPane); // se agrega al panel principal, que seria el que se crea con el constructor, el contenedor que tiene las areas de texto y se ejecuta la funcion crearPanel
  }

  public void crearPanel() {
    ventana = new JPanel();
    listaTexto.add(new JTextPane());
    listaScroll.add(new JScrollPane(listaTexto.get(contadorPanel))); // se agrega el area de texto al scroll
    listaArchivos.add(new File(""));

    enumeraciones.add(new NumeroLinea(listaTexto.get(contadorPanel)));
    Utilidades.numerarLineas(numeracion, enumeraciones.get(contadorPanel), listaScroll.get(contadorPanel));

    listaManager.add(new UndoManager());
    listaTexto.get(contadorPanel).getDocument().addUndoableEditListener(listaManager.get(contadorPanel));
    ventana.add(listaScroll.get(contadorPanel)); // se agrega el scroll (que ya tiene al area de texto dentro) a la ventana
    tPane.addTab("Pestaña " + (contadorPanel + 1), ventana);
    tPane.setSelectedIndex(contadorPanel); // se hace para que una vez que se cree una nueva area de texto, quede seleccionada


    listaTexto.get(contadorPanel).selectAll();
    Utilidades.cambiarApariencia("normal", listaTexto);


    contadorPanel++;
  }


  public void crearItem(String nombre, String tipo, String accion) {
    item = new JMenuItem(nombre);
    switch (tipo) {
      case "archivo":
        archivo.add(item);
        if (accion.equals("nuevo")) {
          item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              crearPanel();
            }
          });
        } else if (accion.equals("abrir")) {
          item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              crearPanel();
              JFileChooser selector = new JFileChooser();
              selector.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
              int resultado = selector.showOpenDialog(listaTexto.get(tPane.getSelectedIndex()));


              if (resultado == JFileChooser.APPROVE_OPTION) {
                try {
                  boolean existeRuta = false;
                  for (int i = 0; i < tPane.getTabCount(); i++) {
                    File file = selector.getSelectedFile();
                    // BUG : cuando se abre un archivo, luego otro, despues se vuelve a abrir el primero y despues se vuelve a abrir el segundo, se crea una pestania nueva con el segundo
                    // y en listaArchivos se pierde la ubicacion del indice 1. es decir, el path del segundo archivo abierto deja existir en listaArchivos.
                    // CORREGIR BUGGGGG // SOLUCIONAR LUEGO, NO ES IMPORTANTE AHORA
                    if (listaArchivos.get(i).getPath().equals(file.getPath())) {
                      existeRuta = true;
                      break;
                    }
                  }
                  if (!existeRuta) {
                    File file = selector.getSelectedFile();
                    listaArchivos.set(tPane.getSelectedIndex(), file);
                    FileReader entrada = new FileReader(listaArchivos.get(tPane.getSelectedIndex()).getPath());
                    BufferedReader buffer = new BufferedReader(entrada);
                    String linea = "";
                    tPane.setTitleAt(tPane.getSelectedIndex(), listaArchivos.get(tPane.getSelectedIndex()).getName());

                    while (linea != null) {
                      linea = buffer.readLine();
                      if (linea != null) {
                        Utilidades.append(linea, listaTexto.get(tPane.getSelectedIndex()));
                      }
                    }
                  } else {
                    File f = selector.getSelectedFile();
                    for (int i = 0; i < tPane.getTabCount(); i++) {
                      if (listaArchivos.get(i).getPath().equals(f.getPath())) {
                        tPane.setSelectedIndex(i);
                        listaManager.remove(tPane.getTabCount() - 1);
                        listaTexto.remove(tPane.getTabCount() - 1);
                        listaScroll.remove(tPane.getTabCount() - 1);
                        tPane.remove(tPane.getTabCount() - 1);
                        listaArchivos.remove(tPane.getTabCount() - 1);
                        contadorPanel--;
                        break;
                      }
                    }
                  }
                } catch (IOException exception) {
                  throw new RuntimeException(exception);

                }
              } else {
                listaManager.remove(tPane.getTabCount() - 1);
                listaTexto.remove(tPane.getTabCount() - 1);
                listaScroll.remove(tPane.getTabCount() - 1);
                listaArchivos.remove(tPane.getTabCount() - 1);
                tPane.remove(tPane.getTabCount() - 1);


                contadorPanel--;
              }
            }
          });
        } else if (accion.equals("guardar")) { // BUG: Cuando presiono guardar sin tener ningun pestania abierta me tira error, diciendo que los indices de la lista de listaArchivos es menor al del seleccionado.
          // SOLUCIONAR LUEGO, NO ES IMPORTANTE AHORA
          item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              // guardar como en caso de que el archivo todavia no exista
              if (listaArchivos.get(tPane.getSelectedIndex()).getPath().equals("")) {
                JFileChooser guardarArchivo = new JFileChooser();
                int opcion = guardarArchivo.showOpenDialog(null);
                if (opcion == JFileChooser.APPROVE_OPTION) {
                  File archivo = guardarArchivo.getSelectedFile();
                  listaArchivos.set(tPane.getSelectedIndex(), archivo);
                  tPane.setTitleAt(tPane.getSelectedIndex(), archivo.getName());
                  try {
                    FileWriter fw = new FileWriter(listaArchivos.get(tPane.getSelectedIndex()).getPath());
                    String texto = listaTexto.get(tPane.getSelectedIndex()).getText();
                    for (int i = 0; i < texto.length(); i++) {
                      fw.write(texto.charAt(i));
                    }
                    fw.close();


                  } catch (IOException ex) {
                    throw new RuntimeException(ex);
                  }

                }
              } else {
                try {
                  FileWriter fw = new FileWriter(listaArchivos.get(tPane.getSelectedIndex()).getPath());
                  String texto = listaTexto.get(tPane.getSelectedIndex()).getText();
                  for (int i = 0; i < texto.length(); i++) {
                    fw.write(texto.charAt(i));
                  }
                  fw.close();


                } catch (IOException ex) {
                  throw new RuntimeException(ex);
                }
              }
            }
          });
        } else if (accion.equals("guardar como")) {
          item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              if (listaArchivos.get(tPane.getSelectedIndex()).getPath().equals("")) {
                JFileChooser guardarArchivo = new JFileChooser();
                int opcion = guardarArchivo.showOpenDialog(null);
                if (opcion == JFileChooser.APPROVE_OPTION) {
                  File archivo = guardarArchivo.getSelectedFile();
                  listaArchivos.set(tPane.getSelectedIndex(), archivo);
                  tPane.setTitleAt(tPane.getSelectedIndex(), archivo.getName());
                  try {
                    FileWriter fw = new FileWriter(listaArchivos.get(tPane.getSelectedIndex()).getPath());
                    String texto = listaTexto.get(tPane.getSelectedIndex()).getText();
                    for (int i = 0; i < texto.length(); i++) {
                      fw.write(texto.charAt(i));
                    }
                    fw.close();


                  } catch (IOException ex) {
                    throw new RuntimeException(ex);
                  }

                }
              } else {
                try {
                  FileWriter fw = new FileWriter(listaArchivos.get(tPane.getSelectedIndex()).getPath());
                  String texto = listaTexto.get(tPane.getSelectedIndex()).getText();
                  for (int i = 0; i < texto.length(); i++) {
                    fw.write(texto.charAt(i));
                  }
                  fw.close();


                } catch (IOException ex) {
                  throw new RuntimeException(ex);
                }
              }
            }
          });
        }
        break;
      case "editar":
        editar.add(item);
        if (accion.equals("deshacer")) {
          item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              if (listaManager.get(tPane.getSelectedIndex()).canUndo()) {
                listaManager.get(tPane.getSelectedIndex()).undo();
              }
            }
          });
        } else if (accion.equals("rehacer")) {
          item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              if (listaManager.get(tPane.getSelectedIndex()).canRedo()) {
                listaManager.get(tPane.getSelectedIndex()).redo();
              }
            }
          });
        } else if (accion.equals("copiar")) {
          item.addActionListener(new DefaultEditorKit.CopyAction());
        } else if (accion.equals("cortar")) {
          item.addActionListener(new DefaultEditorKit.CutAction());
        } else if (accion.equals("pegar")) {
          item.addActionListener(new DefaultEditorKit.PasteAction());
        }
        break;
      case "seleccion": // BUG: cuando no tengo seleccionada el area de texto, osea tengo seleccionada la pestania, no funciona el seleccionar todo, tengo que primero posicionarme sobre el area de texto y luego ahi funciona.
        // NO ES UN BUG CRITICO NI IMPORTANTE, solucionar luego
        seleccion.add(item);
        if (accion.equals("seleccionar todo")) {
          item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              listaTexto.get(tPane.getSelectedIndex()).selectAll();
            }
          });
        }
        break;
      case "ver":
        ver.add(item);
        if (accion.equals("numeracion")) {
          item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              numeracion = !numeracion;
              Utilidades.numerarLineas(numeracion, enumeraciones.get(tPane.getSelectedIndex()), listaScroll.get(tPane.getSelectedIndex()));
            }
          });
          break;
        }
      case "apariencia": // BUG IMPORTANTE : Cuando tengo mas de una pestania abierta y cambio de apariencia, en la que no tengo seleccionada se cambia el fondo pero no cambian los otros
        // aplicados a la fuente. SOLUCIONAR PRIMERO
        aparariencia.add(item);
        if (accion.equals("normal")) {
          item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              listaTexto.get(tPane.getSelectedIndex()).selectAll();
              Utilidades.cambiarApariencia("normal", listaTexto);
            }
          });
        } else if (accion.equals("oscuro")) {
          item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              listaTexto.get(tPane.getSelectedIndex()).selectAll();
              Utilidades.cambiarApariencia("oscuro", listaTexto);
            }
          });
        }
    }
  }
}


