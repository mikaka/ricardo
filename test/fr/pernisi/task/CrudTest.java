package fr.pernisi.task;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import fr.pernisi.task.entities.PersonalTask;
import fr.pernisi.task.object.PersonalTaskBean;
import fr.pernisi.task.repository.Repository;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

public class CrudTest  extends WithApplication {

    @Test
    public void testList() {
        Repository repository = app.injector().instanceOf(Repository.class);
        repository.create(new PersonalTask("title", "body"));

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/tasks");

        Result result = route(app, request);
        final String body = contentAsString(result);
        assertThat(body, containsString("body"));
    }
	    
    @Test
    public void testTimeoutOnUpdate() {
        Repository repository = app.injector().instanceOf(Repository.class);
        repository.create(new PersonalTask("title", "body"));

        JsonNode json = Json.toJson(new PersonalTaskBean("1", "some title", "somebody"));

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .bodyJson(json)
                .uri("/tasks/1");

        Result result = route(app, request);
        assertThat(result.status(), equalTo(GATEWAY_TIMEOUT));
    }

    @Test
    public void testCircuitBreakerOnShow() {
        Repository repository = app.injector().instanceOf(Repository.class);
        repository.create(new PostData("title", "body"));

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/tasks/1");

        Result result = route(app, request);
        assertThat(result.status(), equalTo(SERVICE_UNAVAILABLE));
    }
}
