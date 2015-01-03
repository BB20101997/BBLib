package bb.test.gui;

import bb.test.lib.ButtonNames;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"serial", "rawtypes", "javadoc"})
class ObjCreateDialog extends JDialog implements ActionListener
{

	private Class clazz;
	@SuppressWarnings("unused")
	private       Object     obj         = null;
	@SuppressWarnings("unused")
	private       JTextField objName     = new JTextField("Please enter a name for the Object!");
	private final JTextField clazzString = new JTextField("Please enter a fully qualified Class path!");
	private Object[]     objA;
	private JComponent[] jcA;
	private Class[]      cA;
	private final JList<Constructor> constructors = new JList<>();
	private final JButton            enterButton  = new JButton(ButtonNames.ENTER);
	private final JPanel             paraPanel    = new JPanel();
	private final JScrollPane        paraScroll   = new JScrollPane(paraPanel);
	private final Box                b            = new Box(BoxLayout.Y_AXIS);
	private       Object             retObject    = null;
	private       int                state        = 0;

	public ObjCreateDialog(JFrame j) {

		super(j, "Create a new Object", true);
		enterButton.addActionListener(this);
		add(b);
		guiSetupByState();

	}

	private ObjCreateDialog(JDialog j, String name) {

		super(j, "New Object", true);
		state = 1;
		System.out.println("String : " + name);
		try {
			clazz = Class.forName(name);
		} catch(ClassNotFoundException e2) {
			e2.printStackTrace();
		}
		System.out.println("Class : " + clazz);
		Constructor[] constructs = clazz.getConstructors();
		constructors.setListData(constructs);
		enterButton.addActionListener(this);
		add(b);
		guiSetupByState();

	}

	private void guiSetupByState()
	{

		b.removeAll();

		switch(state){
			case 0 : {
				b.add(clazzString);
				b.add(enterButton);
			}
			case 1 : {
				b.add(constructors);
				b.add(enterButton);
			}
			case 2 : {
				b.add(paraScroll);
				b.add(enterButton);
			}
		}

		pack();
	}

	private Class getWrapperClassFromPrimitive(Class t)
	{

		if(t == int.class) { return Integer.class; }
		if(t == short.class) { return Short.class; }
		if(t == float.class) { return Float.class; }
		if(t == boolean.class) { return Boolean.class; }
		if(t == double.class) { return Double.class; }
		if(t == long.class) { return Long.class; }
		if(t == char.class) { return Character.class; }
		return t;

	}

	@SuppressWarnings({"unchecked"})
	@Override
	public void actionPerformed(ActionEvent e)
	{

		if((state == 2)
				&& (e.getActionCommand().equals(ButtonNames.NEWOBJBUTTON) || e.getActionCommand().equals(ButtonNames.CHANGEOBJ)))
		{
			JComponent j = (JComponent) e.getSource();
			if(j instanceof JButton)
			{
				JButton jb = (JButton) j;
				List<JComponent> jL = new ArrayList<>();
				Collections.addAll(jL, jcA);

				int i = jL.indexOf(jb);
				ObjCreateDialog diaL = new ObjCreateDialog(this, cA[i].getName());
				diaL.setVisible(true);
				objA[i] = diaL.getReturn();
				jb.setText(ButtonNames.CHANGEOBJ);
				guiSetupByState();

			}

		}

		if(e.getActionCommand().equals(ButtonNames.ENTER))
		{

			switch(state){

				case 0 : {
					String ct = clazzString.getText();
					System.out.println("String : " + ct);
					try
					{
						clazz = Class.forName(ct);
						System.out.println("Class : " + clazz);
						Constructor[] cA = clazz.getConstructors();
						constructors.setListData(cA);
						state = 1;
					}
					catch(ClassNotFoundException e2)
					{
						java.awt.Toolkit.getDefaultToolkit().beep();
						e2.printStackTrace();
					}

					guiSetupByState();
					break;
				}
				case 1 : {
					cA = constructors.getSelectedValue().getParameterTypes();
					if(cA.length == 0)
					{
						try
						{
							retObject = clazz.newInstance();
						}
						catch(InstantiationException | IllegalAccessException e1)
						{
							e1.printStackTrace();
						}

						state = 3;
						setVisible(false);
						return;
					}
					jcA = new JComponent[cA.length];
					objA = new Object[cA.length];
					for(int i = 0; i < cA.length; i++)
					{
						if(cA[i].isPrimitive() || (cA[i] == String.class))
						{
							JTextField j = new JTextField("Enter a " + cA[i].getName());
							jcA[i] = j;
						}
						else
						{
							JButton j = new JButton(ButtonNames.NEWOBJBUTTON);
							j.addActionListener(this);
							jcA[i] = j;
						}
						paraPanel.add(jcA[i]);
					}
					state = 2;
					guiSetupByState();
					break;
				}
				case 2 : {

					for(int i = 0; i < jcA.length; i++)
					{
						if(jcA[i] instanceof JTextField)
						{
							JTextField jT = (JTextField) jcA[i];
							Object s = jT.getText();
							if(cA[i].isPrimitive())
							{
								Method m;
								try
								{
									if(cA[i] != char.class)
									{
										m = getWrapperClassFromPrimitive(cA[i]).getMethod("valueOf", String.class);
									}
									else
									{
										s = ((String) s).toCharArray()[0];
										objA[i] = s;
										continue;

									}
								}
								catch(NoSuchMethodException | SecurityException e1)
								{
									state = -1;
									e1.printStackTrace();
									return;

								}

								try
								{
									s = m.invoke(m, s);
								}
								catch(IllegalAccessException | IllegalArgumentException
										| InvocationTargetException e1)
								{
									e1.printStackTrace();
									java.awt.Toolkit.getDefaultToolkit().beep();
									return;
								}

								objA[i] = s;
							}
							else
							{
								objA[i] = s;
							}

						}
					}

					try
					{
						retObject = constructors.getSelectedValue().newInstance(objA);
						System.out.println("Created Object of type : " + objA.getClass());
						setVisible(false);
					}
					catch(InstantiationException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		}
	}

	Object getReturn()
	{

		return retObject;
	}
}