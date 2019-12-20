package com.bridgelabz.fundoo.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.common.net.HttpHeaders;

import io.swagger.annotations.Api;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Api(value = "Fundoo")
public class WebClientController {
	
	private static final String BASE_URL="http://localhost:9090";
	
	
//	
//	@Autowired
//	private WebClient.Builder webClientBuilder;
//	
//	
	
	private WebClient webClient;
	
	public void init() {
		webClient=WebClient.builder()
				.baseUrl(BASE_URL)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
	
	public Flux<String> getAllNotes(){
		return webClient.get().uri("/notes").retrieve().bodyToFlux(String.class);
	}
//	
//	@PostMapping("/create")
//	public Mono<Note> 
}
