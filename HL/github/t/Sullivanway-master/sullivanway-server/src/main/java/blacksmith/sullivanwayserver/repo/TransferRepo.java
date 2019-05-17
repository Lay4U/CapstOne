package blacksmith.sullivanwayserver.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import blacksmith.sullivanwayserver.model.Transfer;

@Repository
public interface TransferRepo extends CrudRepository<Transfer, Long> {

}
