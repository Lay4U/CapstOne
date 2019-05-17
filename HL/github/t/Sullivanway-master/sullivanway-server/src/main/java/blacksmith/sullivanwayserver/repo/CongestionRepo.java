package blacksmith.sullivanwayserver.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import blacksmith.sullivanwayserver.model.Congestion;

@Repository
public interface CongestionRepo extends CrudRepository<Congestion, Long> {

}
