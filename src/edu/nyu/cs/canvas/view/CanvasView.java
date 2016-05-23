package edu.nyu.cs.canvas.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import edu.nyu.cs.canvas.controller.CanvasController;
import edu.nyu.cs.canvas.model.CanvasPoint;
import edu.nyu.cs.canvas.CanvasConfig;

/**
 * This is the view class that implements the observer
 * The top panel has grid of buttons for launching application specific commands
 * The middle panel stores the actual canvas for drawing 
 * @see CanvasPanel
 * The bottom panel stores controls for adjusting paint properties
 *
 * NOTE: To close the current view, press the close view button in the top action panel.
 * To exit out of the application(and closing all views) use standard exit window
 *
 * @author abhishek
 */
public class CanvasView implements CanvasObserver {
  //stores the number of views currently in memory, goes to zero when all observers are released
  public static int canvasCount;
  //assigns different and strictly increasing IDs to a view
  private int ID;
  private JFrame frameMain;
  private JPanel centerPanel;
  private CanvasController controller;
  private Color color;
  private CanvasPanel canvas;
  private CanvasPoint startPoint;
  private CanvasPoint endPoint;
  private int stroke;
  private final JSlider slider = new JSlider(JSlider.HORIZONTAL, 2, 10, 2);

  /**
  * getter for the paint property 'stroke' in view
  * @return returns paint property 'stroke' in view
  */
  public int getStroke() {
    return this.stroke;
  }

  /**
  * getter for the paint property 'color' in view
  * @return returns paint property 'color' in view
  */
  public Color getColor() {
    return this.color;
  }

  /**
  * getter for the 'prospective' start point for a new line/point
  * @return returns the last updated start point
  */
  public CanvasPoint getStartPoint() {
    return this.startPoint;
  }

  /**
  * getter for the last updated endpoint of the point/line drawn
  * @return returns the endpoint of last point/line/shape
  */
  public CanvasPoint getEndPoint() {
    return this.endPoint;
  }

  /**
  * Controller invokes this method to draw a line between current start
  * and end points
  * @see CanvasPanel
  * @see CanvasController
  * @param startPoint is the last start point recorded in model
  * @param endPoint is the end point recorded in model
  */
  @Override
  public void drawLine(CanvasPoint startPoint, CanvasPoint endPoint) {
    this.canvas.drawLine(startPoint, endPoint);
  }

  /**
  * receives the notification from controller about updating the new start point
  * once a point/segment has been drawn 
  * @see CanvasController
  * @param endPoint is the new endPoint recorded in model
  */
  @Override
  public void setStartPoint(CanvasPoint endPoint) {
    this.startPoint = new CanvasPoint.Builder(endPoint.getX(), endPoint.getY()).
      pointWithColor(endPoint.getColor()).pointWithStroke(endPoint.getStroke()).build();
  }

  /**
  * returns the canvas element of the view
  * @see CanvasPanel
  * @return returns the canvas element
  */
  public CanvasPanel getCanvas() {
    return this.canvas;
  }

  /**
  * receives a notification from controller to draw a new stateful view
  * if a new view is required to have it(stateful fork is requested by
  * one of the existing views
  * @param allPoints is the set of all entities stored by model
  * @see CanvasPoint
  */
  @Override
  public void copyContent(ArrayList<CanvasPoint> allPoints) {
    this.canvas.setPoints(allPoints);
    this.canvas.draw();
  }

  /**
  * whenever a reset is requested, controller notifies all view to reset
  * their paint properties and repaint.
  */
  @Override
  public void reset() {
    this.color = CanvasConfig.defaultColor;
    this.stroke = CanvasConfig.defaultStroke;
    this.canvas.repaint();
    this.slider.setValue(CanvasConfig.defaultStroke);
  }

  /**
  * getter for the main frame of the view
  * @return returns the main frame of the view
  */
  public JFrame getMainFrame() {
    return this.frameMain;
  }

  /**
  * getter for the ID of the current view
  * @return returns the ID of the current view
  */
  public int getID() {
    return this.ID;
  }

  /**
  * Constructor for the view object, creates a view object and ties it to a controller
  * so that it can send/receive updates to/from model via controller
  * @param controller is the controller reference for the view
  */
  public CanvasView(CanvasController controller) {
    this.controller = controller;
    canvasCount++;
    this.ID = canvasCount;
    this.controller.viewUpdate();
  }

