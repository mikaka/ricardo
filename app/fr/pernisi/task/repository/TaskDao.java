package fr.pernisi.task.repository;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import javax.inject.Inject;

import fr.pernisi.task.entities.PersonalTask;
import fr.pernisi.task.object.PersonalTaskBean;
import play.libs.concurrent.HttpExecutionContext;

/**
 * Handles presentation of Post resources, which map to JSON.
 */
public class TaskDao {

    private final Repository repository;
    private final HttpExecutionContext ec;

    @Inject
    public TaskDao(Repository repository, HttpExecutionContext ec) {
        this.repository = repository;
        this.ec = ec;
    }

    public CompletionStage<Stream<PersonalTaskBean>> find() {
        return repository.list().thenApplyAsync(postDataStream -> {
            return postDataStream.map(data -> new PersonalTaskBean(data));
        }, ec.current());
    }

    public CompletionStage<PersonalTaskBean> create(PersonalTaskBean resource) {
        final PersonalTask data = new PersonalTask(resource.getTitle(), resource.getDescription());
        return repository.create(data).thenApplyAsync(savedData -> {
            return new PersonalTaskBean(savedData);
        }, ec.current());
    }

    public CompletionStage<Optional<PersonalTaskBean>> lookup(String id) {
        return repository.get(Long.parseLong(id)).thenApplyAsync(optionalData -> {
            return optionalData.map(data -> new PersonalTaskBean(data));
        }, ec.current());
    }

    public CompletionStage<Optional<PersonalTaskBean>> update(String id, PersonalTaskBean resource) {
        final PersonalTask data = new PersonalTask(resource.getTitle(), resource.getDescription());
        return repository.update(Long.parseLong(id), data).thenApplyAsync(optionalData -> {
            return optionalData.map(op -> new PersonalTaskBean(op));
        }, ec.current());
    }


}
