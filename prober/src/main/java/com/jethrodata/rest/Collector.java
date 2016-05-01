package com.jethrodata.rest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Resource monitor, collects system resource (cpu, meomry etc...) every given interval
 * Enables to mark certain timestamps with a string and collecting the resources usage at given timestamps
 * @author Angel Hermon
 */
public class Collector {
	
	private static final String[] DEAFULT_PROCESSES_TO_MONITOR = new String[]{"JethroServer", "JethroMaint"};
	private static final String COMMAND_HEADER = "COMMAND";
	private static final String CPU_HEADER = "%CPU";
	private static final String MEM_HEADER = "%MEM";
	
	private static volatile Collector instance = null;
	private static final ExecutorService executor = Executors.newSingleThreadExecutor();
	private int interval = 1000;
	
	private ResourceSamples<Float> cpu = new ResourceSamples<>();
	private ResourceSamples<Float> mem = new ResourceSamples<>();
	
	private Set<String> processesesToMonitor = new HashSet<>();

	public static Collector getInstance() {
		if (instance == null) {
			synchronized (Collector.class) {
				if(instance == null) {
					instance = new Collector();
				}
			}
		}
		return instance;
	}
	private Collector() {
		processesesToMonitor.addAll(Arrays.asList(DEAFULT_PROCESSES_TO_MONITOR));
		executor.execute(collect);
	}
	
	private boolean cpuFlag = true;
	private boolean memFlag = true;
	
	public boolean isCpuFlag() {
		return cpuFlag;
	}
	public void setCpuFlag(boolean cpuFlag) {
		this.cpuFlag = cpuFlag;
	}
	public boolean isMemFlag() {
		return memFlag;
	}
	public void setMemFlag(boolean memFlag) {
		this.memFlag = memFlag;
	}
	
	private final Runnable collect = new Runnable() {

		@Override
		public void run() {
			System.out.println("Collector started");
			while(true) {
				try {
					Thread.sleep(interval);
					if (cpuFlag) {
						cpu.addSample(sampleCpu());
					}
					if (memFlag) {
						mem.addSample(sampleMem());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}

		
	};
	
	public Map<String, Float> sampleMem() {
		Map<String, Float> out = new HashMap<>();
		for (Map<String, String> process : Utils.ps()) {
			for (String processToMonitor : processesesToMonitor) {
				if (process.get(COMMAND_HEADER).contains(processToMonitor)) {
					out.put(process.get(COMMAND_HEADER), Float.parseFloat(process.get(MEM_HEADER)));
				}
			}
		}
		return out;
	}

	public Map<String, Float> sampleCpu() {
		Map<String, Float> out = new HashMap<>();
		for (Map<String, String> process : Utils.ps()) {
			for (String processToMonitor : processesesToMonitor) {
				if (process.get(COMMAND_HEADER).contains(processToMonitor)) {
					out.put(process.get(COMMAND_HEADER), Float.parseFloat(process.get(CPU_HEADER)));
				}
			}
		}
		return out;
	}
	
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	public void mark(String mark) {
		cpu.mark(mark);
		mem.mark(mark);
	}
	
	public Map<Long, Map<String, Float>> getCpuBetweenMarks(String startMark, String endMark) {
		return cpu.getSamplesBetweenMarks(startMark, endMark);
	}
	public Map<Long, Map<String, Float>> getMemBetweenMarks(String startMark, String endMark) {
		return mem.getSamplesBetweenMarks(startMark, endMark);
	}
	
	public void monitorProcess(String process) {
		processesesToMonitor.add(process);
	}
	public void stopMonitoringProcess(String process) {
		processesesToMonitor.remove(process);
	}
	
	
}
