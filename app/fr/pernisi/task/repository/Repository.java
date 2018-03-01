package fr.pernisi.task.repository;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import fr.pernisi.task.entities.PersonalTask;

public interface Repository {

    CompletionStage<Stream<PersonalTask>> list();

    CompletionStage<PersonalTask> create(PersonalTask personalTask);

    CompletionStage<Optional<PersonalTask>> get(Long id);

    CompletionStage<Optional<PersonalTask>> update(Long id, PersonalTask personalTask);
}

