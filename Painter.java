import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.event.*;
import java.util.Vector;
import java.awt.BasicStroke; 

public class Painter extends Applet{

	Vector<Shape> shapes;
	public static Color shapeColor = Color.BLACK;
	public static int currentShape = Shape.lineMode;
	public static boolean filled = false;
	public static boolean dotted = false;		
	private Checkbox fillChk;
	private Checkbox dottChk;
	private Button redButton, greenButton,blueButton;
	private Button rectButton, lineButton, ovalButton, freeButton, eraserButton;
	private Button resetButton;

	public void init(){
		shapes = new Vector<Shape>();
		shapes.add(new Line());
	}
	public void start(){
		this.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				shapes.lastElement().startX=e.getX();
				shapes.lastElement().startY=e.getY();
				shapes.lastElement().endX=e.getX();
				shapes.lastElement().endY=e.getY();
			}
			public void mouseReleased(MouseEvent e){

				if(e.getX()>=getWidth())  shapes.lastElement().endX=getWidth(); 
				else  shapes.lastElement().endX=e.getX();

				if(e.getY()>=getHeight())  shapes.lastElement().endY=getHeight();				
				else  shapes.lastElement().endY=e.getY();

				if( shapes.lastElement().dragged ){
					
				switch(currentShape)
				{
					case Shape.lineMode:
						shapes.add(new Line());
						break;
					case Shape.rectMode:
					    shapes.add(new Rectangle());
						break;
					case Shape.ovalMode:
					    shapes.add(new Oval());
						break;
					case Shape.freeMode :
					    shapes.add(new FreeDrawing());
						break;
					case Shape.eraserMode:
				        shapes.add(new Eraser());
						break;		
					default:
						shapes.add(new Line());
						break;
				
				}
				shapes.lastElement().color = shapeColor;
				shapes.lastElement().filled = filled;
				}
				else{
					shapes.remove(shapes.size()-1);
				}
				repaint();
			}
		});
		this.addMouseMotionListener(new MouseAdapter(){
			public void mouseDragged(MouseEvent e){
				shapes.lastElement().dragged=true;
				shapes.lastElement().endX = e.getX();
				shapes.lastElement().endY = e.getY();

				repaint();
			}  
		});
		redButton = new Button("red");
		redButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				shapeColor = Color.RED;
				shapes.lastElement().color = shapeColor;
			} 
		});
		greenButton = new Button("green");
		greenButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				shapeColor = Color.GREEN;
				shapes.lastElement().color = shapeColor;

			} 
		});
		blueButton = new Button("blue");
		blueButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				shapeColor = Color.BLUE;
				shapes.lastElement().color = shapeColor;

			} 
		});
		add(redButton);
		add(greenButton);
		add(blueButton);	
		lineButton = new Button("Line");
		lineButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				currentShape = Shape.lineMode;
				shapes.add(new Line());
				shapes.lastElement().color = shapeColor;
			}
		});
		ovalButton = new Button("Oval");
		ovalButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				currentShape = Shape.ovalMode;
				shapes.add(new Oval());
				shapes.lastElement().color=shapeColor;
				shapes.lastElement().filled = filled;
			}
		});
		rectButton = new Button("Reactangle");
		rectButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				currentShape = Shape.rectMode;
				shapes.add(new Rectangle());
				shapes.lastElement().color=shapeColor;
				shapes.lastElement().filled = filled;
			}
		});
		freeButton = new Button("Free draw");
		freeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				currentShape = Shape.freeMode;
				shapes.add(new FreeDrawing());
				shapes.lastElement().color=shapeColor;
			}
		});
		eraserButton = new Button("Erase");
		eraserButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				currentShape = Shape.eraserMode;
				shapes.add(new Eraser());
			}
		});
		add(lineButton);
		add(rectButton);
		add(ovalButton);
		add(freeButton);
		add(eraserButton);

		fillChk =new Checkbox("Filled");
		fillChk.addItemListener( new ItemListener(){
			public void itemStateChanged(ItemEvent e){
                if(fillChk.getState()){
					filled = true;
				}
				else{
					filled = false;
				}
				shapes.lastElement().filled = filled;
			}
		});
		add(fillChk);
		dottChk =new Checkbox("Dotted");
		dottChk.addItemListener( new ItemListener(){
			public void itemStateChanged(ItemEvent e){
                if(dottChk.getState()){
					dotted = true;
				}
				else{
					dotted = false;
				}
				shapes.lastElement().dotted = dotted;
			}
		});
		add(dottChk);

		resetButton = new Button("Reset");
		resetButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				shapes.removeAllElements();
				shapes.add(new Rectangle());
				shapes.lastElement().color = Color.WHITE;
				repaint();
			}
		});
		add(resetButton);
	}
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D) g;		
		for(int i=0;i<shapes.size();i++){
			if( shapes.get(i).dragged ){
				g2d.setColor(shapes.get(i).color );
				shapes.get(i).draw(g2d,shapes.get(i).startX, shapes.get(i).startY, shapes.get(i).endX, shapes.get(i).endY);
			}
		}
	}
}

