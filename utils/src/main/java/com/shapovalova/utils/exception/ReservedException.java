package com.shapovalova.utils.exception;

public class ReservedException extends RuntimeException {
    public ReservedException(){}

    public ReservedException(String message){super(message);}

    public ReservedException(Throwable cause){super(cause);}

    public ReservedException(String message, Throwable cause){super(message, cause);}
}
