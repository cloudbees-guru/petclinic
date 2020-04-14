package com.cloudbees.rollout;

import io.rollout.configuration.RoxContainer;
import io.rollout.flags.RoxFlag;

public class FlagsContainer implements RoxContainer {

	public RoxFlag addFlag = new RoxFlag();

	public RoxFlag newSave = new RoxFlag();

}
