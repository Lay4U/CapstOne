package blacksmith.sullivanwayserver.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import blacksmith.sullivanwayserver.model.Elevator;

@Repository
public interface ElevatorRepo extends CrudRepository<Elevator, Long> {

}
