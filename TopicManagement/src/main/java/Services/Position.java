/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author BVKieu
 */
public class Position {
    int x;
    int y;
    JFrame view;

    public Position(JFrame view) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.x = (int) ((dimension.getWidth() - view.getWidth()) / 2);
        this.y = (int) ((dimension.getHeight() - view.getHeight()) / 2);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
}
