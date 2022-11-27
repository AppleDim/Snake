import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 900;
    static final int SCREEN_HEIGHT = 900;
    static final int UNIT_SIZE = 30;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final String outputFile = "GameFiles\\Score.txt";
    static int DELAY = 100;
    static String dateTime;
    static int score;
    static StringBuilder sb;
    static List<Integer> scoreList;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;
    int fruitsEaten;
    int fruitX;
    int fruitY;
    int tempX;
    int tempY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    char tempDirection;
    Random random;


    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT + 100));
        this.setBackground(new Color(0, 51, 25));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.setLayout(null);
        startGame();
    }

    public void startGame() {
        newFruit();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            for (int i = 0; i < (SCREEN_HEIGHT + UNIT_SIZE) / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
                g.setColor(new Color(0, 0, 0));
            }
            int imageIcons = fruitsEaten % 8;
            switch (imageIcons) {
                case 0 ->
                        g.drawImage(new ImageIcon("GameFiles\\redApple.png").getImage(), fruitX, fruitY, UNIT_SIZE, UNIT_SIZE, null);
                case 1 ->
                        g.drawImage(new ImageIcon("GameFiles\\strawberry.png").getImage(), fruitX, fruitY, UNIT_SIZE, UNIT_SIZE, null);
                case 2 ->
                        g.drawImage(new ImageIcon("GameFiles\\banana.png").getImage(), fruitX, fruitY, UNIT_SIZE, UNIT_SIZE, null);
                case 3 ->
                        g.drawImage(new ImageIcon("GameFiles\\orange.png").getImage(), fruitX, fruitY, UNIT_SIZE, UNIT_SIZE, null);
                case 4 ->
                        g.drawImage(new ImageIcon("GameFiles\\grape.png").getImage(), fruitX, fruitY, UNIT_SIZE, UNIT_SIZE, null);
                case 5 ->
                        g.drawImage(new ImageIcon("GameFiles\\cherry.png").getImage(), fruitX, fruitY, UNIT_SIZE, UNIT_SIZE, null);
                case 6 ->
                        g.drawImage(new ImageIcon("GameFiles\\pear.png").getImage(), fruitX, fruitY, UNIT_SIZE, UNIT_SIZE, null);
                case 7 ->
                        g.drawImage(new ImageIcon("GameFiles\\peach.png").getImage(), fruitX, fruitY, UNIT_SIZE, UNIT_SIZE, null);

            }


            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    switch (direction) {
                        case 'U' -> {
                            g.drawImage(new ImageIcon("GameFiles\\headUp.png").getImage(), x[0], y[0], UNIT_SIZE, UNIT_SIZE, null);
                            tempDirection = 'U';
                        }
                        case 'D' -> {
                            g.drawImage(new ImageIcon("GameFiles\\headDown.png").getImage(), x[0], y[0], UNIT_SIZE, UNIT_SIZE, null);
                            tempDirection = 'D';
                        }
                        case 'L' -> {
                            g.drawImage(new ImageIcon("GameFiles\\headLeft.png").getImage(), x[0], y[0], UNIT_SIZE, UNIT_SIZE, null);
                            tempDirection = 'L';
                        }
                        case 'R' -> {
                            g.drawImage(new ImageIcon("GameFiles\\headRight.png").getImage(), x[0], y[0], UNIT_SIZE, UNIT_SIZE, null);
                            tempDirection = 'R';
                        }
                    }
                } else {
                    g.setColor(new Color(90, 150, 90));
                    g.fillRoundRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE, 10, 10);
                  /*  switch (tempDirection) {
                        case 'U', 'D' -> {
                            g.drawImage(new ImageIcon("GameFiles\\bodyVert.png").getImage(), x[i], y[i], UNIT_SIZE, UNIT_SIZE, null);
                            tempDirection = 'U';
                        }
                        case 'L', 'R' -> {
                            g.drawImage(new ImageIcon("GameFiles\\bodyHor.png").getImage(), x[i], y[i], UNIT_SIZE, UNIT_SIZE, null);
                            tempDirection = 'R';
                        }
                    }
                }
                   */

                    g.setColor(Color.WHITE);
                    g.setFont(new Font("MV Boli", Font.PLAIN, 100));
                    FontMetrics metrics = getFontMetrics(g.getFont());
                    g.drawString("Score: " + fruitsEaten,
                            (SCREEN_WIDTH - metrics.stringWidth("Score: " + fruitsEaten)) / 2,
                            SCREEN_HEIGHT + 85);
                }
            }
        } else gameOver(g);
    }

    public void newFruit() {
        boolean flag = false;
        while (!flag) {
            flag = true;
            tempX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
            tempY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;

            for (int i = 0; i < bodyParts; i++) {
                if (tempX == x[i] && tempY == y[i]) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                fruitX = tempX;
                fruitY = tempY;
            }
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void checkFruit() {
        if ((x[0] == fruitX) && (y[0] == fruitY)) {
            bodyParts++;
            fruitsEaten++;
            int randomSound = (int) ((Math.random()) * 4);
            switch (randomSound) {
                case 0 -> Sound.playSound("GameFiles\\eat1.wav");
                case 1 -> Sound.playSound("GameFiles\\eat2.wav");
                case 2 -> Sound.playSound("GameFiles\\eat3.wav");
                case 3 -> Sound.playSound("GameFiles\\eat4.wav");
            }
            newFruit();
            if (timer.getDelay() > 50) {
                timer.setDelay(DELAY - fruitsEaten * 2);
            }
        }
    }

    public void checkCollisions() {

        //checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
        if (x[0] < 0) {
            x[0] = SCREEN_WIDTH - UNIT_SIZE;
            // running = false;
        }
        if (x[0] > SCREEN_WIDTH - UNIT_SIZE) {
            x[0] = 0;
            // running = false;
        }
        if (y[0] < 0) {
            y[0] = SCREEN_HEIGHT - UNIT_SIZE;
            //  running = false;
        }
        if (y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
            y[0] = 0;
            //  running = false;
        }
    }

    public void checkRunning() {
        if (!running) {
            timer.stop();
            dateTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd LLLL yyyy HH:mm:ss"));
            score = fruitsEaten;
            writeToFileStatistics();
        }
    }

    public void writeToFileStatistics() {
        try {
            String text = dateTime + ": " + score + "\n";
            Files.write(Paths.get(outputFile), text.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.RED);
        g.setFont(new Font("Times New Roman", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + fruitsEaten,
                (SCREEN_WIDTH - metrics1.stringWidth("Score: " + fruitsEaten)) / 2,
                SCREEN_HEIGHT / 4);
        g.drawString("HighScore: " + calculateHighScore(),
                (SCREEN_WIDTH - metrics1.stringWidth("HighScore: " + calculateHighScore())) / 2,
                SCREEN_HEIGHT / 3);
        //Game OVER text
        g.setColor(Color.RED);
        g.setFont(new Font("Times New Roman", Font.BOLD, 125));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over",
                (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2,
                SCREEN_HEIGHT / 2 + 100);
        int deathSound = (int) (Math.random() * 3);
        switch (deathSound) {
            case 0 -> Sound.playSound("GameFiles\\death1.wav");
            case 1 -> Sound.playSound("GameFiles\\death2.wav");
            case 2 -> Sound.playSound("GameFiles\\death3.wav");
        }
    }

    public static int calculateHighScore() {
        sb = new StringBuilder();
        scoreList = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader("GameFiles\\Score.txt"))) {
            while (fileReader.ready()) {
                sb.append(fileReader.readLine()).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] lines = sb.toString().split("\n");
        for (String line : lines) {
            scoreList.add(Integer.parseInt(line.substring(line.lastIndexOf(":") + 2)));
        }
        Collections.sort(scoreList);
        return scoreList.get(scoreList.size() - 1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFruit();
            checkCollisions();
            checkRunning();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
