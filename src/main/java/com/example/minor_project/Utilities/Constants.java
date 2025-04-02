package com.example.minor_project.Utilities;

public class Constants {

    //ONLY STUDENT AUTHORITIES
    public static final String STUDENT_SELF_INFO_AUTHORITY = "STUDENT_SELF_INFO";
    public static final String STUDENT_BOOKS_VIEW = "ONLY_AVAILABLE_BOOKS";
    //ONLY ADMIN AUTHORITIES
    public static final String ADMIN_AUTHORITY_FOR_STUDENT = "STUDENT_INFO";
    public static final String CREATE_ADMIN = "READ_ADMIN";
    public static final String CREATE_BOOK = "CREATE_BOOK";
    public static final String UPDATE_BOOK = "UPDATE_BOOK";
    public static final String DELETE_BOOK = "DELETE_BOOK";
    public static final String INITIATE_TRANSACTION = "INITIATE_TRANSACTION";
    public static final String ADMIN_BOOKS_VIEW = "ALL_BOOKS";
    //BOTH BY STUDENT AND ADMIN
    public static final String UPLOAD_CSV = "UPLOAD_THROUGH_CSV";
    public static String DELIMITER = "::";
    public static String STUDENT_USER = "student";
    public static String ADMIN_USER = "admin";
    public static final String INVALID_USER = "invalid user";
    public static final String STUDENT_PREFIX = "stud::";
    public static final Integer STUDENT_CACHE_EXPIRY = 5;

}
