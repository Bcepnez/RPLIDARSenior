import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jzy3d.chart.AWTChart;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import javax.swing.JSplitPane;

public class Main extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextField statusMessage;

	private ArrayList<Dataset> setData = new ArrayList<Dataset>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}
	
	
	double makeItInt(double x){
		double ret;
		if((x-((int)x))>0.5) ret = Math.ceil(x);
		else ret = Math.floor(x);
		return ret;
	}
	private static String dir = System.getProperty("user.dir");
	private void ReadCSV() {
		String Filedir = dir+"/datatest2.csv";
		BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(Filedir));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] data = line.split(cvsSplitBy);
//                data[0] = distance;
//                data[1] = angle;
//                data[3] = height;.
                Dataset dataset = new Dataset(Double.parseDouble(data[0]), 
                		Double.parseDouble(data[1]), 
                		Double.parseDouble(data[2]));
                setData.add(dataset);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
	}
	ArrayList<Double> distances;
	ArrayList<Double> angles;
	ArrayList<Double> CoordinateX;
	ArrayList<Double> CoordinateY;
	ArrayList<Double> CoordinateZ;
	ArrayList<Coord3d> coOrdinate;
	final static double maxDistance = 800;
	int count=0;
	private void WriteCSV() {
    	// The name of the file to open.
        String fileName = dir+"\\dataset-CoOrdinatePoint.csv";

        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for(int i = 0;i<count;i++){	
            	bufferedWriter.write(distances.get(i)+","+angles.get(i)+","+CoordinateX.get(i)+","+CoordinateY.get(i)+","+CoordinateZ.get(i));
            	bufferedWriter.newLine(); 
            }
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println("Error writing to file '" + fileName + "'");
        }
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		distances = new ArrayList<Double>();
		angles = new ArrayList<Double>();		
		CoordinateX = new ArrayList<Double>();
		CoordinateY = new ArrayList<Double>();
		CoordinateZ = new ArrayList<Double>();
		coOrdinate = new ArrayList<Coord3d>();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 906, 543);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		statusMessage = new JTextField();
		statusMessage.setText("Ready to calculate!");
		statusMessage.setEditable(false);
		statusMessage.setColumns(10);
		statusMessage.setBounds(115, 441, 217, 45);
		panel.add(statusMessage);
		
		JLabel lblX = new JLabel("Status Message :");
		lblX.setBounds(12, 441, 108, 45);
		panel.add(lblX);
						
		JButton btnCalculate = new JButton("Calculate");
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				count = 0;
				ReadCSV();
				for(int i = 0 ; i < setData.size();i++){
					double ang = Math.toRadians(setData.get(i).getAngle());
					double distance = setData.get(i).getDistance();
					double hight = setData.get(i).getHeight();
					double Y = (distance)*Math.cos(ang)+maxDistance;
					double X = (distance)*Math.sin(ang)+maxDistance;
					CoordinateX.add(X);
					CoordinateY.add(Y);
					CoordinateZ.add(hight);
					distances.add(setData.get(i).getDistance());
					angles.add(setData.get(i).getAngle());
					
					Coord3d temp = new Coord3d(CoordinateX.get(i),CoordinateY.get(i),CoordinateZ.get(i));
					coOrdinate.add(temp);
					System.out.println("Co-Ordinate of "+distance+":"+setData.get(i).getAngle()+" ("+
							CoordinateX.get(i)+" : "+
							CoordinateY.get(i)+" : "+
							CoordinateZ.get(i)+")");
					count++;
				}
				statusMessage.setText("Write to file complete!");
				WriteCSV();
			}
		});
		btnCalculate.setBounds(12, 346, 319, 78);
		panel.add(btnCalculate);
	}
}

