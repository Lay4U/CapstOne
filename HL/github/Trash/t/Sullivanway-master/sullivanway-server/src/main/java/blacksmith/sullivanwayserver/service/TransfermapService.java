package blacksmith.sullivanwayserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blacksmith.sullivanwayserver.model.Transfermap;
import blacksmith.sullivanwayserver.repo.TransfermapRepo;

@Service
public class TransfermapService {

	@Autowired
	TransfermapRepo repo;
	
	public List<Transfermap> getAll() {
		List<Transfermap> list = new ArrayList<>();
		repo.findAll().forEach((row) -> {
			list.add(row);
		});
		return list;
	}
}
