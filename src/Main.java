import java.awt.BorderLayout;
import java.awt.Canvas;
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

public class Main {

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
		String Filedir = dir+"/datatest.csv";
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
	double[] distances;
	double[] angles;
	int[] CoOrX;
	int[] CoOrY;
	double[] heightLevel;
	int count=0;
	private void WriteCSV() {
    	// The name of the file to open.
        String fileName = dir+"\\dataset-CoOrdinatePoint.csv";

        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for(int i = 0;i<count;i++){	
            	bufferedWriter.write(distances[i]+","+angles[i]+","+CoOrX[i]+","+CoOrY[i]+","+heightLevel[i]);
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
		distances = new double[10000];
		angles = new double[10000];
		heightLevel = new double[10000];
		CoOrX = new int[10000];
		CoOrY = new int[10000];
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
				MAP map = new MAP();
				for(int i = 0 ; i < setData.size();i++){
					double ang = Math.toRadians(setData.get(i).getAngle());
					double distance = setData.get(i).getDistance();
					double hight = setData.get(i).getHeight();
					double Y = (distance)*Math.cos(ang);
					double X = (distance)*Math.sin(ang);
					int CoX = (int) Math.floor(X);
					int CoY = (int) Math.floor(Y);
					System.out.println("Co-Ordinate of "+distance+":"+setData.get(i).getAngle()+" ("+CoX+" : "+CoY+" : "+hight+")");
					distances[count] = distance;
					angles[count] = setData.get(i).getAngle();
					CoOrX[count] = CoX;
					CoOrY[count] = CoY;
					heightLevel[count] = setData.get(i).getHeight();
					count++;
//					map.setDat(CoX+50,(CoY*-1)+50);
				}
				statusMessage.setText("Write to file complete!");
//				map.show();
				WriteCSV();
			}
		});
		btnCalculate.setBounds(12, 346, 319, 78);
		panel.add(btnCalculate);
		
		Canvas canvas = new Canvas();
		canvas.setBounds(348, 10, 530, 476);
		panel.add(canvas);
	}
}
