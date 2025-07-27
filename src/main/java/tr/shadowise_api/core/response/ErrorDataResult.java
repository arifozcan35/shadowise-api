package tr.shadowise_api.core.response;

public class ErrorDataResult<T> implements IDataResult<T>{
    public String message;
    public boolean success = false;
    public T data;

    public ErrorDataResult (T data) {
        this.data = data;
    }

    public ErrorDataResult (T data, String message) {
        this.data = data;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }
}
