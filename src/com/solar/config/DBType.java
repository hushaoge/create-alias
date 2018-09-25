package com.solar.config;

/**
 * @author hushaoge
 * @date 2018/8/27 13:31
 */
public enum DBType {
    ORACLE("oracle.jdbc.OracleDriver"),
    SQLSERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    MYSQL("com.mysql.jdbc.Driver");

    private String driverClassName;
    DBType(String driverClassName){
        this.driverClassName = driverClassName;
    }
    public static DBType getType(String driverClassName){
        try {
            DBType result = SQLSERVER;
            for(DBType type : DBType.values()){
                if(type.getDriverClassName().equals(driverClassName)){
                    result = type;
                    break;
                }
            }
            return result;
        } catch (Exception e) {
            return SQLSERVER;
        }
    }
    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
