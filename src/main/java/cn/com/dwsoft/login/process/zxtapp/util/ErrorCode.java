package cn.com.dwsoft.login.process.zxtapp.util;

public enum ErrorCode {
    FAILED(1, "操作失败",Boolean.FALSE),
    SUCCESS(0, "执行成功",Boolean.TRUE);
    private long code;
    private String msg;
    private boolean success;
    ErrorCode(final long code, final String msg,final Boolean success) {
        this.code = code;
        this.msg = msg;
        this.success = success;
    }
    public long getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ErrorCode setMsg(String msg){
        this.msg = msg;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }
}
