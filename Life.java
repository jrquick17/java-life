import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Life 
{
	static JFrame worldFrame = new JFrame();
	static JFrame infoFrame = new JFrame();
	static JButton buttons[];
	static JButton ranBut = new JButton("Randomize");
	static JButton clrBut = new JButton("Clear");
	static JButton pasBut = new JButton("Pause");
	static JButton stepBut = new JButton("Step");
	static JButton simBut = new JButton("Simulate");

	static JTextField rowField;
	static JTextField colField;
	static JTextField spdField;
	static JTextField chncField;
	static JTextField genField;

	static Random gen = new Random();
	static int ROW = 50, COLUMN = 50, CHANCE = 5, GENERATIONS = 10000, SPEED = 0;
	static int world[];
	static int temp[];

	static boolean pressed = false, pause = false;

	public static int getRows()
	{
		if (!(rowField == null))
			return Integer.parseInt(rowField.getText());
		else 
			return ROW;
	}

	public static int getColumns()
	{
		if (!(colField == null))
			return Integer.parseInt(colField.getText());
		else
			return COLUMN;
	}

	public static int getSpeed()
	{
		if (!(spdField == null))
			return Integer.parseInt(spdField.getText());
		else
			return SPEED;
	}

	public static int getChance()
	{
		if (!(chncField == null))
			return Integer.parseInt(chncField.getText());
		else
			return CHANCE;
	}

	public static int getGenerations()
	{
		return Integer.parseInt(genField.getText());
	}

	public static void randomize()
	{
		pause = true; 
		
		for (int i = 0; i < getRows()*getColumns(); i++)
			if (gen.nextInt(getChance()) == 1)
				world[i] = 1;
			else 
				world[i] = 0;
	}

	public static void print()
	{
		for (int i = 0; i < ROW*COLUMN; i++)
			if ((i+1) % ROW == 0)
				System.out.println(world[i]);
			else
				System.out.print(world[i]);
	}

	public static int getNeighbors(int place)
	{
		int neighbors = 0;

		for (int i = 0, curN = 999; i < 8; i++)
		{
			if (i == 0)
				curN = place-1;
			else if (i == 1)
				curN = place+1;
			else if (i == 2)
				curN = place-ROW-1;
			else if (i == 3)
				curN = place-ROW;
			else if (i == 4)
				curN = place-ROW+1;
			else if (i == 5)
				curN = place+ROW-1;
			else if (i == 6)
				curN = place+ROW;
			else if (i == 7)
				curN = place+ROW+1;

			if (curN < 0)
				curN = (ROW*COLUMN)+curN;
			else if (curN > (ROW*COLUMN)-1)
				curN = curN - (ROW*COLUMN);

			neighbors += world[curN];
		}

		return neighbors;
	}

	public static int getStatus(int place, int status)
	{
		int neighbors = getNeighbors(place);

		if (status == 1)
			if (neighbors < 2)
				return 0;
			else if (neighbors == 2 || neighbors == 3)
				return 1;
			else if (neighbors > 3)
				return 0;

		if (status == 0)
			if (neighbors == 3)
				return 1;
			else
				return 0;

		return 9;
	}

	public static void simulateGeneration(int generations)
	{
		for (int gen = 0; gen < generations; gen++)
		{
			for (int i = 0; i < ROW*COLUMN; i++)
				temp[i] = getStatus(i, world[i]);

			for (int i = 0; i < ROW*COLUMN; i++)
				world[i] = temp[i];

			highlight();
			
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (pause)
			{
				gen += generations;
				pause = false;
			}
		}
	}

	public static void createButtons()
	{
		buttons = new JButton[ROW*COLUMN];

		for (int i = 0; i < ROW*COLUMN; i++)
		{
			buttons[i] = new JButton();
			buttons[i].setBorderPainted(false);
			buttons[i].addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e)
				{
					for (int i = 0; i < buttons.length; i++)
						if ((""+e.getSource()).equalsIgnoreCase(buttons[i].toString()))
						{
							if (world[i] == 0)
							{
								world[i] = 1;
								buttons[i].setBackground(Color.BLACK);
							}
							else
							{
								world[i] = 0;
								buttons[i].setBackground(Color.WHITE);
							}

							buttons[i].repaint();
						}
				}
			});
		}

	}

	public static void highlight()
	{
		for (int i = 0; i < ROW*COLUMN; i++)
			if (world[i] == 1)
				buttons[i].setBackground(Color.BLACK);
			else 
				buttons[i].setBackground(Color.WHITE);

		for (int i = 0; i < ROW*COLUMN; i++)
			buttons[i].repaint();
	}

	public static void addButtonActions()
	{
		ranBut.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				randomize();
				highlight();
			}
		});

		stepBut.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				simulateGeneration(1);
			}
		});

		simBut.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				pressed = true;
			}
		});
		
		pasBut.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				pause = true;
			}
		});

		clrBut.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				for (int i = 0; i < ROW*COLUMN; i++)
					world[i] = 0;

				highlight();
			}
		});
	}

	public static void createOptions()
	{
		JPanel butPanel = new JPanel();
		butPanel.setLayout(new GridLayout(1,4));
		butPanel.setVisible(true);

		addButtonActions();

		butPanel.add(ranBut);
		butPanel.add(clrBut);
		butPanel.add(pasBut);
		butPanel.add(stepBut);
		butPanel.add(simBut);

		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(2,5));
		textPanel.setVisible(true);
		JLabel rowLbl = new JLabel("Number of Rows: ");
		JLabel colLbl = new JLabel("Number of Columns: ");
		JLabel genLbl = new JLabel("Number of Generations: ");
		JLabel spdLbl = new JLabel("Speed (0-100): ");
		JLabel chncLbl = new JLabel("Chance of Living (1/x): ");
		rowField = new JTextField("" + ROW);
		colField = new JTextField("" + COLUMN);
		spdField = new JTextField("" + SPEED);
		chncField = new JTextField("" + CHANCE);
		genField = new JTextField("" + GENERATIONS);
		textPanel.add(rowLbl);
		textPanel.add(colLbl);
		textPanel.add(genLbl);
		textPanel.add(spdLbl);
		textPanel.add(chncLbl);
		textPanel.add(rowField);
		textPanel.add(colField);
		textPanel.add(genField);
		textPanel.add(spdField);
		textPanel.add(chncField);

		infoFrame.add(butPanel);
		infoFrame.add(textPanel);
	}

	public static void beginState()
	{
		world= new int[ROW*COLUMN];
		temp = new int[ROW*COLUMN];
		
		randomize();
		createButtons();
		
		for (int i = 0; i < buttons.length; i++)
			worldFrame.add(buttons[i]);
		
		highlight();
	}

	public static void main(String[]args) throws InterruptedException
	{	
		beginState();

		worldFrame.setSize(500,500);
		worldFrame.setLayout(new GridLayout(getRows(), getColumns()));
		worldFrame.setVisible(true);

		createOptions();

		infoFrame.setSize(600,150);
		infoFrame.setLayout(new GridLayout(2,1));
		infoFrame.setVisible(true);

		while (true)
		{
			if (pressed)
			{
				simulateGeneration(getGenerations());
				pressed = false;
			}
		}
	}
}
