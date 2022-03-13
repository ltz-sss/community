package life.majiang.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NOT_FOUND("你找的问题不存在了，要不要换个试试？");
    private String message;

    @Override
    public String toString() {
        return super.toString();
    }

    CustomizeErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
