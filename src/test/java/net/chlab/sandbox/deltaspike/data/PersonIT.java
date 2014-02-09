package net.chlab.sandbox.deltaspike.data;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PersonIT {

    @Inject
    PersonRepository personRepository;

    @Resource
    UserTransaction userTransaction;

    @Deployment
    public static WebArchive deployment() {
        final File[] libraries =
                Maven.resolver().loadPomFromFile("pom.xml").importCompileAndRuntimeDependencies().resolve()
                        .withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class)//
                .addClasses(Person.class, Person_.class, PersonRepository.class) //
                .addClass(DataSourceProducer.class) //
                .addAsResource("META-INF/persistence.xml") //
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") //
                .addAsLibraries(libraries);
    }

    @Before
    public void startTransaction() throws Exception {
        userTransaction.begin();
    }

    @After
    public void rollbackTransaction() throws Exception {
        if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
            userTransaction.rollback();
        }
    }

    @Test
    public void findByName() throws Exception {
        final Person person = createPerson("Hugo");
        assertThat(personRepository.findByName("Hugo").getId(), is(person.getId()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void countByName() throws Exception {
        createPerson("Hugo");

        final Person example = new Person();
        example.setName("Hugo");

        assertThat(personRepository.count(example, Person_.name), is(1L));
    }

    private Person createPerson(final String name) {
        final Person person = new Person();
        person.setName("Hugo");
        personRepository.saveAndFlush(person);
        return person;
    }
}
