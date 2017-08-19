package com.jpinfo.mudengine.being;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.jpinfo.mudengine.being.client.ItemServiceClient;
import com.jpinfo.mudengine.common.being.Being;
import com.jpinfo.mudengine.common.security.TokenService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class BeingTests {
	
	private static final Integer testBeingType = 1;
	private static final String testBeingClass = "CLASSA";
	private static final String testWorldName = "aforgotten";
	private static final Integer testPlaceCode = 1;
	private static final Integer testQuantity = 2;
	
	private static final String testAttrA = "AAA";
	private static final String testAttrB = "AAB";
	private static final String testAttrC = "BAA";
	private static final String testAttrD = "BAB";
	
	private static final String testSkillA = "SKLA"; 
	private static final String testSkillB = "SKLB";
	private static final String testSkillC = "SKLC";
	private static final String testSkillD = "SKLD";
	

	private static final Integer test2BeingType = 2;
	private static final String test2BeingClass = "CLASSB";
	private static final String test2WorldName = "fake";
	private static final Integer test2PlaceCode = 2;
	private static final Integer test2Quantity = 3;
	
	private static final Long testPlayerId = 1L;
	private static final Long test2PlayerId = 2L;
	
	@MockBean
	private ItemServiceClient mockItem;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	/**
	 * Create the internal authentication token
	 * and put it in a HttpHeader
	 * @return
	 */
	private HttpHeaders getAuthHeaders() {
		HttpHeaders authHeaders = new HttpHeaders();
		authHeaders.add(TokenService.HEADER_TOKEN, TokenService.buildToken("internal", 999L));
		
		return authHeaders;
	}


	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testCrud() {
		
		// Creating the authentication token
		HttpEntity<Object> authEntity = new HttpEntity<Object>(getAuthHeaders());
		
		Map<String, Object> urlVariables = new HashMap<String, Object>();
		
		// *********** CREATE **********
		// =============================
		urlVariables.put("beingType", BeingTests.testBeingType);
		urlVariables.put("beingClass", BeingTests.testBeingClass);
		urlVariables.put("worldName", BeingTests.testWorldName);
		urlVariables.put("placeCode", BeingTests.testPlaceCode);
		urlVariables.put("quantity", BeingTests.testQuantity);

		ResponseEntity<Being> responseCreate= restTemplate.exchange(
				"/being/?beingType={beingType}&beingClass={beingClass}&worldName={worldName}&placeCode={placeCode}&quantity={quantity}", 
				HttpMethod.PUT, authEntity, Being.class, urlVariables);
		
		assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseCreate.getBody()).isNotNull();
		
		Being createdBeing = responseCreate.getBody();
		
		assertThat(createdBeing.getBeingType()).isEqualTo(BeingTests.testBeingType);
		assertThat(createdBeing.getBeingClass()).isEqualTo(BeingTests.testBeingClass);
		assertThat(createdBeing.getCurWorld()).isEqualTo(BeingTests.testWorldName);
		assertThat(createdBeing.getCurPlaceCode()).isEqualTo(BeingTests.testPlaceCode);
		assertThat(createdBeing.getQuantity()).isEqualTo(BeingTests.testQuantity);
	
		// Check attributes
		assertThat(createdBeing.getAttrs().get(BeingTests.testAttrA)).isNotNull();
		assertThat(createdBeing.getAttrs().get(BeingTests.testAttrB)).isNotNull();
		assertThat(createdBeing.getAttrs().get(BeingTests.testAttrC)).isNull();
		assertThat(createdBeing.getAttrs().get(BeingTests.testAttrD)).isNull();
		
		// Check skills
		assertThat(createdBeing.getSkills().get(BeingTests.testSkillA)).isNotNull();
		assertThat(createdBeing.getSkills().get(BeingTests.testSkillB)).isNotNull();
		assertThat(createdBeing.getSkills().get(BeingTests.testSkillC)).isNull();
		assertThat(createdBeing.getSkills().get(BeingTests.testSkillD)).isNull();
		
		// ************ READ ***********
		// =============================

		urlVariables.put("beingCode", createdBeing.getBeingCode());
		
		ResponseEntity<Being> responseRead= restTemplate.exchange(
				"/being/{beingCode}", HttpMethod.GET, authEntity, Being.class, urlVariables);

		assertThat(responseRead.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseRead.getBody()).isNotNull();
		
		Being readBeing = responseRead.getBody();
		
		assertThat(readBeing.getBeingType()).isEqualTo(BeingTests.testBeingType);
		assertThat(readBeing.getBeingClass()).isEqualTo(BeingTests.testBeingClass);
		assertThat(readBeing.getCurWorld()).isEqualTo(BeingTests.testWorldName);
		assertThat(readBeing.getCurPlaceCode()).isEqualTo(BeingTests.testPlaceCode);
		assertThat(readBeing.getQuantity()).isEqualTo(BeingTests.testQuantity);
		
		
		
		// *********** UPDATE **********
		// =============================
		readBeing.setBeingType(BeingTests.test2BeingType);
		readBeing.setBeingClass(BeingTests.test2BeingClass);
		readBeing.setCurWorld(BeingTests.test2WorldName);
		readBeing.setCurPlaceCode(BeingTests.test2PlaceCode);
		readBeing.setQuantity(BeingTests.test2Quantity);
		
		urlVariables.clear();
		urlVariables.put("beingCode", readBeing.getBeingCode());
		
		HttpEntity<Being> requestEntity = new HttpEntity<Being>(readBeing, getAuthHeaders());
		
		ResponseEntity<Being> responseUpdate = restTemplate.exchange(
				"/being/{beingCode}", HttpMethod.POST, requestEntity, Being.class, urlVariables);

		assertThat(responseUpdate.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseUpdate.getBody()).isNotNull();
		
		Being updatedBeing = responseUpdate.getBody();
		
		assertThat(updatedBeing.getBeingType()).isEqualTo(BeingTests.test2BeingType);
		assertThat(updatedBeing.getBeingClass()).isEqualTo(BeingTests.test2BeingClass);
		assertThat(updatedBeing.getCurWorld()).isEqualTo(BeingTests.test2WorldName);
		assertThat(updatedBeing.getCurPlaceCode()).isEqualTo(BeingTests.test2PlaceCode);
		assertThat(updatedBeing.getQuantity()).isEqualTo(BeingTests.test2Quantity);

		// Check attributes
		assertThat(updatedBeing.getAttrs().get(BeingTests.testAttrA)).isNull();
		assertThat(updatedBeing.getAttrs().get(BeingTests.testAttrB)).isNull();
		assertThat(updatedBeing.getAttrs().get(BeingTests.testAttrC)).isNotNull();
		assertThat(updatedBeing.getAttrs().get(BeingTests.testAttrD)).isNotNull();
		
		// Check skills
		assertThat(updatedBeing.getSkills().get(BeingTests.testSkillA)).isNull();
		assertThat(updatedBeing.getSkills().get(BeingTests.testSkillB)).isNull();
		assertThat(updatedBeing.getSkills().get(BeingTests.testSkillC)).isNotNull();
		assertThat(updatedBeing.getSkills().get(BeingTests.testSkillD)).isNotNull();
		
		
		// *********** DELETE **********
		// =============================
		urlVariables.clear();
		urlVariables.put("beingCode", readBeing.getBeingCode());
		
		ResponseEntity<Being> responseDelete = restTemplate.exchange(
				"/being/{beingCode}", HttpMethod.DELETE, authEntity, Being.class, urlVariables);
		
		assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	public void testCrudWithPlayer() {
		
		// Creating the authentication token
		HttpEntity<Object> authEntity = new HttpEntity<Object>(getAuthHeaders());
		
		
		Map<String, Object> urlVariables = new HashMap<String, Object>();
		
		// *********** CREATE **********
		// =============================
		urlVariables.put("beingType", BeingTests.testBeingType);
		urlVariables.put("beingClass", BeingTests.testBeingClass);
		urlVariables.put("worldName", BeingTests.testWorldName);
		urlVariables.put("placeCode", BeingTests.testPlaceCode);
		urlVariables.put("playerId", BeingTests.testPlayerId);

		ResponseEntity<Being> responseCreate= restTemplate.exchange(
				"/being/?beingType={beingType}&beingClass={beingClass}&worldName={worldName}&placeCode={placeCode}&playerId={playerId}", 
				HttpMethod.PUT, authEntity, Being.class, urlVariables);
		
		assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseCreate.getBody()).isNotNull();
		assertThat(responseCreate.getBody().getPlayerId()).isEqualTo(BeingTests.testPlayerId);

		// *********** CREATE SECOND ***********
		// =====================================
		urlVariables.clear();
		urlVariables.put("beingType", BeingTests.test2BeingType);
		urlVariables.put("beingClass", BeingTests.test2BeingClass);
		urlVariables.put("worldName", BeingTests.test2WorldName);
		urlVariables.put("placeCode", BeingTests.test2PlaceCode);
		urlVariables.put("playerId", BeingTests.testPlayerId);
		
		responseCreate= restTemplate.exchange(
				"/being/?beingType={beingType}&beingClass={beingClass}&worldName={worldName}&placeCode={placeCode}&playerId={playerId}", 
				HttpMethod.PUT, authEntity, Being.class, urlVariables);
		
		assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseCreate.getBody()).isNotNull();
		assertThat(responseCreate.getBody().getPlayerId()).isEqualTo(BeingTests.testPlayerId);

		
		// *********** CREATE ANOTHER ***********
		// ======================================
		urlVariables.clear();
		urlVariables.put("beingType", BeingTests.test2BeingType);
		urlVariables.put("beingClass", BeingTests.test2BeingClass);
		urlVariables.put("worldName", BeingTests.test2WorldName);
		urlVariables.put("placeCode", BeingTests.test2PlaceCode);
		urlVariables.put("playerId", BeingTests.test2PlayerId);
		
		responseCreate= restTemplate.exchange(
				"/being/?beingType={beingType}&beingClass={beingClass}&worldName={worldName}&placeCode={placeCode}&playerId={playerId}", 
				HttpMethod.PUT, authEntity, Being.class, urlVariables);
		
		assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseCreate.getBody()).isNotNull();
		
		Being anotherBeing = responseCreate.getBody();
		
		assertThat(anotherBeing.getPlayerId()).isEqualTo(BeingTests.test2PlayerId);
		
		// *********** READ ALL FROM PLAYER ***********
		// ==============================================
		urlVariables.clear();
		urlVariables.put("playerId", BeingTests.testPlayerId);
		
		ResponseEntity<Being[]> responseRead = restTemplate.exchange(
				"/being/player/{playerId}", 
				HttpMethod.GET, authEntity, Being[].class, urlVariables);
		
		assertThat(responseRead.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseRead.getBody().length).isEqualTo(2);
		
		
		// *********** DELETE ALL FROM PLAYER ***********
		// ==============================================
		urlVariables.clear();
		urlVariables.put("playerId", BeingTests.testPlayerId);
		
		ResponseEntity<String> responseDelete = restTemplate.exchange(
				"/being/player/{playerId}", 
				HttpMethod.DELETE, authEntity, String.class, urlVariables);
		
		assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		
		// *********** READ ALL FROM PLAYER *************
		// ==============================================
		urlVariables.clear();
		urlVariables.put("playerId", BeingTests.testPlayerId);
		
		responseRead = restTemplate.exchange(
				"/being/player/{playerId}", HttpMethod.GET, authEntity, Being[].class, urlVariables);
		
		assertThat(responseRead.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseRead.getBody().length).isEqualTo(0);


		// *********** READ ALL FROM ANOTHER PLAYER *************
		// ======================================================
		urlVariables.clear();
		urlVariables.put("playerId", BeingTests.test2PlayerId);
		
		responseRead = restTemplate.exchange(
				"/being/player/{playerId}", HttpMethod.GET, authEntity, Being[].class, urlVariables);
		
		assertThat(responseRead.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseRead.getBody().length).isEqualTo(1);
		assertThat(responseRead.getBody()[0].getBeingCode()).isEqualTo(anotherBeing.getBeingCode());
		
		// *********** CLEANUP *************
		// =================================
		urlVariables.clear();
		urlVariables.put("beingCode", anotherBeing.getBeingCode());
		ResponseEntity<Being> responseCleanup = restTemplate.exchange(
				"/being/{beingCode}", HttpMethod.DELETE, authEntity, Being.class, urlVariables);
		
		assertThat(responseCleanup.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void testDestroyFromPlace() {
		
		// Creating the authentication token
		HttpEntity<Object> authEntity = new HttpEntity<Object>(getAuthHeaders());

		Map<String, Object> urlVariables = new HashMap<String, Object>();
		
		// *********** CREATE **********
		// =============================
		urlVariables.put("beingType", BeingTests.testBeingType);
		urlVariables.put("beingClass", BeingTests.testBeingClass);
		urlVariables.put("worldName", BeingTests.testWorldName);
		urlVariables.put("placeCode", BeingTests.testPlaceCode);

		ResponseEntity<Being> responseCreate= restTemplate.exchange(
				"/being/?beingType={beingType}&beingClass={beingClass}&worldName={worldName}&placeCode={placeCode}", 
				HttpMethod.PUT, authEntity, Being.class, urlVariables);
		
		assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseCreate.getBody()).isNotNull();

		// *********** CREATE SECOND ***********
		// =====================================
		urlVariables.clear();
		urlVariables.put("beingType", BeingTests.testBeingType);
		urlVariables.put("beingClass", BeingTests.testBeingClass);
		urlVariables.put("worldName", BeingTests.testWorldName);
		urlVariables.put("placeCode", BeingTests.testPlaceCode);
		
		responseCreate= restTemplate.exchange(
				"/being/?beingType={beingType}&beingClass={beingClass}&worldName={worldName}&placeCode={placeCode}", 
				HttpMethod.PUT, authEntity, Being.class, urlVariables);
		
		assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseCreate.getBody()).isNotNull();
		
		// *********** CREATE ANOTHER ***********
		// ======================================
		urlVariables.clear();
		urlVariables.put("beingType", BeingTests.test2BeingType);
		urlVariables.put("beingClass", BeingTests.test2BeingClass);
		urlVariables.put("worldName", BeingTests.test2WorldName);
		urlVariables.put("placeCode", BeingTests.test2PlaceCode);
		
		responseCreate= restTemplate.exchange(
				"/being/?beingType={beingType}&beingClass={beingClass}&worldName={worldName}&placeCode={placeCode}", 
				HttpMethod.PUT, authEntity, Being.class, urlVariables);
		
		assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseCreate.getBody()).isNotNull();
		
		Being anotherBeing = responseCreate.getBody();
		
		// *********** READ ALL FROM PLACE ***********
		// ==============================================
		urlVariables.clear();
		urlVariables.put("worldName", BeingTests.testWorldName);
		urlVariables.put("placeCode", BeingTests.testPlaceCode);
		
		ResponseEntity<Being[]> responseRead = restTemplate.exchange(
				"/being/place/{worldName}/{placeCode}", 
				HttpMethod.GET, authEntity, Being[].class, urlVariables);
		
		assertThat(responseRead.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseRead.getBody().length).isEqualTo(2);
		
		
		// *********** DELETE ALL FROM PLACE ***********
		// ==============================================
		urlVariables.clear();
		urlVariables.put("worldName", BeingTests.testWorldName);
		urlVariables.put("placeCode", BeingTests.testPlaceCode);
		
		ResponseEntity<String> responseDelete = restTemplate.exchange(
				"/being/place/{worldName}/{placeCode}", 
				HttpMethod.DELETE, authEntity, String.class, urlVariables);
		
		assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		
		// *********** READ ALL FROM PLACE *************
		// ==============================================
		urlVariables.clear();
		urlVariables.put("worldName", BeingTests.testWorldName);
		urlVariables.put("placeCode", BeingTests.testPlaceCode);
		
		responseRead = restTemplate.exchange(
				"/being/place/{worldName}/{placeCode}", 
				HttpMethod.GET, authEntity, Being[].class, urlVariables);
		
		assertThat(responseRead.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseRead.getBody().length).isEqualTo(0);


		// *********** READ ALL FROM ANOTHER PLACE *************
		// ======================================================
		urlVariables.clear();
		urlVariables.put("worldName", BeingTests.test2WorldName);
		urlVariables.put("placeCode", BeingTests.test2PlaceCode);
		
		responseRead = restTemplate.exchange(
				"/being/place/{worldName}/{placeCode}", 
				HttpMethod.GET, authEntity, Being[].class, urlVariables);
		
		assertThat(responseRead.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseRead.getBody().length).isEqualTo(1);
		assertThat(responseRead.getBody()[0].getBeingCode()).isEqualTo(anotherBeing.getBeingCode());
		
		// *********** CLEANUP *************
		// =================================
		urlVariables.clear();
		urlVariables.put("beingCode", anotherBeing.getBeingCode());
		ResponseEntity<Being> responseCleanup = restTemplate.exchange(
				"/being/{beingCode}", 
				HttpMethod.DELETE, authEntity, Being.class, urlVariables);
		
		assertThat(responseCleanup.getStatusCode()).isEqualTo(HttpStatus.OK);
		
	}

}