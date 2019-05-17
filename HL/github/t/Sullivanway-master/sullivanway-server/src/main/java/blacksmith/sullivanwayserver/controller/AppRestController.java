package blacksmith.sullivanwayserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blacksmith.sullivanwayserver.model.Congestion;
import blacksmith.sullivanwayserver.model.Downline;
import blacksmith.sullivanwayserver.model.Elevator;
import blacksmith.sullivanwayserver.model.Station;
import blacksmith.sullivanwayserver.model.Transfer;
import blacksmith.sullivanwayserver.model.Transfermap;
import blacksmith.sullivanwayserver.service.CongestionService;
import blacksmith.sullivanwayserver.service.DownlineService;
import blacksmith.sullivanwayserver.service.ElevatorService;
import blacksmith.sullivanwayserver.service.StationService;
import blacksmith.sullivanwayserver.service.TransferService;
import blacksmith.sullivanwayserver.service.TransfermapService;

@RestController
@RequestMapping("/api/subway")
public class AppRestController {

	@Autowired
	private CongestionService congestionService;
	@Autowired
	private DownlineService downlineService;
	@Autowired
	private ElevatorService elevatorService;
	@Autowired
	private StationService stationService;
	@Autowired
	private TransferService transferService;
	@Autowired
	private TransfermapService transfermapService;
	
	@GetMapping(value = "/congestions")
	public ResponseEntity<List<Congestion>> getCongestions() {
		return new ResponseEntity<>(congestionService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/downlines")
	public ResponseEntity<List<Downline>> getDownlines() {
		return new ResponseEntity<>(downlineService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/elevators")
	public ResponseEntity<List<Elevator>> getElevators() {
		return new ResponseEntity<>(elevatorService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/stations")
	public ResponseEntity<List<Station>> getStations() {
		return new ResponseEntity<>(stationService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/transfers")
	public ResponseEntity<List<Transfer>> getTransfers() {
		return new ResponseEntity<>(transferService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/transfermaps")
	public ResponseEntity<List<Transfermap>> getTransfermaps() {
		return new ResponseEntity<>(transfermapService.getAll(), HttpStatus.OK);
	}
}
