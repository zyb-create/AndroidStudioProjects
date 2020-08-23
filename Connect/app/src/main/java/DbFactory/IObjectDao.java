package DbFactory;

import java.util.List;

/**
 * Created by zyb15 on 2020/3/14.
 */

public interface IObjectDao {
    boolean login(String var1, String var2);

    boolean save(User var1);

    boolean delete(String var1);

    boolean update(String var1, User var2);

    List<User> findAll();

    User findByUsername(String var1);
}
