package edu.nyu.cs.canvas.model;

import java.awt.Color;
import java.util.ArrayList;
import edu.nyu.cs.canvas.CanvasConfig;

/**
 * Model class for the Canvas. Model contains entities capturing both geometry
 * and paint properties, and method to manipulate geometry
 * @author abhishek
 */
public final class CanvasModel {
  private Color color;
  private int stroke;
  private static CanvasModel model = null;
  private static ArrayList<CanvasPoint> allPoints;

  /**
  * static getter for model object
  * @return singleton model object
  */
  public static CanvasModel getInstance() {
    if(model == null) {
      model = new CanvasModel();
      allPoints = new ArrayList<CanvasPoint>();
    }
    return model;
  }

  /**
  * Add a model entity to the list of existing entities in model
  * @param point is a model entity
  */
  public void addPoint(CanvasPoint point) {
    if(point != null) {
      allPoints.add(point);
    }
  }

  /**
  * @return returns list of model entities currently stored in model
  */
  public ArrayList<CanvasPoint> getAllPoints() {
    return allPoints;
  }

  /**
  * private constructor to disable external instantiation and enforce singleton pattern
  */
  private CanvasModel() {
    this.stroke = CanvasConfig.defaultStroke;
    this.color = CanvasConfig.defaultColor;
  }

  /**
  * resets the model and releases all held model entities
  */
  public void resetModel() {
    this.stroke = CanvasConfig.defaultStroke;
    this.color = CanvasConfig.defaultColor;
    allPoints.clear();
  }

  /**
  * setter for paint property
  * @param color is the updated paint property 'color'
  */
  public void setColor(Color color) {
    if(color != null) {
      this.color = color;
    }
  }

  /**
  * getter for paint property
  * @return returns the presently set paint property 'color'
  */
  public Color getColor() {
    return this.color;
  }

  /**
  * setter for paint property 'stroke'
  * @param stroke is the updated paint property 'stroke'
  */
  public void setStroke(int stroke) {
    if(stroke > 0) {
      this.stroke = stroke;
    }
  }

  /**
  * getter for paint property
  * @return returns the presently set paint property 'stroke'
  */
  public int getStroke() {
    return this.stroke;
  }
}