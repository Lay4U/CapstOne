package blacksmith.sullivanwayserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blacksmith.sullivanwayserver.model.Transfer;
import blacksmith.sullivanwayserver.repo.TransferRepo;

@Service
public class TransferService {

	@Autowired
	TransferRepo repo;
	
	public List<Transfer> getAll() {
		List<Transfer> list = new ArrayList<>();
		repo.findAll().forEach((row) -> {
			list.add(row);
		});
		return list;
	}
}
