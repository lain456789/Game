package userLogin;

/**
 * 输入验证类，负责验证用户输入的有效性
 */
public class InputValidator {
    /**
     * 检查输入是否非空
     * @param input 输入字符串
     * @return 是否非空
     */
    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }
    /**
     * 检查密码是否有效
     * @param password 密码
     * @return 是否有效
     */
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        // 密码长度至少为6位
        return password.length() >= 6;
    }

    /**
     * 检查用户名是否有效
     * @param username 用户名
     * @return 是否有效
     */
    public static boolean isValidUsername(String username) {
        if (username == null) {
            return false;
        }
        // 用户名长度在3-20之间
        return username.length() >= 3 && username.length() <= 20;
    }

    /**
     * 检查账号是否有效
     * @param account 账号
     * @return 是否有效
     */
    public static boolean isValidAccount(String account) {
        if (account == null) {
            return false;
        }
        // 账号长度在5-20之间
        return account.length() >= 5 && account.length() <= 20;
    }
}