package edu.nyu.cs.canvas;
import java.awt.Color;
import edu.nyu.cs.canvas.model.CanvasPoint;

/**
 * This class represents the default configuration items for the Canvas application items
 * @author abhishek
 */
public class CanvasConfig {
  //default start location for a view UI
  static public final int startX = 100;
  static public final int startY = 100;
  //default paint settings
  static public final int defaultStroke = 2;
  static public final Color defaultColor = Color.BLUE;
  //default windw settings
  static public final int defaultHeight = 600;
  static public final int defaultWidth = 800;
  //default method for returning an end marker
  static public CanvasPoint getEndMarker() {
    return new CanvasPoint.Builder(-1, -1).build();
  }
}