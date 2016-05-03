package com.jethrodata.rest.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jethrodata.rest.Collector;
import com.jethrodata.rest.Utils;


@RestController
public class Controllers {
	
	private static final Collector collector = Collector.getInstance();
	@RequestMapping("/")
	public ResponseEntity<String> test() {
		return ResponseEntity.ok(Utils.ps().toString());
	}
	/**
	 * Toggle cpu monitoring on/off
	 * @param toggle - on/off
	 */
	@RequestMapping(value="cpu/toggle/{toggle}")
	public ResponseEntity<Void> toggleCpuCollection(@PathVariable("toggle") boolean toggle) {
		collector.setCpuFlag(toggle);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Toggle mem monitoring on/off
	 * @param toggle - on/off
	 */
	@RequestMapping(value="mem/toggle/{toggle}")
	public ResponseEntity<Void> toggleMemCollection(@PathVariable("toggle") boolean toggle) {
		collector.setMemFlag(toggle);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Start monitoring process
	 * @param process - process to monitor
	 */
	@RequestMapping(value="monitor/{process}")
	public ResponseEntity<Void> monitor(@PathVariable("process") String process) {
		collector.monitorProcess(process);
		return ResponseEntity.ok().build();
	}
	/**
	 * Stop monitoring process
	 * @param process - process to monitor
	 */
	@RequestMapping(value="stop-monitoring/{process}")
	public ResponseEntity<Void> stopMonitoring(@PathVariable("process") String process) {
		collector.stopMonitoringProcess(process);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value="mark/{mark}")
	public ResponseEntity<Void> mark(@PathVariable("mark") String mark) {
		collector.mark(mark);
		return ResponseEntity.ok().build();
	}
	@RequestMapping(value="cpu/start/{start}/end/{end}")
	public ResponseEntity<Map> sampleCpuBetweenMarks(@PathVariable("start") String start, @PathVariable("end") String end) {
		System.out.println(String.format("Start:%s", start));
		System.out.println(String.format("End:%s", end));
		return ResponseEntity.ok(collector.getCpuBetweenMarks(start, end));
	}
	@RequestMapping(value="mem/start/{start}/end/{end}")
	public ResponseEntity<Map> sampleMemBetweenMarks(@PathVariable("start") String start, @PathVariable("end") String end) {
		return ResponseEntity.ok(collector.getMemBetweenMarks(start, end));
	}
	
	@RequestMapping(value="cpu/current")
	public ResponseEntity<Map> getCurrentCpu() {
		return ResponseEntity.ok(collector.sampleCpu());
	}
	@RequestMapping(value="mem/current")
	public ResponseEntity<Map> getCurrentMem() {
		return ResponseEntity.ok(collector.sampleMem());
	}
	
//	@RequestMapping(value="query", method= {RequestMethod.POST})
//	public ResponseEntity<String> executeQuery() {
//		
//	}
	
}
