package DbFactory;

import java.sql.*;
import java.sql.SQLException;

/**
 * Created by zyb15 on 2020/3/14.
 */

public class DnUtil {


    private static Connection con=null;
    private static PreparedStatement psmt=null;
    private static Statement s=null;
    private static ResultSet rs=null;

    private static synchronized Connection Connect()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con=(Connection) DriverManager.getConnection("jdbc:mysql://192.168.1.5:3306/as_user","root","root");
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return con;
    }

    public static boolean ExecuteUpdate(String str)
    {
        int result=0;
        try {
            s=Connect().createStatement();
            result=s.executeUpdate(str);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Close();
        if(result>0)
            return true;
        else
            return false;

    }


    public static ResultSet Search(String str)
    {
        try {
            s=Connect().createStatement();
            rs=s.executeQuery(str);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(rs!=null)
            return rs;
        return null;
    }

    public static PreparedStatement executePreparedStatement(String str)
    {

        try {
            psmt=(PreparedStatement) Connect().prepareStatement(str);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return psmt;

    }

    public static void RollBack()
    {
        try {
            Connect().rollback();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void Close()
    {
        if(rs!=null)
            try {
                rs.close();
            }catch(SQLException e) {
                e.printStackTrace();
            }

        if(s!=null)
            try {
                s.close();
            }catch(SQLException e) {
                e.printStackTrace();
            }

        if(psmt!=null)
            try {
                psmt.close();
            }catch(SQLException e) {
                e.printStackTrace();
            }
        if(con!=null)
            try {
                con.close();
            }catch(SQLException e) {
                e.printStackTrace();
            }
    }


}
