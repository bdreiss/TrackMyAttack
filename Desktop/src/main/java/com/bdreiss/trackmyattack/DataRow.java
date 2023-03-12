package main.java.com.bdreiss.trackmyattack;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.awt.Dimension;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Date;

class DataRow extends JPanel{
	
	public DataRow(String title, ArrayList<Boolean> data, Color color){

		setMinimumSize(new Dimension(super.getWidth(),Dimensions.HEIGHT.value()));
		setMaximumSize(new Dimension(super.getWidth(),Dimensions.HEIGHT.value()));
		setPreferredSize(new Dimension(super.getWidth(),Dimensions.HEIGHT.value()));
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets.left = Dimensions.SPACE.value();
		c.insets.right = 0;
		c.insets.bottom = 0;
		c.insets.top = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;

		JLabel label = new JLabel(title);

		
		label.setMinimumSize(new Dimension(Dimensions.LABEL_WIDTH.value(),Dimensions.HEIGHT.value()));
		label.setMaximumSize(new Dimension(Dimensions.LABEL_WIDTH.value(),Dimensions.HEIGHT.value()));
		label.setPreferredSize(new Dimension(Dimensions.LABEL_WIDTH.value(),Dimensions.HEIGHT.value()));

		add(label,c);
				
		JPanel dataPanel = new JPanel();
		
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
		layout.setVgap(0);
		layout.setHgap(0);

		dataPanel.setLayout(layout);
			
		for (Boolean b: data) {

			JPanel box = new JPanel(){
			
				@Override
				public void paint(Graphics graphics){
			
					if (b)
						graphics.setColor(color);
					else
						graphics.setColor(Colors.EMPTY_COLOR.value());
					
					graphics.fillRect(0,0,Dimensions.WIDTH.value(),Dimensions.HEIGHT.value());
			
				}
		
			};
		
			box.setMinimumSize(new Dimension(Dimensions.WIDTH.value(),Dimensions.HEIGHT.value()));
			box.setMaximumSize(new Dimension(Dimensions.WIDTH.value(),Dimensions.HEIGHT.value()));
			box.setPreferredSize(new Dimension(Dimensions.WIDTH.value(),Dimensions.HEIGHT.value()));
			
			dataPanel.add(box);	
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = Dimensions.RATIO.value();
		c.gridx = 1;
		c.gridy = 0;
		add(dataPanel,c);
		
	} 

	public DataRow(ArrayList<ArrayList<Boolean>> data){
		
		setPreferredSize(new Dimension(super.getWidth(),Dimensions.HEIGHT.value()));
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets.left = Dimensions.SPACE.value();
		c.insets.right = 0;
		c.insets.bottom = 0;
		c.insets.top = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		

		

		JLabel label = new JLabel();

		
		label.setMinimumSize(new Dimension(Dimensions.LABEL_WIDTH.value(),Dimensions.HEIGHT.value()));
		label.setMaximumSize(new Dimension(Dimensions.LABEL_WIDTH.value(),Dimensions.HEIGHT.value()));
		label.setPreferredSize(new Dimension(Dimensions.LABEL_WIDTH.value(),Dimensions.HEIGHT.value()));

		add(label,c);
				
		JPanel dataPanel = new JPanel();
		
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
		layout.setVgap(0);
		layout.setHgap(0);

		dataPanel.setLayout(layout);
			
		ArrayList<JLabel> labels = new ArrayList<>();
		
		if ((data.size()>0) && (data.get(0).size()>6)){

			Date date = new Date();
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			for (int i = data.get(0).size(); i > 0; i -= 7) {
				
				localDate = localDate.minusWeeks(1);
				int month = localDate.getMonthValue();
				int day = localDate.getDayOfMonth();
				JLabel dateLabel = new JLabel(month + "-" + day);
				dateLabel.setMinimumSize(new Dimension(Dimensions.WIDTH.value()*7,Dimensions.HEIGHT.value()));
				dateLabel.setMaximumSize(new Dimension(Dimensions.WIDTH.value()*7,Dimensions.HEIGHT.value()));
				dateLabel.setPreferredSize(new Dimension(Dimensions.WIDTH.value()*7,Dimensions.HEIGHT.value()));
				
				labels.add(dateLabel);
			}
		}
		
		for (int i = labels.size(); i > 0; i--)
			dataPanel.add(labels.get(i-1));

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = Dimensions.RATIO.value();
		c.gridx = 1;
		c.gridy = 0;
		add(dataPanel,c);
		
	} 
}
