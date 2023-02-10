package io.project.fastwork.services.exception;

public class WorkerNotFound extends Exception{
    public WorkerNotFound() {
        super();
    }

    public WorkerNotFound(String message) {
        super(message);
    }
}
