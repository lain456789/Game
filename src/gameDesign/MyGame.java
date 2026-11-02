package gameDesign;

import userLogin.User;
import userLogin.UserManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * 游戏面板组件
 */
public class MyGame extends JPanel {
    private final Image plane;
    private final Image bgImage;
    private Plane player;
    private Shell[] shells;

    private long lastSpawnTime = 0; // 上次生成小球的时间
    private long lastStageIncreaseTime = 0; // 上次增加阶段的时间

    private int lives = GameConfig.INITIAL_LIVES;
    private boolean isGameOver = false;
    private boolean isGameRunning = false;
    private boolean isGamePaused = false;
    private int currentStage = 1;
    private long gameStartTime = 0; // 游戏开始时间，用于计算游戏时长

    private CollisionDetectionThread collisionDetectionThread; // 碰撞检测线程
    private String highestStage = "0"; // 最高阶段记录，使用字符串支持"complete"
    private User currentUser; // 当前登录的用户信息
    private final Random random;

    /**
     * 构造函数
     */
    public MyGame() {
        // 加载图片资源
        this.plane = GameUtil.getImage("gameDesign/Images/DAN.png");
        this.bgImage = GameUtil.getImage("gameDesign/Images/img.png");
        this.random = new Random();
        
        // 初始化游戏对象
        initGameObjects();
    }

    /**
     * 初始化游戏对象
     */
    private void initGameObjects() {
        // 初始化飞机
        player = new Plane(plane, 0, 0, GameConfig.PLANE_SPEED, GameConfig.PLANE_SIZE, GameConfig.PLANE_SIZE);
        player.live = true;
        
        // 初始化小球数组
        shells = new Shell[GameConfig.INITIAL_BALLS_COUNT];
        int ballSize = calculateBallSize();
        for (int i = 0; i < shells.length; i++) {
            shells[i] = new Shell(0, 0, GameConfig.BASE_BALL_SPEED, ballSize, ballSize);
            shells[i].live = false;
        }
    }
    
    /**
     * 重写paintComponent方法，绘制游戏界面
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        // 绘制背景
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, width, height, this);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, width, height);
        }

        // 绘制游戏信息
        Font font = new Font("宋体", Font.BOLD, 20);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("血量:" + lives, 30, 30);
        g.drawString("当前阶段:" + currentStage, 150, 30);
        g.drawString("最高阶段:" + highestStage, 300, 30);

        // 游戏进行中且未暂停时，更新游戏状态
        if (isGameRunning && !isGamePaused && !isGameOver) {
            updateGameState();
        }

        // 绘制游戏元素
        if (player.live) {
            player.draw(g);
        }

        for (Shell shell : shells) {
            if (shell.live) {
                shell.draw(g);
                // 检查小球是否超出边界
                if (shell.isOutOfBounds(height)) {
                    shell.live = false;
                }
            }
        }

        // 绘制游戏状态提示
        drawGameStatus(g, width, height);
    }
    
    /**
     * 更新游戏状态
     */
    private void updateGameState() {
        long currentTime = System.currentTimeMillis();
        long gameDuration = currentTime - gameStartTime;
        
        // 检查游戏时长是否达到60秒，通关游戏
        if (gameDuration >= 60000) {
            endGame(true);
            return;
        }

        // 阶段系统，每10秒增加一个阶段
        if (currentTime - lastStageIncreaseTime >= GameConfig.STAGE_INCREASE_INTERVAL) {
            currentStage++;
            lastStageIncreaseTime = currentTime;
            
            // 检查是否达到最高阶段
            if (currentStage > 6) {
                endGame(true);
                return;
            }
        }

        // 生成新的小球
        if (currentTime - lastSpawnTime >= GameConfig.SPAWN_INTERVAL) {
            spawnNewBalls(getBallsPerStage());
            lastSpawnTime = currentTime;
        }
    }
    
    /**
     * 根据当前阶段获取生成小球的数量
     */
    private int getBallsPerStage() {
        return 6 + (currentStage - 1) * 2;
    }
    
