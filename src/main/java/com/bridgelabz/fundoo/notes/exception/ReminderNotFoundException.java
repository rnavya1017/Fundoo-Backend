package com.bridgelabz.fundoo.notes.exception;

public class ReminderNotFoundException extends RuntimeException{
    public ReminderNotFoundException(String message){
        super(message);
    }
}
