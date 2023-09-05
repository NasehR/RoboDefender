package edu.curtin.saed.assignment1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WallBuilder {
    private final int MAX_WALLS = 10;
    private final long BUILD_DELAY = 2000; // 2000 milliseconds
    private final JFXArena arena;
    private final BlockingQueue<Wall> buildQueue;
    private Thread addingWallThread = null;
    private Thread displayingWallThread = null;

    public WallBuilder(JFXArena arena) {
        this.arena = arena;
        buildQueue = new ArrayBlockingQueue<>(MAX_WALLS);
    }
}
