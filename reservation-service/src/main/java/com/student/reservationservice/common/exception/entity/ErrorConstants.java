package com.student.reservationservice.common.exception.entity;

public class ErrorConstants {
    public static final String USER_NOT_FOUND_MESSAGE = "User with id: %s was not found.";
    public static final String CALENDAR_DAY_NOT_FOUND_MESSAGE = "Calendar day with id: %s was not found.";
    public static final String SERVICE_TYPE_NOT_FOUND_MESSAGE = "Service type with id: %s was not found.";
    public static final String CITY_NOT_FOUND_MESSAGE = "City with code: %s was not found.";
    public static final String VOIVODESHIP_ID_NOT_FOUND_MESSAGE = "Voivodeship with id: %s was not found.";
    public static final String VOIVODESHIP_NAME_NOT_FOUND_MESSAGE = "Voivodeship with name: %s was not found.";
    public static final String COMPETENCY_INFORMATION_NOT_FOUND_MESSAGE = "Competency information with id: %s was not found.";
    public static final String VISIT_NOT_FOUND_MESSAGE = "Visit with id: %s was not found.";
    public static final String VISIT_POSITION_NOT_FOUND_MESSAGE = "Visit position with id: %s was not found.";
    public static final String DATE_IS_LOCKED_MESSAGE = "Given start date to doctor is already reserved.";
    public static final String CITY_ALREADY_EXISTS_MESSAGE = "City with code: %s already exists.";
    public static final String VOIVODESHIP_ALREADY_EXISTS_MESSAGE = "Voivodeship with name: %s already exists.";
    public static final String EMAIL_ALREADY_EXISTS_MESSAGE = "User with email: %s already exists.";
    public static final String INCORRECT_CITY_CODE_FORMAT_MESSAGE = "Given city code: %s is incorrect. Valid format is: ##-###";
    public static final String INCORRECT_EMAIL_FORMAT_MESSAGE = "Email: %s is incorrect.";
    public static final String INCORRECT_TIME_FORMAT_MESSAGE = "Provided time format is incorrect.";
    public static final String INCORRECT_TIMESTAMP_FORMAT_MESSAGE = "Provided timestamp format is incorrect.";
    public static final String INCORRECT_PESEL_MESSAGE = "Given PESEL: %s is incorrect. Valid PESEL should consist of 11 digits.";
    public static final String INCORRECT_LOGIN_DATA_MESSAGE = "Provided login or password is incorrect.";
    public static final String VISIT_CANCELLATION_FORBIDDEN_MESSAGE = "Visit with id: %s cannot be cancel, because it is too late. Limit before is %s and remaining hours is %s";
}
