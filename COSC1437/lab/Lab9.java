package Lab9;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Lab9 {
	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 700);
		MyPanel panel = new MyPanel();
		frame.add(panel);
		frame.setVisible(true);
		frame.addMouseListener(panel);
	

		// thread of execution
		while (true) {
			Thread.sleep(40);
			panel.updateValue();
			panel.repaint();
		}
	}

}

interface CanBeDrawn {
	void draw(Graphics g);
}

class MyPanel extends JPanel implements MouseListener{
	int angleOfProgressCircle = 0; // use to draw Progress Circle
	boolean isRed = true; // use to draw Progress Circle
	
	int frameNumber = 0;
	public final int frameWidth = 110; // adjust
	private ArrayList<ClickObject> clickObjects = new ArrayList<>();
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		Arrow a = new Arrow(new Point(50, 50), new Point(150, 180));

		drawPentagon(g);
		a.draw(g);

		Cube cube = new Cube(100, 100, 50, new Color[] { Color.yellow, Color.green, Color.pink });
		cube.draw(g);

		drawImage(g);

		Sphere sphere = new Sphere(300, 250,75);
		sphere.draw(g);

		drawTriangularPrism(g);

		draw5pointedStar(g);
		 
		drawProgressCircle(g);
	
		drawClickObjects(g);
	}
	

	public void drawPentagon(Graphics g) {
		g.setColor(Color.black);
		Polygon polygon = new Polygon();
		int x = 300;
		int y = 50;
		int size = 150;
		polygon.addPoint(x, y);
		polygon.addPoint(x - size * 3 / 5, y + size * 2 / 5);
		polygon.addPoint(x - size * 2 / 5, y + size);
		polygon.addPoint(x + size * 2 / 5, y + size);
		polygon.addPoint(x + size * 3 / 5, y + size * 2 / 5);
		g.drawPolygon(polygon);

	}

	public void updateValue() {
		frameNumber = (frameNumber + 1) % 6;
		angleOfProgressCircle = (angleOfProgressCircle + 5) % 360;
		if (angleOfProgressCircle == 0) {
			isRed = !isRed;
		}

	}

	private void drawImage(Graphics g) {

		try {

			BufferedImage bi = ImageIO.read(new File("runner.png"));
			g.drawImage(bi.getSubimage(frameWidth * frameNumber, 0, frameWidth, 181), 750, 50, null);

		} catch (Exception e) {

		}
	}

	public void drawTriangularPrism(Graphics g) {
		int edge = 80; // edge of rectangle
		int length = 100;
		Point[] points = new Point[6];
		points[0] = new Point(100, 300);
		points[1] = new Point((int) (points[0].x + edge * Math.cos(Math.toRadians(60))),
				(int) (points[0].y + edge * Math.sin(Math.toRadians(60))));
		points[2] = new Point((int) (points[0].x - edge * Math.cos(Math.toRadians(60))),
				(int) (points[0].y + edge * Math.sin(Math.toRadians(60))));

		points[3] = new Point((int) (points[0].x + length * Math.cos(Math.toRadians(20))),
				(int) (points[0].y - length * Math.sin(Math.toRadians(20))));
		points[4] = new Point((int) (points[1].x + length * Math.cos(Math.toRadians(20))),
				(int) (points[1].y - length * Math.sin(Math.toRadians(20))));

		points[5] = new Point((int) (points[2].x + length * Math.cos(Math.toRadians(20))),
				(int) (points[2].y - length * Math.sin(Math.toRadians(20))));
		g.setColor(Color.black);
		Polygon front = new Polygon();
		front.addPoint(points[0].x, points[0].y);
		front.addPoint(points[1].x, points[1].y);
		front.addPoint(points[2].x, points[2].y);
		g.drawPolygon(front);
		front.reset();

		front.addPoint(points[0].x, points[0].y);
		front.addPoint(points[3].x, points[3].y);
		front.addPoint(points[4].x, points[4].y);
		front.addPoint(points[1].x, points[1].y);

		g.drawPolygon(front);

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[] { 5 }, 2));
		g2.drawLine(points[2].x, points[2].y, points[5].x, points[5].y);
		g2.drawLine(points[3].x, points[3].y, points[5].x, points[5].y);
		g2.drawLine(points[4].x, points[4].y, points[5].x, points[5].y);
		g2.setStroke(new BasicStroke());
	}
	
	public void draw5pointedStar(Graphics g) {
		Point middle = new Point(600, 350);
		Point[] inPoints = new Point[5];
		Point[] outPoints = new Point[5];
		int inSize = 40;
		int outSize = 100;
		Polygon star = new Polygon();
		
		for (int i = 0; i < 5; i++) {
			inPoints[i] = new Point();
			outPoints[i] = new Point();
			inPoints[i].x = (int) (middle.x + inSize * Math.cos(Math.toRadians(72 * i))); 
			inPoints[i].y = (int) (middle.y + inSize * Math.sin(Math.toRadians(72 * i))); 
			outPoints[i].x = (int) (middle.x + outSize * Math.cos(Math.toRadians(36 + 72 * i))); 
			outPoints[i].y = (int) (middle.y + outSize * Math.sin(Math.toRadians(36 + 72 * i)));
			
			star.addPoint(inPoints[i].x, inPoints[i].y);
			star.addPoint(outPoints[i].x, outPoints[i].y);
		}
		g.drawPolygon(star);
		
		// Paint star
		Color[] colors = {Color.red, Color.orange, Color.yellow, Color.green, Color.blue};
		for (int i = 0; i < 5; i++) {
			star.reset();
			star.addPoint(middle.x, middle.y);
			star.addPoint(inPoints[i].x, inPoints[i].y);
			star.addPoint(outPoints[i].x, outPoints[i].y);
			star.addPoint(inPoints[(i + 1) % 5].x, inPoints[(i + 1) % 5].y);
			g.setColor(colors[i]);
			g.fillPolygon(star);
		}
	}
	
	public void drawProgressCircle(Graphics g) {
		int x = 800;
		int y = 300;
		int radius = 50;
		g.setColor(Color.black);
		g.drawOval(x, y, 2 * radius, 2 * radius);
		g.setColor(isRed ? Color.red : Color.green);
		g.fillOval(x, y, 2 * radius, 2 * radius);
		g.setColor(isRed ? Color.green : Color.red);
		g.fillArc(x, y, 2 * radius, 2 * radius, 0, angleOfProgressCircle);
	}

	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		clickObjects.add(new ClickObject(p));
	}
	public void mouseEntered(MouseEvent e)	{}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {};
	public void mouseReleased(MouseEvent e)	{}

	public void drawClickObjects(Graphics g) {
		ArrayList<ClickObject> newClickObjects = new ArrayList<>();
		for (ClickObject co : this.clickObjects) {
			co.draw(g);
			co.updateValue();
			if (!co.fadesCompletely()) {
				newClickObjects.add(co);
			}
		}
		clickObjects = newClickObjects;
	}
}

