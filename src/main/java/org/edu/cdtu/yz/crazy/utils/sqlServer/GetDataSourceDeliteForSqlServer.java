package org.edu.cdtu.yz.crazy.utils.sqlServer;

import org.edu.cdtu.yz.crazy.been.Filed;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拿到目标库的目标  表和字段列表明细
 *
 * @author wenc
 * @create 2019-07-20 9:32
 */
public class GetDataSourceDeliteForSqlServer {

    /**
     * 
     * @param targetTableList   目标库的表集合
     * @return
     * @throws Exception
     */
    public Map<String, List<Filed>> getMyMetaDataFileds(List<String> targetTableList) {
        Map<String, List<Filed>> ListMap = new HashMap<>();
        String url = "jdbc:sqlserver://192.168.3.246:1433;DatabaseName=test_01";
        String user = "wenc";
        String password = "Admin!123";
        Connection conn = null;
        String dbName = "test_01";
        String schemaPattern ="dbo";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //yuan
            //源机

                conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("sqlServer源库连接成功");
            }
            DatabaseMetaData dbmd = conn.getMetaData();


            ResultSet resultSet = dbmd.getTables(dbName, schemaPattern, "%", new String[]{"TABLE"});

            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                System.out.println("------------读取"+tableName+"-------------");
                System.out.println();
                System.out.println();
                boolean flag =false;
                List<Filed> filedList = new ArrayList<>();
                for (String targetTable:targetTableList
                ) {
                    if(targetTable.equalsIgnoreCase(tableName)){
                        flag=true;
                        break;
                    }

                }
                if(flag){
                    ResultSet rs = dbmd.getColumns(dbName, schemaPattern, tableName, "%");
                    while (rs.next()) {
                        Filed filed = new Filed();
                        filed.setFiled(rs.getString("COLUMN_NAME"));
                        filed.setType(rs.getString("TYPE_NAME"));
                        filedList.add(filed);
                    }
                    ListMap.put(tableName, filedList);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return ListMap;
    }

}
