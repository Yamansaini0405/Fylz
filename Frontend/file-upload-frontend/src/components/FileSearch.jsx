import React, { useState, useEffect } from 'react';
import { Search, File, FileText, Image, Archive, Download, Calendar, Filter, Eye } from 'lucide-react';
import { useAuth } from './useAuth';

const FileSearch = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [isSearching, setIsSearching] = useState(false);
  const [filterType, setFilterType] = useState('all');
 const [mockFiles, setMockFiles] = useState([])
  const {token} = useAuth();


  const fetchFiles = async () => {
    try{
        const res = await fetch(`http://localhost:8080/api/files/my-files`,{
            headers: {
        Authorization: `Bearer ${token}`
      }
        });
        const data = await res.json();
        setMockFiles(data)
        console.log(data);
        
    } catch(err){
        console.error('Failed to fetch files', err);
    }
  };
  useEffect(() => {
    fetchFiles();
  }, []);

  const allFiles = async () => {
    try{
        const allRes = await fetch(`http://localhost:8080/api/files/all`,{
            headers: {
        Authorization: `Bearer ${token}`
      }
        });
        const all = await allRes.json();
        setSearchResults(all)
        console.log(all);
        
    } catch(err){
        console.error('Failed to fetch files', err);
    }
  };
  useEffect(() => {
    allFiles();
  }, []);

  const handleSearch = (query) => {
    setSearchQuery(query);
    setIsSearching(true);
    
    
    setTimeout(() => {
      if (query.trim() === '') {
        setSearchResults([]);
      } 
      else {
        let results = searchResults.filter(file =>
          file.fileName.toLowerCase().includes(query.toLowerCase()) ||
          file.uploadedBy.toLowerCase().includes(query.toLowerCase())
          
        );

        // Apply filter
        if (filterType !== 'all') {
          results = results.filter(file => {
            if (filterType === 'documents') return file.fileName.includes('pdf') || file.type.includes('doc') || file.type.includes('ppt');
            if (filterType === 'images') return file.fileName.includes('jpg')|| file.fileName.includes('png');
            if (filterType === 'archives') return file.fileName.includes('zip') || file.type.includes('rar');
            return true;
          });
        }
        
        setSearchResults(results);
      }
      setIsSearching(false);
    }, 300);
  };

  const handleDownload = async (fileName) => {
  try {
    // const encodedFileName = encodeURIComponent(fileName);
    const res = await fetch(`http://localhost:8080/api/files/download?key=${fileName}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (!res.ok) throw new Error('Network response was not ok');

    const blob = await res.blob();
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = fileName; // this will set the downloaded file name
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  } catch (err) {
    console.error('Failed to download file:', err);
  }
};




  const getFileIcon = (fileName) => {
    if (fileName.includes('jpg') ||fileName.includes('png')) return <Image className="w-5 h-5" />;
    if (fileName.includes('pdf') || fileName.includes('doc') || fileName.includes('ppt')) return <FileText className="w-5 h-5" />;
    if (fileName.includes('zip') || fileName.includes('rar')) return <Archive className="w-5 h-5" />;
    return <File className="w-5 h-5" />;
  };

  const getFileTypeColor = (fileName) => {
    if (fileName.includes('jpg') || fileName.includes('png') ) return 'text-green-600 bg-green-50';
    if (fileName.includes('pdf')) return 'text-red-600 bg-red-50';
    if (fileName.includes('doc')) return 'text-blue-600 bg-blue-50';
    if (fileName.includes('zip') || fileName.includes('rar')) return 'text-orange-600 bg-orange-50';
    return 'text-purple-600 bg-purple-50';
  };

  const displayFiles = searchQuery ? searchResults : mockFiles.slice(0, 6);

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-50 to-indigo-100 flex items-center justify-center p-4">
      <div className="bg-white rounded-xl shadow-lg p-8 w-full max-w-4xl">
        
        <div className="text-center mb-6">
          <div className="w-16 h-16 bg-purple-600 rounded-full flex items-center justify-center mx-auto mb-4">
            <Search className="w-8 h-8 text-white" />
          </div>
          <h2 className="text-2xl font-bold text-gray-800 mb-2">Search Files</h2>
          <p className="text-gray-600">Find your files quickly and efficiently</p>
        </div>

        
        <div className="relative mb-6">
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <Search className="w-5 h-5 text-gray-400" />
          </div>
          <input
            type="text"
            placeholder="Search any files on cloud..."
            value={searchQuery}
            onChange={(e) => handleSearch(e.target.value)}
            className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent outline-none transition-all text-gray-700"
          />
        </div>

        
        <div className="flex items-center space-x-2 mb-6">
          <Filter className="w-4 h-4 text-gray-600" />
          <span className="text-sm font-medium text-gray-600">Filter:</span>
          <div className="flex space-x-2">
            {[
              { key: 'all', label: 'All Files' },
              { key: 'documents', label: 'Documents' },
              { key: 'images', label: 'Images' },
              { key: 'archives', label: 'Archives' }
            ].map(filter => (
              <button
                key={filter.key}
                onClick={() => {
                  setFilterType(filter.key);
                  if (searchQuery) handleSearch(searchQuery);
                }}
                className={`px-3 py-1 text-sm rounded-full transition-colors ${
                  filterType === filter.key
                    ? 'bg-purple-600 text-white'
                    : 'bg-gray-100 text-gray-600 hover:bg-purple-100'
                }`}
              >
                {filter.label}
              </button>
            ))}
          </div>
        </div>

        
        {isSearching && (
          <div className="text-center py-12">
            <div className="animate-spin w-8 h-8 border-2 border-purple-600 border-t-transparent rounded-full mx-auto mb-4"></div>
            <p className="text-gray-600">Searching files...</p>
          </div>
        )}

        
        {!isSearching && searchQuery && searchResults.length === 0 && (
          <div className="text-center py-12">
            <File className="w-16 h-16 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-800 mb-2">No files found</h3>
            <p className="text-gray-600">Try adjusting your search terms or filters.</p>
          </div>
        )}

        
        {!isSearching && displayFiles.length > 0 && (
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-lg font-semibold text-gray-800">
              {searchQuery ? `Search Results (${searchResults.length})` : 'Your Recent Files'}
            </h3>
            <span className="text-sm text-gray-500">
              {displayFiles.length} file{displayFiles.length !== 1 ? 's' : ''}
            </span>
          </div>
        )}

        
        {!isSearching && displayFiles.length > 0 && (
          <div className="space-y-3">
            {displayFiles.map((file) => (
              <div
                key={file.id}
                className="flex items-center justify-between p-4 bg-gray-50 rounded-lg border hover:bg-purple-50 hover:border-purple-200 transition-all duration-200 hover:shadow-md"
              >
                <div className="flex items-center space-x-4 flex-1">
                  <div className={`p-2 rounded-lg ${getFileTypeColor(file.fileName)}`}>
                    {getFileIcon(file.fileName)}
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="font-medium text-gray-800 truncate">{file.fileName}</p>
                    <div className="flex items-center space-x-4 text-sm text-gray-500 mt-1">
                      
                      <span className="flex items-center">
                        <Calendar className="w-3 h-3 mr-1" />
                        {file.uploadTime}
                      </span>
                      <span>by {file.uploadedBy}</span>
                    </div>
                  </div>
                </div>
                <div className="flex items-center space-x-2">
                  <button className="text-purple-600 hover:text-purple-800 p-2 hover:bg-purple-100 rounded-lg transition-colors">
                    <Eye className="w-4 h-4" />
                  </button>
                  <button className="text-purple-600 hover:text-purple-800 p-2 hover:bg-purple-100 rounded-lg transition-colors"
                  onClick={() => handleDownload(file.fileUrl)}>
                    <Download className="w-4 h-4" />
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        
        <div className="mt-8 text-center">
          <button className="text-purple-600 hover:text-purple-800 font-medium text-sm hover:underline">
            Advanced Search Options
          </button>
        </div>

        
        <div className="mt-8 pt-6 border-t border-gray-200 text-center text-sm text-gray-500">
          <p>Total files in system: 1,247 • Last updated: 2 minutes ago</p>
        </div>
      </div>
    </div>
  );
};

export default FileSearch;