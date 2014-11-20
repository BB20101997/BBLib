package bb.test.gui;

import bb.test.lib.ButtonNames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"serial", "javadoc"})
public class TestGUI extends JFrame implements ActionListener
{

	public List<Object> objList = new ArrayList<>();
	public List<String> strList = new ArrayList<>();

	private JButton butNewObj = new JButton(ButtonNames.NEWOBJBUTTON);
	public  JButton butEnter  = new JButton(ButtonNames.ENTER);

	private JPanel    mainPanel = new JPanel();
	private BoxLayout BLO       = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

	private JTextArea  ConsoleArea = new JTextArea();
	private JTextField InputField  = new JTextField();

	public TestGUI() {

		mainPanel.setLayout(BLO);
		mainPanel.add(ConsoleArea);
		mainPanel.add(InputField);
		pack();
		InputField.setMaximumSize(new Dimension(InputField.getMaximumSize().width, InputField.getPreferredSize().height));
		InputField.setMinimumSize(new Dimension(InputField.getMinimumSize().width, InputField.getPreferredSize().height));
		mainPanel.add(butNewObj);
		butNewObj.addActionListener(this);
		add(mainPanel);
		pack();
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{

		switch(arg0.getActionCommand()){
			case ButtonNames.NEWOBJBUTTON : {
				new ObjCreateDialog(this).setVisible(true);
			}
		}
	}
}
