/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.rollout.FlagsService;

/**
 * @author cvanball
 */
@Configuration
public class PetClinicConfiguration {

	@Autowired
	private FlagsService flags;

}
