package com.jpinfo.mudengine.player.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jpinfo.mudengine.common.being.Being;
import com.jpinfo.mudengine.common.exception.AccessDeniedException;
import com.jpinfo.mudengine.common.exception.EntityNotFoundException;
import com.jpinfo.mudengine.common.exception.IllegalParameterException;
import com.jpinfo.mudengine.common.player.Player;
import com.jpinfo.mudengine.common.player.Session;
import com.jpinfo.mudengine.common.security.MudUserDetails;
import com.jpinfo.mudengine.common.security.TokenService;
import com.jpinfo.mudengine.common.utils.LocalizedMessages;
import com.jpinfo.mudengine.player.client.BeingServiceClient;
import com.jpinfo.mudengine.player.model.MudSession;
import com.jpinfo.mudengine.player.model.converter.SessionConverter;
import com.jpinfo.mudengine.player.repository.SessionRepository;

@Service
public class SessionServiceImpl {
	
	@Autowired
	private BeingServiceClient beingClient;
	
	@Autowired
	private SessionRepository sessionRepository;
	
	@Autowired
	private TokenService tokenService;
	
	public Session getActiveSession() {
		
		return sessionRepository.findActiveSession(getActiveUsername())
				.map(SessionConverter::convert)
				.orElseThrow(() -> new EntityNotFoundException(LocalizedMessages.SESSION_NOT_FOUND));
	}

	public Session createSession(Player player, String clientType, String ipAddress) {
		
		switch(player.getStatus()) {
		
		case Player.STATUS_ACTIVE:
			
			// Find all the active sessions and terminate them
			sessionRepository.findAllActiveSession(getActiveUsername()).forEach(d -> {
				
				d.setSessionEnd(LocalDateTime.now());
				sessionRepository.save(d);
			});
					
			// Creates a new session
			MudSession dbSession = new MudSession();
			
			dbSession.setPlayerId(player.getPlayerId());
			dbSession.setSessionStart(LocalDateTime.now());
			dbSession.setClientType(clientType);
			dbSession.setIpAddress(ipAddress);
						
			Session response = SessionConverter.convert(
					sessionRepository.save(dbSession)
					);
			
			createAuthContext(player, response);
						
			return response;
					
		case Player.STATUS_PENDING: 
			throw new IllegalParameterException(LocalizedMessages.PLAYER_CHANGE_PASSWORD);
			
		default: 
			throw new IllegalParameterException(LocalizedMessages.PLAYER_NO_LOGIN);
		}
	}
	

	public Session setActiveBeing(Long beingCode) {
		
		MudSession dbSession = 
				sessionRepository.findActiveSession(getActiveUsername())
				.orElseThrow(() -> new EntityNotFoundException(LocalizedMessages.SESSION_NOT_FOUND));

		// Obtain the being
		Being selectedBeing = beingClient.getBeing(beingCode);
		
		// Set the beingCode in the session object					
		dbSession.setBeingCode(beingCode);
		
		// Update in database				
		sessionRepository.save(dbSession);
		
		// Converts the dbSession object to Session
		Session sessionData = SessionConverter.convert(dbSession);
		sessionData.setCurWorldName(selectedBeing.getCurWorld());
		
		updateAuthContext(sessionData);
		
		return sessionData;
	}
	
	public Session destroyBeing(Long destroyedBeingCode) {
		
		MudSession dbSession =
				sessionRepository.findActiveSession(getActiveUsername())
				.orElseThrow(() -> new EntityNotFoundException(LocalizedMessages.SESSION_NOT_FOUND));
		
		// If the being is the currently selected
		if (destroyedBeingCode.equals(dbSession.getBeingCode())) {
			
			// Remove it from session as well
			dbSession.setBeingCode(null);
		}

		// Update the database
		Session response = SessionConverter.convert(
				sessionRepository.save(dbSession)
				);
		
		updateAuthContext(response);
		
		return response;
	}
	
	private void createAuthContext(Player playerData, Session sessionData) {
		
		try {
		
			// Update the authToken
			String token = tokenService.buildToken(
					playerData.getUsername(), 
					playerData, 
					sessionData);
			
			SecurityContextHolder.getContext().setAuthentication(
					tokenService.getAuthenticationFromToken(token)
					);
		} catch(IOException e) {
			throw new AccessDeniedException(LocalizedMessages.PLAYER_ACCESS_DENIED);
		}
	}
	
	private void updateAuthContext(Session sessionData) {
		
		String originalToken = String.valueOf(
				SecurityContextHolder.getContext().getAuthentication().getCredentials()
				);
		
		MudUserDetails uDetails = (MudUserDetails)
				SecurityContextHolder.getContext().getAuthentication().getDetails();
		
		try {
			// Update the authToken
			String token = tokenService.updateToken(originalToken,
					uDetails.getPlayerData(),
					sessionData
					);
			
			SecurityContextHolder.getContext().setAuthentication(
					tokenService.getAuthenticationFromToken(token)
					);
		} catch(IOException e) {
			
			throw new AccessDeniedException(LocalizedMessages.PLAYER_ACCESS_DENIED);
		}
	}
	
	private String getActiveUsername() {
		
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}

}
