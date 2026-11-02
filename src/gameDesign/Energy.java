package gameDesign;

import java.awt.*;

/**
 * 简化的能量条/血条类 - 只显示文字血量
 */
public class Energy {
    private int x, y; // 坐标
    private int currentEnergy; // 当前能量值
    private int maxEnergy; // 最大能量值

    /**
     * 构造函数
     * @param x x坐标
     * @param y y坐标
     * @param maxEnergy 最大能量值
     */
    public Energy(int x, int y, int maxEnergy) {
        this.x = x;
        this.y = y;
        this.maxEnergy = maxEnergy;
        this.currentEnergy = maxEnergy;
    }

    /**
     * 绘制文字形式的血量
     * @param g 图形上下文
     */
    public void draw(Graphics g) {
        // 保存当前颜色和字体
        Color c = g.getColor();
        Font f = g.getFont();
        
        // 设置字体和颜色
        g.setFont(new Font("宋体", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        
        // 绘制文字形式的血量
        String bloodText = "血量: " + currentEnergy + "/" + maxEnergy;
        g.drawString(bloodText, x, y);
        
        // 恢复颜色和字体
        g.setColor(c);
        g.setFont(f);
    }

    /**
     * 设置当前能量值
     * @param energy 能量值
     */
    public void setCurrentEnergy(int energy) {
        if (energy < 0) {
            energy = 0;
        } else if (energy > maxEnergy) {
            energy = maxEnergy;
        }
        this.currentEnergy = energy;
    }

    /**
     * 获取当前能量值
     * @return 当前能量值
     */
    public int getCurrentEnergy() {
        return currentEnergy;
    }

    /**
     * 减少能量值
     * @param amount 减少的数量
     */
    public void decreaseEnergy(int amount) {
        setCurrentEnergy(currentEnergy - amount);
    }

    /**
     * 增加能量值
     * @param amount 增加的数量
     */
    public void increaseEnergy(int amount) {
        setCurrentEnergy(currentEnergy + amount);
    }

    /**
     * 是否能量耗尽
     * @return 是否能量耗尽
     */
    public boolean isEmpty() {
        return currentEnergy <= 0;
    }
}