package bb.util.gui;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class ChangeDialog extends JDialog implements ActionListener,
ItemListener{

	@SuppressWarnings("rawtypes")
	private final JComboBox combo1;
	private final List<String>                    list1       = new ArrayList<>();
	private final List<UIManager.LookAndFeelInfo> lookAndFeel = new ArrayList<>();
	private UIManager.LookAndFeelInfo LAF;


	@Override
	public void itemStateChanged(ItemEvent arg0) {

		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		switch(arg0.getActionCommand()) {
			case ("Ok"): {
				LAF = lookAndFeel.get(combo1.getSelectedIndex());

				setVisible(false);
				return;
			}

			default: {
				setVisible(false);
		}
		}
				
	}
	
	
	@ SuppressWarnings("rawtypes")
	public ChangeDialog(JFrame J,String Title){
		super(J, Title, true);
		setResizable(false);
		
		combo1 = new JComboBox();
		JButton ok = new JButton("Ok");
		JButton abbrechen = new JButton("Abbrechen");
		
		getLookAndFeels();
		
		setLayout(new BorderLayout());
		
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		addWindowListener(new MainWindowListener());
		
		add("North",combo1);
		add("West", ok);
		add("East", abbrechen);
		
		ok.addActionListener(this);
		abbrechen.addActionListener(this);
		
		pack();
		
	}
	
	@ SuppressWarnings("unchecked")
	void getLookAndFeels(){
		UIManager.LookAndFeelInfo a[] = UIManager.getInstalledLookAndFeels();
		for(int i = 0;i<a.length;i++){
				list1.add(a[i].getName());
			lookAndFeel.add(a[i]);
			combo1.addItem(list1.get(i));
		}
	}
	public LookAndFeelInfo getLookAndFeel(){
		
		return LAF;
	
	}
	private class MainWindowListener extends WindowAdapter {

		public void windowClosing(WindowEvent e) {
			java.awt.Toolkit.getDefaultToolkit().beep();

		}
	}
}
