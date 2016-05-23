package edu.nyu.cs.canvas.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import edu.nyu.cs.canvas.CanvasConfig;
import edu.nyu.cs.canvas.model.CanvasModel;
import edu.nyu.cs.canvas.model.CanvasPoint;
import java.awt.Color;

public class CanvasModelTest {
  private CanvasModel model;

  @Before
  public void setUp() throws Exception {
    this.model = CanvasModel.getInstance();
  }

  @Test
  public void testSingleton() {
    CanvasModel model1 = CanvasModel.getInstance();
    CanvasModel model2 = CanvasModel.getInstance();
    assertTrue("Multiple instances detected", model1.equals(model2));
    assertTrue("Multiple instances detected", model.equals(model1));
    assertTrue("Multiple instances detected", model.equals(model2));
  }

  @Test
  public void testCanvasPointBuilder() {
    CanvasPoint p = new CanvasPoint.Builder(2, 3).pointWithColor(Color.GRAY).
      pointWithStroke(5).build();
    boolean correctBuild = false;
    if(p.getX() == 2 && p.getY() == 3 && p.getColor() == Color.GRAY && p.getStroke() == 5) {
      correctBuild = true;
    }
    assertTrue("Wrong build by builder", correctBuild);
    p = new CanvasPoint.Builder(-1, -1).build();
    assertTrue("Wrong build by builder", p.isEndMarker());
  }

  @Test
  public void testAddNullPointToModel() {
    model.addPoint(null);
    boolean nullFound = false;
    for(int i = 0; i < this.model.getAllPoints().size(); ++i) {
      if(this.model.getAllPoints().get(i) == null) {
        nullFound = true;
        break;
      }
    }
    assertFalse("Null point should not be added to model", nullFound);
    this.model.resetModel();
  }

  @Test
  public void testAddPointToModel() {
    model.addPoint(new CanvasPoint.Builder(2, 3).build());
    boolean found = false;
    CanvasPoint p = new CanvasPoint.Builder(2, 3).build();
    for(int i = 0; i < this.model.getAllPoints().size(); ++i) {
      CanvasPoint temp = this.model.getAllPoints().get(i);
      if(temp.getX() == p.getX() && temp.getY() == p.getY()) {
        found = true;
        break;
      }
    }
    assertTrue("Built geometry not found in model", found);
    this.model.resetModel();
  }

  @Test
  public void testAddSegmentToModel() {
    model.addPoint(new CanvasPoint.Builder(4, 5).build());
    model.addPoint(new CanvasPoint.Builder(6, 7).build());
    CanvasPoint src = new CanvasPoint.Builder(4, 5).build();
    CanvasPoint dst = new CanvasPoint.Builder(6, 7).build();
    boolean segmentFound = false;
    for(int i = 0; i < model.getAllPoints().size() - 1; ++i) {
      CanvasPoint prev = model.getAllPoints().get(i);
      CanvasPoint next = model.getAllPoints().get(i+1);
      if(prev.getX() == src.getX() && prev.getY() == src.getY() &&
        next.getX() == dst.getX() && next.getY() == dst.getY()) {
        segmentFound = true;
        break;
      }
    }
    assertTrue("Built geometry not found in model", segmentFound);
    this.model.resetModel();
  }

  @Test
  public void testNumberOfPointsInModel() {
    this.model.resetModel();
    assertTrue("Wrong geometry count in model", this.model.getAllPoints().size() == 0);
    model.addPoint(new CanvasPoint.Builder(4, 5).build());
    model.addPoint(new CanvasPoint.Builder(6, 7).build());
    assertTrue("Wrong geometry count in model", this.model.getAllPoints().size() == 2);
    this.model.resetModel();
  }

  @Test
  public void testModelPaintProperties() {
    this.model.setColor(Color.GRAY);
    this.model.setStroke(5);
    assertTrue("Wrong model update", this.model.getColor().equals(Color.GRAY));
    assertTrue("Wrong model update", this.model.getStroke() == 5);
    this.model.resetModel();
  }

  @Test
  public void testNullAndInvalidModelPaintProperties() {
    this.model.setColor(Color.RED);
    this.model.setColor(null);
    assertTrue("Null color should not be set", this.model.getColor() == Color.RED);
    this.model.setStroke(6);
    this.model.setStroke(0);
    assertTrue("Non positive stroke should not be set", this.model.getStroke() > 0);
  }

  @Test
  public void testModelReset() {
    model.addPoint(new CanvasPoint.Builder(4, 5).build());
    model.addPoint(new CanvasPoint.Builder(6, 7).build());
    this.model.resetModel();
    assertTrue("Wrong model reset", this.model.getColor().equals(CanvasConfig.defaultColor));
    assertTrue("Wrong model reset", this.model.getStroke() == CanvasConfig.defaultStroke);
    assertTrue("Wrong model reset", this.model.getAllPoints().size() == 0);
  }
}