abstract class Shape{
	int startX, startY, endX, endY;
	public Color color;
	public boolean filled;
	public boolean dotted;
	boolean dragged;
	public static final int lineMode = 0;
	public static final int ovalMode = 1;
	public static final int rectMode = 2;
	public static final int freeMode = 3;
	public static final int eraserMode = 4;

	public abstract void draw(Graphics2D  g,int startX, int startY, int endX, int endY);
}
class Line extends Shape{
	public void draw(Graphics2D  g,int startX, int startY, int endX, int endY){
		g.setColor(color);
        if (dotted) {
            Stroke dottedStroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{2, 2}, 0); 
            g.setStroke(dottedStroke); 
        }
        g.drawLine(startX, startY, endX, endY); 
	}
}

class Oval extends Shape{
	private int width, height;
	private int temp;

	 public void draw(Graphics2D  g2d,int startX, int startY, int endX, int endY) {
        g2d.setColor(color);
        if (dotted) {
            Stroke dottedStroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{2, 2}, 0); 
            g2d.setStroke(dottedStroke); 
        }
        if (filled) {
            g2d.fillOval(Math.min(startX, endX), Math.min(startY, endY), 
                    Math.abs(endX - startX), Math.abs(endY - startY));
        } else {
            g2d.drawOval(Math.min(startX, endX), Math.min(startY, endY), 
                    Math.abs(endX - startX), Math.abs(endY - startY));
        }
    }
}
class Rectangle extends Shape{
	private int width;
	private int height;
	private int temp;
	
	public void draw(Graphics2D  g2d,int startX, int startY, int endX, int endY) {
        g2d.setColor(color);
        if (dotted) {
            Stroke dottedStroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{2, 2}, 0); 
            g2d.setStroke(dottedStroke); 
        }
        if (filled) {
            g2d.fillRect(Math.min(startX, endX), Math.min(startY, endY), 
                    Math.abs(endX - startX), Math.abs(endY - startY));
        } else {
            g2d.drawRect(Math.min(startX, endX), Math.min(startY, endY), 
                    Math.abs(endX - startX), Math.abs(endY - startY));
        }
    }
}
class FreeDrawing extends Shape{
	private int oldX, oldY;
	private Vector<Line> freeLines;
	public FreeDrawing(){
		freeLines = new Vector();
	}
	public void draw(Graphics2D  g,int startX, int startY, int endX, int endY){
		if (oldX == 0 && oldY == 0){
			oldX = startX;
			oldY = startY;
		}
		freeLines.add(new Line());
		freeLines.lastElement().startX = oldX;
		freeLines.lastElement().startY = oldY;
		freeLines.lastElement().endX = endX;
		freeLines.lastElement().endY = endY;
		for(Line line : freeLines){
			line.draw(g,line.startX,line.startY,line.endX,line.endY);
		}	
		oldX=endX;
		oldY=endY;
	}
}
class Eraser extends Shape{
	private int oldX , oldY;
	Vector<Rectangle> rects;
	Rectangle movingEraser;
			
	public Eraser(){
		rects=new Vector();
	}
			
	public void draw(Graphics2D  g,int x1, int y1, int x2, int y2){	
		if(oldX == 0 && oldY == 0) {oldX=x1;oldY=y1;}
		if(dragged){	
			rects.add(new Rectangle());
			rects.lastElement().startX = oldX - 8;
			rects.lastElement().startY= oldY - 8;
			rects.lastElement().endX = x2 + 8;
			rects.lastElement().endY = y2 + 8;
				 
			rects.lastElement().filled=true;
			g.setColor(Color.white);
		    for(Rectangle rect : rects)
				rect.draw(g,rect.startX,rect.startY,rect.endX,rect.endY);
		}				
		else{
			movingEraser=new Rectangle();
			movingEraser.startX = oldX - 8;
			movingEraser.startY  = oldY - 8;
			movingEraser.endX = x2 + 8;
			movingEraser.endY = y2 + 8;
			movingEraser.filled=false;
			g.setColor(Color.black);          				    
			movingEraser.draw(g,movingEraser.startX,movingEraser.startY,movingEraser.endX,movingEraser.endY);
			}
			oldX=x2;
			oldY=y2;
	} 
}