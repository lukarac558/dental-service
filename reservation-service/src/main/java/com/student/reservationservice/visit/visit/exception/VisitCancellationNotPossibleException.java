package com.student.reservationservice.visit.visit.exception;

public class VisitCancellationNotPossibleException extends RuntimeException {
    private static final String MESSAGE = "Visit with id: %s cannot be cancel, because it is too late. Limit before is %s and remaining hours is %s";

        public VisitCancellationNotPossibleException(Long id, int hoursLimit, long remainingHours) {
            super(String.format(MESSAGE, id, hoursLimit, remainingHours));
        }
}
