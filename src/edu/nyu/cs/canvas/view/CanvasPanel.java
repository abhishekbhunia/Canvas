package edu.nyu.cs.canvas.view;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import edu.nyu.cs.canvas.CanvasConfig;
import edu.nyu.cs.canvas.model.CanvasPoint;

/**
 * Custom JPanel for emulating a canvas with modified paint properties.
 * It is aware of observer updates, therefore can make stateful as well
 * as stateless updates
 * @author abhishek
 */
@SuppressWarnings("serial")
public class CanvasPanel extends JPanel {

  //stores points transferred from controller when a stateful update is requested
  private ArrayList<CanvasPoint> allPoints;

  /**
  * Reusable marker for skipping gaps between disjoint line segments
  * Uses a static marker to prevent creating multiple markers, which are high in demand
  */
  private static CanvasPoint endMarker = CanvasConfig.getEndMarker();

  /**
  * Constructor for custom Canvas
  */
  protected CanvasPanel() {
    allPoints = new ArrayList<CanvasPoint>();
  }

  /**
  * setter for carrying the stateful update from controller
  * @param allPoints is the set of 'model entities' presently drawn and
  * shared by other canvases
  * @see CanvasPoint
  */
  protected void setPoints(ArrayList<CanvasPoint> allPoints) {
    this.allPoints = allPoints;
  }

  /**
  * overrides the paint-component method to provide application specific paint behavior
  */
  @Override
  public void paintComponent(final Graphics g) {
    super.paintComponent(g);
    CanvasPoint start = endMarker;
    CanvasPoint end = endMarker;
    for(int i = 0; i < allPoints.size() - 1; ++i) {
      CanvasPoint prev = allPoints.get(i);
      CanvasPoint next = allPoints.get(i+1);
      if((prev.getX() == -1) && (prev.getY() == -1) &&
        (next.getX() == -1) && (next.getY() == -1)) {
        start = endMarker;
        end = endMarker;
      }
      else {
        if (start.isEndMarker()) {
          start = (prev.isEndMarker() ? next : prev);
        }
        if (end.isEndMarker()) {
          end = (prev.isEndMarker() ? next : prev);
        }
        if (!start.isEndMarker() && !end.isEndMarker()) {
          Graphics2D g2 = (Graphics2D) g;
          g2.setStroke(new BasicStroke(start.getStroke()));
          g2.setColor(start.getColor());
          g2.draw(new Line2D.Float(start.getX(), start.getY(), end.getX(), end.getY()));
          start = end;
          end = endMarker;
        }
      }
    }
  }

  /**
  * repaint the canvas when a visual update is required
  */
  protected void draw() {
    repaint();
  }

  /**
  * draws a line segment on the custom canvas
  * @see CanvasPanel
  * @param startPoint is the staring point of the segment
  * @param endPoint is the terminating point of the segment
  */
  protected void drawLine(CanvasPoint startPoint, CanvasPoint endPoint) {
    Graphics graphics = this.getGraphics();
    graphics.setColor(startPoint.getColor());
    Graphics2D g2D = (Graphics2D) graphics;
    g2D.setStroke(new BasicStroke(startPoint.getStroke()));
    g2D.draw(new Line2D.Float(startPoint.getX(), startPoint.getY(),
      endPoint.getX(), endPoint.getY()));
  }
}