import java.util.HashSet;
import java.util.ArrayList;
public class Main {
    public static void main(String args[]){
        /**
         * Commands go under this comment. Create a custom graph by using the graph object and specify the number of
         * vertices. For example, if you want a graph with 5 vertices, use "Graph g = new Graph(5);". Then you can use
         * the "g.addEdge()" and "g.removeEdge()" to connect the vertices. Look at the graph class for any other
         * public methods available to you.
         *
         * If you need common graphs such as complete graphs, cycle graphs, cube graphs, etc, a GraphBuilder object
         * may be more useful. Initialize a GraphBuilder object using "GraphBuilder gb = new GraphBuilder();". Then
         * you can use its methods to create specific graphs. For example, to create a K_4 graph, use "Graph g1 = gb
         * .createCompleteGraph(4);". To also create K_{2,4} you can then use  "Graph g2 = gb
         * .createCompleteBipartiteGraph(2,4);"
         *
         * Lastly, to use the sudoku class, initialize a SudokuGrid object and provide it with an array of size 81
         * which represents the numbers in the grid. Use null, to represent empty squares. Here's an example.
         * Integer[] values = {
         *                 null,null,null,2,6,null,7,null,1,
         *                 6,8,null,null,7,null,null,9,null,
         *                 1,9,null,null,null,4,5,null,null,
         *                 8,2,null,1,null,null,null,4,null,
         *                 null,null,4,6,null,2,9,null,null,
         *                 null,5,null,null,null,3,null,2,8,
         *                 null,null,9,3,null,null,null,7,4,
         *                 null,4,null,null,5,null,null,3,6,
         *                 7,null,3,null,1,8,null,null,null
         *                 };
         *         SudokuGrid sud = new SudokuGrid(values);
         * Once the SudokuGrid option is initialized, you can use the .solve() method to solve the grid and the
         * .printSolution() method to output the completed grid to the console.
         *
         * Unfortunately I have not yet provided error handling but hopefully that will come in the future. Enjoy!
         */
        
    }
}

// Graph object which is represented by an adjacency matrix.
//
// int vertexCount: The number of vertices in the graph.
// boolean[][] adjMatrix: The 2D array matrix that represents the graph. the indices represent the vertices and the
// value of a cell in the matrix represents an edge if true, or no edge if false.
// Integer[][] degreeArray: The 2D array holding the vertices and their respective degrees. It is two dimensional in
// order to keep track of the vertex indices when sorted by descending degree.
// Integer[] coloredArray: An array that holds the color of the indexed vertex.
// boolean[] colored: An array that keeps track of which vertices have been colored.
class Graph{
    private int vertexCount =0;
    private boolean[][] adjMatrix;
    private Integer[][] vertexDegrees;
    private Integer[] vertexColors;
    private boolean[] isColored;
    // Graph constructor.
    public Graph(int vertexCount) {
        this.vertexCount = vertexCount;
        adjMatrix = new boolean[vertexCount][vertexCount];
        vertexDegrees = new Integer[vertexCount][2];
        vertexColors = new Integer[vertexCount];
        isColored = new boolean[vertexCount];
    }

    // Returns the number of vertices in the graph.
    public int getVertexCount() {
        return vertexCount;
    }

    public boolean[][] getAdjMatrix() {
        return adjMatrix;
    }

    // Adds an edge to the graph between the two specified vertices.
    public void addEdge(int v1,int v2){
        adjMatrix[v1][v2] = true;
        adjMatrix[v2][v1] = true;
    }

    // Removes the edge to the graph between the two specified vertices.
    public void removeEdge(int v1, int v2){
        adjMatrix[v1][v2] = false;
        adjMatrix[v2][v1] = false;
    }

    // Returns the degree of the passed vertex.
    public int degreeOf(int v){
        int count = 0;
        for (int i = 0; i < vertexCount; i++){
            if (adjMatrix[v][i]) count++;
        }
        return count;
    }

