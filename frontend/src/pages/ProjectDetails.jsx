import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import Header from "../components/Header";
import Footer from "../components/Footer";
import TaskItem from "../components/TaskItem";
import TaskModal from "../components/TaskModal";
import EditTaskModal from "../components/EditTaskModal";
import { useProjectTasks } from "../hooks/useProjectTasks";

export default function ProjectDetails({ token, onLogout }) {
  const { id } = useParams();
  const navigate = useNavigate();

  const { tasks, loading, addTask, completeTask, deleteTask, editTask } = useProjectTasks(id, token);

  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedTask, setSelectedTask] = useState(null);
  const [search, setSearch] = useState("");
  const [sortBy, setSortBy] = useState("default"); // default, due, completed

  // Filter and sort tasks
  let filteredTasks = tasks.filter((t) =>
    t.title.toLowerCase().includes(search.toLowerCase())
  );

  if (sortBy === "due") {
    filteredTasks.sort((a, b) => new Date(a.dueDate || 0) - new Date(b.dueDate || 0));
  } else if (sortBy === "completed") {
    filteredTasks.sort((a, b) => a.completed - b.completed);
  }

  const completedCount = tasks.filter((t) => t.completed).length;
  const progress = tasks.length ? Math.round((completedCount / tasks.length) * 100) : 0;

  // Add task handler (closes modal)
  const handleAddTask = async (task) => {
    await addTask(task);
    setShowAddModal(false);
  };

  // Edit task handler (closes modal)
  const handleEditTask = async (updatedTask) => {
    await editTask(updatedTask);
    setShowEditModal(false);
  };

  return (
    <div className="flex flex-col min-h-screen bg-gray-50">
      <Header onLogout={onLogout} onDashboardClick={() => navigate("/dashboard")} />

      <main className="flex-1 p-8">
        <button
          onClick={() => navigate("/dashboard")}
          className="mb-4 text-indigo-600 font-medium hover:underline"
        >
          ← Back to Projects
        </button>

        <h1 className="text-3xl font-bold mb-6">Project Details</h1>

        {/* Search and Sort */}
        <div className="flex flex-col md:flex-row gap-4 mb-4">
          <input
            type="text"
            placeholder="Search tasks..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="flex-1 px-4 py-2 border rounded-xl focus:outline-none focus:ring-2 focus:ring-indigo-500 placeholder-gray-400"
          />
          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            className="px-4 py-2 border rounded-xl focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="default">Sort by Default</option>
            <option value="due">Sort by Due Date</option>
            <option value="completed">Sort by Completed</option>
          </select>
        </div>

        {/* Progress */}
        <div className="mb-6">
          <div className="flex justify-between mb-1 text-gray-700 font-medium">
            <span>Progress</span>
            <span>{progress}%</span>
          </div>
          <div className="w-full bg-gray-300 rounded-full h-4">
            <div
              className="bg-gradient-to-r from-green-400 to-teal-500 h-4 rounded-full transition-all duration-500"
              style={{ width: `${progress}%` }}
            />
          </div>
        </div>

        {/* Add Task Button */}
        <button
          onClick={() => setShowAddModal(true)}
          className="mb-6 px-5 py-2 rounded-full bg-gradient-to-r from-green-400 to-teal-500 text-white font-semibold shadow-lg hover:scale-105 transform transition duration-300 hover:shadow-2xl"
        >
          + Add Task
        </button>

        {/* Task List */}
        {loading ? (
          <p>Loading...</p>
        ) : (
          <ul className="space-y-4">
            {filteredTasks.map((task) => (
              <TaskItem
                key={task.id}
                task={task}
                onComplete={completeTask}
                onDelete={deleteTask}
                onEdit={(t) => {
                  setSelectedTask(t);
                  setShowEditModal(true);
                }}
              />
            ))}
          </ul>
        )}
      </main>

      <Footer />

      {/* Add Task Modal */}
      <TaskModal
        show={showAddModal}
        onClose={() => setShowAddModal(false)}
        onSubmit={handleAddTask}
      />

      {/* Edit Task Modal */}
      <EditTaskModal
        show={showEditModal}
        task={selectedTask}
        onClose={() => setShowEditModal(false)}
        onSubmit={handleEditTask}
      />
    </div>
  );
}
