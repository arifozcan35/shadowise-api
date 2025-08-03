package tr.shadowise_api.core.response;

public class SuccessDataResult<T> implements IDataResult<T>{
    public String message;
    public boolean success = true;
    public T data;

    public boolean isSuccess() {
        return success;
    }
    
    public T getData() {
        return data;
    }

    public SuccessDataResult (T data) {
        this.data = data;
    }

    public SuccessDataResult (T data, String message) {
        this.data = data;
        this.message = message;
    }
}
