package io.project.fastwork.services.exception;

public class WorkAlreadyExists extends Exception{
    public WorkAlreadyExists() {
        super();
    }

    public WorkAlreadyExists(String message) {
        super(message);
    }
}
