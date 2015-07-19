import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;

import javax.swing.*;

public class SimulatedAnnealing extends JPanel {
	static int[] data = new int[40];
	static int[] data2 = new int[40];
	    final int PAD = 20;
	 
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2 = (Graphics2D)g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                            RenderingHints.VALUE_ANTIALIAS_ON);
	        int w = getWidth();
	        int h = getHeight();
	        // Draw ordinate.
	        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
	        // Draw abcissa.
	        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
	        // Draw labels.
	        Font font = g2.getFont();
	        FontRenderContext frc = g2.getFontRenderContext();
	        LineMetrics lm = font.getLineMetrics("0", frc);
	        float sh = lm.getAscent() + lm.getDescent();
	        // Ordinate label.
	        String s = "y axis";
	        float sy = PAD + ((h - 2*PAD) - s.length()*sh)/2 + lm.getAscent();
	        for(int i = 0; i < s.length(); i++) {
	            String letter = String.valueOf(s.charAt(i));
	            float sw = (float)font.getStringBounds(letter, frc).getWidth();
	            float sx = (PAD - sw)/2;
	            g2.drawString(letter, sx, sy);
	            sy += sh;
	        }
	        s = "x axis";
	        sy = h - PAD + (PAD - sh)/2 + lm.getAscent();
	        float sw = (float)font.getStringBounds(s, frc).getWidth();
	        float sx = (w - sw)/2;
	        g2.drawString(s, sx, sy);
	        // Draw lines.
	        //double xInc = (double)(w - 2*PAD)/(data.length-1);
	        //double scale = (double)(h - 2*PAD)/getMax();
	        g2.setPaint(Color.green.darker());
	        for(int i = 0; i < data.length-2; i+=2) {
	            double x1 = data[i];
	            double y1 = data[i+1];
	            double x2 = data[i+2];
	            double y2 = data[i+3];
	            g2.draw(new Line2D.Double(x1, y1, x2, y2));
	        }
	        // Mark data points.
	        g2.setPaint(Color.red);
	        for(int i = 0; i < data.length-1; i+=2) {
	            double x = data[i];
	            //double y = h - PAD - scale*data[i];
	            double y = data[i+1];
	            g2.fill(new Ellipse2D.Double(x-2, y-2, 6, 6));
	        }
	    }
	 
	    private int getMax() {
	        int max = -Integer.MAX_VALUE;
	        for(int i = 0; i < data.length; i++) {
	            if(data[i] > max)
	                max = data[i];
	        }
	        return max;
	    }
    // Calculate the acceptance probability
    public static double acceptanceProbability(int engery, int newEngery, double temperature) {
        // If the new solution is better, accept it
        if (newEngery < engery) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((engery - newEngery) / temperature);
    }
    
    public static void main(String[] args) {
        // Create and add our cities
        City city = new City(60, 200);
        TourManager.addCity(city);
        City city2 = new City(180, 200);
        TourManager.addCity(city2);
        City city3 = new City(80, 180);
        TourManager.addCity(city3);
        City city4 = new City(140, 180);
        TourManager.addCity(city4);
        City city5 = new City(20, 160);
        TourManager.addCity(city5);
        City city6 = new City(100, 160);
        TourManager.addCity(city6);
        City city7 = new City(200, 160);
        TourManager.addCity(city7);
        City city8 = new City(140, 140);
        TourManager.addCity(city8);
        City city9 = new City(40, 120);
        TourManager.addCity(city9);
        City city10 = new City(100, 120);
        TourManager.addCity(city10);
        City city11 = new City(180, 100);
        TourManager.addCity(city11);
        City city12 = new City(60, 80);
        TourManager.addCity(city12);
        City city13 = new City(120, 80);
        TourManager.addCity(city13);
        City city14 = new City(180, 60);
        TourManager.addCity(city14);
        City city15 = new City(20, 40);
        TourManager.addCity(city15);
        City city16 = new City(100, 40);
        TourManager.addCity(city16);
        City city17 = new City(200, 40);
        TourManager.addCity(city17);
        City city18 = new City(20, 20);
        TourManager.addCity(city18);
        City city19 = new City(60, 20);
        TourManager.addCity(city19);
        City city20 = new City(160, 20);
        TourManager.addCity(city20);

        // Set initial temp
        double temp = 100000;

        // Cooling rate
        double coolingRate = 0.0001;

        // Initialize intial solution
        Tour currentSolution = new Tour();
        currentSolution.generateIndividual();
        
        System.out.println("Initial solution distance: " + currentSolution.getDistance());
        // displaying the initial solution.
        
        JFrame f1 = new JFrame();
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        for(int j=0;j<20;j++){
        	data[j*2] = currentSolution.getCity(j).x;
        	data[j*2 + 1] = currentSolution.getCity(j).y;
        }
        f1.add(new SimulatedAnnealing());
        f1.setSize(300,300);
        f1.setVisible(true);

        // Set as current best
        Tour best = new Tour(currentSolution.getTour());
        
        // Loop until system has cooled
        while (temp > 1) {
            // Create new neighbour tour
            Tour newSolution = new Tour(currentSolution.getTour());

            // Get a random positions in the tour
            int tourPos1 = (int) (newSolution.tourSize() * Math.random());
            int tourPos2 = (int) (newSolution.tourSize() * Math.random());

            // Get the cities at selected positions in the tour
            City citySwap1 = newSolution.getCity(tourPos1);
            City citySwap2 = newSolution.getCity(tourPos2);

            // Swap them
            newSolution.setCity(tourPos2, citySwap1);
            newSolution.setCity(tourPos1, citySwap2);
            
            // Get energy of solutions
            int currentEngery = currentSolution.getDistance();
            int neighbourEngery = newSolution.getDistance();

            // Decide if we should accept the neighbour
            if (acceptanceProbability(currentEngery, neighbourEngery, temp) > Math.random()) {
                currentSolution = new Tour(newSolution.getTour());
            }

            // Keep track of the best solution found
            if (currentSolution.getDistance() < best.getDistance()) {
                best = new Tour(currentSolution.getTour());
            }
            
            // Cool system
            temp *= 1-coolingRate;
        }
        // real ans 863 probably 
        System.out.println("Final solution distance: " + best.getDistance());
        //System.out.println("Tour: " + best);
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        for(int j=0;j<20;j++){
        	data[j*2] = best.getCity(j).x;
        	data[j*2 + 1] = best.getCity(j).y;
        }
        f.add(new SimulatedAnnealing());
        f.setSize(300,300);
        f.setLocation(300,00);
        f.setVisible(true);
    }
}