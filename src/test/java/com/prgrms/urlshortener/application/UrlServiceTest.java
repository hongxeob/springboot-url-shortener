package com.prgrms.urlshortener.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.urlshortener.application.dto.UrlCreateRequest;
import com.prgrms.urlshortener.application.dto.UrlResponse;
import com.prgrms.urlshortener.domain.AlgorithmType;
import com.prgrms.urlshortener.domain.Url;
import com.prgrms.urlshortener.domain.UrlRepository;
import com.prgrms.urlshortener.global.ErrorCode;
import com.prgrms.urlshortener.global.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

	@InjectMocks
	UrlService urlService;

	@Mock
	UrlRepository urlRepository;

	private Url url;

	@BeforeEach
	void setUp() {
		url = Url.builder()
			.originUrl("www.naver.com")
			.algorithmType(AlgorithmType.BASE62)
			.build();
	}

	@Test
	@DisplayName("shortUrl 생성 성공")
	void createShortUrlTest() throws Exception {

		//given
		UrlCreateRequest urlCreateRequest = new UrlCreateRequest(url.getOriginUrl(), url.getAlgorithmType());

		//when
		url = mock(Url.class);
		when(urlRepository.save(any(Url.class))).thenReturn(url);
		when(url.getId()).thenReturn(1L);

		UrlResponse urlResponse = urlService.createShortUrl(urlCreateRequest);

		//then
		assertThat(url.getOriginUrl()).isEqualTo(urlResponse.originUrl());
		assertThat(url.getShortUrl()).isEqualTo(urlResponse.shortUrl());
	}

	@Test
	@DisplayName("이미 등록된 URL의 경우 originUrl 조회 성공")
	void getOriginUrlTest() throws Exception {

		//given
		UrlCreateRequest urlCreateRequest = new UrlCreateRequest(url.getOriginUrl(), url.getAlgorithmType());
		url = mock(Url.class);
		when(urlRepository.save(any(Url.class))).thenReturn(url);
		when(url.getId()).thenReturn(1L);

		UrlResponse urlResponse = urlService.createShortUrl(urlCreateRequest);
		Long requestCount = 1L;

		url = mock(Url.class);

		//when
		when(urlRepository.findByShortUrl(urlResponse.shortUrl())).thenReturn(Optional.of(url));
		when(url.getOriginUrl()).thenReturn(urlResponse.originUrl());
		when(url.getShortUrl()).thenReturn(urlResponse.shortUrl());
		when(url.getRequestCount()).thenReturn(requestCount);

		UrlResponse urlResponse2 = urlService.getOriginUrl(urlResponse.shortUrl());

		//then
		assertThat(urlResponse2)
			.hasFieldOrPropertyWithValue("originUrl", urlResponse.originUrl())
			.hasFieldOrPropertyWithValue("shortUrl", urlResponse.shortUrl())
			.hasFieldOrPropertyWithValue("requestCount", requestCount);
	}

	@Test
	@DisplayName("[예외] shortUrl에 해당하는 URL이 없으면 예외를 발생시킨다.")
	void notFoundUrlTest() {
		//given
		String unknownUrl = "ABCDEFGH";

		when(urlRepository.findByShortUrl(unknownUrl)).thenReturn(Optional.empty());
		//when
		assertThatThrownBy(() -> {
			urlService.getOriginUrl(unknownUrl);
		}).isInstanceOf(BusinessException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_FOUND_URL);
	}
}
