package pc;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Treshhold extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JSlider slider;
	private JSlider slider_1;
	private JSlider slider_2;
	private JSlider slider_3;
	private JSlider slider_4;
	private JSlider slider_5;
	private int H_MIN = 0;
	private int H_MAX = 255;
	private int S_MIN = 0;
	private int S_MAX = 255;
	private int V_MIN = 0;
	private int V_MAX = 255;

	/**
	 * Create the frame.
	 */
	public Treshhold() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 356, 427);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 20));

		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblHmin = new JLabel("H_MIN");
		panel.add(lblHmin);

		slider = new JSlider(0, 255, 0);
		panel.add(slider);

		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				textField.setText(String.valueOf(slider.getValue()));
				H_MIN = slider.getValue();
			}
		});

		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(3);

		textField.addActionListener(new ActionListener() {    
			@Override
			public void actionPerformed(ActionEvent e) {
				try
				{
					slider.setValue(Integer.parseInt(textField.getText()));
					H_MIN = slider.getValue();
				}
				catch(Exception ex)
				{
					textField.setText("fejl");
					textField.setToolTipText("Set Value in Range between 0 - 179 ") ;
				}
			}
		});


		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);

		JLabel lblHmax = new JLabel("H_MAX");
		panel_1.add(lblHmax);

		slider_1 = new JSlider(0, 255, 255);
		panel_1.add(slider_1);

		slider_1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				textField_1.setText(String.valueOf(slider_1.getValue()));
				H_MAX = slider_1.getValue();
			}
		});

		textField_1 = new JTextField();
		textField_1.setColumns(3);
		panel_1.add(textField_1);

		textField_1.addActionListener(new ActionListener() {    
			@Override
			public void actionPerformed(ActionEvent e) {
				try
				{
					slider_1.setValue(Integer.parseInt(textField_1.getText()));
					H_MAX = slider_1.getValue();
				}
				catch(Exception ex)
				{
					textField_1.setText("fejl");
					textField_1.setToolTipText("Set Value in Range between 0 - 179 ") ;
				}
			}
		});

		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);

		JLabel lblSmin = new JLabel("S_MIN");
		panel_2.add(lblSmin);

		slider_2 = new JSlider(0,256, 0);
		panel_2.add(slider_2);

		slider_2.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				textField_2.setText(String.valueOf(slider_2.getValue()));
				S_MIN = slider_2.getValue();
			}
		});

		textField_2 = new JTextField();
		textField_2.setColumns(3);
		panel_2.add(textField_2);

		textField_2.addActionListener(new ActionListener() {   

			@Override
			public void actionPerformed(ActionEvent e) {

				try
				{
					slider_2.setValue(Integer.parseInt(textField_2.getText()));
					S_MIN = slider_2.getValue();
				}
				catch(Exception ex)
				{
					textField_2.setText("fejl");

				}
			}
		});

		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3);

		JLabel lblSmax = new JLabel("S_MAX");
		panel_3.add(lblSmax);

		slider_3 = new JSlider(0,255,255);
		panel_3.add(slider_3);

		slider_3.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				textField_3.setText(String.valueOf(slider_3.getValue()));
				S_MAX = slider_3.getValue();
			}
		});

		textField_3 = new JTextField();
		textField_3.setColumns(3);
		panel_3.add(textField_3);

		textField_3.addActionListener(new ActionListener() {    
			@Override
			public void actionPerformed(ActionEvent e) {
				try
				{
					slider_3.setValue(Integer.parseInt(textField_3.getText()));
					S_MAX = slider_3.getValue();
				}
				catch(Exception ex)
				{
					textField_3.setText("fejl");

				}
			}
		});

		JPanel panel_4 = new JPanel();
		contentPane.add(panel_4);

		JLabel lblVmin = new JLabel("V_MIN");
		panel_4.add(lblVmin);

		slider_4 = new JSlider(0,255,0);
		panel_4.add(slider_4);

		slider_4.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				textField_4.setText(String.valueOf(slider_4.getValue()));
				V_MIN = slider_4.getValue();
			}
		});

		textField_4 = new JTextField();
		textField_4.setColumns(3);
		panel_4.add(textField_4);

		textField_4.addActionListener(new ActionListener() {    
			@Override
			public void actionPerformed(ActionEvent e) {
				try
				{
					slider_4.setValue(Integer.parseInt(textField_4.getText()));
					V_MIN = slider_4.getValue();
				}
				catch(Exception ex)
				{
					textField_4.setText("fejl");

				}
			}
		});

		JPanel panel_5 = new JPanel();
		contentPane.add(panel_5);

		JLabel lblVmax = new JLabel("V_MAX");
		panel_5.add(lblVmax);

		slider_5 = new JSlider(0,255,255);
		panel_5.add(slider_5);

		slider_5.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				textField_5.setText(String.valueOf(slider_5.getValue()));
				V_MAX = slider_5.getValue();
			}
		});

		textField_5 = new JTextField();
		textField_5.setColumns(3);
		panel_5.add(textField_5);

		textField_5.addActionListener(new ActionListener() {    
			@Override
			public void actionPerformed(ActionEvent e) {
				try
				{
					slider_5.setValue(Integer.parseInt(textField_5.getText()));
					V_MAX = slider_5.getValue();
				}
				catch(Exception ex)
				{
					textField_5.setText("fejl");
				}
			}
		});
	}

	public int getH_MIN() {
		return H_MIN;
	}

	public int getH_MAX() {
		return H_MAX;
	}

	public int getS_MIN() {
		return S_MIN;
	}

	public int getS_MAX() {
		return S_MAX;
	}

	public int getV_MIN() {
		return V_MIN;
	}

	public int getV_MAX() {
		return V_MAX;
	}
}
