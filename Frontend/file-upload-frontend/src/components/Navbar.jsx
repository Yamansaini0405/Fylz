import { Upload, Search, User } from 'lucide-react';
import { useAuth } from './useAuth';
import { Link } from 'react-router-dom';

export default function Navbar() {
    const {logout} = useAuth();
  return (
    <nav className="bg-white shadow-sm border-b border-gray-200 px-6 py-4 rounded-b-3xl">
      <div className="flex items-center justify-between max-w-7xl mx-auto">
        
        <div className="flex items-center space-x-2">
          <div className="w-8 h-8 bg-purple-600 rounded-full flex items-center justify-center">
            <Upload className="w-4 h-4 text-white" />
          </div>
          <span className="text-lg font-semibold text-gray-800">File System</span>
        </div>

        
        <div className="flex items-center space-x-6">
          <button className="flex items-center space-x-2 text-gray-900 hover:text-purple-600 transition-colors font-semibold">
            <Search className="w-4 h-4" />
           <Link to="/search"> <span>Search File</span></Link>
          </button>
          
          <button className="flex items-center space-x-2 text-gray-900 hover:text-purple-600 transition-colors font-semibold">
            <Upload className="w-4 h-4" />
            <Link to="/"><span>Upload File</span></Link>
          </button>
          
          
           <button onClick={logout} className="p-2 text-gray-900 hover:text-purple-600 transition-colors font-semibold">Logout</button>
          
        </div>
      </div>
    </nav>
  );
}