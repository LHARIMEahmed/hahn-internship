import { useState } from "react";

export default function ProjectModal({ show, onClose, onSubmit, loading }) {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(title, description);
    setTitle("");
    setDescription("");
  };

  if (!show) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 backdrop-blur-sm flex items-center justify-center z-50">
      <div className="bg-white bg-opacity-95 backdrop-blur-lg rounded-3xl shadow-2xl w-full max-w-md p-8 animate-fadeIn">
        <h3 className="text-2xl font-bold mb-6 text-gray-800">Create New Project</h3>
        <form onSubmit={handleSubmit} className="space-y-5">
          <input
            type="text"
            placeholder="Project Title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
            className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-400 placeholder-gray-400"
          />
          <textarea
            placeholder="Project Description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
            className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-400 placeholder-gray-400"
          />
          <div className="flex justify-end gap-3">
            <button
              type="button"
              onClick={onClose}
              className="px-5 py-2 rounded-full bg-gray-300 text-gray-700 font-medium hover:bg-gray-400 transition"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={loading}
              className="px-5 py-2 rounded-full bg-gradient-to-r from-green-400 to-teal-500 text-white font-semibold shadow-lg hover:scale-105 transform transition duration-300 hover:shadow-2xl disabled:opacity-50"
            >
              {loading ? "Creating..." : "Create"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
