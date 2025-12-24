import { useEffect, useState } from "react";
import { Menu } from "@headlessui/react";
import { UserCircleIcon, PlusIcon } from "@heroicons/react/24/outline";
import { useNavigate } from "react-router-dom";

export default function Dashboard({ token, email, onLogout }) {
  const navigate = useNavigate();
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [showProjectModal, setShowProjectModal] = useState(false);
  const [newTitle, setNewTitle] = useState("");
  const [newDescription, setNewDescription] = useState("");
  const [creatingProject, setCreatingProject] = useState(false);

  // Fetch projects and their tasks with progress
  const fetchProjects = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch("http://localhost:8081/projects", {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("Failed to fetch projects");
      const data = await res.json();

      const projectsWithProgress = await Promise.all(
        data.map(async (p) => {
          const tasksRes = await fetch(`http://localhost:8081/projects/${p.id}/tasks`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          const tasks = (await tasksRes.json()) || []; // default empty array
          const completed = tasks.filter((t) => t.completed).length;
          const progress = tasks.length ? Math.round((completed / tasks.length) * 100) : 0;
          return { ...p, tasks, completed, progress };
        })
      );

      setProjects(projectsWithProgress);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProjects();
  }, [token]);

  const handleCreateProject = async (e) => {
    e.preventDefault();
    setCreatingProject(true);
    try {
      const res = await fetch("http://localhost:8081/projects", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ title: newTitle, description: newDescription }),
      });
      if (!res.ok) throw new Error("Failed to create project");
      const created = await res.json();
      setProjects((prev) => [created, ...prev]);
      setShowProjectModal(false);
      setNewTitle("");
      setNewDescription("");
    } catch (err) {
      alert(err.message);
    } finally {
      setCreatingProject(false);
    }
  };

  // Modern progress circle component
  const ProgressCircle = ({ percentage }) => {
    const radius = 50;
    const stroke = 8;
    const normalizedRadius = radius - stroke * 2;
    const circumference = normalizedRadius * 2 * Math.PI;
    const strokeDashoffset = circumference - (percentage / 100) * circumference;

    return (
      <svg height={radius * 2} width={radius * 2} className="transform -rotate-90">
        <circle
          stroke="#e5e7eb"
          fill="transparent"
          strokeWidth={stroke}
          r={normalizedRadius}
          cx={radius}
          cy={radius}
        />
        <circle
          stroke="url(#grad)"
          fill="transparent"
          strokeWidth={stroke}
          strokeLinecap="round"
          strokeDasharray={`${circumference} ${circumference}`}
          style={{ strokeDashoffset }}
          r={normalizedRadius}
          cx={radius}
          cy={radius}
          className="transition-all duration-700"
        />
        <defs>
          <linearGradient id="grad" x1="0%" y1="0%" x2="100%" y2="0%">
            <stop offset="0%" stopColor="#4ade80" />
            <stop offset="100%" stopColor="#22d3ee" />
          </linearGradient>
        </defs>
        <text
          x="50%"
          y="50%"
          dominantBaseline="middle"
          textAnchor="middle"
          className="text-sm font-semibold fill-gray-700"
        >
          {percentage}%
        </text>
      </svg>
    );
  };

  return (
    <div className="flex flex-col min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-indigo-600 text-white px-8 py-4 shadow-md flex justify-between items-center">
        <h1 className="text-xl font-bold">Hahn Internship</h1>
        <div className="flex items-center gap-4">
          <button
            onClick={() => setShowProjectModal(true)}
            className="flex items-center gap-2 px-5 py-2 rounded-full bg-gradient-to-r from-green-400 to-teal-500 text-white font-semibold shadow-lg hover:scale-105 transform transition duration-300 hover:shadow-2xl"
          >
            <PlusIcon className="w-5 h-5" /> Project
          </button>
          <Menu as="div" className="relative inline-block text-left">
            <Menu.Button className="flex items-center focus:outline-none">
              <UserCircleIcon className="w-8 h-8 text-white" />
            </Menu.Button>
            <Menu.Items className="absolute right-0 mt-2 w-36 origin-top-right bg-white divide-y divide-gray-100 rounded-md shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none z-50">
              <div className="px-1 py-1">
                <Menu.Item>
                  {({ active }) => (
                    <button
                      onClick={onLogout}
                      className={`${
                        active ? "bg-gray-100" : ""
                      } group flex w-full items-center rounded-md px-2 py-2 text-sm text-gray-700`}
                    >
                      Logout
                    </button>
                  )}
                </Menu.Item>
              </div>
            </Menu.Items>
          </Menu>
        </div>
      </header>

      {/* Main */}
      <main className="flex-1 p-8">
        <h2 className="text-2xl font-semibold mb-6">Your Projects</h2>

        {loading ? (
          <p className="text-gray-500">Loading projects...</p>
        ) : error ? (
          <p className="text-red-500">{error}</p>
        ) : projects.length === 0 ? (
          <p className="text-gray-500">You haven't created any projects yet.</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {projects.map((p) => (
              <div
                key={p.id}
                className="relative bg-white rounded-2xl shadow-xl p-6 border border-gray-200 hover:shadow-2xl transform hover:-translate-y-1 transition cursor-pointer flex flex-col items-center"
                onClick={() => navigate(`/projects/${p.id}`)}
              >
                <ProgressCircle percentage={p.progress} />
                <h3 className="text-xl font-bold mt-4 text-center">{p.title}</h3>
                <p className="text-gray-600 text-center mb-2">{p.description}</p>
                <div className="flex gap-2 text-sm text-gray-500">
                  <span>{p.completed} / {p.tasks?.length || 0} tasks</span>
                </div>
              </div>
            ))}
          </div>
        )}
      </main>

      {/* Footer */}
      <footer className="bg-gray-900 text-white p-4 text-center">
        © 2025 Hahn Internship. All rights reserved.
      </footer>

      {/* Create Project Modal */}
      {showProjectModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 backdrop-blur-sm flex items-center justify-center z-50">
          <div className="bg-white bg-opacity-95 backdrop-blur-lg rounded-3xl shadow-2xl w-full max-w-md p-8 animate-fadeIn">
            <h3 className="text-2xl font-bold mb-6 text-gray-800">Create New Project</h3>
            <form onSubmit={handleCreateProject} className="space-y-5">
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">Title</label>
                <input
                  type="text"
                  value={newTitle}
                  onChange={(e) => setNewTitle(e.target.value)}
                  required
                  className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-400 placeholder-gray-400"
                  placeholder="Project Name"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">Description</label>
                <textarea
                  value={newDescription}
                  onChange={(e) => setNewDescription(e.target.value)}
                  required
                  className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-400 placeholder-gray-400"
                  placeholder="Project Description"
                />
              </div>
              <div className="flex justify-end gap-3">
                <button
                  type="button"
                  onClick={() => setShowProjectModal(false)}
                  className="px-5 py-2 rounded-full bg-gray-300 text-gray-700 font-medium hover:bg-gray-400 transition"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={creatingProject}
                  className="px-5 py-2 rounded-full bg-gradient-to-r from-green-400 to-teal-500 text-white font-semibold shadow-lg hover:scale-105 transform transition duration-300 hover:shadow-2xl disabled:opacity-50"
                >
                  {creatingProject ? "Creating..." : "Create"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
