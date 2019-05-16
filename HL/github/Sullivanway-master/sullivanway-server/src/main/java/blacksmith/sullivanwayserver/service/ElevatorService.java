package blacksmith.sullivanwayserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blacksmith.sullivanwayserver.model.Elevator;
import blacksmith.sullivanwayserver.repo.ElevatorRepo;

@Service
public class ElevatorService {

	@Autowired
	ElevatorRepo repo;
	
	public List<Elevator> getAll() {
		List<Elevator> list = new ArrayList<>();
		repo.findAll().forEach((row) -> {
			list.add(row);
		});
		return list;
	}
}
