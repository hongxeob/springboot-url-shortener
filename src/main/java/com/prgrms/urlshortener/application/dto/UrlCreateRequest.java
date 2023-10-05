package com.prgrms.urlshortener.application.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import com.prgrms.urlshortener.domain.AlgorithmType;
import com.prgrms.urlshortener.domain.Url;

public record UrlCreateRequest(
	@URL(message = "올바르지 않은 형식입니다.")
	@NotBlank(message = "변환할 URL을 입력해 주세요.")
	String originUrl,

	@NotNull(message = "변환을 시도할 알고리즘 타입을 선택해주세요.")
	AlgorithmType algorithmType) {

	public static Url toUrl(UrlCreateRequest urlCreateRequest) {

		return Url.builder()
			.originUrl(urlCreateRequest.originUrl())
			.algorithmType(urlCreateRequest.algorithmType())
			.build();
	}
}
