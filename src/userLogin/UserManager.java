package userLogin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理器类，负责用户信息的存储和管理
 */
public class UserManager {
    private List<User> users; // 用户列表
    private String csvFilePath; // CSV文件路径

    /**
     * 构造函数
     * @param csvFilePath CSV文件路径
     */
    public UserManager(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        this.users = new ArrayList<>();
        loadUsers(); // 加载用户信息
    }

    /**
     * 从CSV文件加载用户信息
     */
    public void loadUsers() {
        users.clear(); // 清空现有用户列表
        
        File file = new File(csvFilePath);
        if (!file.exists()) {
            // 如果文件不存在，创建一个空文件
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    User user = User.fromCSVString(line);
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存用户信息到CSV文件
     */
    public void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            for (User user : users) {
                writer.write(user.toCSVString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加新用户
     * @param user 用户对象
     * @return 是否添加成功
     */
    public boolean addUser(User user) {
        // 检查账号是否已存在
        if (getUserByAccount(user.getAccount()) != null) {
            return false;
        }
        
        users.add(user);
        saveUsers();
        return true;
    }

    /**
     * 通过账号获取用户
     * @param account 账号
     * @return 用户对象
     */
    public User getUserByAccount(String account) {
        for (User user : users) {
            if (user.getAccount().equals(account)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 用户登录
     * @param account 账号
     * @param password 密码
     * @return 用户对象
     */
    public User login(String account, String password) {
        User user = getUserByAccount(account);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /**
     * 更新用户的历史最高记录为新的阶段
     * @param account 账号
     * @param highestStage 最高记录
     * @return 是否更新成功
     */
    public boolean updateHighestStage(String account, int highestStage) {
        User user = getUserByAccount(account);
        if (user != null) {
            if (user.isCurrentStageHigher(highestStage)) {
                user.setHighestStage(String.valueOf(highestStage));
                saveUsers();
                return true;
            }
        }
        return false;
    }
    
    /**
     * 将用户的历史最高记录更新为"complete"
     * @param account 账号
     * @return 是否更新成功
     */
    public boolean updateHighestStageToComplete(String account) {
        User user = getUserByAccount(account);
        if (user != null) {
            if (!user.getHighestStage().equals("complete")) {
                user.setHighestStage("complete");
                saveUsers();
                return true;
            }
        }
        return false;
    }

    /**
     * 检查用户的历史最高记录是否为"complete"
     * @param account 账号
     * @return 是否为"complete"
     */
    public boolean isHighestStageComplete(String account) {
        User user = getUserByAccount(account);
        if (user != null) {
            return user.getHighestStage().equals("complete");
        }
        return false;
    }

    /**
     * 获取所有用户
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}