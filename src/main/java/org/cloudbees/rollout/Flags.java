package org.cloudbees.rollout;

import io.rollout.flags.RoxFlag;
import io.rollout.configuration.RoxContainer;

import io.rollout.client.ConfigurationFetchedHandler;
import io.rollout.client.FetcherResults;
import io.rollout.flags.RoxVariant;
import io.rollout.rox.server.Rox;
import io.rollout.rox.server.RoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Create Roxflags in the Flags container class
public class Flags implements RoxContainer {

	// Define the feature flags
	public RoxFlag enableTutorial = new RoxFlag(false);

	public RoxVariant titleColors = new RoxVariant("White", new String[] { "White", "Blue", "Green" });

	public RoxFlag enableFeatureOne = new RoxFlag(false);

	// public RoxVariant titleColors = new RoxVariant("White", new String[] { "White",
	// "Blue", "Green" });

	// --END Flags

}
