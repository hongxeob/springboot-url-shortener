package com.prgrms.urlshortener.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	NOT_FOUND_URL("U01", "URL을 찾을 수 없습니다"),

	INVALID_URL_PATTERN("U02", "잘못된 URL 형식입니다.");

	private String code;
	private String message;
}
