package com.groot.flow.constant;

/**
 * @author : chenhaitao934
 * @date : 11:59 下午 2020/6/22
 */
public enum GrootResponseCode {
    JOB_IN_PROCESS(1),
    JOB_RUN_SUCCESS(2),
    JOB_RUN_FAIL(3),
    NO_AVAILABLE_WORK_THREAD(4);
    private int code;

    GrootResponseCode(int code) {
        this.code = code;
    }

    public static GrootResponseCode valueOf(int code) {
        for (GrootResponseCode responseCode : GrootResponseCode.values()) {
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
