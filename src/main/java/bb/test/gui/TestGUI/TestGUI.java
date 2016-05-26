package bb.test.gui.TestGUI;

import bb.test.lib.ButtonNames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"serial", "javadoc"})
public class TestGUI extends JFrame implements ActionListener {

	public List<Object> objList = new ArrayList<>();
	public List<String> strList = new ArrayList<>();

	public JButton butEnter = new JButton(ButtonNames.ENTER);

	public TestGUI() {

		JPanel mainPanel = new JPanel();
		BoxLayout BLO = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
		mainPanel.setLayout(BLO);
		JTextArea consoleArea = new JTextArea();
		mainPanel.add(consoleArea);
		JTextField inputField = new JTextField();
		mainPanel.add(inputField);
		pack();
		inputField.setMaximumSize(new Dimension(inputField.getMaximumSize().width, inputField.getPreferredSize().height));
		inputField.setMinimumSize(new Dimension(inputField.getMinimumSize().width, inputField.getPreferredSize().height));
		JButton butNewObj = new JButton(ButtonNames.NEWOBJBUTTON);
		mainPanel.add(butNewObj);
		butNewObj.addActionListener(this);
		add(mainPanel);
		pack();
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		switch(arg0.getActionCommand()) {
			case ButtonNames.NEWOBJBUTTON: {
				new ObjCreateDialog(this).setVisible(true);
			}
		}
	}
}
