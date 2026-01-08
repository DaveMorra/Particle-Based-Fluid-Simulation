import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import Models.Particle;
import Models.UserMouseEvent;

/**
 * The {@code ParticleEnv} class manages a collection of particles within a 
 * bounded 2D environment. It handles simulation steps including gravity, 
 * collisions, wall interactions, and user input.
 */
public class ParticleEnv {

    private List<Particle> globalParticles;
    private List<Particle>[][] localParticles;
    private double gravityForce = 10;
    private double maxX = 580;
    private double maxY = 555;

    /**
     * Initializes the particle environment with a set of randomly placed particles.
     * Particles are distributed into spatial grid cells for efficient neighbor lookups.
     */
    public ParticleEnv(){
        globalParticles = new ArrayList<Particle>();
        ArrayList<Particle>[][] localParticles = new ArrayList[(int)Math.ceil(maxY/40)+1][(int)Math.ceil(maxX/40)+1];
        this.localParticles = localParticles;

        for(int i = 0; i < 1500; i++) {
            globalParticles.add(new Particle((int)(Math.random() * (maxY-350)+350), (int)(Math.random() * maxX)));
        }

        for(int y = 0; y < Math.ceil(maxY/40)+1; y++) {
            for(int x = 0; x < Math.ceil(maxX/40)+1; x++) {
                localParticles[y][x] = new ArrayList<Particle>();
            }
        }

        for (Particle p : globalParticles) {
            localParticles[(int)Math.ceil(p.y/40)][(int)Math.ceil(p.x/40)].add(p);
            p.localIndexY = (int)Math.ceil(p.y/40);
            p.localIndexX = (int)Math.ceil(p.x/40);
        }

    }

    /**
     * Advances the simulation by one step, applying gravity, 
     * particle interactions, wall collisions, and optional mouse forces.
     *
     * @param mouseEvent the latest mouse event (attract or repel particles), may be null
     */
    public void simulate(UserMouseEvent mouseEvent){

        updateLocalParticles();

        //change all force vectors according to other partcles and gravity
        for (Particle p : globalParticles) {

            //calculate forces
            calculateForceOfNearbyParticles(p);
            calculateForceOfMouseEvent(p, mouseEvent);
            calculateForceOfNearbyWalls(p);

            //assign force to particles velocity
            p.xVelocity += p.xForce/120;
            p.yVelocity += p.yForce/120;

        }


        //apply velocity vectors to postion
        for (Particle p : globalParticles) {

            p.x += p.xVelocity;
            p.y += p.yVelocity;

            p.xVelocity *= .99;
            p.yVelocity *= .99;
        }

        //ensure postion is legal
        for (Particle p : globalParticles) {

            //ensure particle is inside box (JFrame screen)
            p.x = min(p.x, maxX);
            p.y = min(p.y, maxY);
            p.x = max(p.x, 0);
            p.y = max(p.y, 0);

            //if particle is hitting a wall, make the particle bounce with reduced velocity
            if(p.x == 0 && p.xVelocity < 0) {p.xVelocity = -p.xVelocity/2;}     //left wall
            if(p.x == maxX && p.xVelocity > 0) {p.xVelocity = -p.xVelocity/2;}  //right wall
            if(p.y == 0 && p.yVelocity < 0) {p.yVelocity = -p.yVelocity/2;}     //top wall
            if(p.y == maxY && p.yVelocity > 0) {p.yVelocity = -p.yVelocity/2;}  //bottom wall

        }
    }

    /**
     * Applies mouse interaction forces to a particle if a valid event is present.
     *
     * @param p the particle affected
     * @param mouseEvent user input event with force properties
     */
    private void calculateForceOfMouseEvent(Particle p, UserMouseEvent mouseEvent) {
        
        //if mouse event does not exist or is expired, leave the function
        if(mouseEvent == null || !mouseEvent.notExpiredEvent) {return;}

        double force;
        double xForce = 0;
        double yForce = 0;
        double dis = Math.sqrt(((p.x-mouseEvent.x)*(p.x-mouseEvent.x)) + ((p.y-mouseEvent.y)*(p.y-mouseEvent.y)));

        if(dis < 100) {force = ( dis * -1 ) + 40;}
        else {force = 0;}

        if(!mouseEvent.attractParticles) {force *= -1;}

        xForce = force * Math.cos(Math.atan(divide((p.y-mouseEvent.y),(p.x-mouseEvent.x))));
        yForce = force * Math.cos(Math.atan(divide((p.x-mouseEvent.x), (p.y-mouseEvent.y))));

        if(p.x < mouseEvent.x) {xForce *= -1;}
        if(p.y < mouseEvent.y) {yForce *= -1;}

        p.xForce += xForce;
        p.yForce += yForce;     

    }

