package gameDesign;

/**
 * 游戏参数配置类，集中管理所有游戏参数
 */
public class GameConfig {
    // 小球参数
    public static final int INITIAL_BALLS_COUNT = 6; // 初始小球数量（一阶段）
    public static final int BALLS_INCREASE_AMOUNT = 6; // 每次递增的小球数量
    public static final int BASE_BALL_SPEED = 6; // 基础小球移动速度（用于计算相对速度）
    public static final int BALL_SIZE_PERCENTAGE = 4; // 小球大小相对于窗口宽度的百分比（4%）
    
    // 生成参数
    public static final int SPAWN_INTERVAL = 2000; // 小球生成频率（毫秒）
    public static final int STAGE_INCREASE_INTERVAL = 10000; // 阶段递增间隔（毫秒）
    
    // 游戏参数
    public static final int GAME_DURATION = 300000; // 游戏持续时间（毫秒）
    public static final int INITIAL_LIVES = 3; // 初始生命值
    
    // 窗口参数
    public static final int INITIAL_WINDOW_WIDTH = 500; // 初始窗口宽度
    public static final int INITIAL_WINDOW_HEIGHT = 500; // 初始窗口高度
    
    // 飞机参数
    public static final int PLANE_SIZE = 40; // 飞机大小
    public static final int PLANE_SPEED = 10; // 飞机移动速度
}