package com.epam.esm.repository.util;

import java.util.Map;
import java.util.Set;

public class SqlQueryBuilder {
    private static final String FIRST_PART_OF_UPDATE_GIFT_CERTIFICATE_STRING = "UPDATE gift_certificate SET ";
    private static final String FIRST_PART_OF_FIND_AND_SORT_GIFT_CERTIFICATES_STRING = "SELECT id, name, " +
            "description, price, duration, create_date, last_update_date FROM gift_certificate ORDER BY ";
    private static final String QUESTION_MARK = "=? ";
    private static final String COMMA = ", ";
    private static final String WHERE_ID = "WHERE id = ?";

    public String buildQueryForUpdate(Map<String, Object> paramMap) {
        StringBuilder stringBuilder = new StringBuilder(FIRST_PART_OF_UPDATE_GIFT_CERTIFICATE_STRING);
        Set<String> paramNames = paramMap.keySet();
        int count = 1;
        for (String paramName : paramNames) {
            if (count < paramNames.size()) {
                stringBuilder.append(paramName);
                stringBuilder.append(QUESTION_MARK);
                stringBuilder.append(COMMA);
                count++;
            } else {
                stringBuilder.append(paramName);
                stringBuilder.append(QUESTION_MARK);
                stringBuilder.append(WHERE_ID);
            }
        }
        return stringBuilder.toString();
    }

    public String buildFindAndSortQuery(String columnName, String orderType) {
        return FIRST_PART_OF_FIND_AND_SORT_GIFT_CERTIFICATES_STRING + columnName + " " + orderType;
    }
}
