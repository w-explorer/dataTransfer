package org.edu.cdtu.yz.runner;

import org.edu.cdtu.yz.crazy.been.Filed;
import org.edu.cdtu.yz.crazy.utils.mysql.BigDataFastTransferForMysql;
import org.edu.cdtu.yz.crazy.utils.sqlServer.BigDataFastTransferForSqlServer;
import org.edu.cdtu.yz.crazy.utils.sqlServer.GetDataSourceDeliteForSqlServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    public void toTransfer(Map<String, List<Filed>> myMetaDataFileds, Map<String, List<String>> targetTableMap){
        for (String tableName:myMetaDataFileds.keySet()) {
            boolean flag = false;
            StringBuffer fileds = new StringBuffer();
            List<Filed> filedList = myMetaDataFileds.get(tableName);
            for (Filed filed:filedList
            ) {
                if (flag)
                    fileds.append(","+filed.getFiled());
                else {
                    fileds.append(filed.getFiled());
                    flag=true;
                }
            }
            //根据targetTableMap 是否为空  判断执行哪一个数据传输功能
            if(targetTableMap.size()!=0){
                List<String> stringList = new ArrayList<>();
                for (String targetTable:
                targetTableMap.keySet()) {
                    if(tableName.equals(targetTable)){
                        //拿到 一对多配置的源库 表的list
                        stringList = targetTableMap.get(targetTable);
                    }
                }
                new BigDataFastTransferForSqlServer().biubiubiu(tableName, fileds, filedList,stringList);
            }else {
                new BigDataFastTransferForMysql().biubiubiu(tableName, fileds, filedList);
            }
        }
    }

    @Override
    public void run(ApplicationArguments var1) throws Exception{
        System.out.println("MyApplicationRunner class will be execute when the project was started!");

       //mysql 传输strat
//        Map<String, List<Filed>> myMetaDataFileds = new GetDataSourceDelite().getMyMetaDataFileds();
//         toTransfer(myMetaDataFileds);

        //mysql end



        //用于查看源库和目标库的  字段长度不一致的明细   写入日志中用于查看源库和目标库的  字段长度不一致的明细   写入日志中
//        new GetDataSourceDeliteToshowDiff().getMyMetaDataFileds();



        //配置一对多的规则  sqlServer  Map<A,List<B>> A是目标机表1  B是源机表n  A->B  1 D对n
        Map<String, List<String>> targetTableMap = new HashMap<>();
        ArrayList<String> tableList1 = new ArrayList<>();
        tableList1.add("a");
//        tableList1.add("b");
//        tableList1.add("c");
        targetTableMap.put("test1",tableList1);

        ArrayList<String> tableList2 = new ArrayList<>();
        tableList2.add("d");
        tableList2.add("e");
        targetTableMap.put("test2",tableList2);

        ArrayList<String> targetTableList = new ArrayList<>();
        for (String targetTable:
        targetTableMap.keySet()) {
            targetTableList.add(targetTable);
        }
        Map<String, List<Filed>> stringListMap = new GetDataSourceDeliteForSqlServer().getMyMetaDataFileds(targetTableList);
        toTransfer(stringListMap,targetTableMap);
    }
}
