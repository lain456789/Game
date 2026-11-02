package userLogin;

import gameDesign.MyGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * 用户管理系统，包含登录和注册功能
 */
public class UserManagementSystem {
    // 主框架
    private JFrame mainFrame;
    // 主面板，使用CardLayout进行界面切换
    private JPanel mainPanel;
    // 卡片布局管理器
    private CardLayout cardLayout;
    // 用户管理器
    private UserManager userManager;
    // 游戏面板
    private MyGame gamePanel;

    // 登录界面组件
    private JLabel accountLabel;
    private JTextField accountField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerLinkButton;
    private JPanel loginPanel;

    // 注册界面组件
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel registerAccountLabel;
    private JTextField registerAccountField;
    private JLabel registerPasswordLabel;
    private JPasswordField registerPasswordField;
    private JButton registerButton;
    private JButton backButton;
    private JPanel registerPanel;

    /**
     * 构造函数，初始化用户管理系统
     */
    public UserManagementSystem() {
        // 创建用户管理器
        userManager = new UserManager("users_Information/user.csv");
        
        // 确保用户信息目录存在
        File dir = new File("users_Information");
        if (!dir.exists()) {
            dir.mkdir();
        }
        
        // 初始化界面
        initUI();
    }

    /**
     * 初始化用户界面
     */
    private void initUI() {
        // 创建主框架
        mainFrame = new JFrame("游戏");
        mainFrame.setSize(500, 500);
        mainFrame.setLocationRelativeTo(null); // 居中显示
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建主面板和卡片布局
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        // 创建并添加登录界面
        loginPanel = createLoginPanel();
        mainPanel.add(loginPanel, "login");

        // 创建并添加注册界面
        registerPanel = createRegisterPanel();
        mainPanel.add(registerPanel, "register");

        // 将主面板添加到主框架
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    /**
     * 创建登录界面
     * @return 登录界面面板
     */
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("登录");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // 账号标签和输入框
        accountLabel = new JLabel("账号:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(accountLabel, gbc);

        accountField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(accountField, gbc);

        // 密码标签和输入框
        passwordLabel = new JLabel("密码:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        // 登录按钮
        loginButton = new JButton("登录");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String account = accountField.getText();
                String password = new String(passwordField.getPassword());
                handleLogin(account, password);
            }
        });
        panel.add(loginButton, gbc);

        // 注册链接按钮
        registerLinkButton = new JButton("没有账号？点击注册");
        registerLinkButton.setContentAreaFilled(false);
        registerLinkButton.setBorderPainted(false);
        registerLinkButton.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        registerLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 切换到注册界面
                cardLayout.show(mainPanel, "register");
            }
        });
        panel.add(registerLinkButton, gbc);

        return panel;
    }

    /**
     * 创建注册界面
     * @return 注册界面面板
     */
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("注册");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // 用户名标签和输入框
        usernameLabel = new JLabel("用户名:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        // 账号标签和输入框
        registerAccountLabel = new JLabel("账号:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(registerAccountLabel, gbc);

        registerAccountField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(registerAccountField, gbc);

        // 密码标签和输入框
        registerPasswordLabel = new JLabel("密码:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(registerPasswordLabel, gbc);

        registerPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(registerPasswordField, gbc);

        // 注册按钮
        registerButton = new JButton("注册");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String account = registerAccountField.getText();
                String password = new String(registerPasswordField.getPassword());
                handleRegister(username, account, password);
            }
        });
        panel.add(registerButton, gbc);

        // 返回登录按钮
        backButton = new JButton("返回登录");
        gbc.gridy = 5;
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 切换到登录界面
                cardLayout.show(mainPanel, "login");
            }
        });
        panel.add(backButton, gbc);

        return panel;
    }

    /**
     * 处理登录逻辑
     * @param account 账号
     * @param password 密码
     */
    private void handleLogin(String account, String password) {
        // 验证输入
        if (!InputValidator.isNotEmpty(account) || !InputValidator.isNotEmpty(password)) {
            showErrorMessage("登录失败", "账号和密码不能为空");
            return;
        }
        
        // 尝试登录
        User user = userManager.login(account, password);
        if (user != null) {
            // 登录成功，显示游戏界面
            showGamePanel(user);
        } else {
            // 登录失败
            showErrorMessage("登录失败", "账号或密码错误");
        }
    }

    /**
     * 处理注册逻辑
     * @param username 用户名
     * @param account 账号
     * @param password 密码
     */
    private void handleRegister(String username, String account, String password) {
        // 验证输入
        if (!InputValidator.isValidUsername(username)) {
            showErrorMessage("注册失败", "用户名长度应在3-20个字符之间");
            return;
        }
        if (!InputValidator.isValidAccount(account)) {
            showErrorMessage("注册失败", "账号长度应在5-20个字符之间");
            return;
        }
        if (!InputValidator.isValidPassword(password)) {
            showErrorMessage("注册失败", "密码长度应至少为6个字符");
            return;
        }
        
        // 创建新用户
        User newUser = new User(username, account, password);
        
        // 添加用户
        if (userManager.addUser(newUser)) {
            // 注册成功，切换到登录界面
            showErrorMessage("注册成功", "账号注册成功，请登录");
            cardLayout.show(mainPanel, "login");
        } else {
            // 注册失败
            showErrorMessage("注册失败", "账号已存在");
        }
    }

    /**
     * 显示游戏界面
     * @param user 当前登录的用户
     */
    private void showGamePanel(User user) {
        if (gamePanel == null) {
            gamePanel = new MyGame();
            gamePanel.setCurrentUser(user);
            gamePanel.initGame(); 
            mainPanel.add(gamePanel, "game"); 
        } else {
            // 更新当前用户
            gamePanel.setCurrentUser(user);
        }
        
        // 更新窗口标题
        mainFrame.setTitle("射击游戏 - 玩家: " + user.getUsername());
        
        // 显示游戏面板
        cardLayout.show(mainPanel, "game");
        gamePanel.requestFocusInWindow();
    }

    /**
     * 显示错误信息弹窗
     * 
     * @param title 弹窗标题
     * @param message 错误信息
     */
    private void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.ERROR_MESSAGE);
    }
}