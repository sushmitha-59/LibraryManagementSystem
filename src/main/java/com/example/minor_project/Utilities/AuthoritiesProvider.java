package com.example.minor_project.Utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AuthoritiesProvider {
    private static HashMap<String, String> authoritiesMap;

    public static String getAuthorities(String userType) {

        if (authoritiesMap == null) {
            authoritiesMap = new HashMap<>();
            List<String> studentAuthorities = Arrays.asList(
                    Constants.STUDENT_SELF_INFO_AUTHORITY,
                    Constants.STUDENT_BOOKS_VIEW
            );

            List<String> adminAuthorities = Arrays.asList(
                    Constants.CREATE_ADMIN,
                    Constants.INITIATE_TRANSACTION,
                    Constants.CREATE_BOOK,
                    Constants.DELETE_BOOK,
                    Constants.UPDATE_BOOK,
                    Constants.ADMIN_AUTHORITY_FOR_STUDENT,
                    Constants.UPLOAD_CSV,
                    Constants.ADMIN_BOOKS_VIEW
            );

            String adminAuthoritiesAsString = String.join(Constants.DELIMITER, adminAuthorities);
            String studentAuthoritiesAsString = String.join(Constants.DELIMITER, studentAuthorities);

            authoritiesMap.put(Constants.STUDENT_USER, studentAuthoritiesAsString);
            authoritiesMap.put(Constants.ADMIN_USER, adminAuthoritiesAsString);

        }

        return authoritiesMap.getOrDefault(userType, Constants.INVALID_USER);
        //"STUDENT_SELF_INFO::MAKE_PAYMENT::READ_BOOK"
    }
}
