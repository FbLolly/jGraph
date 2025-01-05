package cameraPkg;

import java.awt.Graphics;
import java.awt.Point;


//this should work decently as a rudimentary implementation of a camera object
//(similiar to raylibs)
//Making all of the methods and variables in this class static would help with
//simplicity but limit the program to a single camera, so I choose not to do that
public class Camera{
    private double zoom;
    private Point offSet; //how distant the view is from point 0,0 (standard)
    private Point ogOffSet;

    public Camera(){
        this.zoom = 1;
        this.offSet = new Point(0, 0);
        this.ogOffSet = new Point(0, 0);
    }

    public Camera(double zoom, Point offSet){
        this.zoom = zoom;
        this.offSet = offSet;
        this.ogOffSet = new Point(offSet.x, offSet.y);

        this.offSet.x = (int)(ogOffSet.x * this.zoom);
        this.offSet.y = (int)(ogOffSet.y * this.zoom);
    }

    
    public void drawRectangle(int x, int y, int width, int height, Graphics g){
        x *= this.zoom;
        y *= this.zoom;

        g.fillRect(x + this.offSet.x, y + this.offSet.y, (int)(width*this.zoom), (int)(height*this.zoom));
    }

    public void drawLine(double x1, double y1, double x2, double y2, Graphics g){
        x1 *= this.zoom;
        y1 *= this.zoom;
        x2 *= this.zoom;
        y2 *= this.zoom;

        g.drawLine((int)(x1 + this.offSet.x), (int)(y1 + this.offSet.y), (int)(x2 + this.offSet.x), (int)(y2 + this.offSet.y));
    }

    public void drawPoint(double x, double y, Graphics g){
        x *= this.zoom;
        y *= this.zoom;

        //for some god forsaken reason java awt Graphics does not have a drawPixel method
        //I will have to unfortunately implement it myself with the camera

        g.drawRect((int)(x + this.offSet.x), (int)(y + this.offSet.y), 1, 1); //dimension is always 1x1 (even with zoom)
    }

    public void drawString(String string, double x, double y, Graphics g){
        x *= this.zoom;
        y *= this.zoom;

        g.drawString(string, (int)(x + this.offSet.x), (int)(y + this.offSet.y));
    }


    public void setZoom(double zoom){
        this.zoom = zoom;

        this.offSet.x = this.ogOffSet.x;
        this.offSet.y = this.ogOffSet.y;
    }
    public double getZoom(){
        return this.zoom;
    }

    public void setOffSet(Point offSet){
        this.offSet = offSet;
        this.ogOffSet = new Point(offSet.x, offSet.y);
    }

    public Point getOffSet(){
        return this.offSet;
    }
}
