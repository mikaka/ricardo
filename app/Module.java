import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;

import fr.pernisi.task.repository.Repository;
import fr.pernisi.task.repository.TaskRepository;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class Module extends AbstractModule {

    @Override
    public void configure() {
        bind(Repository.class).to(TaskRepository.class).asEagerSingleton();
    }
}


