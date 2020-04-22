
package com.cloudbees.rollout;

import io.rollout.client.ConfigurationFetchedHandler;
import io.rollout.client.FetcherResults;
import io.rollout.configuration.RoxContainer;
import io.rollout.flags.RoxFlag;
import io.rollout.flags.RoxVariant;
import io.rollout.rox.server.Rox;
import io.rollout.rox.server.RoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.system.WelcomeController;

// Create Roxflags in the Flags container class
public class FlagsContainer implements RoxContainer {

	// Define the feature flags
	public static RoxFlag enableFeatureOne = new RoxFlag(true);

	// public RoxVariant titleColors = new RoxVariant("White", new String[] { "White",
	// "Blue", "Green" });

	// --END Flags

	private static Logger logger = LoggerFactory.getLogger(FlagsContainer.class);

	public static FlagsContainer conf = null;
	static {
		// innit FlagsContainer
		if (conf == null) {
			try {
				conf = new FlagsContainer();
				Rox.register("default", conf);
				// Rox.setup("5e95ad1fa6de03e3b693732d", withRoxOptions()).get();
				//TODO: inject key by  property during bootstrap with system property
                Rox.setup("5e95ad1fa6de03e3b693732d").get();
				Rox.fetch();
				logger.info("rollout init:", conf.toString());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static RoxOptions withRoxOptions() {
		return new RoxOptions.Builder().withConfigurationFetchedHandler(new ConfigurationFetchedHandler() {
			@Override
			public void onConfigurationFetched(FetcherResults results) {
				logger.info("Got Rollout configuration " + results.toString());
			}
		}).build();
	}

}
