package com.prgrms.urlshortener.application.algorithm;

public interface Algorithm {
	String encoding(Long id);

	Long decoding(String shotUrl);
}
