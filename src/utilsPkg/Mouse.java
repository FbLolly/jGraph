package utilsPkg;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener{
    public int x, y;
    public boolean clicked;

    public Mouse(){
        this.x = 0;
        this.y = 0;
    }

    public void updateMousePos(){
        Point help = MouseInfo.getPointerInfo().getLocation();

        this.x = help.x;
        this.y = help.y;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.clicked = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.clicked = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }
}