    /**
     * 绘制游戏状态提示
     */
    private void drawGameStatus(Graphics g, int width, int height) {
        if (isGameOver) {
            String gameOverText = currentStage > 6 ? "已通关" : "游戏结束";
            Color textColor = currentStage > 6 ? Color.GREEN : Color.RED;
            
            g.setColor(textColor);
            g.setFont(new Font("宋体", Font.BOLD, 50));
            int textWidth = g.getFontMetrics().stringWidth(gameOverText);
            g.drawString(gameOverText, (width - textWidth) / 2, height / 2);
            
            // 提示按空格键重新开始游戏
            String restartText = "按空格键重新开始游戏";
            g.setColor(Color.WHITE);
            g.setFont(new Font("宋体", Font.PLAIN, 20));
            textWidth = g.getFontMetrics().stringWidth(restartText);
            g.drawString(restartText, (width - textWidth) / 2, height / 2 + 50);
        } else if (isGamePaused) {
            String pauseText = "游戏暂停";
            g.setColor(Color.WHITE);
            g.setFont(new Font("宋体", Font.BOLD, 30));
            int textWidth = g.getFontMetrics().stringWidth(pauseText);
            g.drawString(pauseText, (width - textWidth) / 2, height / 2);
        } else if (!isGameRunning) {
            String startText = "按空格键开始游戏";
            g.setColor(Color.WHITE);
            g.setFont(new Font("宋体", Font.BOLD, 30));
            int textWidth = g.getFontMetrics().stringWidth(startText);
            g.drawString(startText, (width - textWidth) / 2, height / 2);
        }
    }

    /**
     * 生成新的小球
     * @param count 生成小球的数量
     */
    private void spawnNewBalls(int count) {
        int windowWidth = getWidth() > 0 ? getWidth() : GameConfig.INITIAL_WINDOW_WIDTH;
        int ballSize = calculateBallSize();
        double ballSpeed = GameConfig.BASE_BALL_SPEED;
        
        int spawnedCount = 0;
        int availableSlots = 0;
        
        // 计算可用的空位数
        for (Shell shell : shells) {
            if (!shell.live) {
                availableSlots++;
            }
        }
        
        // 如果可用空位不足，扩展数组
        if (availableSlots < count) {
            int newSize = shells.length + (count - availableSlots);
            resizeShellsArray(newSize);
        }
        
        // 生成新的小球
        for (Shell shell : shells) {
            if (spawnedCount >= count) break;
            
            if (!shell.live) {
                // 生成随机x坐标，确保小球不会超出窗口边界
                int x = random.nextInt(windowWidth - ballSize);
                int y = -ballSize; // 从窗口外顶部生成
                
                shell.x = x;
                shell.y = y;
                shell.speed = ballSpeed;
                shell.width = ballSize;
                shell.height = ballSize;
                shell.live = true;
                spawnedCount++;
            }
        }
    }

    /**
     * 计算小球大小（相对于窗口宽度的百分比）
     */
    private int calculateBallSize() {
        int windowWidth = getWidth() > 0 ? getWidth() : GameConfig.INITIAL_WINDOW_WIDTH;
        return Math.max(1, (int) (windowWidth * (GameConfig.BALL_SIZE_PERCENTAGE / 100.0)));
    }
    
