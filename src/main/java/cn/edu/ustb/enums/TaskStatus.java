package cn.edu.ustb.enums;

public enum TaskStatus {
    PENDING,
    WAITING_DEPENDENCIES,
    RUNNING,
    SUCCESS,
    FAILED;

    public boolean isTerminalState() {
        return this == SUCCESS || this == FAILED;
    }
}
