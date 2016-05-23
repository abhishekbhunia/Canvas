package edu.nyu.cs.canvas.model;

import java.awt.Color;
import edu.nyu.cs.canvas.CanvasConfig;

/**
 * This class designs the custom 'model entity' used by the model of the Canvas App
 * @author abhishek
 */
public class CanvasPoint {
  private final int X;
  private final int Y;
  private final Color color;
  private final int stroke;

  /**
  * Builder class for model entity
  * @author abhishek
  */
  public static class Builder {
    //required parameters
    private final int X;
    private final int Y;
    //optional parameters
    private Color color = CanvasConfig.defaultColor;
    private int stroke = CanvasConfig.defaultStroke;

    /**
    * Builder constructor
    * @param X is the x coordinate of a point in canvas
    * @param Y is the y coordinate of a point in canvas
    */
    public Builder(int X, int Y) {
      this.X = X;
      this.Y = Y;
    }

    /**
    * Builds a builder entity with paint property 'color'
    * @param color is the color of the point in canvas
    * @return returns an instance reference of model entity
    */
    public Builder pointWithColor(Color color) {
      this.color = color;
      return this;
    }

    /**
    * Builds a builder entity with paint property 'stroke'
    * @param stroke is the stroke/thickness of the point in canvas
    * @return returns an instance reference of model entity
    */
    public Builder pointWithStroke(int stroke) {
      this.stroke = stroke;
      return this;
    }

    /**
    * build method for actually creating the 'CanvasPoint' model entity
    * by calling the private constructor of 'CanvasPoint'
    * @return returns a 'CanvasPoint' object
    */
    public CanvasPoint build() {
      return new CanvasPoint(this);
    }
  }

  /**
  * getter for x coordinate of 'CanvasPoint' model entity
  * @return returns x coordinate of 'CanvasPoint' object
  */
  public int getX() {
    return this.X;
  }

  /**
  * getter for y coordinate of 'CanvasPoint' model entity
  * @return returns y coordinate of 'CanvasPoint' object
  */
  public int getY() {
    return this.Y;
  }

  /**
  * getter for 'CanvasPoint' model entity property 'color' that maps to the color
  * of a point drawn in canvas
  * @return returns the 'color' property of 'CanvasPoint' object
  */
  public Color getColor() {
    return this.color;
  }

  /**
  * getter for 'CanvasPoint' model entity property 'stroke' that maps to the stroke
  * of a point drawn in canvas
  * @return returns the 'stroke' property
  */
  public int getStroke() {
    return this.stroke;
  }

  /**
  * method check if this 'CanvasPoint' model entity marks the separation of two
  * disjoint line segments
  * @return true if this entity is a separation marker, false otherwise
  */
  public boolean isEndMarker() {
    if (this.getX() == -1 && this.getY() == -1) {
      return true;
    }
    return false;
  }

  /**
  * private constructor to disable outside initialization and enforce builder pattern
  * @param builder is the builder object to construct a 'CanvasPoint' model entity
  */
  private CanvasPoint(Builder builder) {
    this.X = builder.X;
    this.Y = builder.Y;
    this.color = builder.color;
    this.stroke = builder.stroke;
  }
}