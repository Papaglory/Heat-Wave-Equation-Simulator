import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author Marius
 * 
 * Menu is created for 900 by 900. It is not supposed to be resizable, but if not, 
 * a bug makes not everything appear.
 *
 */
public class MainMenu {
	
	// String containing the app instructions for the instructions button.
	final String m_instructions = """
    If 'heat equation' is selected, then only the alpha value is used. Likewise for 'wave equation'.
	In the case of the 'Wave Dampening equation', both alpha and beta are used.

    Input points given by the user are assumed to be sorted with respect to x. At least one point
	must be selected. Note that the boundaries are assumed to be value 0 due to the initial condition.
	This means that we have value 0 at 0 and L. The points at 0 and L are automatically added when
	clicking 'start'

	When using the presets for the points, the value of L is assumed to be 9.8.

	Alpha - Thermal diffusion constant corresponding to the heat equation.
	Beta - Wave number constant. Represents the spatial frequency of the wave corresponding to the wave equation.
	L - This is the 'length' of our function, that is, we consider the domain given by [0,L].
	Scale - Scales the visual representation of the simulation. Higher values increasees the size.
	Padding in X - Pad the representation of the simulation in x direction.
	Padding in Y - Pad the representation of the simulation in y direction.
	Fourier Terms - Number of terms in the fourier series.
	Delta X - The step size used when evaluating from [0,L]. This affects the visual smoothness of the function.

    """;


	// Window size settings
	final int m_width = 900, m_height = 900;

	/*
	 * The settings below are included to give a default setting in case the user does not input anything. 
	 */
	//drawer parameter settings:
	int m_scale 		= 100;
	int m_paddingX 		= 100;
	int m_paddingY 		= 400;
	int m_screenWidth 	= 1100;
	int m_screenHeight 	= 1000;
	//Differential equation parameter settings:
	int m_fourierTerms 	= 50;
	float m_deltaX 		= 0.05f;
	float m_alpha		= 0.5f;
	float m_beta 		= 3f;
	float m_L 			= 9.8f;
	//Animation parameter settings:
	List<Vertex> points = new ArrayList<>();

	/*
	 * Need these when creating the simulation depending on the user settings or it wont work.
	 */
	Heat heat;
	Wave wave;
	WaveDamping damping;
	
	JTextField scaleTF, paddingXTF, paddingYTF, screenWidthTF, screenHeightTF;
	JTextField fourierTermsTF, deltaXTF, alphaTF, betaTF, LTF;

	// Used for inputting vertices by the user
	JTextField xInputField, yInputField;
	JTextArea logTextArea;
	
	int rightLPadding = 25;
	
	static JFrame frame;
	
	static JLayeredPane layeredPane;
	
