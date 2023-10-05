package com.prgrms.urlshortener.global.exception;

import com.prgrms.urlshortener.global.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {

	private final ErrorCode errorCode;

}
