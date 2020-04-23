package org.springframework.samples.petclinic.rollout;

import io.rollout.configuration.RoxContainer;
import io.rollout.flags.RoxFlag;
import io.rollout.rox.server.Rox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.samples.petclinic.system.WelcomeController;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

@Controller
public class FlagsController implements RoxContainer {

	private final static Logger logger = LoggerFactory.getLogger(WelcomeController.class);

	@Value("${ffkey}")
	private String ffkey;

	public RoxFlag enableFeatureOne = new RoxFlag(true);

	@PostConstruct
	void postConstruct() {
		logger.info("Initializing Rollout flags...");
		Rox.register("default", this);
		try {
			Rox.setup(ffkey).get();
			Rox.fetch();
		}
		catch (InterruptedException ie) {
			logger.error("Exception while initializing Rollout flags: ", ie);
		}
		catch (ExecutionException ee) {
			logger.error("Exception while initializing Rollout flags: ", ee);
		}
	}

}
