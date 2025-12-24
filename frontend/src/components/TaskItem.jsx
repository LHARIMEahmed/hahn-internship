import { useState } from "react";

export default function TaskItem({ task, onComplete, onDelete, onEdit }) {
  return (
    <li className="bg-white p-4 rounded-2xl shadow-lg flex justify-between items-center transition hover:shadow-2xl hover:-translate-y-1">
      <div>
        <span className={`${task.completed ? "line-through text-gray-400" : "text-gray-800"} font-medium`}>
          {task.title}
        </span>
        <p className="text-sm text-gray-500">{task.description}</p>
        {task.dueDate && <p className="text-sm text-gray-400 mt-1">Due: {new Date(task.dueDate).toLocaleDateString()}</p>}
      </div>
      <div className="flex gap-2">
        {!task.completed && (
          <button
            onClick={() => onComplete(task.id)}
            className="px-3 py-1 rounded-full bg-gradient-to-r from-green-400 to-teal-500 text-white font-medium shadow hover:scale-105 transform transition"
          >
            Complete
          </button>
        )}
        <button
          onClick={() => onEdit(task)}
          className="px-3 py-1 rounded-full bg-gradient-to-r from-yellow-400 to-orange-500 text-white font-medium shadow hover:scale-105 transform transition"
        >
          Edit
        </button>
        <button
          onClick={() => onDelete(task.id)}
          className="px-3 py-1 rounded-full bg-gradient-to-r from-red-500 to-rose-500 text-white font-medium shadow hover:scale-105 transform transition"
        >
          Delete
        </button>
      </div>
    </li>
  );
}

