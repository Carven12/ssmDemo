package org.example.ssmDemo.entity;

import java.io.Serializable;

/**
 * @author liangc
 */
public class ResultJson<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3532722026498382483L;
	private int code;
    private String msg;
    private T data;

    public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static ResultJson<?> ok() {
        return ok("");
    }

    public static ResultJson<Object> ok(Object o) {
        return new ResultJson<Object>(ResultCode.SUCCESS, o);
    }

    public static ResultJson<?> failure(ResultCode code) {
        return failure(code, "");
    }

    public static ResultJson<Object> failure(ResultCode code, Object o) {
        return new ResultJson<Object>(code, o);
    }

    public ResultJson (ResultCode resultCode) {
        setResultCode(resultCode);
    }

    public ResultJson (ResultCode resultCode,T data) {
        setResultCode(resultCode);
        this.data = data;
    }

    public void setResultCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code +
                ", \"msg\":\"" + msg + '\"' +
                ", \"data\":\"" + data + '\"'+
                '}';
    }
}
