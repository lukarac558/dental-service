package com.student.api.exception.handler;

public class ErrorConstants {
    private ErrorConstants(){}

    public static final String USER_NOT_FOUND_MESSAGE = "User with id: %s was not found.";
    public static final String DOCTOR_NOT_FOUND_MESSAGE = "Doctor associated with service type ids: %s was not found.";
    public static final String USER_BY_EMAIL_NOT_FOUND_MESSAGE = "User with email: %s was not found.";
    public static final String CALENDAR_DAY_NOT_FOUND_MESSAGE = "Calendar day with id: %s was not found.";
    public static final String SERVICE_TYPE_NOT_FOUND_MESSAGE = "Service type with id: %s was not found.";
    public static final String COMPETENCY_INFORMATION_EMAIL_NOT_FOUND_MESSAGE = "Competency information for doctor with email: %s was not found.";
    public static final String COMPETENCY_INFORMATION_ALREADY_EXIST_MESSAGE = "Competency information for doctor with email: %s was already exists.";
    public static final String CITY_NOT_FOUND_MESSAGE = "City with id: %s was not found.";
    public static final String VOIVODESHIP_ID_NOT_FOUND_MESSAGE = "Voivodeship with id: %s was not found.";
    public static final String VISIT_NOT_FOUND_MESSAGE = "Visit with id: %s was not found.";
    public static final String VISIT_POSITION_NOT_FOUND_MESSAGE = "Visit position with id: %s was not found.";
    public static final String EMAIL_ALREADY_EXISTS_MESSAGE = "User with email: %s already exists.";
    public static final String INCORRECT_TIME_FORMAT_MESSAGE = "Provided time format is incorrect.";
    public static final String INCORRECT_TIMESTAMP_FORMAT_MESSAGE = "Provided timestamp format is incorrect.";
    public static final String INCORRECT_PESEL_MESSAGE = "Given PESEL: %s is incorrect. Valid PESEL should consist of 11 digits.";
    public static final String INCORRECT_START_DATE_MESSAGE = "Given start date: %s is incorrect.";
    public static final String VISIT_CANCELLATION_FORBIDDEN_MESSAGE = "Visit with id: %s cannot be cancel, because it is too late. Limit before is %s and remaining hours is %s";
    public static final String VISIT_APPROVAL_FORBIDDEN_MESSAGE = "Visit with id: %s cannot be approved, because it is too late. Time to approve visit is %s minutes";
    public static final String VISIT_IS_ALREADY_APPROVED_MESSAGE = "Visit with id: %s is already approved.";
    public static final String FAILED_TO_AUTHENTICATE = "Request didn't contain valid token";
}
