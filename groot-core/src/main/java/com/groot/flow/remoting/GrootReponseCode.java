package com.groot.flow.remoting;

/**
 * @author : chenhaitao934
 * @date : 9:17 下午 2020/8/6
 */
public enum GrootReponseCode {
    SUCCESS(0),
    SYSTEM_ERROR(1),
    SYSTEM_BUSY(2),
    REQUEST_CODE_NOT_SUPPORTED(3),
    REQUEST_PARAM_ERROR(4);
    private int code;

    GrootReponseCode(int code) {
        this.code = code;
    }

    public static GrootReponseCode valueOf(int code) {
        for (GrootReponseCode responseCode : GrootReponseCode.values()) {
            if (responseCode.code == code) {
                return responseCode;
            }
        }
        throw new IllegalArgumentException("can't find the response code !");
    }

    public int code() {
        return this.code;
    }
}
