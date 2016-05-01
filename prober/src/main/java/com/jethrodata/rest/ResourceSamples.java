package com.jethrodata.rest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A bean containing samples of resource (cpu, memory etc..) over time.
 * the samples are contained within a map, with the times tamp as a key, and the resource at that point od time as the value.
 * Additionally, contains a mark to time stamp mapping, enabling to mark certain points in the sample, for analysing ceratin sagments of the sample.
 * 
 * @author Angel Hermon
 */
public class ResourceSamples<T> {
	
	private LinkedHashMap<Long, Map<String, T>> samples = new LinkedHashMap<>();
	private Map<String, Long> markMapping = new HashMap<>();
	private String currentMark = null;
	
	public void addSample(Map<String, T> sample) {
		long ts = System.currentTimeMillis();
		samples.put(ts, sample);
		if (currentMark != null) {
			markMapping.put(currentMark, ts);
			currentMark = null;
		}
	}
	
	public Map<Long, Map<String, T>> getSamplesBetweenMarks(String startMark, String endMark) {
		if (! markMapping.containsKey(startMark)) {
			return null;
		}
		if (! markMapping.containsKey(endMark)) {
			return null;
		}
		if(markMapping.get(startMark) > markMapping.get(endMark)) {
			return null;
		}
		boolean started = false;
		boolean ended = false;
		Map<Long, Map<String, T>> out = new LinkedHashMap<>();
		for(long k : samples.keySet()) {
			if(k == markMapping.get(startMark)) {
				started = true;
			}
			if(k == markMapping.get(endMark)) {
				ended = true;
			}
			if (ended) {
				break;
			}
			if(started) {
				out.put(k, samples.get(k));
			}
		}
		return out;
		
	}

	public void mark(String mark) {
		this.currentMark = mark;
	}

	@Override
	public String toString() {
		return "ResourceSamples [samples=" + samples + ", markMapping=" + markMapping + ", currentMark=" + currentMark + "]";
	}
}
