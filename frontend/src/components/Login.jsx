import { useState } from "react";

export default function Login({ onLogin, onSwitchToRegister }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(false);
    setLoading(true);

    try {
      const res = await fetch("http://localhost:8081/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      if (!res.ok) throw new Error("Identifiants invalides");

      const data = await res.json();
      setSuccess(true);
      onLogin?.(data.token, email); // <-- ici on envoie token + email
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex flex-col md:flex-row">
      <div className="hidden md:flex flex-1 bg-gradient-to-br from-indigo-600 to-purple-700 items-center justify-center p-16">
        <div className="text-white max-w-lg">
          <h1 className="text-5xl font-extrabold mb-6">Hahn Internship</h1>
          <p className="text-lg leading-relaxed mb-6">
            Gérez vos projets, vos tâches et vos équipes dans une plateforme moderne et sécurisée.
          </p>
          <p className="italic text-indigo-200">Bienvenue sur votre espace de travail !</p>
        </div>
      </div>

      <div className="flex-1 flex items-center justify-center p-6 bg-gray-50">
        <div className="w-full max-w-md bg-white/90 backdrop-blur-xl rounded-3xl shadow-2xl p-10 border border-gray-200">
          <h2 className="text-3xl font-bold text-gray-900 mb-4">Connexion</h2>
          <p className="text-sm text-gray-500 mb-6">Entrez vos identifiants pour continuer</p>

          {error && (
            <div className="mb-4 rounded-lg bg-red-100 text-red-700 px-4 py-2 text-sm animate-pulse">
              {error}
            </div>
          )}

          {success && (
            <div className="mb-4 rounded-lg bg-green-100 text-green-700 px-4 py-2 text-sm animate-pulse">
              Connexion réussie !
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-gray-600 mb-1">Email</label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="email@exemple.com"
                required
                className="w-full px-5 py-3 rounded-xl border border-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-600 mb-1">Mot de passe</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                required
                className="w-full px-5 py-3 rounded-xl border border-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition"
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full py-3 rounded-xl bg-indigo-600 text-white font-semibold hover:bg-indigo-700 transition disabled:opacity-50"
            >
              {loading ? "Connexion..." : "Se connecter"}
            </button>
          </form>

          <div className="mt-6 text-center text-sm text-gray-500">
            Pas encore de compte ?{" "}
            <button
              onClick={onSwitchToRegister}
              className="text-indigo-600 font-semibold hover:underline"
            >
              Créer un compte
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
