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

public class Life {
    static JFrame worldFrame = new JFrame();
    static JFrame infoFrame = new JFrame();
    static JButton buttons[];
    static JButton buttonRandom = new JButton("Randomize");
    static JButton buttonClear = new JButton("Clear");
    static JButton buttonPause = new JButton("Pause");
    static JButton buttonStep = new JButton("Step");
    static JButton buttonSimulate = new JButton("Simulate");

    static JTextField fieldRow;
    static JTextField fieldColumn;
    static JTextField fieldSpeed;
    static JTextField fieldChance;
    static JTextField fieldGenerate;

    static Random randomGenerator = new Random();
    static final int ROW = 50, COLUMN = 50, CHANCE = 5, GENERATIONS = 10000, SPEED = 0;
    static int world[];
    static int temp[];

    static boolean pressed = false, pause = false;

    public static int getRows() {
        if (!(fieldRow == null)) {
            return Integer.parseInt(fieldRow.getText());
        } else {
            return ROW;
        }
    }

    public static int getColumns() {
        if (!(fieldColumn == null)) {
            return Integer.parseInt(fieldColumn.getText());
        } else {
            return COLUMN;
        }
    }

    public static int getSpeed() {
        if (!(fieldSpeed == null)) {
            return Integer.parseInt(fieldSpeed.getText());
        } else {
            return SPEED;
        }
    }

    public static int getChance() {
        if (!(fieldChance == null)) {
            return Integer.parseInt(fieldChance.getText());
        } else {
            return CHANCE;
        }
    }

    public static int getGenerations() {
        return Integer.parseInt(fieldGenerate.getText());
    }

    public static void randomize() {
        pause = true; 
        
        for (int i = 0; i < getRows()*getColumns(); i++) {
            if (randomGenerator.nextInt(getChance()) == 1) {
                world[i] = 1;
            } else {
                world[i] = 0;
            }
        }
    }

    public static void print() {
        for (int i = 0; i < ROW*COLUMN; i++) {
            if ((i + 1) % ROW == 0) {
                System.out.println(world[i]);
            } else {
                System.out.print(world[i]);
            }
        }
    }

    public static int getNeighbors(int place) {
        int neighbors = 0;

        for (int i = 0, curN = 999; i < 8; i++) {
            if (i == 0) {
                curN = place - 1;
            } else if (i == 1) {
                curN = place + 1;
            } else if (i == 2) {
                curN = place - ROW - 1;
            } else if (i == 3) {
                curN = place - ROW;
            } else if (i == 4) {
                curN = place - ROW + 1;
            } else if (i == 5) {
                curN = place + ROW - 1;
            } else if (i == 6) {
                curN = place + ROW;
            } else if (i == 7) {
                curN = place + ROW + 1;
            }

            if (curN < 0) {
                curN = (ROW * COLUMN) + curN;
            } else if (curN > (ROW*COLUMN)-1) {
                curN = curN - (ROW * COLUMN);
            }

            neighbors += world[curN];
        }

        return neighbors;
    }

    public static int getStatus(int place, int status) {
        int neighbors = getNeighbors(place);

        if (status == 1) {
            if (neighbors < 2) {
                return 0;
            } else if (neighbors == 2 || neighbors == 3) {
                return 1;
            } else if (neighbors > 3) {
                return 0;
            }
        }

        if (status == 0) {
            if (neighbors == 3) {
                return 1;
            } else {
                return 0;
            }
        }

        return 9;
    }

    public static void simulateGeneration(int generations) {
        for (int gen = 0; gen < generations; gen++) {
            for (int i = 0; i < ROW*COLUMN; i++) {
                temp[i] = getStatus(i, world[i]);
            }

            for (int i = 0; i < ROW*COLUMN; i++) {
                world[i] = temp[i];
            }

            highlight();
            
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            if (pause) {
                gen += generations;
                pause = false;
            }
        }
    }

    public static void createButtons() {
        buttons = new JButton[ROW*COLUMN];

        for (int i = 0; i < ROW*COLUMN; i++) {
            buttons[i] = new JButton();
            buttons[i].setBorderPainted(false);
            buttons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < buttons.length; i++) {
                        if (("" + e.getSource()).equalsIgnoreCase(buttons[i].toString())) {
                            if (world[i] == 0) {
                                world[i] = 1;
                                buttons[i].setBackground(Color.BLACK);
                            } else {
                                world[i] = 0;
                                buttons[i].setBackground(Color.WHITE);
                            }

                            buttons[i].repaint();
                        }
                    }
                }
            });
        }
    }

    public static void highlight() {
        for (int i = 0; i < ROW * COLUMN; i++) {
            if (world[i] == 1) {
                buttons[i].setBackground(Color.BLACK);
            } else {
                buttons[i].setBackground(Color.WHITE);
            }
        }

        for (int i = 0; i < ROW * COLUMN; i++) {
            buttons[i].repaint();
        }
    }

    public static void addButtonActions() {
        buttonRandom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                randomize();
                highlight();
            }
        });

        buttonStep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateGeneration(1);
            }
        });

        buttonSimulate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pressed = true;
            }
        });
        
        buttonPause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pause = true;
            }
        });

        buttonClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < ROW*COLUMN; i++) {
                    world[i] = 0;
                }

                highlight();
            }
        });
    }

    public static void createOptions() {
        JPanel butPanel = new JPanel();
        butPanel.setLayout(new GridLayout(1,4));
        butPanel.setVisible(true);

        addButtonActions();

        butPanel.add(buttonRandom);
        butPanel.add(buttonClear);
        butPanel.add(buttonPause);
        butPanel.add(buttonStep);
        butPanel.add(buttonSimulate);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(2,5));
        textPanel.setVisible(true);
        JLabel rowLbl = new JLabel("Number of Rows: ");
        JLabel colLbl = new JLabel("Number of Columns: ");
        JLabel genLbl = new JLabel("Number of Generations: ");
        JLabel spdLbl = new JLabel("Speed (0-100): ");
        JLabel chncLbl = new JLabel("Chance of Living (1/x): ");
        fieldRow = new JTextField("" + ROW);
        fieldColumn = new JTextField("" + COLUMN);
        fieldSpeed = new JTextField("" + SPEED);
        fieldChance = new JTextField("" + CHANCE);
        fieldGenerate = new JTextField("" + GENERATIONS);
        textPanel.add(rowLbl);
        textPanel.add(colLbl);
        textPanel.add(genLbl);
        textPanel.add(spdLbl);
        textPanel.add(chncLbl);
        textPanel.add(fieldRow);
        textPanel.add(fieldColumn);
        textPanel.add(fieldGenerate);
        textPanel.add(fieldSpeed);
        textPanel.add(fieldChance);

        infoFrame.add(butPanel);
        infoFrame.add(textPanel);
    }

    public static void beginState() {
        world= new int[ROW*COLUMN];
        temp = new int[ROW*COLUMN];
        
        randomize();
        createButtons();
        
        for (int i = 0; i < buttons.length; i++) {
            worldFrame.add(buttons[i]);
        }
        
        highlight();
    }

    public static void main(String[]args) throws InterruptedException {
        beginState();

        worldFrame.setSize(500,500);
        worldFrame.setLayout(new GridLayout(getRows(), getColumns()));
        worldFrame.setVisible(true);

        createOptions();

        infoFrame.setSize(600,150);
        infoFrame.setLayout(new GridLayout(2,1));
        infoFrame.setVisible(true);

        while (true) {
            if (pressed) {
                simulateGeneration(getGenerations());
                pressed = false;
            }
        }
    }
}
