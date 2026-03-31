package com.cnbsoft.plugin.generator.vo;

import java.io.Serializable;

public class ColumnInfo implements Serializable {

    private static final long serialVersionUID = 3407848844891248639L;

    private String catalogName;
    private String columnClassName;
    private Integer columnDisplaySize;
    private String columnLabel;
    private String columnName;
    private Integer columnType;
    private Integer jdbcType;
    private String columnTypeName;
    private Integer precision;
    private Integer scale;
    private String tableName;
    private Boolean primaryKey;
    private boolean autoIncrement;
    private boolean caseSensitive;
    private boolean currency;
    private boolean searchable;
    private boolean readOnly;
    private boolean signed;
    private boolean writable;
    private Integer nullable;

    public String getCatalogName() { return catalogName; }
    public void setCatalogName(String catalogName) { this.catalogName = catalogName; }

    public String getColumnClassName() { return columnClassName; }
    public void setColumnClassName(String columnClassName) { this.columnClassName = columnClassName; }

    public Integer getColumnDisplaySize() { return columnDisplaySize; }
    public void setColumnDisplaySize(Integer columnDisplaySize) { this.columnDisplaySize = columnDisplaySize; }

    public String getColumnLabel() { return columnLabel; }
    public void setColumnLabel(String columnLabel) { this.columnLabel = columnLabel; }

    public String getColumnName() { return columnName; }
    public void setColumnName(String columnName) { this.columnName = columnName; }

    public Integer getColumnType() { return columnType; }
    public void setColumnType(Integer columnType) { this.columnType = columnType; }

    public Integer getJdbcType() { return jdbcType; }
    public void setJdbcType(Integer jdbcType) { this.jdbcType = jdbcType; }

    public String getColumnTypeName() { return columnTypeName; }
    public void setColumnTypeName(String columnTypeName) { this.columnTypeName = columnTypeName; }

    public Integer getPrecision() { return precision; }
    public void setPrecision(Integer precision) { this.precision = precision; }

    public Integer getScale() { return scale; }
    public void setScale(Integer scale) { this.scale = scale; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public Boolean getPrimaryKey() { return primaryKey; }
    public void setPrimaryKey(Boolean primaryKey) { this.primaryKey = primaryKey; }

    public boolean isAutoIncrement() { return autoIncrement; }
    public void setAutoIncrement(boolean autoIncrement) { this.autoIncrement = autoIncrement; }

    public boolean isCaseSensitive() { return caseSensitive; }
    public void setCaseSensitive(boolean caseSensitive) { this.caseSensitive = caseSensitive; }

    public boolean isCurrency() { return currency; }
    public void setCurrency(boolean currency) { this.currency = currency; }

    public boolean isSearchable() { return searchable; }
    public void setSearchable(boolean searchable) { this.searchable = searchable; }

    public boolean isReadOnly() { return readOnly; }
    public void setReadOnly(boolean readOnly) { this.readOnly = readOnly; }

    public boolean isSigned() { return signed; }
    public void setSigned(boolean signed) { this.signed = signed; }

    public boolean isWritable() { return writable; }
    public void setWritable(boolean writable) { this.writable = writable; }

    public Integer getNullable() { return nullable; }
    public void setNullable(Integer nullable) { this.nullable = nullable; }
}
