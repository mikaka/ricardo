package fr.pernisi.task.repository;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import fr.pernisi.task.entities.PersonalTask;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import play.db.jpa.JPAApi;

/**
 * A repository that provides a non-blocking API with a custom execution context
 * and circuit breaker.
 */
@Singleton
public class TaskRepository implements Repository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext ec;
    private final CircuitBreaker circuitBreaker = new CircuitBreaker().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public TaskRepository(JPAApi api, DatabaseExecutionContext ec) {
        this.jpaApi = api;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Stream<PersonalTask>> list() {
        return supplyAsync(() -> wrap(em -> select(em)), ec);
    }

    @Override
    public CompletionStage<PersonalTask> create(PersonalTask personalTask) {
        return supplyAsync(() -> wrap(em -> insert(em, personalTask)), ec);
    }

    @Override
    public CompletionStage<Optional<PersonalTask>> get(Long id) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> lookup(em, id))), ec);
    }

    @Override
    public CompletionStage<Optional<PersonalTask>> update(Long id, PersonalTask personalTask) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> modify(em, id, personalTask))), ec);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Optional<PersonalTask> lookup(EntityManager em, Long id) throws SQLException {
       return Optional.ofNullable(em.find(PersonalTask.class, id));
    }

    private Stream<PersonalTask> select(EntityManager em) {
        TypedQuery<PersonalTask> query = em.createQuery("SELECT p FROM PersonalTask p", PersonalTask.class);
        return query.getResultList().stream();
    }

    private Optional<PersonalTask> modify(EntityManager em, Long id, PersonalTask personalTask) throws InterruptedException {
        final PersonalTask data = em.find(PersonalTask.class, id);
        if (data != null) {
            data.title = personalTask.title;
            data.description = personalTask.description;
        }
        Thread.sleep(10000L);
        return Optional.ofNullable(em.merge(data));
    }

    private PersonalTask insert(EntityManager em, PersonalTask personalTask) {
        em.persist(personalTask);
        return personalTask;
    }


}
