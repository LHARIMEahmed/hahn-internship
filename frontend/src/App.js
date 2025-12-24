import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Dashboard from "./pages/Dashboard";
import ProjectDetails from "./pages/ProjectDetails";
import Login from "./components/Login";
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
    localStorage.setItem("token", jwt);    // persist token
    localStorage.setItem("email", userEmail); // persist email
  };

  const handleLogout = () => {
    setToken(null);
    setEmail(null);
    localStorage.removeItem("token");  // clear token
    localStorage.removeItem("email");  // clear email
  };

  return (
    <BrowserRouter>
      <Routes>
        {!token ? (
          <Route
            path="*"
            element={<Login onLogin={handleLogin} />}
          />
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
