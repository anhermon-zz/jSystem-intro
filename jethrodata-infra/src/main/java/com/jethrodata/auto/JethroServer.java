package com.jethrodata.auto;

import com.jethrodata.auto.cli.AbstractStation;
import com.jethrodata.auto.logs.AbstractLogCollector;

import jsystem.framework.system.SystemObjectImpl;

public class JethroServer extends SystemObjectImpl{

	public AbstractStation cli;
	
	public AbstractLogCollector logCollector;
	
	public AbstractProber prober;
}
