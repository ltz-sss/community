package life.majiang.community.exception;

public class CustomizeException extends RuntimeException{
    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
    public CustomizeException(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public Integer getCode(){
        return code;
    }

}
