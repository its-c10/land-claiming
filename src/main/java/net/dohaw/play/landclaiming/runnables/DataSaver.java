package net.dohaw.play.landclaiming.runnables;

import net.dohaw.play.landclaiming.LandClaiming;
import org.bukkit.scheduler.BukkitRunnable;

public class DataSaver extends BukkitRunnable {

    private LandClaiming plugin;

    public DataSaver(LandClaiming plugin){
        this.plugin = plugin;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        plugin.getPlayerDataManager().saveData();
        plugin.getRegionDataManager().saveData();
        plugin.getLogger().info("Saving all player data...");
        plugin.getLogger().info("Saving all region data...");
    }
}
