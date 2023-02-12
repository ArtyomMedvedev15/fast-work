package io.project.fastwork.services.exception;

public class WorkAlreadyAdded extends Exception{
    public WorkAlreadyAdded() {
        super();
    }

    public WorkAlreadyAdded(String message) {
        super(message);
    }
}