    /**
     * 根据窗口大小调整实体大小和速度
     * @param newWidth 新窗口宽度
     * @param newHeight 新窗口高度
     */
    private void adjustEntitySizeAndSpeed(int newWidth, int newHeight) {
        // 确保窗口大小有效
        newWidth = newWidth > 0 ? newWidth : GameConfig.INITIAL_WINDOW_WIDTH;
        newHeight = newHeight > 0 ? newHeight : GameConfig.INITIAL_WINDOW_HEIGHT;
        
        // 计算缩放比例，基于原始窗口大小
        double widthScale = (double) newWidth / GameConfig.INITIAL_WINDOW_WIDTH;
        
        // 调整飞机大小
        int newPlaneSize = Math.max(1, (int) (GameConfig.PLANE_SIZE * widthScale));
        player.width = newPlaneSize;
        player.height = newPlaneSize;
        
        // 更新飞机的缓冲图像用于像素级碰撞检测
        if (plane != null && newPlaneSize > 0) {
            player.bufferedImg = new java.awt.image.BufferedImage(newPlaneSize, newPlaneSize, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g2d = player.bufferedImg.createGraphics();
            g2d.drawImage(plane, 0, 0, newPlaneSize, newPlaneSize, null);
            g2d.dispose();
        }
        
        // 保持飞机在窗口底部正中间
        player.x = (newWidth - newPlaneSize) / 2;
        player.y = newHeight - newPlaneSize;
        
        // 计算小球大小（相对于窗口宽度的百分比）
        int ballSize = calculateBallSize();
        
        // 调整所有小球的大小和速度
        for (Shell shell : shells) {
            if (shell != null && shell.live) {
                // 调整小球大小
                shell.width = ballSize;
                shell.height = ballSize;
                
                // 更新小球的缓冲图像用于像素级碰撞检测
                shell.bufferedImg = new java.awt.image.BufferedImage(ballSize, ballSize, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                java.awt.Graphics2D g2d = shell.bufferedImg.createGraphics();
                g2d.setColor(java.awt.Color.YELLOW);
                g2d.fillOval(0, 0, ballSize, ballSize);
                g2d.dispose();
                
                // 确保小球在窗口范围内
                if (shell.x + ballSize > newWidth) {
                    shell.x = newWidth - ballSize;
                }
            }
        }
    }
    
    /**
     * 调整小球数组大小
     * @param newSize 新的数组大小
     */
    private void resizeShellsArray(int newSize) {
        Shell[] newShells = new Shell[newSize];
        int ballSize = calculateBallSize();
        double ballSpeed = GameConfig.BASE_BALL_SPEED;
        
        // 复制现有小球到新数组
        int copyCount = Math.min(shells.length, newSize);
        System.arraycopy(shells, 0, newShells, 0, copyCount);
        
        // 为新数组中的剩余位置创建新的小球
        for (int i = copyCount; i < newSize; i++) {
            newShells[i] = new Shell(0, 0, ballSpeed, ballSize, ballSize);
            newShells[i].live = false;
        }
        
        // 更新小球数组
        shells = newShells;
    }

    /**
     * 处理碰撞事件
     */
    public void handleCollision() {
        if (!isGameRunning || isGamePaused || isGameOver) {
            return;
        }
        
        for (Shell shell : shells) {
            if (shell.live && player.live) {
                // 使用像素级碰撞检测
                if (player.isCollidedWith(shell)) {
                    // 飞机与小球碰撞
                    shell.live = false;
                    lives--;
                    
                    // 如果血量为0，游戏结束
                    if (lives <= 0) {
                        player.live = false;
                        endGame(false);
                    }
                }
            }
        }
    }
    
    /**
     * 结束游戏
     * @param isWin 是否通关
     */
    private void endGame(boolean isWin) {
        isGameOver = true;
        isGameRunning = false;
        
        // 保存最高阶段记录
        saveHighestStage();
        
        // 停止碰撞检测线程
        if (collisionDetectionThread != null) {
            collisionDetectionThread.shutdown();
        }
    }

    /**
     * 保存最高阶段记录
     */
    private void saveHighestStage() {
        String newStage;
        long gameDuration = System.currentTimeMillis() - gameStartTime;
        
        // 检查是否游戏时长达到60秒，或者当前阶段超过6，都标记为"complete"
        if (currentStage > 6 || gameDuration >= 60000) {
            newStage = "complete";
        } else {
            newStage = String.valueOf(currentStage);
        }
        
        // 比较当前记录与新记录
        boolean shouldSave = false;
        if (highestStage.equals("0")) {
            shouldSave = true;
        } else if (highestStage.equals("complete")) {
            shouldSave = false;
        } else if (newStage.equals("complete")) {
            shouldSave = true;
        } else {
            int highestStageNum = Integer.parseInt(highestStage);
            int currentStageNum = Integer.parseInt(newStage);
            shouldSave = currentStageNum > highestStageNum;
        }
        
        if (shouldSave) {
            highestStage = newStage;
            
            // 保存到全局文件
            try (java.io.PrintWriter writer = new java.io.PrintWriter("highest_stage.txt")) {
                writer.println(highestStage);
            } catch (java.io.IOException e) {
                System.out.println("保存最高阶段记录失败: " + e.getMessage());
            }
            
            // 更新当前用户的最高记录到CSV文件
            if (currentUser != null) {
                try {
                    // 创建UserManager实例来更新用户记录
                    UserManager userManager = new UserManager("users_Information/user.csv");
                    
                    if (newStage.equals("complete")) {
                        // 更新为"complete"
                        userManager.updateHighestStageToComplete(currentUser.getAccount());
                    } else {
                        // 更新为数字阶段
                        userManager.updateHighestStage(currentUser.getAccount(), Integer.parseInt(newStage));
                    }
                    
                    // 更新当前用户对象的最高记录
                    currentUser.setHighestStage(newStage);
                } catch (Exception e) {
                    System.out.println("更新用户最高记录失败: " + e.getMessage());
                }
            }
        }
    }

    /**
     * 加载最高阶段记录
     */
    private void loadHighestStage() {
        java.io.File file = new java.io.File("highest_stage.txt");
        if (file.exists()) {
            try (java.util.Scanner scanner = new java.util.Scanner(file)) {
                if (scanner.hasNextLine()) {
                    highestStage = scanner.nextLine().trim();
                }
            } catch (java.io.IOException e) {
                System.out.println("加载最高阶段记录失败: " + e.getMessage());
            }
        }
    }

    /**
     * 重置游戏
     */
    private void resetGame() {
        lives = GameConfig.INITIAL_LIVES;
        isGameOver = false;
        isGameRunning = false;
        isGamePaused = false;
        currentStage = 1;
        lastSpawnTime = 0;
        lastStageIncreaseTime = 0;
        
        // 重新初始化游戏对象
        initGameObjects();
        
        // 设置飞机初始位置
        int windowWidth = getWidth() > 0 ? getWidth() : GameConfig.INITIAL_WINDOW_WIDTH;
        int windowHeight = getHeight() > 0 ? getHeight() : GameConfig.INITIAL_WINDOW_HEIGHT;
        
        player.x = (windowWidth - player.width) / 2;
        player.y = windowHeight - player.height;
        player.setWindowSize(windowWidth, windowHeight);
        
        // 调整实体大小和速度
        adjustEntitySizeAndSpeed(windowWidth, windowHeight);
        
        // 重新开始碰撞检测线程
        if (collisionDetectionThread != null) {
            collisionDetectionThread.shutdown();
        }
        collisionDetectionThread = new CollisionDetectionThread(this);
        collisionDetectionThread.startCollisionDetection();
    }



    /**
     * 初始化游戏
     */
    public void initGame() {
        // 加载最高阶段记录
        loadHighestStage();
        int windowWidth = getWidth() > 0 ? getWidth() : GameConfig.INITIAL_WINDOW_WIDTH;
        int windowHeight = getHeight() > 0 ? getHeight() : GameConfig.INITIAL_WINDOW_HEIGHT;
        
        // 初始化游戏对象
        initGameObjects();
        
        // 设置飞机初始位置和窗口大小
        player.x = (windowWidth - player.width) / 2;
        player.y = windowHeight - player.height;
        player.setWindowSize(windowWidth, windowHeight);
        
        // 初始调整实体大小和速度
        adjustEntitySizeAndSpeed(windowWidth, windowHeight);
        
        // 初始化碰撞检测线程
        collisionDetectionThread = new CollisionDetectionThread(this);
        collisionDetectionThread.startCollisionDetection();

        // 添加键盘监听
        this.addKeyListener(new KeyMonitor());
        this.setFocusable(true);
        
        // 添加组件大小变化监听器
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                // 更新窗口大小
                int newWidth = getWidth();
                int newHeight = getHeight();
                
                // 更新飞机的窗口大小限制
                player.setWindowSize(newWidth, newHeight);
                
                // 根据窗口大小调整实体大小和速度
                adjustEntitySizeAndSpeed(newWidth, newHeight);
                
                // 如果游戏正在运行，重新生成所有小球的位置
                if (isGameRunning && !isGameOver) {
                    // 重新分布当前存活的小球
                    int liveBallsCount = 0;
                    for (Shell shell : shells) {
                        if (shell.live) {
                            liveBallsCount++;
                        }
                    }
                    
                    // 先将所有小球标记为不存活
                    for (Shell shell : shells) {
                        shell.live = false;
                    }
                    
                    // 重新生成小球
                    spawnNewBalls(liveBallsCount);
                }
            }
        });

