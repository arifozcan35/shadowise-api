package tr.shadowise_api.core.response;

public class SuccessResult implements IResult {
    public String message;
    public boolean success = true;

    public SuccessResult(){

    }
    public SuccessResult(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }
}
