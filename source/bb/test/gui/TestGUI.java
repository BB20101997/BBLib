package bb.test.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bb.test.lib.ButtonNames;

@SuppressWarnings({ "serial", "javadoc" })
public class TestGUI extends JFrame implements ActionListener
{

	public List<Object>	objList		= new ArrayList<Object>();
	public List<String>	strList		= new ArrayList<String>();

	public JButton		butNewObj	= new JButton(ButtonNames.NEWOBJBUTTON);
	public JButton		butEnter	= new JButton(ButtonNames.ENTER);

	public JPanel		mainPanel	= new JPanel();
	public BoxLayout	BLO			= new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

	public JTextArea	ConsoleArea	= new JTextArea();
	public JTextField	InputField	= new JTextField();

	public TestGUI()
	{

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
