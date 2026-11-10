package br.com.josedev.to_do_list.service;

import br.com.josedev.to_do_list.model.Task;
import br.com.josedev.to_do_list.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Task findTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Erro ao Buscar a Tarefa pelo Id: " + id)
        );
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskUpdated) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com id: " + id));
        existingTask.setTitle(taskUpdated.getTitle());
        existingTask.setDescription(taskUpdated.getDescription());
        existingTask.setStatus(taskUpdated.getStatus());
        existingTask.setPriority(taskUpdated.getPriority());

        return taskRepository.save(existingTask);
    }

    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}