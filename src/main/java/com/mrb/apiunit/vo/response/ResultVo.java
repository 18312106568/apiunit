package com.mrb.apiunit.vo.response;

import java.io.Serializable;

/**
 * 通用结果集封装
 * @author xiaozefeng
 */
public class ResultVo implements Serializable{
    private static final long serialVersionUID = -1608344412477317388L;
    private Integer code;

    private String msg;

    private Object data;

    public static ResultVo ok(){
        return new ResultVo(200, "success", null);
    }

    public static ResultVo ok(String message){
        return new ResultVo(200, message, null);
    }

    public static ResultVo ok(Object data){
        return new ResultVo(200, "success", data);
    }

    public static ResultVo error(String message){
        return new ResultVo(501, message, null);
    }

    public static ResultVo error(String message, Object data){
        return new ResultVo(500, message, null);
    }


    public ResultVo() {
    }

    public ResultVo(Integer code, String message, Object data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultVo{" +
                "code=" + code +
                ", message='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
