import { Routes, Route, Link } from "react-router-dom";





import AuthSystem from "./components/AuthSystem";
import { useAuth } from "./components/useAuth";
import FileUpload from "./components/FileUplaod";
import FileSearch from "./components/FileSearch";
import ProtectedRoute from "./routes/ProtectedRoute";
import Navbar from "./components/Navbar";

export default function App() {
  const { token, role, logout } = useAuth();

  return (
    <>
      <nav>
        {token ? (
          <>
            <Routes>
              <Route path="/*" element={<Navbar />} />
            </Routes>
            {role === "ADMIN" && <Link to="/admin">Admin</Link>}
            
          </>
        ) : (
          <>
            {/* <Link to="/login">Login</Link>
            <Link to="/signup">Signup</Link> */}
          </>
        )}
      </nav>

      <Routes>
        <Route path="/login" element={<AuthSystem />} />
        <Route path="/signup" element={<AuthSystem />} />
        
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <FileUpload />
            </ProtectedRoute>
          }
        />
        <Route
          path="/search"
          element={
            <ProtectedRoute>
              <FileSearch/>
            </ProtectedRoute>
           
          }
        />
      </Routes>
    </>
  );
}
