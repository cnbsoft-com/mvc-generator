package com.cnbsoft.generator.vo;

import java.io.Serializable;

public class PrimaryInfo implements Serializable {

    private static final long serialVersionUID = -2243540034309364139L;

    private String tableName;
    private String columnName;
    private String primaryKeyName;

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public String getColumnName() { return columnName; }
    public void setColumnName(String columnName) { this.columnName = columnName; }

    public String getPrimaryKeyName() { return primaryKeyName; }
    public void setPrimaryKeyName(String primaryKeyName) { this.primaryKeyName = primaryKeyName; }
}