    /**
     * Calculates forces on a particle due to nearby particles and gravity.
     *
     * @param p the particle being updated
     */
    private void calculateForceOfNearbyParticles(Particle p) {
        
        //assign default force values
        p.xForce = 0;
        p.yForce = gravityForce;

        //avoid near by particles
        double dis;
        double force, xForce, yForce;
        for(int i = 0; i < 9; i++) {
            for (Particle n : getNearbyParticles(i, p)) {
                dis = Math.sqrt(((p.x-n.x)*(p.x-n.x)) + ((p.y-n.y)*(p.y-n.y)));
                if(p != n) {

                    if(dis < 35) {force = ( dis * -1 ) + 30;}
                    else if(dis < 40) {force = (dis * 1) - 40;}
                    else {force = 0;}

                    xForce = force * Math.cos(Math.atan(divide((p.y-n.y),(p.x-n.x))));
                    yForce = force * Math.cos(Math.atan(divide((p.x-n.x), (p.y-n.y))));

                    if(p.x < n.x) {xForce *= -1;}
                    if(p.y < n.y) {yForce *= -1;}

                    p.xForce += xForce;
                    p.yForce += yForce;

                }
            }
        }
    }

    /**
     * Applies repelling forces from environment boundaries.
     *
     * @param p the particle being updated
     */
    private void calculateForceOfNearbyWalls(Particle p) {
        //repel particles from wall
        if(p.x < 5) {p.xForce = 5;}                //left wall
        if(p.x > maxX - 5) {p.xForce = -5;}        //right wall
        if(p.y < 5 ) {p.yForce = 5;}               //top wall
        if(p.y > maxY - 5) {p.yForce = -20;};      //bottom wall
    }

    /**
     * Returns particles from a neighboring cell around a given particle.
     *
     * @param i index of neighbor cell (0–8)
     * @param p reference particle
     * @return list of nearby particles
     */
    private List<Particle> getNearbyParticles(int i, Particle p) {
        if(i == 0 && p.localIndexY > 0 && p.localIndexX > 0) {return localParticles[p.localIndexY-1][p.localIndexX-1];}                                           //top left neighbour
        else if(i == 1 && p.localIndexY > 0) {return localParticles[p.localIndexY-1][p.localIndexX];}                                                             //top neighbour
        else if(i == 2 && p.localIndexY > 0 && p.localIndexX < Math.ceil(maxX/40)) {return localParticles[p.localIndexY-1][p.localIndexX+1];}                     //top right
        else if(i == 3 && p.localIndexX > 0) {return localParticles[p.localIndexY][p.localIndexX-1];}                                                             //left neighbour
        else if(i == 4) {return localParticles[p.localIndexY][p.localIndexX];}                                                                                    //own square
        else if(i == 5 && p.localIndexX < Math.ceil(maxX/40)) {return localParticles[p.localIndexY][p.localIndexX+1];}                                            //right neighbour
        else if(i == 6 && p.localIndexX > 0 && p.localIndexY < Math.ceil(maxY/40)) {return localParticles[p.localIndexY+1][p.localIndexX-1];}                     //bottom left neighbour
        else if(i == 7 && p.localIndexY < Math.ceil(maxY/40)) {return localParticles[p.localIndexY+1][p.localIndexX];}                                            //bottom neighbour
        else if(i == 8 && p.localIndexX > Math.ceil(maxX/40) && p.localIndexY < Math.ceil(maxY/40)) {return localParticles[p.localIndexY+1][p.localIndexX+1];}    //bottom right neighbour
        return new ArrayList<Particle>();                                                                                                                         //return empty list
    }

    /**
     * Updates the spatial grid to reflect each particle’s current position.
     */
    private void updateLocalParticles() {
        for (Particle p : globalParticles) {
            if(p.y != (int)Math.ceil(p.y/40) || p.x != (int)Math.ceil(p.x/40)) {
                localParticles[p.localIndexY][p.localIndexX].remove(p);
                p.localIndexY = (int)Math.ceil(p.y/40);
                p.localIndexX = (int)Math.ceil(p.x/40);
                localParticles[p.localIndexY][p.localIndexX].add(p);
            }
        }
    }

    /**
     * Safe division method to avoid divide-by-zero errors.
     *
     * @param n1 numerator
     * @param n2 denominator
     * @return quotient, or {@code Double.POSITIVE_INFINITY} if denominator is zero
     */
    private double divide(double n1, double n2) {
        if(n2 == 0) return Double.POSITIVE_INFINITY;
        return n1/n2;
    }

    /**
     * Provides the current list of all particles in the environment.
     *
     * @return the global particle list
     */
    public List<Particle> getParticles() {
        return globalParticles;
    }

    /**
     * Returns the smaller of two values.
     */
    private double min(double n1, double n2) {
        if(n1 > n2) return n2;
        return n1;
    }

    /**
     * Returns the larger of two values.
     */
    private double max(double n1, double n2) {
        if(n1 > n2) return n1;
        return n2;
    }

}
