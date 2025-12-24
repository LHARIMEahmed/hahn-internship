import { useState } from "react";
import { Menu } from "@headlessui/react";
import { UserCircleIcon, PlusIcon } from "@heroicons/react/24/outline";
import { useNavigate } from "react-router-dom";
import { useProjects } from "../hooks/useProjects";
import ProjectModal from "../components/ProjectModal"; // we'll create a reusable modal

export default function Dashboard({ token, email, onLogout }) {
  const navigate = useNavigate();
  const { projects, loading, error, creating, createProject } = useProjects(token);

  const [showModal, setShowModal] = useState(false);

  const handleCreateProject = async (title, description) => {
    await createProject(title, description);
    setShowModal(false);
  };

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
            onClick={() => setShowModal(true)}
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
                      className={`${active ? "bg-gray-100" : ""} group flex w-full items-center rounded-md px-2 py-2 text-sm text-gray-700`}
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
                  <span>{p.completed} / {p.tasks.length} tasks</span>
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

      {/* Modal */}
      {showModal && (
        <ProjectModal
          show={showModal}
          onClose={() => setShowModal(false)}
          onSubmit={handleCreateProject}
          loading={creating}
        />
      )}
    </div>
  );
}