  /**
  * Inner class for representing mouse press event and corresponding handler
  * @author abhishek
  */
  private class CanvasMousePressListener extends MouseAdapter {
    /**
    * On mouse-press, current view updates the new start point and notifies
    * the controller so that it can update the model. Programmatically inserts
    * an end marker to mark the start of a drawing element
    * @see CanvasPanel
    */
    @Override
    public void mousePressed(MouseEvent e) {
      controller.segmentEnd();
      startPoint = new CanvasPoint.Builder(e.getX(), e.getY()).
        pointWithColor(getColor()).pointWithStroke(getStroke()).build();
      controller.viewUpdate(getStartPoint());
    }
  }

  /**
  * Inner class for representing mouse drag event and corresponding handler
  * @author abhishek
  */
  private class CanvasMouseMotionListener extends MouseMotionAdapter {
    /**
    * On mouse-drag, current view updates the new end point and notifies
    * the controller so that it can update the model and broadcast all observers
    * (including this one) to draw a line/point between start and end points.
    * Inserts an end marker to mark the end of current drawing element
    * @see CanvasPanel
    */
    @Override
      public void mouseDragged(MouseEvent e) {
      endPoint = new CanvasPoint.Builder(e.getX(), e.getY()).pointWithColor(getColor())
        .pointWithStroke(getStroke()).build();
      controller.viewUpdate(getStartPoint(),getEndPoint());
      controller.segmentEnd();
    }
  }

  /**
  * creates the overall UI for the view
  * @return returns the mail UI frame
  */
  private JPanel createCanvas() {
    JPanel canvasFrame = new JPanel();
    this.canvas = new CanvasPanel();
    this.canvas.setPreferredSize(new Dimension(CanvasConfig.defaultWidth,
      CanvasConfig.defaultHeight));
    this.canvas.setBackground(Color.WHITE);
    this.canvas.repaint();
    this.canvas.addMouseListener(new CanvasMousePressListener());
    this.canvas.addMouseMotionListener(new CanvasMouseMotionListener());
    canvasFrame.add(canvas);
    canvasFrame.setBackground(Color.BLACK);
    canvasFrame.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    return canvasFrame;
  }

