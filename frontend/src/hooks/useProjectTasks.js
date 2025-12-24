import { useState, useEffect } from "react";

export function useProjectTasks(projectId, token) {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchTasks = async () => {
    setLoading(true);
    try {
      const res = await fetch(`http://localhost:8081/projects/${projectId}/tasks`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const data = await res.json();
      setTasks(data);
    } catch (err) {
      console.error("Failed to fetch tasks:", err);
    } finally {
      setLoading(false);
    }
  };

  const addTask = async (task) => {
    try {
      await fetch(`http://localhost:8081/projects/${projectId}/tasks`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(task),
      });
      fetchTasks();
    } catch (err) {
      console.error("Failed to add task:", err);
    }
  };

  const completeTask = async (taskId) => {
    try {
      await fetch(`http://localhost:8081/projects/${projectId}/tasks/${taskId}/complete`, {
        method: "PATCH",
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchTasks();
    } catch (err) {
      console.error("Failed to complete task:", err);
    }
  };

  const deleteTask = async (taskId) => {
    try {
      await fetch(`http://localhost:8081/projects/${projectId}/tasks/${taskId}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchTasks();
    } catch (err) {
      console.error("Failed to delete task:", err);
    }
  };

  // ===================== EDIT TASK =====================
  const editTask = async (task) => {
    try {
      await fetch(`http://localhost:8081/projects/${projectId}/tasks/${task.id}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(task),
      });
      fetchTasks();
    } catch (err) {
      console.error("Failed to edit task:", err);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, [projectId]);

  return { tasks, loading, addTask, completeTask, deleteTask, editTask };
}
