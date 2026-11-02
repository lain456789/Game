package gameDesign;

/**
 * 碰撞检测线程类
 */
public class CollisionDetectionThread extends Thread {
    private MyGame game;
    private boolean running;

    /**
     * 构造函数
     * @param game 游戏主类实例
     */
    public CollisionDetectionThread(MyGame game) {
        this.game = game;
        this.running = true;
    }

    /**
     * 启动碰撞检测
     */
    public void startCollisionDetection() {
        running = true;
        start();
    }

    /**
     * 停止碰撞检测
     */
    public void shutdown() {
        running = false;
        interrupt();
    }

    /**
     * 线程运行方法，持续检测碰撞
     */
    @Override
    public void run() {
        while (running) {
            try {
                // 每20毫秒检测一次碰撞，减少CPU使用率
                Thread.sleep(20);
                game.handleCollision();
            } catch (InterruptedException e) {
                // 线程被中断时退出循环
                break;
            }
        }
    }
}