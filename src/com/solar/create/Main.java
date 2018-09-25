package com.solar.create;

import com.solar.config.DBType;
import com.solar.config.PropsConfig;
import oracle.jdbc.driver.OracleDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        String basePath = null;
        Logger logger = null;
        OutputStream ops = null;
        try {
            String driverClassName = PropsConfig.getValueByKey("jdbc", "jdbc.driverClassName");
            String url = PropsConfig.getValueByKey("jdbc", "jdbc.url");
            String userName = PropsConfig.getValueByKey("jdbc", "jdbc.username");
            String password = PropsConfig.getValueByKey("jdbc", "jdbc.password");

            // 第一步
            Driver driver = null;
            switch (DBType.getType(driverClassName)){
                case SQLSERVER: driver = new OracleDriver();
                    break;
                case ORACLE: driver = new OracleDriver();
                    break;
                case MYSQL: ;
                default:  driver = new OracleDriver();break;
            }

            DriverManager.deregisterDriver(driver);

            //第二步：获取连接
            Connection connect = DriverManager.getConnection(url, userName, password);

            //第三步：获取执行sql语句对象
            Statement statement = connect.createStatement();

            ResultSet result1 = statement.executeQuery("select CODE,TABLE_NAME FROM MDM_MDC_DICT ");

            StringBuffer alias = new StringBuffer();

            while (result1.next()){
                String dictCode = result1.getString("CODE");
                String tableName = result1.getString("TABLE_NAME");
                String sql = "SELECT T1.COLUMN_NAME AS FIELD_NAME, T2.MSG_NODE_NAME AS MSG_NODE_NAME FROM USER_TAB_COLS T1,( " +
                        "SELECT MSG_NODE_NAME, FIELD_NAME FROM  MDM_MDC_DICT_ATTR WHERE DICT_CODE = '"+dictCode+"' " +
                        "AND MSG_NODE_NAME IS NOT NULL ) T2 WHERE  T1.COLUMN_NAME = T2.FIELD_NAME AND T1.TABLE_NAME = '"+tableName+"'";
                Statement statement2 = connect.createStatement();
                ResultSet resultSet = statement2.executeQuery(sql);
                alias.append(dictCode).append("        ").append(tableName).append("\n");
                //第五步：处理结果集
                while (resultSet.next()) {
                    String fieldName = resultSet.getString("FIELD_NAME");
                    String msgNodeName = resultSet.getString("MSG_NODE_NAME");
                    alias.append(" ").append(fieldName).append(" ").append("AS").append(" ").append(msgNodeName).append(",");
                }
                alias.append("\n\n\n");

            }
            String fileDir = PropsConfig.getValueByKey("dev", "alais.file.dir");
            File file = new File(fileDir+"/alias.sql");
            ops = new FileOutputStream(file);
            ops.write(alias.toString().getBytes());
            ops.flush();
        } catch (SQLException e) {
            logger.info(e.toString());
        } catch (IOException e) {
            logger.info(e.toString());
        } finally {
            if(ops != null){
                try {
                    ops.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.exit(0);
        }
    }
}
