package blacksmith.sullivanwayserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blacksmith.sullivanwayserver.model.Downline;
import blacksmith.sullivanwayserver.repo.DownlineRepo;

@Service
public class DownlineService {

	@Autowired
	DownlineRepo repo;
	
	public List<Downline> getAll() {
		List<Downline> list = new ArrayList<>();
		repo.findAll().forEach((row) -> {
			list.add(row);
		});
		return list;
	}
}
