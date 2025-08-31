package Models;

/**
 * Represents a single particle in the simulation, 
 * storing its position, velocity, force, and grid indices.
 */
public class Particle{
    public double x;
    public double y;
    public double xForce;
    public double yForce;
    public double xVelocity;
    public double yVelocity;
    public int localIndexY;
    public int localIndexX;

    /**
     * Creates a particle at the given (y, x) position with zero initial velocity and force.
     *
     * @param y initial Y-coordinate
     * @param x initial X-coordinate
     */
    public Particle(double y, double x) {
        this.x = x;
        this.y = y;
        this.xForce = 0;
        this.yForce = 0;
        this.xVelocity = 0;
        this.yVelocity = 0;
    }
}
