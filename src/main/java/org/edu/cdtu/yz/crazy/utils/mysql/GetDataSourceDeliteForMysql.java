package org.edu.cdtu.yz.crazy.utils.mysql;

import org.edu.cdtu.yz.crazy.been.Filed;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拿到目标库 和源库的笛卡尔积的字段明细
 * @author wenc
 * @create 2019-07-20 9:32
 */
public class GetDataSourceDeliteForMysql {

    public  Map<String, List<Filed>> getMyMetaDataFileds() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");
        
        Map<String, List<Filed>> ListMap = new HashMap<>();

        //yuan
        //源机
        String url = "jdbc:mysql://mysql5.cdqdops.org:3306/standard_5101000047?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
        String user = "clean";
        String password = "cDqd!8883";
        Connection conn1 = null;
        String dbName1="standard_5101000047";
        conn1 = DriverManager.getConnection(url, user, password);
        if(conn1!=null){
            System.out.println("0连接成功");
        }
        DatabaseMetaData dbmd1 = conn1.getMetaData();


        //目标机
        //目标机
        String url2 = "jdbc:mysql://mysql6.cdqdops.org:3306/zl_analysis_reader?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
        String user2 = "zl_write";
        String password2 = "Pdw(2323a";
        String dbName2="zl_analysis_reader";

        Connection conn2 = null;
       
        conn2 = DriverManager.getConnection(url2, user2, password2);
        if(conn2!=null){
            System.out.println("连接成功");
        }
        DatabaseMetaData dbmd2 = conn2.getMetaData();


        ResultSet resultSet2 = dbmd2.getTables(dbName2, dbName2, "%", new String[]{"TABLE"});
        while (resultSet2.next()) {
            List<Filed> filedList = new ArrayList<>();
            String tableName = resultSet2.getString("TABLE_NAME");
            System.out.println("------------读取"+tableName+"-------------");

            ResultSet rs = dbmd2.getColumns(dbName2, dbName2, tableName, "%");

            while (rs.next()){
                ResultSet resultSet1 = dbmd1.getTables(dbName1, dbName1, "%", new String[]{"TABLE"});


                while (resultSet1.next()) {
                    String tableName1 = resultSet1.getString("TABLE_NAME");
                    if(tableName1.equals(tableName)){
                        ResultSet rs0 = dbmd1.getColumns(dbName1, dbName1, tableName1, "%");
                        while (rs0.next()){
                            if(rs.getString("COLUMN_NAME").equals(rs0.getString("COLUMN_NAME"))){
                                Filed filed = new Filed();
                                filed.setFiled(rs.getString("COLUMN_NAME") );
                                filed.setType(rs.getString("TYPE_NAME"));
                                filedList.add(filed);
                            }
                        }
                    }

                }



            }
            ListMap.put(tableName,filedList);

        }

        return ListMap;
    }

//    public static void main(String[] args) throws Exception {
//        GetDataSourceDelite getDataSourceDelite = new GetDataSourceDelite();
//        getDataSourceDelite.getMyMetaDataFileds();
//    }

}
