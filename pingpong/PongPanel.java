
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PongPanel extends JPanel implements Runnable{
    static final int GAME_WIDTH =1000;
    static final int GAME_HEIGHT =(int)(GAME_WIDTH*(0.555));
    static final Dimension screen_size = new Dimension(GAME_WIDTH,GAME_HEIGHT);
    static final int ball_diameter=20;
    static final int paddle_width=20;
    static final int paddle_height=100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;

    PongPanel(){
        newPaddles();
        newBall();
        score= new Score(GAME_WIDTH ,GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(screen_size);

        gameThread =new Thread(this);
        gameThread.start();
    }
    public void newBall(){
       random = new Random();
        ball=new Ball((GAME_WIDTH/2)-(ball_diameter/2),/*(GAME_HEIGHT/2)-(ball_diameter/2)*/random.nextInt(GAME_HEIGHT-ball_diameter),ball_diameter,ball_diameter);

    }
    public void newPaddles(){
        paddle1 =new Paddle(0,(GAME_HEIGHT/2)-(paddle_height/2),paddle_width ,paddle_height,1);
        paddle2= new Paddle(GAME_WIDTH -paddle_width,(GAME_HEIGHT/2)-(paddle_height/2),paddle_width ,paddle_height,2);

    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        image =createImage(getWidth(),getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0,this);

    }

    private void draw(Graphics g) {
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);

    }
    public void move(){
        paddle1.move();
        paddle2.move();
        ball.move();

    }
    public void checkCollision(){
        //bounce ball off  top & bottom window edges

        if(ball.y<=0){
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.y>=GAME_HEIGHT - ball_diameter){
            ball.setYDirection(-ball.yVelocity);
        }
        //bounce ball of paddles
        if(ball.intersects(paddle1)){
            ball.xVelocity=Math.abs(ball.xVelocity);
            ball.xVelocity++;//option for more difficulty
            if(ball.yVelocity>0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        if(ball.intersects(paddle2)){
            ball.xVelocity=Math.abs(ball.xVelocity);
           ball.xVelocity++;//option for more difficulty
            if(ball.yVelocity>0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        //stops paddle at  window edges
        if(paddle1.y<=0){
            paddle1.y=0;
        }
        if(paddle1.y>=(GAME_HEIGHT - paddle_height)){
            paddle1.y=GAME_HEIGHT- paddle_height;
        }
        if(paddle2.y<=0){
            paddle2.y=0;
        }
        if(paddle2.y>=(GAME_HEIGHT - paddle_height)){
            paddle2.y=GAME_HEIGHT - paddle_height;
        }

        //Give a player 1point and creates new paddles &ball
        if(ball.x<=0){
            score.player2++;
            newPaddles();
            newBall();
            System.out.println(score.player2);
        }
        if(ball.x>=GAME_WIDTH-ball_diameter){
            score.player1++;
            newPaddles();
            newBall();
            System.out.println(score.player1);
        }
    }
    public void run(){
        //game loop
        long lastTime =System.nanoTime();
        double amountOfTicks=60.0;
        double ns = 1000000000/amountOfTicks;
        double delta=0;
        while(true){
            long now=System.nanoTime();
            delta+= (now-lastTime)/ns;
            lastTime=now;
            if(delta>=1){
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }

    }
    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }
        public void keyReleased(KeyEvent e){
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);

        }
    }
}
