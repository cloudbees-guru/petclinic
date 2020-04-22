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

import com.cloudbees.rollout.Flags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class WelcomeController {

	Logger logger = LoggerFactory.getLogger(WelcomeController.class);

	@GetMapping("/")
	public String welcome() throws IOException {
        
        // Initialize container class that we created earlier
        Flags flags = new Flags();

        // Register the flags container with Rollout
        Rox.register("",flags);

        // Setup the Rollout environment key
        Rox.setup("5ea08b64b9e3830db8ff688a");

        // Boolean flag example
        if (flags.enableTutorial.isEnabled()) {
          // TODO:  Put your code here that needs to be gated
            logger.info("tutorial is enabled");
			return "welcome_featureone";
        }
        else {
            logger.info("tutorial is not enabled");
			return "welcome_featureone";
        }
	}

}
