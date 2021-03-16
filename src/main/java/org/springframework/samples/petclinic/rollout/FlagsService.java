package org.springframework.samples.petclinic.rollout;

import io.rollout.configuration.RoxContainer;
import io.rollout.flags.RoxFlag;
import io.rollout.rox.server.Rox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;

@Service
public class FlagsService implements RoxContainer {

	private final static Logger logger = LoggerFactory.getLogger(FlagsService.class);

	@Value("${ffkey}")
	private String ffkey;

	public RoxFlag enableFeatureOne = new RoxFlag(true);

	public RoxFlag enableMemberLogin = new RoxFlag(true);

	@PostConstruct
	void postConstruct() {
		logger.info("Initializing feature flags... (using: " + ffkey + ")");
		Rox.register("default", this);
		try {
			Rox.setup(ffkey).get();
			Rox.fetch();
		}
		catch (ExecutionException | InterruptedException ee) {
			logger.error("Exception while initializing flags: ", ee);
		}
	}

	public boolean isEnableMemberLogin() {
		return this.enableMemberLogin.isEnabled();
	}

}
