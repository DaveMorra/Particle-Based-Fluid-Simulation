import java.util.concurrent.TimeUnit;
import Models.UserMouseEvent;

/**
 * Runs a basic fluid particle simulation in a JFrame window.
 */
public class FluidSimulator {

    /**
     * Starts the simulation loop, updating the environment,
     * rendering particles, and processing mouse input.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {

        //Initialise particle simulation
        ParticleEnv env = new ParticleEnv();
        JFrameWindow window = new JFrameWindow();
        UserMouseEvent mouseEvent = null;
        int frameRate = 120;

        //Simulate particles until jframe window is closed
        try {
            while(true) {
                env.simulate(mouseEvent);
                window.update(env.getParticles());
                mouseEvent = window.getMouseEvent();
                TimeUnit.MILLISECONDS.sleep(1000/frameRate);
            }
        } catch (Exception e) {
            System.out.println("Error: Unable to update frame");
        }
    }
}
