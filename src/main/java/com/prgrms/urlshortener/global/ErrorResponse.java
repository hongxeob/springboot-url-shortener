package com.prgrms.urlshortener.global;

public record ErrorResponse(
	String code,
	String message
) {
	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode);
	}
	private ErrorResponse(ErrorCode code) {
		this(code.getCode(), code.getMessage());
	}
}
