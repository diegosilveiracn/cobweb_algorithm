/*
 * Main.java
 *
 * Created on October 17, 2007, 3:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package br.unifor.mia.cobweb.view;

import java.io.FilterOutputStream;
import java.io.PrintStream;

/**
 * Belmino
 */
public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Application().setVisible(true);
            }
        });
    }
}