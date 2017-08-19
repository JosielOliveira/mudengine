package com.jpinfo.mudengine.player;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.jpinfo.mudengine.common.player.Player;
import com.jpinfo.mudengine.common.player.PlayerSimpleData;
import com.jpinfo.mudengine.common.player.Session;
import com.jpinfo.mudengine.common.security.TokenService;
import com.jpinfo.mudengine.player.model.MudPlayer;
import com.jpinfo.mudengine.player.repository.PlayerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class PlayerTests {
	
	private static final Long testPlayerId = 1L;
	private static final String testUsername = "josiel";
	private static final String testPassword = "pass";
	private static final String testCountry = "BR";
	private static final String testLanguage = "pt-BR";
	private static final String testName = "Josiel Oliveira";
	private static final String testEmail = "email@test.com";
	
	
	private static final String test2Username = "josiel2";
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private PlayerRepository repository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testSession() {
		
		Map<String, Object> urlVariables = new HashMap<String, Object>();
		
		// Create Session
		urlVariables.put("username", PlayerTests.testUsername);
		urlVariables.put("password", PlayerTests.testPassword);
		
		ResponseEntity<Session> createResponse = restTemplate.exchange(
				"/player/{username}/session?password={password}", HttpMethod.PUT, null, Session.class, urlVariables);
		
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(createResponse.getBody().getSessionId()).isNotNull();
		assertThat(createResponse.getBody().getSessionStart()).isNotNull();
		assertThat(createResponse.getBody().getPlayerId()).isNotNull();
		assertThat(createResponse.getHeaders().containsKey(TokenService.HEADER_TOKEN));
		
		// Get Session (anonymous)
		urlVariables.clear();
		urlVariables.put("username", PlayerTests.testUsername);
		
		ResponseEntity<Session> getResponse = restTemplate.exchange(
				"/player/{username}/session", HttpMethod.GET, null, Session.class, urlVariables);
		
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		
		// Get Session (authenticated)
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add(TokenService.HEADER_TOKEN, createResponse.getHeaders().getFirst(TokenService.HEADER_TOKEN));
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestHeaders);
		
		getResponse = restTemplate.exchange(
				"/player/{username}/session", HttpMethod.GET, requestEntity, Session.class, urlVariables);
		
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.getBody().getPlayerId()).isEqualTo(PlayerTests.testPlayerId);
		assertThat(getResponse.getBody().getSessionId()).isEqualTo(createResponse.getBody().getSessionId());
		
		
		// Delete Session
		ResponseEntity<String> deleteResponse = restTemplate.exchange(
				"/player/{username}/session", HttpMethod.DELETE, requestEntity, String.class, urlVariables);
		
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		// Get Session
		getResponse = restTemplate.exchange(
				"/player/{username}/session", HttpMethod.GET, requestEntity, Session.class, urlVariables);
		
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		
	}
	
	@Test
	public void testPlayer() {
		
		Map<String, Object> urlVariables = new HashMap<String, Object>();
		
		// *********** CREATE PLAYER ******************
		// ============================================
		
		urlVariables.put("username", PlayerTests.test2Username);
		urlVariables.put("email", PlayerTests.testEmail);
		
		ResponseEntity<Player> createResponse = restTemplate.exchange(
				"/player/{username}?email={email}", HttpMethod.PUT, null, Player.class, urlVariables);
		
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(createResponse.getBody().getPlayerId()).isNotNull();
		assertThat(createResponse.getBody().getEmail()).isEqualTo(PlayerTests.testEmail);
		

		// *********** CREATE SESSION ******************
		// =============================================
		
		
		// First I need to get the password generated in database
		MudPlayer dbPlayer = repository.findOne(createResponse.getBody().getPlayerId());
		
		
		// Create Session
		urlVariables.put("username", PlayerTests.test2Username);
		urlVariables.put("password", dbPlayer.getPassword());
		
		ResponseEntity<Session> loginResponse = restTemplate.exchange(
				"/player/{username}/session?password={password}", HttpMethod.PUT, null, Session.class, urlVariables);
		
		assertThat(loginResponse.getHeaders().containsKey(TokenService.HEADER_TOKEN));
		
		// Creation authenticated HttpEntity
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add(TokenService.HEADER_TOKEN, loginResponse.getHeaders().getFirst(TokenService.HEADER_TOKEN));
		HttpEntity<Object> authRequestEntity = new HttpEntity<Object>(requestHeaders); 
		
		
		// *********** GET PLAYER ********************
		// ===========================================
		urlVariables.clear();
		urlVariables.put("username", PlayerTests.test2Username);
		
		ResponseEntity<Player> getResponse = restTemplate.exchange(
				"/player/{username}", HttpMethod.GET, authRequestEntity, Player.class, urlVariables);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.getBody().getPlayerId()).isNotNull();
		assertThat(getResponse.getBody().getCountry()).isNull();
		assertThat(getResponse.getBody().getName()).isNull();
		assertThat(getResponse.getBody().getLanguage()).isNull();

		// *********** UPDATE PLAYER ******************
		// ============================================
		urlVariables.clear();
		urlVariables.put("username", PlayerTests.test2Username);
		
		PlayerSimpleData updatePlayer = new PlayerSimpleData();
		
		updatePlayer.setCountry(PlayerTests.testCountry);
		updatePlayer.setName(PlayerTests.testName);
		updatePlayer.setLanguage(PlayerTests.testLanguage);
		updatePlayer.setEmail(PlayerTests.testEmail);
		
		HttpEntity<PlayerSimpleData> requestEntity = new HttpEntity<PlayerSimpleData>(updatePlayer, requestHeaders);
		
		ResponseEntity<Player> updateResponse = restTemplate.exchange(
				"/player/{username}", HttpMethod.POST, requestEntity, Player.class, urlVariables);
		
		assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(updateResponse.getBody().getCountry()).isEqualTo(PlayerTests.testCountry);
		assertThat(updateResponse.getBody().getName()).isEqualTo(PlayerTests.testName);
		assertThat(updateResponse.getBody().getLanguage()).isEqualTo(PlayerTests.testLanguage);
		assertThat(updateResponse.getBody().getEmail()).isEqualTo(PlayerTests.testEmail);
		
		
		// ************* DELETE PLAYER ********************
		// ================================================
		urlVariables.clear();
		urlVariables.put("username", PlayerTests.test2Username);
		
		ResponseEntity<String> deleteResponse = restTemplate.exchange(
				"/player/{username}", HttpMethod.DELETE, requestEntity, String.class, urlVariables);
		
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		
		// **************** GET PLAYER AFTER DELETE ********************
		// =============================================================
		urlVariables.clear();
		urlVariables.put("username", PlayerTests.test2Username);
		
		getResponse = restTemplate.exchange(
				"/player/{username}", HttpMethod.GET, authRequestEntity, Player.class, urlVariables);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
