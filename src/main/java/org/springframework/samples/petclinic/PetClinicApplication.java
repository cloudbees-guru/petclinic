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

package org.springframework.samples.petclinic;

import com.cloudbees.rollout.FlagsContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.concurrent.ExecutionException;
import io.rollout.configuration.RoxContainer;
import io.rollout.rox.server.Rox;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootApplication(proxyBeanMethods = false)
public class PetClinicApplication extends SpringBootServletInitializer {

	private static FlagsContainer conf = new FlagsContainer();

	static {
		try {
			Rox.register("default", conf);
			Rox.setup("5e8b3658ffd0b27719208a42").get();
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public static FlagsContainer getConf() {
        return conf;
    }

    public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}

}
