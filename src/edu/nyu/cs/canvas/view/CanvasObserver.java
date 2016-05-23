package edu.nyu.cs.canvas.view;

import java.awt.Color;
import java.util.ArrayList;
import edu.nyu.cs.canvas.model.CanvasPoint;

/**
 * This is the baseline interface for an observer. The view of Canvas application
 * or any other class that chooses to obey observer pattern must implement the methods
 * of the interface
 * @author abhishek
 */
public interface CanvasObserver {

  /**
  * Draws a line segment between two point objects with geometric and paint properties
  * @param startPoint marks the start position of the segment
  * @param endPoint marks the end position of the segment
  */
  public void drawLine(CanvasPoint startPoint, CanvasPoint endPoint);

  /**
  * setter for changing the color property of all views
  * @param color is the modified color in model
  */
  public void changeColor(Color color);

  /**
  * setter for changing the stroke property of all views
  * @param stroke is modified stroke in model
  */
  public void changeStroke(int stroke);

  /**
  * resets all the views to default configurations
  * @see edu.nyu.cs.canvas.CanvasConfig
  */
  public void reset();

  /**
  * notifies a view that a stateful update is requested, so that the new view
  * can copy the common drawing state of existing views
  * @param allPoints is the present common view state of all points/segments
  * with their geometric and paint properties. This parameter is passed from the
  * model through controller to the observer
  */
  public void copyContent(ArrayList<CanvasPoint> allPoints);

  /**
  * Updates all observers to update the new start point for drawing once one
  * piece of drawing is completed
  * @param endPoint is the 'model entity point' where the current point/line ends
  * @see CanvasPoint
  */
  public void setStartPoint(CanvasPoint endPoint);
}