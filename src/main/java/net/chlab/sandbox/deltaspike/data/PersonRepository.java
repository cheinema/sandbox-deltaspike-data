package net.chlab.sandbox.deltaspike.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface PersonRepository extends EntityRepository<Person, Long> {

    Person findByName(String name);
}
