package fr.pernisi.task.actions;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import fr.pernisi.task.object.PersonalTaskBean;
import fr.pernisi.task.repository.TaskDao;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * 
 * @author Michael
 *
 */
public class CrudAction  extends Controller {

    private HttpExecutionContext ec;
    private TaskDao handler;
	
    @Inject
    public CrudAction(HttpExecutionContext ec, TaskDao handler) {
        this.ec = ec;
        this.handler = handler;
    }
    
    
    public CompletionStage<Result> listTask() {
        return handler.find().thenApplyAsync(bean -> {
            final List<PersonalTaskBean> personalTasks = bean.collect(Collectors.toList());
            return ok(Json.toJson(personalTasks));
        }, ec.current());
    }
    
    public CompletionStage<Result> getTask(String id) {
        return handler.lookup(id).thenApplyAsync(optionalResource -> {
            return optionalResource.map(resource ->
                ok(Json.toJson(resource))
            ).orElseGet(() ->
                notFound()
            );
        }, ec.current());
    }
    
    public CompletionStage<Result> updateTask(String id) {
        JsonNode json = request().body().asJson();
        PersonalTaskBean bean = Json.fromJson(json, PersonalTaskBean.class);
        return handler.update(id, bean).thenApplyAsync(optionalResource -> {
            return optionalResource.map(r ->
                    ok(Json.toJson(r))
            ).orElseGet(() ->
                    notFound()
            );
        }, ec.current());
    }
    
    public CompletionStage<Result> createTask() {
        JsonNode json = request().body().asJson();
        final PersonalTaskBean resource = Json.fromJson(json, PersonalTaskBean.class);
        return handler.create(resource).thenApplyAsync(savedResource -> {
            return created(Json.toJson(savedResource));
        }, ec.current());
    }
    
}