class Arrow implements CanBeDrawn {
	public final int arrowLength = 50;
	public final int arrowAngle = 45;
	private Point targetPoint;
	private Point startPoint;

	public Arrow(Point start, Point target) {
		this.startPoint = start;
		this.targetPoint = target;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.drawLine(startPoint.x, startPoint.y, targetPoint.x, targetPoint.y);
		// Polygon polygon = new Polygon();

		// Calculate 2 arrows
		double dx = targetPoint.x - startPoint.x;
		double dy = targetPoint.y - startPoint.y;
		double angle = Math.atan2(dy, dx);

		System.out.println(dx + ", " + dy);
		System.out.println(Math.toDegrees(angle));

		double angle1 = angle - Math.toRadians(arrowAngle);
		double angle2 = angle + Math.toRadians(arrowAngle);

		System.out.println(Math.toDegrees(angle1));
		System.out.println(Math.toDegrees(angle2));

		Point arrow1 = new Point((int) (targetPoint.x - arrowLength * Math.cos(angle1)),
				(int) (targetPoint.y - arrowLength * Math.sin(angle1)));
		Point arrow2 = new Point((int) (targetPoint.x - arrowLength * Math.cos(angle2)),
				(int) (targetPoint.y - arrowLength * Math.sin(angle2)));

		g.drawLine(arrow1.x, arrow1.y, targetPoint.x, targetPoint.y);
		g.drawLine(arrow2.x, arrow2.y, targetPoint.x, targetPoint.y);
	}

}

class Sphere implements CanBeDrawn {
	public int radius;
	public int x = 200;
	public int y = 200;

