package blacksmith.sullivanwayserver.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import blacksmith.sullivanwayserver.model.Transfermap;

@Repository
public interface TransfermapRepo extends CrudRepository<Transfermap, Long> {

}
