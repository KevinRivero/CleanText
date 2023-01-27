package com.cleanText.editor;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

public class Utilidades {

  // eesta funcion sirve parta anexar un String a un JTextPane sin sobreescribir la informacion
  public static void append(String linea, JTextPane areaTexto) {
    try {
      Document doc = areaTexto.getDocument();
      doc.insertString(doc.getLength(), linea + "\n", null);
    } catch (BadLocationException exc) {
      exc.printStackTrace();
    }
  }

  // funcion para enumerar las lineas del area de texto

  public static void numerarLineas(boolean numeracion, NumeroLinea linea, JScrollPane scroll) {
    if (numeracion) {
      scroll.setRowHeaderView(linea);
    } else {
      scroll.setRowHeaderView(null);
    }
  }

  public static void cambiarApariencia(String tipo, ArrayList<JTextPane> lista) {
    if (tipo.equals("normal")) {
      for (JTextPane texto : lista) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        // cambiar color de texto
        AttributeSet estilos = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
        // cambiar tamanio de fuente
        estilos = sc.addAttribute(estilos, StyleConstants.FontSize, 14);
        // cambiar tipo de fuente
        estilos = sc.addAttribute(estilos, StyleConstants.FontFamily, "Arial");

        texto.setCharacterAttributes(estilos, false); // se aplican los cambios guardados en estilos a los caracteres del texto
        texto.setBackground(Color.white);
      }
    } else if (tipo.equals("oscuro")) {
      for (JTextPane texto : lista) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        // cambiar color de texto
        AttributeSet estilos = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.lightGray);
        // cambiar tamanio de fuente
        estilos = sc.addAttribute(estilos, StyleConstants.FontSize, 14);
        // cambiar tipo de fuente
        estilos = sc.addAttribute(estilos, StyleConstants.FontFamily, "Arial");

        texto.setCharacterAttributes(estilos, false); // se aplican los cambios guardados en estilos a los caracteres del texto
        texto.setBackground(Color.DARK_GRAY);
      }
    }
  }

  public static JButton crearBoton(URL url, Object contenedor, String rotulo) {
    JButton boton = new JButton(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
    boton.setToolTipText(rotulo);
    ((Container) contenedor).add(boton);
    return boton;

  }

  // Funcionalidades barra de herramientas
  

}