  /**
  * creates the color picker panel which is a set of colored buttons
  * for each color. Selecting any of them will update the controller
  * to notify all views(including this one) to update their current color
  * @return returns the color panel
  */
  private JPanel getColorPicker() {
    JPanel foreGroundPanel = new JPanel();
    JLabel fgLabel = new JLabel();
    fgLabel.setText("Pick Pen Color: ");
    fgLabel.setFont(new Font("Arial", Font.BOLD, 20));
    JButton redForeground = new JButton();
    redForeground.setPreferredSize(new Dimension(50, 50));
    redForeground.setBackground(Color.RED);
    redForeground.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.viewUpdate(Color.RED);
      }
    });
    JButton greenForeground = new JButton();
    greenForeground.setPreferredSize(new Dimension(50, 50));
    greenForeground.setBackground(Color.GREEN);
    greenForeground.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.viewUpdate(Color.GREEN);
      }
    });
    JButton blueForeground = new JButton();
    blueForeground.setPreferredSize(new Dimension(50, 50));
    blueForeground.setBackground(Color.BLUE);
    blueForeground.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.viewUpdate(Color.BLUE);
      }
    });
    JButton yellowForeground = new JButton();
    yellowForeground.setPreferredSize(new Dimension(50, 50));
    yellowForeground.setBackground(Color.YELLOW);
    yellowForeground.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.viewUpdate(Color.YELLOW);
      }
    });
    JButton orangeForeground = new JButton();
    orangeForeground.setPreferredSize(new Dimension(50, 50));
    orangeForeground.setBackground(Color.ORANGE);
    orangeForeground.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.viewUpdate(Color.ORANGE);
      }
    });
    JButton blackForeground = new JButton();
    blackForeground.setPreferredSize(new Dimension(50, 50));
    blackForeground.setBackground(Color.BLACK);
    blackForeground.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.viewUpdate(Color.BLACK);
      }
    });
    JButton greyForeground = new JButton();
    greyForeground.setPreferredSize(new Dimension(50, 50));
    greyForeground.setBackground(Color.GRAY);
    greyForeground.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.viewUpdate(Color.GRAY);
      }
    });
    foreGroundPanel.add(fgLabel);
    foreGroundPanel.add(redForeground);
    foreGroundPanel.add(greenForeground);
    foreGroundPanel.add(blueForeground);
    foreGroundPanel.add(yellowForeground);
    foreGroundPanel.add(orangeForeground);
    foreGroundPanel.add(blackForeground);
    foreGroundPanel.add(greyForeground);
    return foreGroundPanel;
  }

  /**
  * creates the stroke picker slider which is a slider
  * for selecting a stroke between 2 and 10. Moving the slider will update the controller
  * to notify all views(including this one) to update their slider settings
  * @return returns the slider panel
  */
  private JPanel getStrokePicker() {
    JPanel strokePanel = new JPanel();
    JLabel strokeLabel = new JLabel("Stroke: ");
    strokeLabel.setFont(new Font("Arial", Font.BOLD, 20));
    this.slider.setMajorTickSpacing(8);
    this.slider.setMinorTickSpacing(1);
    this.slider.setPaintTicks(true);
    this.slider.setPaintLabels(true);
    this.slider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        controller.viewUpdate(slider.getValue());
      }
    });
    strokePanel.add(strokeLabel);
    strokePanel.add(slider);
    return strokePanel;
  }

  /**
  * creates the top panel with buttons for launching custom actions
  * 1. reset all views to blank
  * 2. launch a black view
  * 3. launch a stateful view
  * 4. close the current view
  * @return returns the action buttons panel
  */
  private JPanel createTopPanel() {
    JPanel buttonPanel = new JPanel(new GridLayout(1,4));
    JButton resetButton = new JButton();
    resetButton.setText("Reset All");
    resetButton.setFont(new Font("Arial", Font.BOLD, 12));
    resetButton.setPreferredSize(new Dimension(CanvasConfig.defaultWidth/4,50));
    resetButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.viewReset();
      }
    });
    JButton addButton = new JButton();
    addButton.setText("Fork Stateful Canvas");
    addButton.setFont(new Font("Arial", Font.BOLD, 12));
    addButton.setPreferredSize(new Dimension(CanvasConfig.defaultWidth/4,50));
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.viewAddCanvas();
      }
    });
    JButton addBlankButton = new JButton();
    addBlankButton.setText("Fork Blank Canvas");
    addBlankButton.setFont(new Font("Arial", Font.BOLD, 12));
    addBlankButton.setPreferredSize(new Dimension(CanvasConfig.defaultWidth/4,50));
    addBlankButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.viewAddBlankCanvas();
      }
    });
    JButton removeButton = new JButton();
    removeButton.setText("Remove This");
    removeButton.setForeground(Color.RED);
    removeButton.setFont(new Font("Arial", Font.BOLD, 12));
    removeButton.setPreferredSize(new Dimension(CanvasConfig.defaultWidth/4,50));
    removeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.viewRemoveCanvas(ID);
      }
    });
    buttonPanel.add(resetButton);
    buttonPanel.add(addButton);
    buttonPanel.add(addBlankButton);
    buttonPanel.add(removeButton);
    return buttonPanel;
  }

  /**
  * Combines color and strokepicker panels
  * @return returns the cmbined panel
  */
  private JPanel createDashBoard() {
    JPanel dashBoard = new JPanel(new BorderLayout());
    dashBoard.add(getColorPicker(), BorderLayout.CENTER);
    dashBoard.add(getStrokePicker(), BorderLayout.SOUTH);
    return dashBoard;
  }

  /**
  * makes the current view UI visible
  */
  public void displayView() {
    this.frameMain = new JFrame("Canvas " + this.ID);
    this.frameMain.setBounds(CanvasConfig.startX + 50*(this.ID - 1), CanvasConfig.startY
      + 50*(this.ID - 1), CanvasConfig.defaultWidth, CanvasConfig.defaultHeight);
    this.frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.frameMain.setResizable(false);
    this.centerPanel = new JPanel(new BorderLayout());
    this.centerPanel.add(this.createTopPanel(), BorderLayout.NORTH);
    this.centerPanel.add(this.createCanvas(), BorderLayout.CENTER);
    this.centerPanel.add(this.createDashBoard(), BorderLayout.SOUTH);
    this.frameMain.add(this.centerPanel);
    this.frameMain.pack();
    this.frameMain.setVisible(true);
  }

  /**
  * receives a notification from controller to update the current
  * stroke with the updated value from model and change the slider
  * accordingly
  */
  @Override
  public void changeStroke(int stroke) {
    this.slider.setValue(stroke);
    this.stroke = stroke;
  }

  /**
  * receives a notification from controller to update the current
  * color with the updated value from model
  */
  @Override
  public void changeColor(Color color) {
    this.color = color;
  }
}