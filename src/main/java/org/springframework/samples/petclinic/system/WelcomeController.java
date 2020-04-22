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
        
        // Define the feature flags
        public RoxFlag enableFeatureOne = new RoxFlag(false);
        public RoxVariant titleColors = new RoxVariant("White", new String[] { "White", "Blue", "Green" });
        //--END  Flags



        private Logger logger = LoggerFactory.getLogger(FlagsContainer.class);
        public static FlagsContainer conf = null;
        static {
            //innit FlagsContainer
            if (conf == null) {
                try {
                    conf = new Flags();
                    Rox.register("default", conf);
                    Rox.setup("5ea08b64b9e3830db8ff688a").get();
                    logger("rollout init:"
                     conf.toString());
                }
                catch (Exception e) {
                      e.printStackTrace();
                }
            }

	}

}
