package blacksmith.sullivanwayserver.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import blacksmith.sullivanwayserver.model.Station;

@Repository
public interface StationRepo extends CrudRepository<Station, Long> {

}
