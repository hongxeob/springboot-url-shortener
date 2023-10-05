package com.prgrms.urlshortener.domain;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.prgrms.urlshortener.global.ErrorCode;
import com.prgrms.urlshortener.global.exception.BusinessException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "urls")
public class Url {

	private static final String URL_REGEX = "^(https?:\\/\\/)?([^.][\\da-z\\.-]+\\.[a-z\\.]{2,6}|[\\d\\.]+)([\\/:?=&#]{1}[\\da-z\\.-]+)*[\\/\\?]?$";
	private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "origin_url", nullable = false)
	private String originUrl;

	@Column(name = "short_url", length = 10)
	private String shortUrl;

	@Column(name = "request_count")
	private Long requestCount;

	@Enumerated(EnumType.STRING)
	private AlgorithmType algorithmType;

	@Builder
	public Url(String originUrl, AlgorithmType algorithmType) {
		checkUrlPattern(originUrl);
		this.originUrl = originUrl;
		this.algorithmType = algorithmType;
		this.requestCount = 0L;
	}

	public void increaseRequestCount() {
		this.requestCount += 1;
	}

	public void updateShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	private void checkUrlPattern(String url) {
		if (!URL_PATTERN.matcher(url).matches()) {
			throw new BusinessException(ErrorCode.INVALID_URL_PATTERN);
		}
	}
}
