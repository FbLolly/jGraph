package utilsPkg;

import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import mainPkg.Defines;
import mainPkg.JGraph;

public class KeyHandler {
	public boolean pressedEscape;
	public int zoom; //either -1, 0 or 1
	public String str;

	public String numX = "", numY = "";
	public boolean done;

	public Point direction;
	public int instruction; //0 = nothing, 1 = create the recession line, r and r^2, 2 = create the power line
	
	public KeyHandler() {
		this.pressedEscape = false;
		this.zoom = 0;
		this.str = "";
		this.done = false;
		this.instruction = 0;

		direction = new Point(0, 0);
	}
	
	public DoublePoint handleKeys(JGraph jshooter) {
		//returns a null point if nothing important happened, returns a point to add if you have to add a point

		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("Z"), "z");
		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("U"), "u");

		for (char i = '0'; i <= '9'; i++){
			jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke(i + ""), i + "");
		}

		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("M"), "-");
		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("P"), ".");

		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("RIGHT"), "right");
		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("LEFT"), "left");
		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("UP"), "up");
		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("DOWN"), "down");

		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("released RIGHT"), "rh");
		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("released LEFT"), "rh");
		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("released UP"), "rv");
		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("released DOWN"), "rv");

		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("C"), "c");
		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("R"), "r");

		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("N"), "n");
		jshooter.getInputMap(Defines.IFW).put(KeyStroke.getKeyStroke("ENTER"), "enter");

		
		jshooter.getActionMap().put("escape", new PressedEsc());
		jshooter.getActionMap().put("z", new PressedZ());
		jshooter.getActionMap().put("u", new PressedU());

		for (char i = '0'; i <= '9'; i++){
			jshooter.getActionMap().put(i + "", new PressedNumber(i));
		}

		jshooter.getActionMap().put("-", new PressedMinus());
		jshooter.getActionMap().put(".", new PressedPoint());

		jshooter.getActionMap().put("right", new PressedRight());
		jshooter.getActionMap().put("left", new PressedLeft());
		jshooter.getActionMap().put("up", new PressedUp());
		jshooter.getActionMap().put("down", new PressedDown());

		jshooter.getActionMap().put("rh", new ReleasedHor());
		jshooter.getActionMap().put("rv", new ReleasedVer());

		jshooter.getActionMap().put("c", new PressedC());
		jshooter.getActionMap().put("r", new PressedR());

		jshooter.getActionMap().put("enter", new PressedEnter());
		jshooter.getActionMap().put("n", new PressedN());

		if (done){
			done = false;
			str = "";
			DoublePoint p = new DoublePoint(Double.parseDouble(numX), Double.parseDouble(numY));
			numX = "";
			numY = "";
			return p;
		}

		return null;
	}
	
	public class PressedEsc extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			pressedEscape = true;
		}
	}

	public class PressedZ extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			zoom = 1;
		}
	}

	public class PressedU extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			zoom = -1;
		}
	}

	public class PressedNumber extends AbstractAction{
		private char get;
		
		public PressedNumber(char get){
			this.get = get;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			str += this.get;
		}
	}

	public class PressedMinus extends AbstractAction{

		@Override
		public void actionPerformed(ActionEvent e) {
			if (str.length() < 1){
				str += "-";
			}
		}
	}

	public class PressedPoint extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (str.length() >= 1){
				str += ".";
			}
		}
	}

	public class PressedLeft extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			direction.x = 1;
		}
	}
	public class PressedRight extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			direction.x = -1;
		}
	}
	public class PressedUp extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			direction.y = 1;
		}
	}
	public class PressedDown extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			direction.y = -1;
		}
	}

	public class ReleasedHor extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			direction.x = 0;
		}
	}
	public class ReleasedVer extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			direction.y = 0;
		}
	}

	public class PressedC extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			instruction = 1;
		}
		
	}

	public class PressedR extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			instruction = 2;
		}
	}

	public class PressedN extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (Defines.showCoordinates)
				Defines.showCoordinates = false;
			else
				Defines.showCoordinates = true;
		}
		
	}

	public class PressedEnter extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (str.equals(""))
				return;

			if (numX.equals("")){
				numX = new String(str);
				str = "";
				return;
			}

			numY = new String(str);
			str = "";

			if ((!numX.equals("")) && (!numY.equals(""))){
				done = true;
				str = "";
				return;
			}
		}
	}
}
