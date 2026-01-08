import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Models.Particle;
import Models.UserMouseEvent;


/**
 * A Swing-based window for rendering the particle simulation.
 * Handles drawing particles and processing mouse events for interaction.
 */
public class JFrameWindow {
    
    private JFrame window;
    private JPanel frame;
    public UserMouseEvent userMouseEvent = null;
    private boolean postParticleinitialization = false;

    /**
     * Creates a new simulation window with fixed size and title.
     */
    public JFrameWindow() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Particle-Based Fluid Simulator");
        window.setSize(600, 600);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
    }

    /**
     * Updates the window by rendering the given list of particles
     * and processing user interactions.
     *
     * @param particles list of particles to render
     */
    public void update(List<Particle> particles) {
        frame = new JPanel() {
        public void paintComponent(Graphics g) {
            g.setColor(Color.white);
            g.fillRect(0, 0, 600, 600);

            if(userMouseEvent != null && userMouseEvent.notExpiredEvent) {
                g.setColor(Color.gray);
                g.fillOval(userMouseEvent.x-5,userMouseEvent.y-5, 10, 10);
            }

            for (Particle p : particles) {
                //g.setColor(Color.RED);
                //g.drawLine((int)p.x + 2,(int)p.y + 2, (int)p.x + (int)p.xForce + 2, (int)p.y + (int)p.yForce + 2); //draw forces
                //g.setColor(Color.GREEN);
                //g.drawLine((int)p.x + 2,(int)p.y + 2, (int)p.x + (int)p.xVelocity * 5 + 2, (int)p.y + (int)p.yVelocity * 5 + 2); //draw forces
                g.setColor(Color.BLUE);
                g.fillOval((int)p.x,(int)p.y, 4, 4);
            }

            }
        };
        
        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                userMouseEvent = new UserMouseEvent(SwingUtilities.isLeftMouseButton(e), e.getY(), e.getX());
            }
        });
        frame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                userMouseEvent = new UserMouseEvent(SwingUtilities.isLeftMouseButton(e), e.getY(), e.getX());
            }
            public void mouseReleased(MouseEvent e) {
                userMouseEvent.notExpiredEvent = false;
            }
        });

        window.repaint();
        window.add(frame);
        if(!postParticleinitialization) {window.setVisible(postParticleinitialization = true);}

    }

    /**
     * Returns the most recent mouse event.
     *
     * @return the current UserMouseEvent, or null if none
     */
    public UserMouseEvent getMouseEvent() {
        return userMouseEvent;
    }

}
