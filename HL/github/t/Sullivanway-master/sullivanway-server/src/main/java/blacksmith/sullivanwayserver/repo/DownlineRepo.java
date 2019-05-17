package blacksmith.sullivanwayserver.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import blacksmith.sullivanwayserver.model.Downline;

@Repository
public interface DownlineRepo extends CrudRepository<Downline, Long> {

}
