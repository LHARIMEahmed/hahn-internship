import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Dashboard from "./pages/Dashboard";
import ProjectDetails from "./pages/ProjectDetails";
import Login from "./components/Login";
import Register from "./components/Register"; // <-- import Register
import { useState, useEffect } from "react";

export default function App() {
  const [token, setToken] = useState(null);
  const [email, setEmail] = useState(null);

  // On app start, read token/email from localStorage
  useEffect(() => {
    const savedToken = localStorage.getItem("token");
    const savedEmail = localStorage.getItem("email");
    if (savedToken) setToken(savedToken);
    if (savedEmail) setEmail(savedEmail);
  }, []);

  const handleLogin = (jwt, userEmail) => {
    setToken(jwt);
    setEmail(userEmail);
    localStorage.setItem("token", jwt);
    localStorage.setItem("email", userEmail);
  };

  const handleLogout = () => {
    setToken(null);
    setEmail(null);
    localStorage.removeItem("token");
    localStorage.removeItem("email");
  };

  return (
    <BrowserRouter>
      <Routes>
        {!token ? (
          <>
            {/* Login route */}
            <Route
              path="/login"
              element={<Login onLogin={handleLogin} />}
            />

            {/* Register route */}
            <Route
              path="/register"
              element={<Register />}
            />

            {/* Redirect all unknown routes to login */}
            <Route path="*" element={<Navigate to="/login" />} />
          </>
        ) : (
          <>
            <Route
              path="/dashboard"
              element={
                <Dashboard
                  token={token}
                  email={email}
                  onLogout={handleLogout}
                />
              }
            />
            <Route
              path="/projects/:id"
              element={<ProjectDetails token={token} onLogout={handleLogout} />}
            />
            <Route path="*" element={<Navigate to="/dashboard" />} />
          </>
        )}
      </Routes>
    </BrowserRouter>
  );
}
