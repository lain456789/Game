package userLogin;

/**
 * 用户类，存储用户信息和历史记录
 */
public class User {
    private String username; // 用户名
    private String account; // 账号
    private String password; // 密码
    private String highestStage; // 历史最高记录，使用字符串支持"complete"

    /**
     * 构造函数
     * @param username 用户名
     * @param account 账号
     * @param password 密码
     */
    public User(String username, String account, String password) {
        this.username = username;
        this.account = account;
        this.password = password;
        this.highestStage = "1"; // 初始最高记录为1
    }

    /**
     * 构造函数
     * @param username 用户名
     * @param account 账号
     * @param password 密码
     * @param highestStage 最高记录
     */
    public User(String username, String account, String password, String highestStage) {
        this.username = username;
        this.account = account;
        this.password = password;
        this.highestStage = highestStage;
    }

    /**
     * 将用户信息转换为CSV格式
     * @return CSV格式的用户信息
     */
    public String toCSVString() {
        return String.format("%s,%s,%s,%s", username, account, password, highestStage);
    }

    /**
     * 从CSV格式创建用户对象
     * @param csvLine CSV格式的用户信息
     * @return 用户对象
     */
    public static User fromCSVString(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 4) {
            return null;
        }
        String username = parts[0];
        String account = parts[1];
        String password = parts[2];
        String highestStage = parts[3];
        return new User(username, account, password, highestStage);
    }

    /**
     * 获取最高记录的整数值，如果是"complete"则返回最大值
     * @return 最高记录的整数值
     */
    public int getHighestStageAsInt() {
        if (highestStage.equals("complete")) {
            return Integer.MAX_VALUE;
        }
        try {
            return Integer.parseInt(highestStage);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 检查当前记录是否超过用户的最高记录
     * @param currentStage 当前记录
     * @return 是否超过最高记录
     */
    public boolean isCurrentStageHigher(int currentStage) {
        if (highestStage.equals("complete")) {
            return false;
        }
        try {
            return currentStage > Integer.parseInt(highestStage);
        } catch (NumberFormatException e) {
            return true;
        }
    }

    // getter和setter方法
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHighestStage() {
        return highestStage;
    }

    public void setHighestStage(String highestStage) {
        this.highestStage = highestStage;
    }
}