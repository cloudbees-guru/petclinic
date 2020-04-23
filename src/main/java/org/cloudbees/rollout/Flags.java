package com.cloudbees.rollout;

import io.rollout.flags.RoxFlag;
import io.rollout.configuration.RoxContainer;

import io.rollout.client.ConfigurationFetchedHandler;
import io.rollout.client.FetcherResults;
import io.rollout.flags.RoxVariant;
import io.rollout.rox.server.Rox;
import io.rollout.rox.server.RoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.system.WelcomeController;

// Create Roxflags in the Flags container class
public class Flags implements RoxContainer {
  // Define the feature flags
  public RoxFlag enableTutorial = new RoxFlag(false);
  public RoxVariant titleColors = new RoxVariant("White", new String[] {"White", "Blue", "Green"});
}
