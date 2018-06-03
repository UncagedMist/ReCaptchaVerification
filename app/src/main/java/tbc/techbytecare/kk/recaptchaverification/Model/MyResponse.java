package tbc.techbytecare.kk.recaptchaverification.Model;

public class MyResponse {

    private boolean success;
    private String message;

    public MyResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
