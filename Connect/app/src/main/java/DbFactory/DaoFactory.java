package DbFactory;

import java.util.List;

/**
 * Created by zyb15 on 2020/3/14.
 */

public class DaoFactory {  //工厂类
    public static IObjectDao getIObjectDao(){return new DaoImp();}

}