	public MainMenu() {
		frame = new JFrame("Main Menu");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBackground(Color.gray);
	    frame.setSize(m_width, m_height);
	    frame.setVisible(true);
		frame.setResizable(true); //need to be true, if not the canvas might not show anything!
		frame.setLayout(null);
		
		layeredPane = new JLayeredPane();
		layeredPane.setBounds(0, 0, m_width, m_height);
		layeredPane.setOpaque(true);
		frame.add(layeredPane);

		// Add point for the initial condition at m_L
		points.add(new Vertex(0,0));
		
		/**
		 * Tells the user how to operate the menu screen.
		 */
		JButton instructionsButton = new JButton("Instructions");
		instructionsButton.setBounds(20 ,125, 200,60);
		layeredPane.add(instructionsButton);
		
		instructionsButton.addActionListener(new ActionListener(){ 
			@Override
	    	public void actionPerformed(ActionEvent e){  
	    	            JOptionPane.showMessageDialog(frame, m_instructions);
	        }
	    });
		
		// Heading
	    JLabel heading = new JLabel("Simulation");
	    heading.setFont(new Font("Calibri", Font.BOLD, 20));
	    heading.setBounds(20, 10, 100,30);
	    layeredPane.add(heading);
	    // Heading
	    JLabel options = new JLabel("Options:");
	    options.setFont(new Font("Calibri", Font.BOLD, 20));
	    options.setBounds(m_width - 150, 20, 100,30);
	    layeredPane.add(options);
	    
	    /**
	     * Button to display all setting values.
	     */
		JButton summaryButton = new JButton("Summary");
       	summaryButton.setBounds(m_width- 250, 50+13*rightLPadding,100,60);  
       	layeredPane.add(summaryButton);
       
       // A string containing all the chosen parameter values corresponding to Summary Button.
	    summaryButton.addActionListener(new ActionListener() {  
			@Override
           	public void actionPerformed(ActionEvent e) {
				String data = 	"Current parameter values:"
						+ "\nScale: " + String.valueOf(m_scale)
						+ "\nPadding in X: " + String.valueOf(m_paddingX)
						+ "\nPadding in Y: " + String.valueOf(m_paddingY)
						+ "\nScreen width: " + String.valueOf(m_screenWidth)
						+ "\nScreen height: " + String.valueOf(m_screenHeight)
						+ "\nFourier terms: " + String.valueOf(m_fourierTerms)
						+ "\nDelta X: " + String.valueOf(m_deltaX)
						+ "\nAlpha: " + String.valueOf(m_alpha)
						+ "\nBeta: " + String.valueOf(m_beta)
						+ "\nL: " + String.valueOf(m_L);
				JOptionPane.showMessageDialog(frame, data);
           	} 
	    });  

       /*
		* A combo box for selecting the desired Equation and its corresponding simulation.
	    */
       String equations[] = {"Wave Damping Equation", "Heat Equation", "Wave Equation",};
       
       JComboBox<String> comboBoxEquations = new JComboBox<>(equations);    
       comboBoxEquations.setBounds(300, 100,175,20);    
       layeredPane.add(comboBoxEquations);  
	    
	    //****************************************************************'
	    
	    /**
	     * These are textfields and labels for all the parameters on the right hand side of the app.
	     */
	    scaleTF = new JTextField("100");  
	    scaleTF.setBounds(m_width - 150, 50, 100, 20);
	    layeredPane.add(scaleTF);
	    JLabel scaleLabel = new JLabel("Scale");  
	    scaleLabel.setBounds(m_width - 250 , 50, 100, 20);
	    layeredPane.add(scaleLabel);

	    paddingXTF = new JTextField("100");  
	    paddingXTF.setBounds(m_width - 150, 50 + rightLPadding, 100, 20);
	    layeredPane.add(paddingXTF);
	    JLabel paddingXLabel = new JLabel("Padding in X");  
	    paddingXLabel.setBounds(m_width - 250 , 50 + rightLPadding, 100, 20);
	    layeredPane.add(paddingXLabel);
	    
	    paddingYTF = new JTextField("400");  
	    paddingYTF.setBounds(m_width - 150, 50 + 2*rightLPadding, 100, 20);
	    layeredPane.add(paddingYTF);
	    JLabel paddingYLabel = new JLabel("Padding in Y");  
	    paddingYLabel.setBounds(m_width - 250 , 50 + 2*rightLPadding, 100, 20);
	    layeredPane.add(paddingYLabel);
	    
	    screenWidthTF = new JTextField("1600");  
	    screenWidthTF.setBounds(m_width - 150, 50 + 3*rightLPadding, 100, 20);
	    layeredPane.add(screenWidthTF);
	    JLabel screenWidthLabel = new JLabel("Screen width");  
	    screenWidthLabel.setBounds(m_width - 250 , 50 + 3*rightLPadding, 100, 20);
	    layeredPane.add(screenWidthLabel);
	    
	    screenHeightTF = new JTextField("1000");  
	    screenHeightTF.setBounds(m_width - 150, 50 + 4*rightLPadding, 100, 20);
	    layeredPane.add(screenHeightTF);
	    JLabel screenHeightLabel = new JLabel("Screen height");  
	    screenHeightLabel.setBounds(m_width - 250 , 50 + 4*rightLPadding, 100, 20);
	    layeredPane.add(screenHeightLabel);
	    
	    fourierTermsTF = new JTextField("50");  
	    fourierTermsTF.setBounds(m_width - 150, 50 + 5*rightLPadding, 100, 20);
	    layeredPane.add(fourierTermsTF);
	    JLabel fourierTermsLabel = new JLabel("Fourier Terms");  
	    fourierTermsLabel.setBounds(m_width - 250 , 50 + 5*rightLPadding, 100, 20);
	    layeredPane.add(fourierTermsLabel);
	    
	    deltaXTF = new JTextField("0.05");
	    deltaXTF.setBounds(m_width - 150, 50 + 6*rightLPadding, 100, 20);
	    layeredPane.add(deltaXTF);
	    JLabel deltaXLabel = new JLabel("Delta X");  
	    deltaXLabel.setBounds(m_width - 250 , 50 + 6*rightLPadding, 100, 20);
	    layeredPane.add(deltaXLabel);
	    
	    alphaTF = new JTextField("0.5");  
	    alphaTF.setBounds(m_width - 150, 50 + 7*rightLPadding, 100, 20);
	    layeredPane.add(alphaTF);
	    JLabel alphaLabel = new JLabel("Alpha");  
	    alphaLabel.setBounds(m_width - 250 , 50 + 7*rightLPadding, 100, 20);
	    layeredPane.add(alphaLabel);
	    
	    betaTF = new JTextField("3");
	    betaTF.setBounds(m_width - 150, 50 + 8*rightLPadding, 100, 20);
	    layeredPane.add(betaTF);
	    JLabel betaLabel = new JLabel("Beta");  
	    betaLabel.setBounds(m_width - 250 , 50 + 8*rightLPadding, 100, 20);
	    layeredPane.add(betaLabel);
	    
	    LTF = new JTextField("9.8");
	    LTF.setBounds(m_width - 150, 50 + 9*rightLPadding, 100, 20);
	    layeredPane.add(LTF);
	    JLabel LLabel = new JLabel("L");  
	    LLabel.setBounds(m_width - 250 , 50 + 9*rightLPadding, 100, 20);
	    layeredPane.add(LLabel);
	    
	    /**
	     * Used to apply parameters the user inputs.
	     */
	    JButton applyButton = new JButton("Apply");
	    applyButton.setBounds(m_width-150, 50+13*rightLPadding,100,60);
	    layeredPane.add(applyButton);
	    
	    applyButton.addActionListener(new ActionListener() {
			@Override
           	public void actionPerformed(ActionEvent e) {
				m_scale 		= Integer.parseInt(scaleTF.getText());
				m_paddingX 		= Integer.parseInt(paddingXTF.getText());
				m_paddingY 		= Integer.parseInt(paddingYTF.getText());
				m_screenWidth 	= Integer.parseInt(screenWidthTF.getText());
				m_screenHeight 	= Integer.parseInt(screenHeightTF.getText());
				m_fourierTerms 	= Integer.parseInt(fourierTermsTF.getText());
				m_deltaX 		= Float.parseFloat(deltaXTF.getText());
				m_alpha			= Float.parseFloat(alphaTF.getText());
				m_beta 			= Float.parseFloat(betaTF.getText());
				m_L 			= Float.parseFloat(LTF.getText());
            } 
	    });

		/*
		 * Code to add x,y points by the user. 
		 */
		// Label and text field for X input
        JLabel xInputLabel = new JLabel("X:");
        xInputLabel.setBounds(20, 200, 20, 20);
        layeredPane.add(xInputLabel);
        
        xInputField = new JTextField();
        xInputField.setBounds(50, 200, 100, 20);
        layeredPane.add(xInputField);

        // Label and text field for Y input
        JLabel yInputLabel = new JLabel("Y:");
        yInputLabel.setBounds(170, 200, 20, 20);
        layeredPane.add(yInputLabel);
        
        yInputField = new JTextField();
        yInputField.setBounds(200, 200, 100, 20);
        layeredPane.add(yInputField);

        // Button to add vertex
        JButton addVertexButton = new JButton("Add Vertex");
        addVertexButton.setBounds(320, 200, 120, 20);
        layeredPane.add(addVertexButton);

        addVertexButton.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
                // Get x and y values from text fields
                double x = Double.parseDouble(xInputField.getText());
                double y = Double.parseDouble(yInputField.getText());
                
                // Do something with x and y values (e.g., create a vertex)
                // You can add your logic here
				Vertex v = new Vertex(x,y);

				points.add(v);

				// Update the log text area with the new point
                logTextArea.append("Point added: (" + x + ", " + y + ")\n");

                // Clear the input fields
                xInputField.setText("");
                yInputField.setText("");
            }
        });

		// Log text area for points
        logTextArea = new JTextArea();
        logTextArea.setBounds(20, 240, 420, 300);
        logTextArea.setEditable(false); // readonly
        layeredPane.add(logTextArea);

		// Button to add preset vertices (preset1)
        JButton preset1Button = new JButton("Preset 1");
        preset1Button.setBounds(20, 550, 120, 20);
        layeredPane.add(preset1Button);

        preset1Button.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
                // Define preset vertices and add them to ArrayList

                // Example 1
				List<Float> xs = new ArrayList<>();
				List<Float> ys = new ArrayList<>();
				xs.add(1f); ys.add(1f);
				xs.add(2f); ys.add(0.4f);
				xs.add(3f); ys.add(1f);
				xs.add(4f); ys.add(1.4f);
				xs.add(5f); ys.add(1f);
				xs.add(6f); ys.add(0.4f);
				xs.add(7f); ys.add(1f);
				xs.add(7.8f); ys.add(0.8f);
				xs.add(8.2f); ys.add(0.4f);
				xs.add(8.6f); ys.add(0.6f);
				xs.add(9.2f); ys.add(0.4f);
				xs.add(9.6f); ys.add(0.2f);

				for (int i = 0; i < xs.size(); i++) {
					float x = xs.get(i);
					float y = ys.get(i);
					Vertex v = new Vertex(x, y);
					points.add(v);
					// Update the log text area with the new point
					logTextArea.append("Point added: (" + x + ", " + y + ")\n");
				}
            }
        });

		// Button to add preset vertices (preset2)
        JButton preset2Button = new JButton("Preset 2");
        preset2Button.setBounds(150, 550, 120, 20);
        layeredPane.add(preset2Button);

        preset2Button.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
                // Define preset vertices and add them to ArrayList

                // Example 2
				List<Float> xs = new ArrayList<>();
				List<Float> ys = new ArrayList<>();
				xs.add(1f); ys.add(4f);
				xs.add(2f); ys.add(3f);
				xs.add(3f); ys.add(2f);
				xs.add(4f); ys.add(2.5f);
				xs.add(5f); ys.add(5f);
				xs.add(6f); ys.add(3f);
				xs.add(7f); ys.add(2f);
				xs.add(8f); ys.add(1f);
				xs.add(9f); ys.add(0.5f);

				for (int i = 0; i < xs.size(); i++) {
					float x = xs.get(i);
					float y = ys.get(i);
					Vertex v = new Vertex(x, y);
					points.add(v);
					// Update the log text area with the new point
					logTextArea.append("Point added: (" + x + ", " + y + ")\n");
				}
            }
        });

		// Button to add preset vertices (preset3)
        JButton preset3Button = new JButton("Preset 3");
        preset3Button.setBounds(280, 550, 120, 20);
        layeredPane.add(preset3Button);

        preset3Button.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
                // Define preset vertices and add them to ArrayList

                // Example 3
				List<Float> xs = new ArrayList<>();
				List<Float> ys = new ArrayList<>();
				xs.add(1f); ys.add(1f);
				xs.add(2f); ys.add(3f);
				xs.add(3f); ys.add(6f);
				xs.add(4f); ys.add(8f);
				xs.add(5f); ys.add(2f);
				xs.add(6f); ys.add(1f);
				xs.add(7f); ys.add(-1f);
				xs.add(8f); ys.add(0.5f);
				xs.add(9f); ys.add(-0.5f);

				for (int i = 0; i < xs.size(); i++) {
					float x = xs.get(i);
					float y = ys.get(i);
					Vertex v = new Vertex(x, y);
					points.add(v);
					// Update the log text area with the new point
					logTextArea.append("Point added: (" + x + ", " + y + ")\n");
				}
            }
        });

		// Start simulation button
		JButton StartButton = new JButton("Start");
		StartButton.setBounds(20,50,200,60);
		StartButton.setOpaque(true);
		layeredPane.add(StartButton, JLayeredPane.DEFAULT_LAYER);
		
		StartButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				// Check if we have more points than the initial condition at 0.
				if (points.size() == 1) {
					JOptionPane.showMessageDialog(frame, "Error: There are no points selected!");
					return;
				}

				// Add point for the initial condition at m_L
				points.add(new Vertex(m_L, 0));

				// Choose to start simulation depending on selection by user
				int i = comboBoxEquations.getSelectedIndex();
				switch(comboBoxEquations.getItemAt(i)) {
				case "Wave Damping Equation":
					damping = new WaveDamping();
					damping.setupWaveDampingEquation(points, m_deltaX, m_fourierTerms, m_alpha, m_beta, m_L, m_screenWidth, m_screenHeight, m_scale, m_paddingX, m_paddingY);
					break;
				case "Wave Equation":
					wave = new Wave();
					wave.setupWaveEquation(points, m_deltaX, m_fourierTerms, m_beta, m_L, m_screenWidth, m_screenHeight, m_scale, m_paddingX, m_paddingY);
					break;
				case "Heat Equation":
					heat = new Heat();
					heat.setupHeatEquation(points, m_deltaX, m_fourierTerms, m_alpha, m_L, m_screenWidth, m_screenHeight, m_scale, m_paddingX, m_paddingY);
					break;
				default:
					//this will never happen.
				}
			}
		});
	}
	
	public static void closeWindow() {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
}