    // Fills the degreeArray with the vertices and their respective degrees.
    public Integer[][] allDegrees(){
        for (int i = 0; i < vertexCount; i++){
            vertexDegrees[i][0] = i;
            vertexDegrees[i][1] = degreeOf(i);
        }
        return vertexDegrees;
    }

    // Returns the vertex with the highest degree of connections to uniquely colored vertices.
    private int highestColorDegree(){
        int maxIndex = 0;
        int maxColor = 0;
        for (int i = 0; i < vertexCount; i++) {
            if (!isColored[i]){
                HashSet<Integer> colors = new HashSet<>();
                for (int j = 0; j < vertexCount; j++) {
                    if (adjMatrix[i][j] && isColored[j] && !colors.contains(vertexColors[i])){
                        colors.add(vertexColors[j]);
                    }
                }
                if (colors.size()>maxColor){
                    maxColor = colors.size();
                    maxIndex = i;
                }
            }
        }
        return maxIndex;
    }

    // Returns a string which represents the adjacency matrix for the graph.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < vertexCount; i++) {
            s.append(i + ": ");
            for (boolean j : adjMatrix[i]) {
                s.append((j ? 1 : 0) + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    // Returns a string which lists the adjacent vertices for the specified vertex.
    public String adjacentVertices(int v){
        StringBuilder s = new StringBuilder();
        s.append(v + ": ");
        for (int i = 0; i<vertexCount; i++){
            if (adjMatrix[v][i]) s.append(i + " ");
        }
        return s.toString();
    }
    // Returns a string which lists each vertex and all its adjacent vertices.
    public String adjacentVertices(){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < vertexCount; i++){
            s.append(adjacentVertices(i));
            s.append("\n");
        }
        return s.toString();
    }

    // Fills an array where the indices are the vertices and the value is the color used to color that vertex.
    // Does so by creating a boolean array that keeps track if a particular vertex is colored. Then creates the 2D
    // array that keeps track of the vertices and their degree. Sorts the array in descending order by degree. Then
    // starting with color 0, we iterate throughout the list of vertices. We set the current vertex to the vertex
    // we're coloring in the array. If the current vertex is not colored, set the current vertex in the colored array
    // to the current color. Then we mark it colored in the colored array. Next, we iterate throughout the other
    // vertices and if the other vertices aren't adjacent to the current vertex and the new vertex in question isn't
    // colored, we compare this new vertex to its adjacent vertices. If its adjacent to a vertex with the current
    // color, we must iterate it to the next color and check that. Once we find a color that isn't adjacent to this
    // new vertex, we color it and mark it as colored in the colored array. Then we go back to the original current
    // color and check the next non-adjacent vertex. Once we've colored all possible vertices this color, move onto
    // the next color and repeat.
    public void welshPowell(){
        vertexDegrees = allDegrees();
        vertexDegrees = sort2DArray(vertexDegrees);
        int currColor = 0;
        for (int i = 0; i < vertexDegrees.length; i++) {
            Integer currVertex = vertexDegrees[i][0];
            if (!isColored[currVertex]){
                vertexColors[currVertex] = currColor;
                isColored[currVertex] = true;
                for (int j = 0; j < vertexCount; j++) {
                    int tempVertex = vertexDegrees[j][0];
                    if (!adjMatrix[currVertex][tempVertex] && !isColored[tempVertex]){
                        if (!hasAdjacentColor(tempVertex,currColor)){
                            vertexColors[tempVertex] = currColor;
                            isColored[tempVertex] = true;
                        }
                    }
                }
                currColor++;
            }
        }
    }

    // Uses the Welsh-Powell algorithm to solve a partially colored graph by finding the empty vertices with the
    // highest degree of colored edges and coloring it the first available color. Repeat for the vertices with
    // descending degrees.
    public Integer[] partiallySolvedWelshPowell(Integer[] knownColorings){
        vertexColors = new Integer[vertexCount];
        isColored = new boolean[vertexCount];
        for (int i = 0; i < knownColorings.length; i++) {
            if (knownColorings[i] != null){
                isColored[i] = true;
                vertexColors[i] = (knownColorings[i] - 1);
            }
        }
        for (int i = 0; i < vertexDegrees.length; i++) {
            Integer currVertex = highestColorDegree();
            if (!isColored[currVertex]){
                int tmpColor = findFirstNonAdjacentColor(currVertex,0);
                vertexColors[currVertex] = tmpColor;
                isColored[currVertex] = true;
            }
        }
        for (int i = 0; i < vertexColors.length; i++) {
            knownColorings[i] = vertexColors[i] + 1;
        }
        return knownColorings;
    }

    // Checks and returns whether or not the passed vertex is adjacent to a vertex of the passed color.
    private boolean hasAdjacentColor(Integer vertex, Integer color){
        for (int i = 0; i < adjMatrix.length; i++) {
            if (adjMatrix[vertex][i] && (vertexColors[i] == color)){
                return true;
            }
        }
        return false;
    }

    // Returns the next color after the passed color that is not adjacent to the specified vertex.
    private Integer findFirstNonAdjacentColor(Integer vertex, Integer color){
        while (hasAdjacentColor(vertex,color)){
            color++;
        }
        return color;
    }

    // Counts and returns the total number of colors used in the coloring.
    private int totalColors(){
        HashSet<Integer> colors = new HashSet<>();
        for (int i = 0; i < vertexColors.length; i++) {
            colors.add(vertexColors[i]);
        }
        return colors.size();
    }

    // Prints out the coloring of each vertex.
    public void printColoring(){
        for (int i = 0; i < vertexColors.length; i++) {
            System.out.println("Vertex " + i + " is colored with color " + vertexColors[i]);
        }
        System.out.println(totalColors() + " colors were used.");
    }

    // Sorts a 2D array based on its second column.
    private Integer[][] sort2DArray(Integer[][] array){
        int temp0;
        int temp1;
        for (int i = 0; i < array.length; i++) {
            for (int j = i; j >0; j--) {
                if (array[j][1] > array[j-1][1]){
                    temp0 = array[j][0];
                    temp1 = array[j][1];
                    array[j][0] = array[j-1][0];
                    array[j][1] = array[j-1][1];
                    array[j-1][0] = temp0;
                    array[j-1][1] = temp1;
                }
            }
        }
        return array;
    }
}

// This class is used to create common graphs such as complete graphs, cycle graphs, wheel graphs, cube graphs,
// complete bipartite graphs, and star graphs.
class GraphBuilder{
    // Creates a complete graph of the degree provided.
    public Graph createCompleteGraph(int degree){
        Graph g = new Graph(degree);
        for (int i = 0; i < degree; i++) {
            for (int j = i; j < degree; j++) {
                if (!(i==j))
                g.addEdge(i,j);
            }
        }
        return g;
    }

