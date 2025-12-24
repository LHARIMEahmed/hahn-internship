import { Menu } from "@headlessui/react";
import { UserCircleIcon } from "@heroicons/react/24/outline";

export default function Header({ onLogout, onAddProject }) {
  return (
    <header className="bg-indigo-600 text-white px-8 py-4 shadow-md flex justify-between items-center">
      <h1 className="text-xl font-bold">Hahn Internship</h1>

      <div className="flex items-center gap-4">
        {onAddProject && (
          <button
            onClick={onAddProject}
            className="px-4 py-2 rounded bg-green-500 hover:bg-green-600"
          >
            + Project
          </button>
        )}

        <Menu as="div" className="relative inline-block text-left">
          <Menu.Button className="flex items-center focus:outline-none">
            <UserCircleIcon className="w-8 h-8 text-white" />
          </Menu.Button>

          <Menu.Items className="absolute right-0 mt-2 w-32 bg-white rounded-md shadow-lg ring-1 ring-black ring-opacity-5 z-50">
            <Menu.Item>
              {({ active }) => (
                <button
                  onClick={() => {
                    if (onLogout) onLogout(); // call the logout handler
                  }}
                  className={`${
                    active ? "bg-gray-100" : ""
                  } w-full text-left px-4 py-2 text-sm text-gray-700`}
                >
                  Logout
                </button>
              )}
            </Menu.Item>
          </Menu.Items>
        </Menu>
      </div>
    </header>
  );
}
