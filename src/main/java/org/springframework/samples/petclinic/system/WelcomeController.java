/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.system;

import org.cloudbees.rollout.Flags;
import io.rollout.flags.RoxFlag;
import io.rollout.configuration.RoxContainer;

import io.rollout.client.ConfigurationFetchedHandler;
import io.rollout.client.FetcherResults;
import io.rollout.flags.RoxVariant;
import io.rollout.rox.server.Rox;
import io.rollout.rox.server.RoxOptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Controller
public class WelcomeController {

	Logger logger = LoggerFactory.getLogger(WelcomeController.class);

	@Value("${ffkey}")
	private String ffkey;

	@GetMapping("/")
	public String welcome() throws IOException {
		// logger.info(Flags.conf.enableFeatureOne.toString());

		Flags flags = new Flags();

		// Register the flags container with Rollout
		Rox.register("", flags);

		// Setup the Rollout environment key
		Rox.setup(ffkey);

		// Boolean flag example
		if (flags.enableTutorial.isEnabled()) {
			// TODO: Put your code here that needs to be gated
		}

		if (flags.enableFeatureOne.isEnabled()) {
			return "welcome_featureone";
		}
		else {
			return "welcome";
		}

	}

}
