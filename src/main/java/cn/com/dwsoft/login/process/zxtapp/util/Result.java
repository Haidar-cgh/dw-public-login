package cn.com.dwsoft.login.process.zxtapp.util;

import cn.com.dwsoft.login.process.zxtapp.util.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sqw
 * @version 1.0
 * @description TODO
 * @ClassName Result
 * @Date 2020/12/12
 * @since jdk1.8
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1000L;
    /**
     * 返回编码
     */
    private long code;
    /**
     * 返回是否成功
     */
    private boolean success = true;
    /**
     * 返回描述信息
     */
    private String msg;
    private String message;
    /**
     * 返回数据
     */
    private T data;

    public Result(){}

    public Result(T data){
        this.data = data;
    }

    public static <T> Result<T> failed(){
        return failed("操作失败");
    }

    public static <T> Result<T> failed(T data){
        return restResult(data, ErrorCode.FAILED.setMsg("操作失败"));
    }

    public static <T> Result<T> failed(String msg){
        return restResult(null,ErrorCode.FAILED.setMsg(msg));
    }

    public static <T> Result<T> failed(String msg,T data){
        return restResult(data,ErrorCode.FAILED.setMsg(msg));
    }

    public static <T> Result<T> success(){
        return success("执行成功");
    }

    public static <T> Result<T> success(T data){
        return restResult(data,ErrorCode.SUCCESS.setMsg("执行成功"));
    }

    public static <T> Result<T> success(String msg){
        return restResult(null,ErrorCode.SUCCESS.setMsg(msg));
    }

    public static <T> Result<T> success(String msg,T data){
        return restResult(data,ErrorCode.SUCCESS.setMsg(msg));
    }

    public static <T> Result<T> restResult(T data, ErrorCode errorCode) {
        return restResult(data, errorCode.getCode(),errorCode.getMsg(),errorCode.isSuccess());
    }

    private static <T> Result<T> restResult(T data, long code, String msg,Boolean success) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setMessage(msg);
        apiResult.setSuccess(success);
        return apiResult;
    }
}
