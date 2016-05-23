package edu.nyu.cs.canvas.test;

import static org.junit.Assert.*;
import java.awt.Color;
import org.junit.Before;
import org.junit.Test;
import edu.nyu.cs.canvas.CanvasConfig;
import edu.nyu.cs.canvas.controller.CanvasController;
import edu.nyu.cs.canvas.model.*;
import edu.nyu.cs.canvas.view.CanvasView;

public class CanvasControllerTest {
  private CanvasModel model;
  private CanvasController controller;

  @Before
  public void setUp() throws Exception {
    this.model = CanvasModel.getInstance();
    this.controller = CanvasController.getInstance(this.model);
  }

  @Test
  public void testSingleton() {
    CanvasController controller1 = CanvasController.getInstance(this.model);
    CanvasController controller2 = CanvasController.getInstance(this.model);
    assertTrue("Different instances detected",controller1.equals(controller2));
  }

  @Test
  public void testNullModelRegistration() {
    assertTrue(CanvasController.getInstance(null) == null);
  }

  @Test
  public void testNullObserver() {
    this.controller.registerView(null);
    boolean nullFound = false;
    assertTrue("Null observer should not be registered",
      this.controller.getObservers().size() == 0);
  }

  @Test
  public void testObserver_Registration() {
    CanvasView view1 = new CanvasView(this.controller);
    this.controller.registerView(view1);
    CanvasView view2 = new CanvasView(this.controller);
    this.controller.registerView(view2);
    CanvasView view3 = new CanvasView(this.controller);
    this.controller.registerView(view3);
    boolean foundAndOrdered = true;
    for(int i = 0; i < 3; ++i) {
      if (((CanvasView)this.controller.getObservers().get(i)).getID() != i+1) {
        foundAndOrdered = false;
        break;
      }
    }
    assertTrue("Wrong observer count",this.controller.getObservers().size() == 3);
    assertTrue("Tagged observer not found",foundAndOrdered);
    this.controller.releaseObservers();
  }

  @Test
  public void testObserver_DeRegistration() {
    CanvasView view1 = new CanvasView(this.controller);
    this.controller.registerView(view1);
    CanvasView view2 = new CanvasView(this.controller);
    this.controller.registerView(view2);
    CanvasView view3 = new CanvasView(this.controller);
    this.controller.registerView(view3);
    boolean notFound = true;
    this.controller.deregisterObserver(1);
    for(int i = 0; i < 2; ++i) {
      if (((CanvasView)this.controller.getObservers().get(i)).getID() == 1) {
        notFound = false;
        break;
      }
    }
    assertTrue("fails de-registering observer", notFound);
    assertTrue("wrong count of observers after de-registering",
      this.controller.getObservers().size() == 2);
    this.controller.releaseObservers();
  }

  @Test
  public void testAddRemoveObserver() {
    assertTrue("Should not detect any observer", this.controller.getObservers().size() == 0);
    CanvasView view1 = new CanvasView(this.controller);
    this.controller.registerView(view1);
    CanvasView view2 = new CanvasView(this.controller);
    this.controller.registerView(view2);
    assertTrue("Observer addition fails", this.controller.getObservers().size() == 2);
    this.controller.releaseObservers();
    assertTrue("Observer removal fails", this.controller.getObservers().size() == 0);
  }

  @Test
  public void testUpdates() {
    CanvasView view1 = new CanvasView(this.controller);
    this.controller.registerView(view1);
    CanvasView view2 = new CanvasView(this.controller);
    this.controller.registerView(view2);
    assertTrue("Wrong default values", view1.getColor().equals(CanvasConfig.defaultColor));
    assertTrue("Wrong default values", view1.getStroke() == CanvasConfig.defaultStroke);
    assertTrue("Wrong default values", view2.getColor().equals(CanvasConfig.defaultColor));
    assertTrue("Wrong default values", view2.getStroke() == CanvasConfig.defaultStroke);
    controller.viewUpdate(Color.RED);
    controller.viewUpdate(5);
    assertTrue(this.model.getColor() == Color.RED);
    assertTrue(this.model.getStroke() == 5);
    assertTrue("Wrong update to observer", view1.getColor().equals(Color.RED));
    assertTrue("Wrong update to observer", view1.getStroke() == 5);
    assertTrue("Wrong update to observer", view2.getColor().equals(Color.RED));
    assertTrue("Wrong update to observer", view2.getStroke() == 5);
    this.model.resetModel();
    this.controller.releaseObservers();
  }

  @Test
  public void testPointAdditionToModel() {
    this.controller.viewUpdate(new CanvasPoint.Builder(9, 8).build());
    boolean found = false;
    for(CanvasPoint p : this.model.getAllPoints()) {
      if(p.getX() == 9 && p.getY() == 8) {
        found = true;
        break;
      }
    }
    assertTrue("Newly added point not fund in model", found);
    this.model.resetModel();
  }

  @Test
  public void testPaintPropertiesUpdate() {
    CanvasView view1 = new CanvasView(this.controller);
    this.controller.registerView(view1);
    CanvasView view2 = new CanvasView(this.controller);
    this.controller.registerView(view2);
    controller.viewUpdate(3);
    assertTrue("Wrong paint property update in model and observer", view1.getStroke() == 3 &&
      view2.getStroke() == 3 && this.model.getStroke() == 3);
    controller.viewUpdate(Color.GREEN);
    assertTrue("Wrong paint property update in model and observer",
      view1.getColor().equals(Color.GREEN) && view2.getColor().equals(Color.GREEN) &&
      this.model.getColor().equals(Color.GREEN));
      this.model.resetModel();
      this.controller.releaseObservers();
  }

  @Test
  public void testEndLineSegment() {
    this.controller.segmentEnd();
    CanvasPoint lastPoint = this.model.getAllPoints().get(this.model.getAllPoints().size() - 1);
    assertTrue("Wrong order of points in model", lastPoint.isEndMarker());
    this.model.resetModel();
    this.controller.releaseObservers();
  }
}