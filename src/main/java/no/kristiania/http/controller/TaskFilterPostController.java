package no.kristiania.http.controller;

import no.kristiania.db.Task;
import no.kristiania.db.TaskDao;
import no.kristiania.http.HttpMessage;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TaskFilterPostController extends AbstractController {
    private static List <Task> filterList;
    private final TaskDao taskDao;

    public TaskFilterPostController(DataSource dataSource) {
        this.taskDao = new TaskDao(dataSource);
    }

    public static List <Task> getFilterList() {
        return filterList;
    }

    @Override
    public void handle(HttpMessage request, Socket socket) throws IOException, SQLException {
        Map <String, String> taskQueryMap = handlePostRequest(request, socket);
        String taskStatus = taskQueryMap.get("taskStatus");
        String memberId = taskQueryMap.get("taskMember");

        filterList = taskDao.filterTaskAndMember(taskStatus, memberId);

        sendPostResponse(socket, "http://localhost:8080/index.html");
    }
}
