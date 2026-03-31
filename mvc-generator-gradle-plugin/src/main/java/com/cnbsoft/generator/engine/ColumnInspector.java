package com.cnbsoft.generator.engine;

import com.cnbsoft.plugin.generator.vo.ColumnInfo;
import com.cnbsoft.plugin.generator.vo.PrimaryInfo;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * DB 테이블 메타데이터를 JDBC로 추출한다.
 * Spring 의존 없음. JDBC 드라이버는 격리된 ClassLoader에서 반사적으로 로드한다.
 *
 * 기존 ColumnHelperImpl 의 핵심 JDBC 로직을 보존하되,
 * static 캐시 → 인스턴스 레벨 Map 으로 변경하여 다중 테이블 생성 시 캐시 오염 방지.
 */
public class ColumnInspector implements Closeable {

    private static final Logger log = Logger.getLogger(ColumnInspector.class.getName());

    private static final String COLUMN_QUERY = "select * from %s where 1=1";
    private static final String COLUMN_NAME  = "COLUMN_NAME";
    private static final String TABLE_NAME   = "TABLE_NAME";
    private static final List<String> AVAILABLE_KEYS = Arrays.asList(COLUMN_NAME, TABLE_NAME);

    private final Connection connection;
    private final Map<String, List<ColumnInfo>> columnCache = new HashMap<>();

    /**
     * @param config      DB 접속 정보
     * @param jdbcLoader  JDBC 드라이버 클래스를 포함하는 격리된 ClassLoader
     */
    public ColumnInspector(GeneratorConfig config, ClassLoader jdbcLoader) throws Exception {
        Class<?> driverClass = jdbcLoader.loadClass(config.dbDriver);
        Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
        // DriverShim: 격리된 ClassLoader 의 Driver를 DriverManager에 등록
        DriverManager.registerDriver(new DriverShim(driver));

        Properties props = new Properties();
        props.setProperty("user", config.dbUsername);
        props.setProperty("password", config.dbPassword != null ? config.dbPassword : "");
        this.connection = DriverManager.getConnection(config.dbUrl, props);
    }

    public List<PrimaryInfo> getPrimaryInfo(String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        String schema = connection.getMetaData().getUserName();
        ResultSet rs = meta.getPrimaryKeys(connection.getCatalog(), schema, tableName.toUpperCase());

        List<PrimaryInfo> ret = new ArrayList<>();
        while (rs.next()) {
            ResultSetMetaData rMeta = rs.getMetaData();
            int rCount = rMeta.getColumnCount();
            PrimaryInfo primaryInfo = new PrimaryInfo();
            Map<String, Object> columnInfoMap = new HashMap<>();

            for (int i = 1; i <= rCount; i++) {
                String key;
                try {
                    key = rMeta.getColumnName(i);
                } catch (SQLException e) {
                    log.fine(e.getMessage());
                    continue;
                }
                try {
                    columnInfoMap.put(key, rs.getObject(key));
                } catch (SQLException e) {
                    log.fine(e.getMessage());
                }
                if (key == null || !AVAILABLE_KEYS.contains(key)) continue;

                String value;
                try {
                    value = rs.getObject(key).toString();
                } catch (NullPointerException | SQLException e) {
                    log.fine(e.getMessage());
                    continue;
                }
                switch (key) {
                    case COLUMN_NAME: primaryInfo.setColumnName(value); break;
                    case TABLE_NAME:  primaryInfo.setTableName(value);  break;
                }
            }
            ret.add(primaryInfo);
        }
        rs.close();
        return ret;
    }

    public List<ColumnInfo> getColumnInfos(String tableName) throws SQLException {
        String upperName = tableName.toUpperCase();
        if (columnCache.containsKey(upperName)) {
            return columnCache.get(upperName);
        }

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(String.format(COLUMN_QUERY, upperName));
        ResultSetMetaData meta = rs.getMetaData();
        int cnt = meta.getColumnCount();

        List<ColumnInfo> columnList = new ArrayList<>();
        List<PrimaryInfo> primaryInfos = getPrimaryInfo(upperName);

        for (int i = 1; i <= cnt; i++) {
            try {
                String columnName = meta.getColumnName(i);
                ColumnInfo column = new ColumnInfo();
                column.setCatalogName(meta.getCatalogName(i));
                column.setColumnClassName(meta.getColumnClassName(i));
                column.setColumnDisplaySize(meta.getColumnDisplaySize(i));
                column.setColumnLabel(meta.getColumnLabel(i));
                column.setColumnName(columnName);
                column.setPrimaryKey(
                        primaryInfos.stream()
                                .anyMatch(p -> p.getColumnName() != null
                                        && p.getColumnName().equalsIgnoreCase(columnName))
                );
                column.setColumnType(meta.getColumnType(i));
                column.setPrecision(meta.getPrecision(i));
                column.setScale(meta.getScale(i));
                column.setAutoIncrement(meta.isAutoIncrement(i));
                column.setCaseSensitive(meta.isCaseSensitive(i));
                column.setNullable(meta.isNullable(i));
                column.setSearchable(meta.isSearchable(i));
                column.setSigned(meta.isSigned(i));
                column.setWritable(meta.isWritable(i));
                columnList.add(column);
            } catch (SQLException e) {
                // 일부 컬럼 오류는 무시하고 계속
                log.fine("Skipping column at index " + i + ": " + e.getMessage());
            }
        }

        rs.close();
        statement.close();

        columnCache.put(upperName, columnList);
        return columnList;
    }

    public List<String> listTablesLike(String pattern) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        String schema = connection.getMetaData().getUserName();

        // 대·소문자 모두 검색 후 대문자로 정규화해 중복 제거
        // - Oracle: 카탈로그에 대문자로 저장 → 대·소문자 패턴 모두 시도
        // - 반환된 이름은 toUpperCase()로 통일해 "TB_USER" / "tb_user" 중복 방지
        Set<String> seen = new LinkedHashSet<>();
        for (String p : new String[]{pattern.toUpperCase(), pattern.toLowerCase()}) {
            ResultSet rs = meta.getTables(connection.getCatalog(), schema, p, new String[]{"TABLE"});
            while (rs.next()) {
                seen.add(rs.getString("TABLE_NAME").toUpperCase());
            }
            rs.close();
        }
        return new ArrayList<>(seen);
    }

    @Override
    public void close() throws IOException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new IOException("Failed to close DB connection", e);
        }
    }

    // ────────────────────────────────────────────────────────────────
    // DriverShim: 격리된 ClassLoader의 Driver를 DriverManager에 위임
    // ────────────────────────────────────────────────────────────────
    private static final class DriverShim implements Driver {
        private final Driver delegate;
        DriverShim(Driver d) { this.delegate = d; }

        @Override public Connection connect(String url, Properties info) throws SQLException { return delegate.connect(url, info); }
        @Override public boolean acceptsURL(String url) throws SQLException { return delegate.acceptsURL(url); }
        @Override public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException { return delegate.getPropertyInfo(url, info); }
        @Override public int getMajorVersion() { return delegate.getMajorVersion(); }
        @Override public int getMinorVersion() { return delegate.getMinorVersion(); }
        @Override public boolean jdbcCompliant() { return delegate.jdbcCompliant(); }
        @Override public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException { return delegate.getParentLogger(); }
    }
}
