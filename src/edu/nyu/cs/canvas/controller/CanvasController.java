package edu.nyu.cs.canvas.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import edu.nyu.cs.canvas.CanvasConfig;
import edu.nyu.cs.canvas.model.CanvasModel;
import edu.nyu.cs.canvas.model.CanvasPoint;
import edu.nyu.cs.canvas.view.CanvasObserver;
import edu.nyu.cs.canvas.view.CanvasView;

/**
 * This is the controller class for the Canvas application. It acts
 * as a conduit to transfer data/instruction between model and view
 *
 * It is designed to follow a singleton pattern as only one controller
 * object is required an application. It acts along with a singleton model
 * and multiple observers(views)
 *
 * @author abhishek
 */
public final class CanvasController {
  private List<CanvasObserver> observers;
  private CanvasModel model;
  private static CanvasController controller = null;
  public ArrayList<CanvasObserver> getObservers() {
    return (ArrayList<CanvasObserver>) this.observers;
  }

  /**
  * Static method for returning the contrller object
  * @param model (singleton) model object
  * @return a (singleton) controller object
  */
  public static CanvasController getInstance(CanvasModel model) {
    if(controller == null && model != null) {
      controller = new CanvasController(model);
    }
    return (model == null) ? null : controller;
  }

  /**
  * Private constructor to enforce Singleton pattern
  * @param model (singleton) object representing the model
  */
  private CanvasController(CanvasModel model) {
    this.model = model;
    this.observers = new ArrayList<CanvasObserver>();
  }

  /**
  * Registers a newly created view with the controller
  * @param observer is the newly created view/observer
  */
  public void registerView(CanvasObserver observer) {
    if(observer instanceof CanvasObserver) {
      observers.add(observer);
      observer.changeColor(this.model.getColor());
      observer.changeStroke(this.model.getStroke());
    }
  }

  /**
  * Creates a new stateful view by launching and registering a blank view,
  * sending it the current state from model and asking it
  * to repaint it's canvas from this state
  */
  public void viewAddCanvas() {
    CanvasView newView = new CanvasView(this);
    this.registerView(newView);
    newView.displayView();
    newView.copyContent(this.model.getAllPoints());
  }

  /**
  * Creates a blank view and registers it
  */
  public void viewAddBlankCanvas() {
    CanvasView newView = new CanvasView(this);
    this.registerView(newView);
    newView.displayView();
  }

  /**
  * De-registers a view
  * @param ID is the ID of the view
  * @return returns the reference to the view being released
  */
  public CanvasObserver deregisterObserver(int ID) {
    CanvasObserver toRemoveObserver = null;
    int toRemoveID = -1;
    for(int i = 0; i < observers.size(); ++i) {
      if(((CanvasView) observers.get(i)).getID() == ID) {
        toRemoveID = i;
      }
    }
    if(toRemoveID != -1) {
      toRemoveObserver = observers.get(toRemoveID);
      observers.remove(toRemoveID);
    }
    return toRemoveObserver;
  }

  /**
  * Removes a current view when requested by user and unregisters it
  * @param ID is the ID of the view requested to be removed
  */
  public void viewRemoveCanvas(int ID) {
    CanvasView toRemove = (CanvasView)deregisterObserver(ID);
    if(toRemove != null) {
      toRemove.getMainFrame().dispose();
    }
  }

  /**
  * Updates newly created views about the model state
  */
  public void viewUpdate() {
    for(CanvasObserver observer : observers) {
      if(observer instanceof CanvasObserver) {
        observer.changeColor(this.model.getColor());
        observer.changeStroke(this.model.getStroke());
      }
    }
  }

  /**
  * Releases all observers and resets total canvas count to 0
  */
  public void releaseObservers() {
    this.observers.clear();
    CanvasView.canvasCount = 0;
  }

  /**
  * When a reset is requested by user, it resets the model as
  * well as the canvas and paint settings state of all views
  */
  public void viewReset() {
    this.model.resetModel();
    for(CanvasObserver observer : observers) {
      if(observer instanceof CanvasObserver) {
        observer.reset();
      }
    }
  }

  /**
  * Updates the model when user picks a color and notifies all views to
  * update their current color
  * @param color is the color selected by the user in the requesting view
  */
  public void viewUpdate(Color color) {
    this.model.setColor(color);
    for(CanvasObserver observer : observers) {
      if(observer instanceof CanvasObserver) {
        observer.changeColor(color);
      }
    }
  }

  /**
  * On mouse press event in any view, updates the model
  * about the start of a new segment
  * @param point
  */
  public void viewUpdate(CanvasPoint point) {
    this.model.addPoint(point);
  }

  /**
  * Updates the model when user selects a stroke and notifies all views to
  * update their current stroke slider
  * @param stroke is the stroke selected by the user in the requesting view
  */
  public void viewUpdate(int stroke) {
    this.model.setStroke(stroke);
    for(CanvasObserver observer : observers) {
      if(observer instanceof CanvasObserver) {
        observer.changeStroke(stroke);
      }
    }
  }

  /**
  * On mouse drag event on any of the views inserts a custom end marker
  * to mark the end of a segment which helps in the custom draw method of the canvas
  * @see edu.nyu.cs.canvas.model.CanvasModel
  */
  public void segmentEnd() {
    this.model.addPoint(CanvasConfig.getEndMarker());
  }

  /**
  * When mouse drag event occurs, it updates the segment end point in model and
  * notifies all views to draw the segment
  * @param startPoint
  * @param endPoint
  */
  public void viewUpdate(CanvasPoint startPoint, CanvasPoint endPoint) {
    this.model.addPoint(endPoint);
    for(CanvasObserver observer : observers) {
      if(observer instanceof CanvasObserver) {
        observer.drawLine(startPoint, endPoint);
        observer.setStartPoint(endPoint);
      }
    }
  }
}