    // Creates a cycle graph of the degree provided.
    public Graph createCycleGraph(int degree){
        Graph g = new Graph(degree);
        g.addEdge((degree-1),0);
        for (int i = 0; i < (degree - 1); i++) {
            g.addEdge(i,i+1);
        }
        return g;
    }

    // Creates a wheel graph of the degree provided
    public Graph createWheelGraph(int degree) {
        Graph g = new Graph(degree + 1);
        g.addEdge((degree - 1), 0);
        for (int i = 0; i < (degree - 1); i++) {
            g.addEdge(i, i + 1);
        }
        for (int i = 0; i < degree; i++) {
            g.addEdge(degree, i);
        }
        return g;
    }

    // Creates a cube graph of the degree provided. Recursively builds the cube starting with the base case of Q_0.
    // Creates two identical cube graphs of one less degree, g1,g2, inserts the adjacency matrix of the two into the
    // correct degree graph we're building, g, (for the second subgraph g2, we need to insert the adjacency matrix in
    // the bottom right quadrant of the adjacency matrix of the main graph g). Then we connect the vertex in subgraph g1
    // with the identical vertex in subgraph g2.
    public Graph createCubeGraph(int degree){
        if(degree == 0){
            Graph g = new Graph(1);
            return g;
        }
        Graph g1 = new Graph((degree-1));
        Graph g2 = new Graph((degree-1));
        g1 = createCubeGraph(degree - 1);
        g2 = createCubeGraph(degree - 1);
        Graph g = new Graph((int)Math.pow(2,degree));
        boolean[][] g1AdjMatrix = g1.getAdjMatrix();
        for (int i = 0; i < ((int) Math.pow(2, (degree - 1))); i++) {
            for (int j = 0; j < ((int) Math.pow(2, (degree - 1))); j++) {
                if(g1AdjMatrix[i][j]){
                    g.addEdge(i,j);
                }
            }
        }
        boolean[][] g2AdjMatrix = g2.getAdjMatrix();
        for (int i = 0; i < ((int) Math.pow(2, (degree - 1))); i++) {
            for (int j = 0; j < ((int) Math.pow(2, (degree - 1))); j++) {
                if(g2AdjMatrix[i][j]){
                    g.addEdge(((int)Math.pow(2,(degree-1))+i),((int)Math.pow(2,(degree-1))+j));
                }
            }
        }
        for (int i = 0; i < ((int)Math.pow(2,(degree-1))); i++) {
            g.addEdge(i,(i+(int)Math.pow(2,degree-1)));
        }
        return g;
    }

