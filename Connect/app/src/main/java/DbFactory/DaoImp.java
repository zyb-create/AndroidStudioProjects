package DbFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zyb15 on 2020/3/14.
 */

public class DaoImp implements IObjectDao {

    private String CONNECT_DriverManager="";

    public boolean login(String username, String password) {
        String sql = "select from tb_users where fd_username ='" + username + "' and fd_password ='" + password + "'";
        User user = null;

        /*try {
            user = (User)this.query.query(DnUtil.Connect(), sql, new BeanHandler(User.class));
        } catch (SQLException var6) {
            var6.printStackTrace();
        }*/

        return user != null;
    }

    public boolean save(User user) {
        String sql = "INSERT \r\nINTO tb_users\r\nVALUES(?,?,?,?,?,?,?,?);";
        Object[] params = new Object[]{};
        int result = 0;

        /*try {
            result = this.query.update(DnUtil.Connect(), sql, params);
        } catch (SQLException var6) {
            var6.printStackTrace();
        }*/

        return result > 0;
    }

    public boolean delete(String username) {
        String sql = "delete from tb_users where fd_username = ?";
        int num = 0;

       /* try {
            num = this.query.update(DnUtil.Connect(), sql, username);
        } catch (SQLException var5) {
            var5.printStackTrace();
        }*/

        return num > 0;
    }

    public boolean update(String uaername, User uaer) {

        return false;
    }

    public List<User> findAll() {
        String sql = "select * from tb_users order by fd_username ";
        List list = null;

        /*try {
            list = (List)this.query.query(DnUtil.Connect(), sql, new BeanListHandler(User.class));
        } catch (SQLException var4) {
            var4.printStackTrace();
        }*/

        return list;
    }

    public User findByUsername(String username) {
        String sql = "select * from tb_users where fd_username ='" + username + "'";
        User user = null;

        /*try {
            user = (User)this.query.query(DnUtil.Connect(), sql, new BeanHandler(User.class));
        } catch (SQLException var5) {
            var5.printStackTrace();
        }*/

        return user;
    }
}
