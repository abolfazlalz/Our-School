package com.futech.our_school.local_database;

import java.util.Map;

public class QueryCreator {

    public static final String INTEGER_KEY = "INTEGER";
    public static final String STRING_KEY = "TEXT";

    public static String getCreateDatabaseQuery(String tableName, Map<String, String> columns, String primaryColumn) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(tableName).append(" (");

        boolean isFirst = true;

        for (Map.Entry<String, String> entry : columns.entrySet()) {
            if (!isFirst) {
                sb.append(", ");
            } else {
                isFirst = false;
            }

            sb.append(entry.getKey()).append(" ").append(entry.getValue());
            if (primaryColumn.equals(entry.getKey())) {
                sb.append(" PRIMARY KEY AUTOINCREMENT");
            }
        }

        sb.append(")");

        return sb.toString();
    }

}
