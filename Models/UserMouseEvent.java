package Models;

/**
 * Represents a mouse interaction event that can attract or repel particles
 * within the simulation at a given position.
 */
public class UserMouseEvent{
    public boolean attractParticles;
    public boolean notExpiredEvent;
    public int x;
    public int y;

    /**
     * Creates a new mouse event at the given position.
     *
     * @param attractParticles true to attract particles, false to repel
     * @param y Y-coordinate of the event
     * @param x X-coordinate of the event
     */
    public UserMouseEvent(boolean attractParticles, int y, int x) {
        this.attractParticles = attractParticles;
        this.x = x;
        this.y = y;
        this.notExpiredEvent = true;
    }
}
