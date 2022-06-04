package com.epam.esm.service.util;

public class Message {
    public static final String GIFT_CERTIFICATE_ID_NOT_FOUND_MSG = "Gift certificate with id=%d not found.";
    public static final String GIFT_CERTIFICATE_NAME_NOT_FOUND_MSG = "Gift certificate with name '%s' not found.";
    public static final String GIFT_CERTIFICATE_ALREADY_EXIST_MSG = "Gift certificate with the name '%s' " +
            "already exists";
    public static final String INVALID_FIELDS_MSG = "Invalid fields";
    public static final String INVALID_NAME_MSG = "Invalid name";
    public static final String INVALID_DESCRIPTION_MSG = "Invalid description";
    public static final String INVALID_PRICE_MSG = "Invalid price";
    public static final String INVALID_DURATION_MSG = "Invalid duration";
    public static final String INVALID_COLUMN_NAME_MSG = "Invalid column name. Please choose 'name', " +
            "'description', 'price', 'duration', 'create_date', 'last_update_date' columns";
    public static final String INVALID_SORT_TYPE_MSG = "Invalid sort type. Sort type can be only 'asc' or 'desc'.";
    public static final String CANNOT_BE_EMPTY_FIELDS_MSG = "Fields 'Name', 'Description', 'Price', 'Duration' " +
            "cannot be empty";
    public static final String CANNOT_BE_DELETED_GIFT_CERTIFICATE_MSG = "Cannot delete. " +
            "There is an order for a gift certificate";
    public static final String TAG_DOES_NOT_EXIST_MSG = "Tag with the name '%s' does not exist";
    public static final String NO_GIFT_CERTIFICATE_MATCHING_MSG = "No gift certificate matching these tags";

    public static final String ORDER_ID_NOT_FOUND_MSG = "Order with id=%d not found.";
    public static final String USER_HAS_NO_ORDERS_MSG = "User with id=%d has no orders.";

    public static final String TAG_NOT_FOUND_MSG = "Tag with id=%d not found.";
    public static final String INVALID_TAG_NAME_MSG = "Invalid tag name";
    public static final String CANNOT_BE_DELETED_TAG_MSG = "The tag cannot be deleted because " +
            "a gift certificate reference exists.";
    public static final String TAG_ALREADY_EXIST_MSG = "Tag with the name '%s' " +
            "already exists";
    public static final String MOST_POPULAR_TAG_NOT_FOUND_MSG = "Tag not found";

    public static final String USER_ID_NOT_FOUND_MSG = "User with id=%d not found.";
    public static final String USER_LOGIN_NOT_FOUND_MSG = "User with login '%s' not found.";
    public static final String INVALID_FIRST_NAME_MSG = "Invalid first name";
    public static final String INVALID_LAST_NAME_MSG = "Invalid last name";
    public static final String INVALID_LOGIN_MSG = "Invalid login name";
    public static final String INVALID_ROLE_MSG = "Invalid role. You can choose only USER role";
    public static final String USER_ALREADY_EXIST_MSG = "User with the login '%s' already exists";

    public static final String PAGE_SIZE_INVALID_MSG = "Invalid page size. " +
            "Elements number on the page should be more than 0.";
    public static final String PAGE_NUMBER_INVALID_MSG = "Invalid page number. " +
            "Minimum page number 1, maximum page number %s";
    public static final String NEGATIVE_PAGE_NUMBER = "Page number must not be less than 1";
    public static final String CHOOSE_PAGINATION_MSG = "Please select the page number and number of items per page";
}
