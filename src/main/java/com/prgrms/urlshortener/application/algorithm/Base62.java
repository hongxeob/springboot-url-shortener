package com.prgrms.urlshortener.application.algorithm;

import org.springframework.stereotype.Component;

@Component
public class Base62 implements Algorithm {

	private static final int BASE62_LENGTH = 62;
	private static final String CODEC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final String PADDING_TOKEN = "S";
	private static final int SHORT_URL_LENGTH = 8;

	@Override
	public String encoding(Long id) {
		StringBuilder encodedCodecBuilder = new StringBuilder();
		while (id > 0) {
			encodedCodecBuilder.append(CODEC.charAt((int) (id % BASE62_LENGTH)));
			id /= BASE62_LENGTH;
		}

		return padToLength(encodedCodecBuilder.toString(), SHORT_URL_LENGTH);

	}

	@Override
	public Long decoding(String shotUrl) {
		Long originId = 0L;
		long power = 1;

		for (int i = 0; i < shotUrl.length(); i++) {
			originId += CODEC.indexOf(shotUrl.charAt(i)) * power;
			power *= BASE62_LENGTH;
		}
		return originId;
	}

	private String padToLength(String shortUrl, int length) {
		if (shortUrl.length() < length) {
			StringBuilder paddedBuilder = new StringBuilder();
			int paddingCount = length - shortUrl.length();

			for (int i = 0; i < paddingCount; i++) {
				paddedBuilder.append(PADDING_TOKEN);
			}

			paddedBuilder.append(shortUrl);
			return paddedBuilder.toString();
		}

		return shortUrl;
	}
}
