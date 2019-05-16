package blacksmith.sullivanwayserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blacksmith.sullivanwayserver.model.Congestion;
import blacksmith.sullivanwayserver.repo.CongestionRepo;

@Service
public class CongestionService {

	@Autowired
	CongestionRepo repo;
	
	public List<Congestion> getAll() {
		List<Congestion> list = new ArrayList<>();
		repo.findAll().forEach((row) -> {
			list.add(row);
		});
		return list;
	}
}
