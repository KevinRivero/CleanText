package com.cleanText.editor;

import javax.swing.*;

public class Main {
  public static void main(String[] args) {

    Marco marco = new Marco();
    Panel panel = new Panel();

    marco.add(panel);
    // para que cuando se cierre la ventana deje de ejecutarse el programa
    marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    marco.setVisible(true);



  }
}
