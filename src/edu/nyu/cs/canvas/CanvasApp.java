package edu.nyu.cs.canvas;

import edu.nyu.cs.canvas.controller.CanvasController;
import edu.nyu.cs.canvas.model.CanvasModel;
import edu.nyu.cs.canvas.view.CanvasView;

/**
 * This is the main class for launching a Canvas App. by default,
 * it creates two views which can be simultaneously drawn on. A range
 * of application behavior specific and paint specific setting are available
 * on each view UI.
 *
 * New views can be launched in two ways - i) stateful fork copies the existing
 * drawing on the new view, ii) blank view simply launches a blank view. The
 * last updates made by any view are enforced in the model and any present/future
 * views.
 *
 * For closing a single view click the "Remove This" button in the view. Clicking
 * the normal window close button will close the application and all the views.
 *
 * A singleton controller manages the application logic and data flow between
 * a singleton model and a variable number of views
 *
 * @author abhishek
 */
public class CanvasApp {
  /**
  * Main method for creating the singleton controller and model and two default views
  * @param argv
  */
  public static void main(String[] argv) {
    CanvasModel model = CanvasModel.getInstance();
    CanvasController controller = CanvasController.getInstance(model);
    CanvasView view1 = new CanvasView(controller);
    CanvasView view2 = new CanvasView(controller);
    controller.registerView(view1);
    controller.registerView(view2);
    view1.displayView();
    view2.displayView();
  }
}