    // Creates a complete bipartite graph with the size of the sets provided.
    public Graph createCompleteBipartiteGraph(int m, int n){
        Graph g = new Graph(m+n);
        for (int i = 0; i < m; i++) {
            for (int j = m; j < (m+n); j++) {
                g.addEdge(i,j);
            }
        }
        return g;
    }

    // Creates a star graph of the degree provided.
    public Graph createStarGraph(int degree){
        Graph g = createCompleteBipartiteGraph(1,degree);
        return g;
    }
}

// This class is used to represent a sudoku grid and provide some simple methods of interest.
//
// Graph grid: a Graph object used to represent the sudoku grid.
// Integer[] values: And array that holds the values currently stored in the sudoku grid.
class SudokuGrid{
    private Graph grid;
    private Integer[] values;
    public SudokuGrid(Integer[] knownValues){
        values = knownValues;
        grid = new Graph(81);
        ArrayList<Integer> connectedSet = new ArrayList<>() ;
        // Connect rows.
        for (int i = 0; i < 81; i += 9) {
            for (int j = i; j < (i+9); j++) {
                connectedSet.add(j);
            }
            connectSet(connectedSet);
            connectedSet.clear();
        }
        // Connect columns.
        for (int i = 0; i < 9; i++) {
            for (int j = i; j < 81; j += 9) {
                connectedSet.add(j);
            }
            connectSet(connectedSet);
            connectedSet.clear();
        }
        //Connect 3x3 squares.
        for (int i = 0; i < 81; i += 27) {
            for (int j = i; j < (i+9); j += 3) {
                for (int k = j; k < (j+27); k += 9) {
                    for (int l = k; l < (k+3); l++) {
                        connectedSet.add(l);
                    }
                }
                connectSet(connectedSet);
                connectedSet.clear();
            }
        }
    }

    // Uses the Welsh-Powell algorithm for partially solved graphs.
    public void solve(){
        values = grid.partiallySolvedWelshPowell(values);
    };
    // Prints the completed grid to the console.
    public void printSolution(){
        for (int i = 0; i < values.length; i++) {
            if (i%9==0) System.out.print("\n");
            System.out.print(values[i] + ",");
        }
    }

    // Helper method used to connect vertices in the sudoku grid graph.
    private void connectSet(ArrayList<Integer> connectedSet){
        for (int i = 0; i < connectedSet.size(); i++) {
            for (int j = 0; j < connectedSet.size(); j++) {
                grid.addEdge(connectedSet.get(i),connectedSet.get(j));
            }
        }
    }
}
