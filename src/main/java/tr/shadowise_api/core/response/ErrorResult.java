package tr.shadowise_api.core.response;

public class ErrorResult implements IResult {
    public String message;
    public boolean success = false;

    public ErrorResult(){

    }
    public ErrorResult(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }
}
