package graphPkg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import cameraPkg.Camera;
import mainPkg.Defines;
import mainPkg.JGraph;

public class Graph {
    private double maxX, maxY;
    private double minX, minY;

    private ArrayList<Double> x;
    private ArrayList<Double> y;

    private boolean calculate;
    
    private double r;
    private double dx, dy, dxy; //standard deviation
    private double avgX, avgY;

    private double mx, qx;
    private double my, qy;

    private String fx, fy;

    public Graph(){
        x = new ArrayList<Double>();
        y = new ArrayList<Double>();

        this.calculate = false;

        readDataFromFile("files/toLoad.txt");
    }

    private void readDataFromFile(String fileName){
        try {
            File file = new File(fileName);
            Scanner scan = new Scanner(file);
            int counter = 0;

            while (scan.hasNextDouble()){
                double next = scan.nextDouble();

                if (counter % 2 == 0){
                    this.x.add(next);
                }else{
                    this.y.add(next);
                }

                counter++;
            }

            scan.close();

            this.refreshGraph();
            this.calculate();
        } catch (FileNotFoundException e) {
            this.calculate = false;
        }
    }

    private void refreshGraph(){
        Double maxX, maxY;
        Double minX, minY;

        if (x.size() < 1){ //x and y size are always equal
            this.maxX = 0;
            this.minX = 0;
            this.maxY = 0;
            this.minY = 0;
            return;
        }

        maxX = x.get(0);
        minX = maxX;
        maxY = y.get(0);
        minY = maxY;

        for (Double i : this.x){
            if (i > maxX){
                maxX = i;
            }

            if (i < minX){
                minX = i;
            }

        }

        for (Double i : this.y){
            if (i > maxY){
                maxY = i;
            }

            if (i < minY){
                minY = i;
            }
        }

        this.maxX = maxX;
        this.minX = minX;
        this.maxY = maxY;
        this.minY = minY;
    }

    public void insertPoint(double x, double y){
        this.x.add(x);
        this.y.add(y);

        refreshGraph();
        stopCalculation();
    }

    public void flush(){
        if (this.x.size() == 0)
            return;

        this.x.remove(this.x.size()-1);
        this.y.remove(this.y.size()-1);

        refreshGraph();
        stopCalculation();
    }

    private void linearRegression(){
        mx = dxy/(dx*dx);
        qx = (-mx*avgX)+avgY;

        if (qx < 0.0001) qx = 0;
        fx = "f(x) = " + String.format("%.3g%n", mx) + "x + " + String.format("%.3g%n", qx) + ";";

        my = dxy/(dy*dy);
        qy = (-my*avgY)+avgX;

        if (qy < 0.0001) qy = 0;
        fy = "f(y) = " + String.format("%.3g%n", my) + "x + " + String.format("%.3g%n", qy) + ";";
    }

    public void calculate(){
        this.calculate = true;

        //calculate averages {
        avgX = 0;
        for (Double i : this.x){
            avgX += i;
        }
        avgX /= this.x.size();

        avgY = 0;
        for (Double i : this.y){
            avgY += i;
        }
        avgY /= this.y.size();

        //}; calculate standard deviations
            //x
        dx = 0;

        for (Double i : this.x){
            dx += (i-avgX)*(i-avgX);
        }
        dx /= this.x.size();

        dx = Math.sqrt(dx);

            //y
        dy = 0;

        for (Double i : this.y){
            dy += (i-avgY)*(i-avgY);
        }
        dy /= this.y.size();

        dy = Math.sqrt(dy);

            //xy
        dxy = 0;

        for (int i = 0; i < this.x.size(); i++){
            dxy += (this.x.get(i)*this.y.get(i));
        }
        dxy /= this.x.size();

        dxy -= (avgX*avgY);

        //calculate the r
        r = dxy/(dx*dy);

        linearRegression();
    }

    public void stopCalculation(){
        this.calculate = false;
    }

    public void paintComponent(Graphics2D g, Camera cam, JGraph jgraph){
        double minX = this.minX, minY = this.minY, maxX = this.maxX, maxY = this.maxY;

        if (this.minX > 0){
            minX = 0;
        }
        if (this.minY > 0){
            minY = 0;
        }
        if (this.maxX < 0){
            maxX = 0;
        }
        if (this.maxY < 0){
            maxY = 0;
        }

        minY *= -1;
        maxY *= -1;

        double temp = minY;
        minY = maxY;
        maxY = temp;

        g.setColor(new Color(100, 100, 100, 150));

        for (int i = (int)minX; i <= maxX; i++){
            cam.drawLine(i, minY, i, maxY, g);
        }

        for (int i = (int)minY; i <= maxY; i++){
            cam.drawLine(minX, i, maxX, i, g);
        }

        g.setColor(Color.white);

        cam.drawLine(0, minY, 0, maxY, g);
        cam.drawLine(minX, 0, maxX, 0, g);

        //draw the points

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(new Color(255, 255, 255, 70));
        for (int i = 0; i < this.x.size(); i++){
            cam.drawPoint(x.get(i), -y.get(i), g);
            if (Defines.showCoordinates)
                cam.drawString("P" + (i + 1) + "(" + x.get(i) + "; " + y.get(i) + ")", x.get(i) + (Defines.maxZoom-cam.getZoom())/Defines.maxZoom, -y.get(i), g);
            else{
                cam.drawString("P" + (i + 1), x.get(i) + (Defines.maxZoom-cam.getZoom())/Defines.maxZoom, -y.get(i), g);
            }
        }

        g.setColor(Color.white);
        if (this.calculate){
            double fontSize = Defines.maxZoom + 10f - (float)cam.getZoom();

            g.drawString("average x: " + String.format("%.3g%n", avgX), 5, (float)(5 + fontSize));
            g.drawString("average y: " + String.format("%.3g%n", avgY), 5, (float)(5 + fontSize*2));
            g.drawString("σx: " + String.format("%.3g%n", dx), 5, (float)(5 + fontSize*3));
            g.drawString("σy: " + String.format("%.3g%n", dy), 5, (float)(5 + fontSize*4));
            g.drawString("σxy: " + String.format("%.3g%n", dxy), 5, (float)(5 + fontSize*5));
            g.drawString("r: " + String.format("%.3g%n", r), 5, (float)(5 + fontSize*6));
            g.drawString("r^2: " + String.format("%.3g%n", r*r), 5, (float)(5 + fontSize*7));
            g.drawString(fx, 5, (float)(5 + fontSize*8));
            g.drawString(fy, 5, (float)(5 + fontSize*9));

            cam.drawLine(this.minX, -1 * ((this.mx * this.minX) + this.qx), 
                        this.maxX, -1 * ((this.mx * this.maxX) + this.qx), g);

            if (!Defines.showCoordinates){
                int num = 9;

                for (int i = 0; i < this.x.size(); i++){
                    num += 1;

                    g.drawString("P" + (i+1) + "(" + this.x.get(i) + "; " + this.y.get(i) + ");", 5, (float)(5 + fontSize*num));
                }
            }
        }
    }
}
