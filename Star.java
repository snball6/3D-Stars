
import com.jogamp.opengl.GL2;

/**
 * @author Sarah Ball
 */
public class Star {

    public double[][] vertices = {
        {0, 5.5, 0}, {-5, 2, 0}, {-3, -4, 0}, {3, -4, 0}, {5, 2, 0},
        {1, 2, 1}, {-1, 2, 1}, {-2, 0, 1}, {0, -1.5, 1}, {2, 0, 1},
        {1, 2, -1}, {-1, 2, -1}, {-2, 0, -1}, {0, -2, -1}, {2, 0, -1}};

    public int[][] faceList = {
        {5, 6, 7, 8, 9}, {14, 13, 12, 11, 10}, //main faces
        {0, 6, 5}, {0, 5, 10}, {0, 10, 11}, {0, 11, 6}, //top point
        {1, 7, 6}, {1, 6, 11}, {1, 11, 12}, {1, 12, 7}, //top left
        {2, 8, 7}, {2, 7, 12}, {2, 12, 13}, {2, 13, 8}, //bottom left
        {3, 9, 8}, {3, 8, 13}, {3, 13, 14}, {3, 14, 9}, //bottom right
        {4, 5, 9}, {4, 9, 14}, {4, 14, 10}, {4, 10, 5}}; //top right
    public double[][] normals = {
        {0, 0, 1}, {0, 0, -1}, //main faces
        {-0.690942, -0.212598, 0.690942}, {-0.690942, -0.212598, -0.690942}, //top 
        {0.690942, -0.212598, -0.690942}, {0.690942, -0.212598, 0.690942},
        {-0.11547, -0.80829, 0.57735}, {-0.0873704, -0.611593, -0.786334}, //top left
        {0.362143, 0.452679, -0.814822}, {0.492366, 0.615457, 0.615457},
        {0.618853, -0.309426, 0.721995}, {0.534522, -0.267261, -0.801784},//bottom left
        {-0.362143, 0.452679, -0.814822}, {-0.421637, 0.527046, 0.737865},
        {0.362143, 0.452679, 0.814822}, {0.421637, 0.527046, -0.737865}, //bottom right
        {-0.618853, -0.309426, -0.721995}, {-0.534522, -0.267261, 0.801784},
        {-0.362143, 0.452679, 0.814822}, {-0.492366, 0.615457, -0.615457}, //top right
        {0.11547, -0.80829, -0.57735}, {0.0873704, -0.611593, 0.786334},};

    /*
    * Constructor loop
    *
    * Based off given code in Introduction to Computer Graphics textbook by 
    * David J. Eck book section 3.4.1 http://math.hws.edu/graphicsbook/c3/s4.html
     */
    public Star(GL2 gl2) {
        for (int i = 0; i < faceList.length; i++) {
            gl2.glNormal3d(normals[i][0], normals[i][1], normals[i][2]);
            gl2.glBegin(GL2.GL_TRIANGLE_FAN);
            for (int j = 0; j < faceList[i].length; j++) {
                int vertexNum = faceList[i][j];  // Index for vertex j of face i.
                double[] vertexCoords = vertices[vertexNum];  // The vertex itself.
                gl2.glVertex3dv(vertexCoords, 0);
            }
            gl2.glEnd();
        }
    }//end constructor
}
