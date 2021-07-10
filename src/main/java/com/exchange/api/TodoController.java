package com.exchange.api;

import com.exchange.postgres.entity.Todo;
import com.exchange.postgres.repository.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/")
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @PostMapping("/input")
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@RequestBody Todo todo) {

        return todoRepository.save(todo);
    }

    @GetMapping("/")
    public List<Todo> getTodos() {

        return todoRepository.findAll();
    }
}
