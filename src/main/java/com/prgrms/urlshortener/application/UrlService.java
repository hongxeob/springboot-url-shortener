package com.prgrms.urlshortener.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.urlshortener.application.algorithm.Algorithm;
import com.prgrms.urlshortener.application.algorithm.Base62;
import com.prgrms.urlshortener.application.dto.UrlCreateRequest;
import com.prgrms.urlshortener.application.dto.UrlResponse;
import com.prgrms.urlshortener.domain.Url;
import com.prgrms.urlshortener.domain.UrlRepository;
import com.prgrms.urlshortener.global.ErrorCode;
import com.prgrms.urlshortener.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UrlService {

	private final UrlRepository urlRepository;
	private final Algorithm algorithm = new Base62();

	public UrlResponse createShortUrl(UrlCreateRequest request) {
		Url url = UrlCreateRequest.toUrl(request);

		return urlRepository.findByOriginUrl(url.getOriginUrl())
			.map(savedUrl -> {
				savedUrl.increaseRequestCount();

				return UrlResponse.from(savedUrl);
			})
			.orElseGet(() -> {
				Url savedUrl = urlRepository.save(url);
				String shortUrl = algorithm.encoding(savedUrl.getId());
				savedUrl.updateShortUrl(shortUrl);

				return UrlResponse.from(savedUrl);
			});
	}

	@Transactional(readOnly = true)
	public UrlResponse getOriginUrl(String shortUrl) {
		Url url = urlRepository.findByShortUrl(shortUrl)
			.orElseThrow(() -> {
				log.warn("URL을 찾을 수 없습니다.");
				throw new BusinessException(ErrorCode.NOT_FOUND_URL);
			});

		return UrlResponse.from(url);
	}
}
