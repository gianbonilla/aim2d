import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener, MouseListener {
    static final int SCREEN_WIDTH = 1600;
    static final int SCREEN_HEIGHT = 900;
    static final int UNIT_SIZE = 150; // change this for bigger circles
    static final int DELAY = 75;
    int circlesClicked = 0; // starting score
    boolean running = false; // game state
    Timer timer;
    Random random;
    //
    int apple1X, apple2X, apple3X; // x coords for circles
    int apple1Y, apple2Y, apple3Y; // y coords for circles
    //
    JPanel clockPanel;
    JLabel clock;
    long startTime;
    long endTime;
    //
    long elapsedMillis;
    long elapsedSeconds;
    long elapsedTenthSeconds;
    //
    int xClick;
    int yClick;
    int gridHitX;
    int gridHitY;
    //
    int sec, min;
    //
    int life=3;
    //
    float hitsPerSecond;
    //
    String causeGameOver; // specify cause of game over
    //

    // SOUND class
    Sound sound = new Sound();
    // --

    //
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.darkGray); // set background color
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        addMouseListener(this);
        startGame();
    }

    // game starts
    public void startGame() {
        spawnCircle();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
        clockMethod();
    }

    // paintComponent method
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // draw method
    public void draw(Graphics g) {
        if (running) {

            for(int i=0; i<SCREEN_WIDTH/UNIT_SIZE; i++) {
                // g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
                // g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            }


            // draw circle
            drawCircle(g);

            // score
            g.setColor(Color.white);
            g.setFont(new Font("Courier New", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(String.valueOf(circlesClicked),(SCREEN_WIDTH - metrics.stringWidth(String.valueOf(circlesClicked)))/2,2*g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }

    // spawn 3 circles at start
    public void spawnCircle() {
        newCircle1Coords();
        newCircle2Coords();
        newCircle3Coords();
    }

    // method to spawn circles in the grid, given coords
    public void drawCircle(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(apple1X, apple1Y, UNIT_SIZE, UNIT_SIZE);
        g.fillOval(apple2X, apple2Y, UNIT_SIZE, UNIT_SIZE);
        g.fillOval(apple3X, apple3Y, UNIT_SIZE, UNIT_SIZE);
    }

    // new circle 1 coords
    public void newCircle1Coords() {
        apple1X = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        apple1Y = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }

    // new circle 2 coords
    public void newCircle2Coords() {
        apple2X = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        apple2Y = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }

    // new circle 3 coords
    public void newCircle3Coords() {
        apple3X = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        apple3Y = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }

    // method to figure out coords where you clicked in the grid
    public void gridify() {
        // gridify clicked coords
        gridHitX = ((xClick/UNIT_SIZE))*UNIT_SIZE;
        gridHitY = ((yClick/UNIT_SIZE))*UNIT_SIZE;
    }

    //  method to check whenever you click a circle
    public void checkCircle() {
        //
        gridify();
        //
        if ((gridHitX == apple1X)&&(gridHitY == apple1Y)) {
            circlesClicked++;
            playSFX(0); // play hit SFX
            newCircle1Coords();
        } else if ((gridHitX == apple2X)&&(gridHitY == apple2Y)) {
            circlesClicked++;
            playSFX(0); // play hit SFX
            newCircle2Coords();
        } else if ((gridHitX == apple3X)&&(gridHitY == apple3Y)) {
            circlesClicked++;
            playSFX(0); // play hit SFX
            newCircle3Coords();
        } else {
            missCircle();
            life--; // subtract 1 life
            // fixes bug that plays sfx in game over screen
            if(life>=0) {
                playSFX(1); // play lost heart SFX
            }
        }
    }

    // game over condition #1
    public void missCircle() {
        if(life==0) {
            running = false;
            causeGameOver = "(used all 3 lives)";
        }
    }

    // game over condition #2
    public void timeRanOut() {
        if(elapsedSeconds==60){
            running=false;
            causeGameOver = "(time ran out)";
        }
    }

    // method
    public void isRunning() {
        if (!running) {
            timer.stop();
        }
    }

    // method for calculating pts per sec
    public void speedStats() {
        if(elapsedSeconds>0)
            hitsPerSecond = (float)circlesClicked/elapsedSeconds;
    }

    // game over screen
    public void gameOver(Graphics g) {
        // GAME OVER
        g.setColor(Color.red);
        g.setFont(new Font("Courier New", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER",(SCREEN_WIDTH - metrics1.stringWidth("GAME OVER"))/2,2*g.getFont().getSize());

        // CAUSE
        g.setColor(Color.red);
        g.setFont(new Font("Courier New", Font.BOLD, 15));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString(causeGameOver,(SCREEN_WIDTH - metrics2.stringWidth(causeGameOver))/2,3*g.getFont().getSize()+60);

        // score label
        g.setColor(Color.green);
        g.setFont(new Font("Courier New", Font.PLAIN, 15));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("SCORE",(SCREEN_WIDTH - metrics3.stringWidth("SCORE"))/2,SCREEN_HEIGHT/2-160);

        // score total
        g.setColor(Color.green);
        g.setFont(new Font("Courier New", Font.BOLD, 200));
        FontMetrics metrics4 = getFontMetrics(g.getFont());
        g.drawString(String.valueOf(circlesClicked),(SCREEN_WIDTH - metrics4.stringWidth(String.valueOf(circlesClicked)))/2,SCREEN_HEIGHT/2);

        // hits per second
        g.setColor(Color.green);
        g.setFont(new Font("Courier New", Font.BOLD, 15));
        FontMetrics metrics5 = getFontMetrics(g.getFont());
        g.drawString(hitsPerSecond + " pts/sec",(SCREEN_WIDTH - metrics5.stringWidth(hitsPerSecond + " pts/sec"))/2,SCREEN_HEIGHT/2+25);

        // restart notice
        g.setColor(Color.green);
        g.setFont(new Font("Courier New", Font.PLAIN, 20));
        FontMetrics metrics6 = getFontMetrics(g.getFont());
        g.drawString("Press 'R' to play again",(SCREEN_WIDTH - metrics6.stringWidth("Press 'R' to play again"))/2,SCREEN_HEIGHT/2+70);
    }

    // restart method
    public void restartGame() {
        setVisible(false);
        new GameFrame();
    }

    // countdown timer
    public void clockMethod() {
        startTime = System.currentTimeMillis();

        clock = new JLabel("00:00");
        clockPanel = new JPanel();
        clockPanel.add(clock);
        add(clockPanel);
    }

    // play sfx
    public void playSFX(int i) {
        sound.setFile(i);
        sound.play();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            missCircle();
            timeRanOut();
            isRunning();
            speedStats();
        }

        if(timer.isRunning())
        {
            endTime = System.currentTimeMillis();

            // elapsed milliseconds
            elapsedMillis = endTime-startTime;
            // elapsed quarter seconds for spawns
            elapsedTenthSeconds = (endTime-startTime)/100;

            // put elapsed seconds into variable
            elapsedSeconds = (endTime-startTime)/1000;

            // declare formatting
            min = (int)elapsedSeconds/60;
            sec = (int)elapsedSeconds%60;
            String countdown = String.format("%02d:%02d", (60-elapsedSeconds) / 60, (60-sec) % 60);

            // display elapsed time (minutes:seconds)
            clock.setText(countdown);

            // spawn circle
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        xClick=e.getX();
        yClick=e.getY();
        checkCircle();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    // restart by pressing 'R'
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                restartGame();
            }
        }
    }
}
