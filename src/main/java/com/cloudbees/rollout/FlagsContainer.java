package com.cloudbees.rollout;

import io.rollout.configuration.RoxContainer;
import io.rollout.flags.RoxFlag;
import io.rollout.flags.RoxVariant;
import io.rollout.rox.server.Rox;

// Create Roxflags in the Flags container class
public class FlagsContainer implements RoxContainer {
    private static FlagsContainer conf = null;
    static {
        if (conf == null) {
            try {
                conf = new FlagsContainer();
                Rox.register("default", conf);
                Rox.setup("5e95ad1fa6de03e3b693732d").get();
                System.out.println("rollout init ");
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static FlagsContainer conf() {
        return conf;
    }
	// Define the feature flags
	public RoxFlag enableFeatureOne = new RoxFlag(false);

	public RoxVariant titleColors = new RoxVariant("White", new String[] { "White", "Blue", "Green" });

}
