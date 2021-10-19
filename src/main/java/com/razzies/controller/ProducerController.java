package com.razzies.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razzies.model.ProducerResponse;
import com.razzies.model.ProducerWinner;
import com.razzies.service.ProducerService;

import javassist.NotFoundException;

@RestController
@RequestMapping(value = "/producer")
public class ProducerController {
	
	@Autowired
	private ProducerService producerService;
	
	@GetMapping(value = "/distant-fast-winners", produces = "application/json")
	public ResponseEntity<ProducerResponse> obterProducers() {
		ResponseEntity<ProducerResponse> httpEntity = null;
		
		try {
			final List<ProducerWinner> max = producerService.getProducerWithLongerRange2Awards();
			final List<ProducerWinner> min = producerService.getProducerWhoGot2AwardsFaster();
			
			if(max.isEmpty() && min.isEmpty()) {
				throw new NotFoundException("Not found producers");
			}
			
			httpEntity = new ResponseEntity<ProducerResponse>(ProducerResponse.builder()
																.max(max)
																.min(min)
																.build(), HttpStatus.OK);
		}catch (NotFoundException e) {
			httpEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}catch (Exception e) {
			httpEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return httpEntity; 
	}
}
