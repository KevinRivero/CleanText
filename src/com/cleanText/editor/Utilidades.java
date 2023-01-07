package com.cleanText.editor;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class Utilidades {

  // eesta funcion sirve parta anexar un String a un JTextPane sin sobreescribir la informacion
  public static void append (String linea, JTextPane areaTexto){
    try {
      Document doc = areaTexto.getDocument();
      doc.insertString(doc.getLength(), linea + "\n", null);
    }catch (BadLocationException exc){
      exc.printStackTrace();
    }
  }
}
