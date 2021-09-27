package util.NetworkUtils.timer;

import Classes.Client;
import GlobalStuff.states.playerStates.SubStates;

public class ShootTimer implements Runnable {
    Client owner;

    public ShootTimer(Client owner) {
        this.owner = owner;
    }

    @Override
    public void run() {
        int shootCounter = 0;
        while (owner.getSubState() == SubStates.SHOOTING) {
            try {
                System.out.println("TODO: SHOOTING " + shootCounter++);
                Thread.sleep(owner.getShootingCooldown());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        owner.setShootTimerThread(null);
    }
}