        // 创建并启动绘画线程
        new PaintThread().start();
    }

    /**
     * 绘画线程，负责定期重绘游戏界面
     */
    class PaintThread extends Thread {
        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 键盘监听类，处理键盘输入
     */
    class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            
            // 游戏控制
            if (isGameRunning && !isGameOver) {
                switch (keyCode) {
                    case KeyEvent.VK_A: // A键控制左移
                        player.left = true;
                        break;
                    case KeyEvent.VK_W: // W键控制上移
                        player.up = true;
                        break;
                    case KeyEvent.VK_D: // D键控制右移
                        player.right = true;
                        break;
                    case KeyEvent.VK_S: // S键控制下移
                        player.down = true;
                        break;
                }
            }
            
            // 游戏开始/暂停
            if (keyCode == KeyEvent.VK_SPACE) {
                if (isGameOver) {
                    // 游戏结束时按空格键重新开始
                    resetGame();
                } else if (isGameRunning) {
                    // 游戏进行中按空格键暂停/继续
                    isGamePaused = !isGamePaused;
                } else {
                    // 游戏未开始时按空格键开始
                    isGameRunning = true;
                    long startTime = System.currentTimeMillis();
                    gameStartTime = startTime;
                    lastSpawnTime = startTime;
                    lastStageIncreaseTime = startTime;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            
            // 游戏控制
            switch (keyCode) {
                case KeyEvent.VK_A: // A键释放
                    player.left = false;
                    break;
                case KeyEvent.VK_W: // W键释放
                    player.up = false;
                    break;
                case KeyEvent.VK_D: // D键释放
                    player.right = false;
                    break;
                case KeyEvent.VK_S: // S键释放
                    player.down = false;
                    break;
            }
        }
    }

    /**双缓冲技术*/
    private Image offScreenImage = null; // 离屏缓冲画布

    /**
     * 重写update方法，实现双缓冲机制
     * 减少界面闪烁
     *
     * @param g 图形上下文
     */
    @Override
    public void update(Graphics g) {
        // 获取当前面板的宽度和高度
        int width = this.getWidth();
        int height = this.getHeight();

        // 如果离屏缓冲画布不存在或大小不匹配，创建一个新的
        if (offScreenImage == null || offScreenImage.getWidth(this) != width || offScreenImage.getHeight(this) != height) {
            offScreenImage = this.createImage(width, height); // 创建与窗口大小相同的离屏缓冲画布
        }

        // 获取离屏缓冲画布的图形上下文
        Graphics gOff = offScreenImage.getGraphics();

        // 在离屏缓冲区域上绘制游戏画面
        paintComponent(gOff);

        // 释放图形上下文资源
        gOff.dispose();

        // 将离屏缓冲区域的内容一次性绘制到屏幕上
        g.drawImage(offScreenImage, 0, 0, this);
    }

    /**
     * 设置当前登录的用户信息
     * @param user 当前登录的用户
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null) {
            // 使用用户的历史最高记录
            this.highestStage = user.getHighestStage();
        }
    }

    /**
     * 获取当前登录的用户信息
     * @return 当前登录的用户
     */
    public User getCurrentUser() {
        return currentUser;
    }
}