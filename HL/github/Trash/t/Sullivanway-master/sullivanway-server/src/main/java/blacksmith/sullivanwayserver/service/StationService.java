package blacksmith.sullivanwayserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blacksmith.sullivanwayserver.model.Station;
import blacksmith.sullivanwayserver.repo.StationRepo;

@Service
public class StationService {

	@Autowired
	StationRepo repo;
	
	public List<Station> getAll() {
		List<Station> list = new ArrayList<>();
		repo.findAll().forEach((row) -> {
			list.add(row);
		});
		return list;
	}
}
