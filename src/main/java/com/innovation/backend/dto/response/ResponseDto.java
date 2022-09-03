package com.innovation.backend.dto.response;

import com.innovation.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private T data;
    private ErrorCode error;

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, data, null);
    }
    public static <T> ResponseDto<T> fail(ErrorCode code) {
        return new ResponseDto<>(false, null, code);
    }

}