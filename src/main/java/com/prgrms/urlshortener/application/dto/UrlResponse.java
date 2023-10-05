package com.prgrms.urlshortener.application.dto;

import com.prgrms.urlshortener.domain.Url;

public record UrlResponse(
	String originUrl,
	String shortUrl,
	Long requestCount
) {
	public static UrlResponse from(Url url) {
		return new UrlResponse(url.getOriginUrl(), url.getShortUrl(), url.getRequestCount());
	}
}