	public Sphere(int x, int y, int radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		float[] dist = { 0.0f, 1.0f };
		Color[] colors = { Color.white, Color.black };
		RadialGradientPaint rgp = new RadialGradientPaint(this.x + (int) (1.2f * this.radius), this.y + (int) (0.6f * this.radius), (int) (1.5 * this.radius), dist, colors);
		g2.setPaint(rgp);
		g2.fillOval(x, y, 2 * radius, 2 * radius);
	}
}

class Cube implements CanBeDrawn {
	public final int x = 500;
	public final int y = 100;
	public int angle = 45;
	public int height, width, depth;
	public Color[] colors;

	public Cube(int width, int height, int depth) {
		this(width, height, depth, null);
	}

	public Cube(int width, int height, int depth, Color[] colors) {
		this.height = height;
		this.width = width;
		this.depth = depth;
		if (colors == null) {
			this.colors = null;
		} else if (colors.length <= 3) {
			this.colors = colors;
		} else {
			this.colors = new Color[] { colors[0], colors[1], colors[2] };
		}
	}

	private boolean setColorForCube(Graphics g, String face) {
		if (colors == null)
			return false;
		if (face.equals("front")) {
			if (colors.length > 0 && colors[0] != null) {
				g.setColor(colors[0]);
				return true;
			}
		} else if (face.equals("top")) {
			if (colors.length > 1 && colors[1] != null) {
				g.setColor(colors[1]);
				return true;
			}
		} else if (face.equals("right")) {
			if (colors.length > 2 && colors[2] != null) {
				g.setColor(colors[2]);
				return true;
			}
		}
		return false;
	}

	private void drawTopFace(Graphics g) {
		double dAngle = Math.toRadians(angle);

		Polygon face = new Polygon(); // top
		face.addPoint(x, y);
		face.addPoint((int) (x + depth * Math.cos(dAngle)), (int) (y - depth * Math.sin(dAngle)));
		face.addPoint((int) (x + width + depth * Math.cos(dAngle)), (int) (y - depth * Math.sin(dAngle)));
		face.addPoint(x + width, y);
		g.setColor(Color.black);
		g.drawPolygon(face);
		if (setColorForCube(g, "top"))
			g.fillPolygon(face);
	}

	private void drawFrontFace(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(x, y, width, height);
		if (setColorForCube(g, "front"))
			g.fillRect(x, y, width, height);
	}

	private void drawRightFace(Graphics g) {
		double dAngle = Math.toRadians(angle);
		Polygon face = new Polygon(); // right
		face.addPoint(x + width, y);
		face.addPoint((int) (x + width + depth * Math.cos(dAngle)), (int) (y - depth * Math.sin(dAngle)));
		face.addPoint((int) (x + width + depth * Math.cos(dAngle)), (int) (y + height - depth * Math.sin(dAngle)));
		face.addPoint(x + width, y + height);
		g.setColor(Color.black);
		g.drawPolygon(face);
		if (setColorForCube(g, "right"))
			g.fillPolygon(face);
		
	}

	public void draw(Graphics g) {
		drawFrontFace(g);
		drawTopFace(g);
		drawRightFace(g);
	}
}

class ClickObject implements CanBeDrawn {
	Point clickPoint;
	int radius;
	private Color color;
	private Color border;
	public ClickObject(Point clickPoint) {
		this.clickPoint = clickPoint;
		this.radius = 10;
		this.color = new Color(37, 150, 190, 128);
		this.border = new Color(106, 90, 205,128);
	}
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke());
		g2.setColor(color);
		g2.fillOval(clickPoint.x - radius, clickPoint.y - radius, 2 * radius, 2 * radius);
		g2.setStroke(new BasicStroke(5));
		g2.setColor(border);
		g2.drawOval(clickPoint.x - radius, clickPoint.y - radius, 2 * radius, 2 * radius);
	}
	
	public void updateValue() {
		color = reduceOpacity(color, 5);
		border = reduceOpacity(border, 5);
		radius += 2;
	}
	public boolean fadesCompletely() {
		if (color.getAlpha()/255.0f == 0.0f) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static Color reduceOpacity(Color c, int amt) {
		int tempAlpha = c.getAlpha() - amt;
		tempAlpha = (tempAlpha > 0 ? tempAlpha : 0);
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), tempAlpha);
	}
}
