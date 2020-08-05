package com.groot.flow.constant;

/**
 * @author : chenhaitao934
 * @date : 11:59 下午 2020/6/22
 */
public enum GrootRequestCode {
    JOB_COMPLETED(10),
    HEART_BEAT(10);
    private int code;

    GrootRequestCode(int code) {
        this.code = code;
    }

    public static GrootRequestCode valueOf(int code) {
        for (GrootRequestCode responseCode : GrootRequestCode.values()) {
            if (responseCode.code == code) {
                return responseCode;
            }
        }
        throw new IllegalArgumentException("can't find the response code !");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
