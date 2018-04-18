
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;

/**
 * Original code base: FourLights.java by David J. Eck 
 * from his Introduction to Computer Graphics textbook 
 * http://math.hws.edu/graphicsbook/ 
 * 
 * Adapted by Sarah Ball to use a star polygonal mesh and adjusted lighting 
 */
public class StarAdaptation extends JPanel implements GLEventListener {

    public static void main(String[] args) {
        JFrame window = new JFrame("Star Lighting Demo");
        StarAdaptation panel = new StarAdaptation();
        window.setContentPane(panel);
        window.pack();
        window.setLocation(50, 50);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    private JCheckBox animating;  // Checked if animation is running.
    private JCheckBox viewpointLight;  // Checked if the white viewpoint light is on.
    private JCheckBox ambientLight;  // Checked if the global ambient light is on.
    private JCheckBox redLight;  // Checked if the red light is on.
    private JCheckBox greenLight;  // Checked if the green light is on.
    private JCheckBox blueLight;  // Checked if the blue light is on.
    private JCheckBox yellowLight;  // Checked if the yellow light is on.
    private JCheckBox purpleLight;  // Checked if the purple light is on.

    private GLJPanel display;
    private Timer animationTimer;

    private int frameNumber = 0;  // The current frame number for an animation.

    private Camera camera;
    private Star centerSt, redSt, greenSt, blueSt, purpleSt, yellowSt;

    /**
     * The constructor adds checkboxes under the display, to control the
     * options.
     */
    public StarAdaptation() {
        GLCapabilities caps = new GLCapabilities(null);
        display = new GLJPanel(caps);
        display.setPreferredSize(new Dimension(600, 600));
        display.addGLEventListener(this);
        setLayout(new BorderLayout());
        add(display, BorderLayout.CENTER);
        camera = new Camera();
        camera.lookAt(5, 10, 30, 0, 0, 0, 0, 1, 0);
        camera.setScale(15);
        camera.installTrackball(display);
        animationTimer = new Timer(30, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                frameNumber++;
                display.repaint();
            }
        });
        ActionListener boxHandler = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (evt.getSource() == animating) {
                    if (animating.isSelected()) {
                        animationTimer.start();
                    } else {
                        animationTimer.stop();
                    }
                } else {
                    display.repaint();
                }
            }
        };
        viewpointLight = new JCheckBox("Viewpoint Light", true);
        redLight = new JCheckBox("Red Light", true);
        blueLight = new JCheckBox("Blue Light", true);
        greenLight = new JCheckBox("Green Light", true);
        yellowLight = new JCheckBox("Yellow Light", true);
        purpleLight = new JCheckBox("Purple Light", true);
        ambientLight = new JCheckBox("Global Ambient Light", true);
        animating = new JCheckBox("Animate", true);
        viewpointLight.addActionListener(boxHandler);
        ambientLight.addActionListener(boxHandler);
        redLight.addActionListener(boxHandler);
        greenLight.addActionListener(boxHandler);
        blueLight.addActionListener(boxHandler);
        yellowLight.addActionListener(boxHandler);
        purpleLight.addActionListener(boxHandler);
        animating.addActionListener(boxHandler);
        JPanel bottom = new JPanel();
        bottom.setLayout(new GridLayout(2, 1));
        JPanel row1 = new JPanel();
        row1.add(animating);
        row1.add(ambientLight);
        bottom.add(row1);
        JPanel row2 = new JPanel();
        row2.add(viewpointLight);
        row2.add(redLight);
        row2.add(greenLight);
        row2.add(blueLight);
        row2.add(yellowLight);
        row2.add(purpleLight);
        bottom.add(row2);
        add(bottom, BorderLayout.SOUTH);
        animationTimer.setInitialDelay(500);
        animationTimer.start();
    }

    // ----------------------------- Methods for drawing -------------------------------
    /**
     * Sets the positions of the colored lights and turns them on and off,
     * depending on the state of the redLight, greenLight, and blueLight
     * options. Draws a small sphere at the location of each light.
     */
    private void lights(GL2 gl) {

        gl.glColor3d(0.5, 0.5, 0.5);
        float zero[] = {0, 0, 0, 1};
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, zero, 0);

        //Build viewpoint light
        if (viewpointLight.isSelected()) {
            gl.glEnable(GL2.GL_LIGHT0);
        } else {
            gl.glDisable(GL2.GL_LIGHT0);
        }

        //Red light setup
        if (redLight.isSelected()) {
            float red[] = {0.5F, 0, 0, 1};
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, red, 0);
            gl.glEnable(GL2.GL_LIGHT1);
        } else {
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, zero, 0);
            gl.glDisable(GL2.GL_LIGHT1);
        }
        //Sphere placement and animation
        gl.glPushMatrix();
        gl.glRotated(-frameNumber, 0, 1, 0);
        gl.glTranslated(10, 0, 0);
        gl.glRotated(90, 0, 1, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, zero, 0);
        gl.glScaled(0.15, 0.15, 0.15);
        redSt = new Star(gl);
        gl.glPopMatrix();

        //green light setup
        if (greenLight.isSelected()) {
            float green[] = {0, 0.5F, 0, 1};
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, green, 0);
            gl.glEnable(GL2.GL_LIGHT2);
        } else {
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, zero, 0);
            gl.glDisable(GL2.GL_LIGHT2);
        }
        // placement and animation
        gl.glPushMatrix();
        gl.glRotated((frameNumber + 100) * 0.8743, 0, 1, 0);
        gl.glTranslated(10, -6, 0);
        gl.glRotated(-30, 0, 0, 1);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, zero, 0);
        gl.glScaled(0.25, 0.25, 0.25);
        greenSt = new Star(gl);
        gl.glPopMatrix();

        //blue light
        if (blueLight.isSelected()) {
            float blue[] = {0, 0, 0.5F, 1};
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, blue, 0);
            gl.glEnable(GL2.GL_LIGHT3);
        } else {
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, zero, 0);
            gl.glDisable(GL2.GL_LIGHT3);
        }
        // placement and animation
        gl.glPushMatrix();
        gl.glRotated((-frameNumber - 100) * 1.3057, 0, 1, 0);
        gl.glTranslated(8, 0, 0);
        gl.glRotated(45, 0, 0, 1);
        gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_POSITION, zero, 0);
        gl.glScaled(0.25, 0.25, 0.25);
        blueSt = new Star(gl);
        gl.glPopMatrix();

        //Yellow light
        if (yellowLight.isSelected()) {
            float yellow[] = {0.5F, 0.5F, 0, 1}; 
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, yellow, 0);
            gl.glEnable(GL2.GL_LIGHT4);
        } else {
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, zero, 0);
            gl.glDisable(GL2.GL_LIGHT4);
        }
        // placement and animation
        gl.glPushMatrix();
        gl.glRotated((-frameNumber + 100) * 1.5, 0, 1, 0);
        gl.glTranslated(8, 0, 0);
        gl.glRotated(45, 0, 0, 1);
        gl.glLightfv(GL2.GL_LIGHT4, GL2.GL_POSITION, zero, 0);
        gl.glScaled(0.25, 0.25, 0.25);
        yellowSt = new Star(gl);
        gl.glPopMatrix();

        //Purple light
        if (purpleLight.isSelected()) {
            float purple[] = {0.5F, 0, 0.5F, 1};
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, purple, 0);
            gl.glEnable(GL2.GL_LIGHT5);
        } else {
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, zero, 0);
            gl.glDisable(GL2.GL_LIGHT5);
        }
        gl.glPushMatrix();
        gl.glRotated(90, 0, 0, 1);
        gl.glRotated((frameNumber - 100) * 1.3057, 0, 1, 0);
        gl.glTranslated(9, 0, 0);
        gl.glLightfv(GL2.GL_LIGHT5, GL2.GL_POSITION, zero, 0);
        gl.glScaled(0.25, 0.25, 0.25);
        purpleSt = new Star(gl);
        gl.glPopMatrix();

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, zero, 0); // Turn off emission color!
    } // end lights()

    // ---------------  Methods of the GLEventListener interface -----------
    /**
     * Draws the scene.
     */
    public void display(GLAutoDrawable drawable) {
        // called when the panel needs to be drawn

        GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        camera.apply(gl);

        lights(gl);

        float zero[] = {0, 0, 0, 1};

        if (ambientLight.isSelected()) {
            gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{0.5F, 0.5F, 0.5F, 1}, 0); //Increased ambient light
        } else {
            gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, zero, 0);
        }

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, new float[]{0.2F, 0.2F, 0.2F, 1}, 0);

        gl.glPushMatrix();
        centerSt = new Star(gl);
        gl.glPopMatrix();
    }

    /**
     * Initialization, including setting up a camera and configuring the four
     * lights.
     */
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0, 0, 0, 1);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_NORMALIZE);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, 1);
        gl.glMateriali(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 32);

        //Viewpoint light
        float dim[] = {0.5F, 0.5F, 0.5F, 1};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, dim, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, dim, 0);

        //red light setup
        float red[] = {0.5F, 0, 0, 1};
        float reda[] = {0.1F, 0, 0, 1};
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, reda, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, red, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, red, 0);

        //green light setup
        float gr[] = {0, 0.5F, 0, 1};
        float gra[] = {0, 0.1F, 0, 1};
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_AMBIENT, gra, 0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, gr, 0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, gr, 0);

        //blue light setup
        float bl[] = {0, 0, 0.6F, 1};
        float bla[] = {0, 0, 0.2F, 1};
        gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_AMBIENT, bla, 0);
        gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_DIFFUSE, bl, 0);
        gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_SPECULAR, bl, 0);

        //yellow light setup
        float yl[] = {0.5F, 0.5F, 0, 1};
        float yla[] = {0.1F, 0.1F, 0, 1};
        gl.glLightfv(GL2.GL_LIGHT4, GL2.GL_AMBIENT, yla, 0);
        gl.glLightfv(GL2.GL_LIGHT4, GL2.GL_DIFFUSE, yl, 0);
        gl.glLightfv(GL2.GL_LIGHT4, GL2.GL_SPECULAR, yl, 0);

        //Purple light setup
        float pul[] = {0.5F, 0, 0.5F, 1};
        float pua[] = {0.1F, 0, 0.1F, 1};
        gl.glLightfv(GL2.GL_LIGHT5, GL2.GL_AMBIENT, pua, 0);
        gl.glLightfv(GL2.GL_LIGHT5, GL2.GL_DIFFUSE, pul, 0);
        gl.glLightfv(GL2.GL_LIGHT5, GL2.GL_SPECULAR, pul, 0);
    }

    /**
     * Called when the size of the GLJPanel changes.
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    /**
     * This is called before the GLJPanel is destroyed.
     */
    public void dispose(GLAutoDrawable drawable) {
    }

}
