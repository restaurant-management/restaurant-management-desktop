package util;

import animatefx.animation.*;
import javafx.scene.Node;

import java.util.Random;

public class RandomAnimation {
    public static AnimationFX random(Node node) {
        AnimationFX result;
        Random r = new Random();
        int random = r.nextInt((9) + 1);

        switch (random) {
            case 0:
                result = new Flash(node);
                break;
            case 1:
                result = new Bounce(node);
                break;
            case 2:
                result = new Pulse(node);
                break;
            case 3:
                result = new RubberBand(node);
                break;
            case 4:
                result = new Shake(node);
                break;
            case 5:
                result = new Swing(node);
                break;
            case 6:
                result = new Tada(node);
                break;
            case 7:
                result = new Wobble(node);
                break;
            case 8:
                result = new Jello(node);
                break;
            case 9:
                result = new BounceIn(node);
                break;
            default:
                result = new Flip(node);
        }
        result.play();
        return result;
    }
}
