import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Drawer extends JComponent {
	
	/**
	 * Compiler Warning if this is not present.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Scales everything up (axis, number line, graph, etc.) or down depending on the value.
	 */
	static int m_SCALE;	
	
	/**
	 * Works by pushing the origin closer to the middle of the screen, away from the edge.
	 */
	static int m_PADDINGX;
	
	/**
	 * Works by pushing the origin closer to the middle of the screen, away from the edge.
	 */
	static int m_PADDINGY;
	
	/**
	 * Label that is added to primaryPanel. It contains information about the simulation.
	 */
	static JLabel infoLabel;
	
	/**
	 * This is the empty window that contains the primary panel. 
	 */
	static JFrame frame;
	
	public static void createWindow(Equation m, Graph graph, int SCREENWIDTH, int SCREENHEIGHT, int SCALE, int PADDINGX, int PADDINGY) {
	    frame = new JFrame("Heat Equation 2D");

		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBackground(Color.gray);
	    frame.setSize(SCREENWIDTH, SCREENHEIGHT);
	    frame.setVisible(true);
		frame.setResizable(true); //need to be true, if not the canvas might not show anything!		
		
		//Finding Origin
		m_SCALE = SCALE;
		m_PADDINGX = PADDINGX;
		m_PADDINGY = PADDINGY;
		final int ORIGINX = m_PADDINGX / 2;
		final int ORIGINY = SCREENHEIGHT - m_PADDINGY;
		
		panel = new PrimaryPanel(graph, ORIGINX, ORIGINY, m_SCALE);
		panel.setBounds(0, 0, SCREENWIDTH, SCREENHEIGHT);
		frame.add(panel);
	    panel.setOpaque(true);
	    //create infoLabel
	    infoLabel = new JLabel();
//	    MainMenu.layeredPane.add(infoLabel, JLayeredPane.POPUP_LAYER);
	    
		JButton StartButton = new JButton("Play");
		StartButton.setBounds(100,50,200,60);
		StartButton.setOpaque(true);
		panel.add(StartButton);
		
		
		StartButton.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){
				m.runSimulation(graph);
			}
		});
	}
	
	static PrimaryPanel panel;
	
	public static void UpdateFrame(int n) {
		frame.repaint();
		//update infoLabel text
		infoLabel.setText("Time:    " + String.valueOf(n) + " seconds");
	}	
}