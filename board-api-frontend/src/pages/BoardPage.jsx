import React, { useEffect, useState } from 'react';
import axios from 'axios';

const BoardPage = () => {
  const [posts, setPosts] = useState([]);
  const [message, setMessage] = useState('');
  const [selectedPost, setSelectedPost] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [viewMode, setViewMode] = useState('list'); // 'list' | 'write' | 'detail'
  const [newPost, setNewPost] = useState({ boardTitle: '', boardContent: '', insertId: '' });

  const fetchPosts = () => {
    axios.get('http://localhost:8080/api/boards')
      .then(res => {
        if (Array.isArray(res.data)) {
          setPosts(res.data);
          setMessage('');
        } else if (res.data.message) {
          setPosts([]);
          setMessage(res.data.message);
        }
      })
      .catch(err => {
        console.error('게시글 로딩 실패:', err);
        setMessage('게시글을 불러오는 데 실패했습니다.');
      });
  };

  const fetchPostById = (id) => {
    axios.get(`http://localhost:8080/api/boards/${id}`)
      .then(res => {
        setSelectedPost(res.data);
        setViewMode('detail');
      })
      .catch(err => {
        console.error('상세 조회 실패:', err);
        setMessage('해당 게시글을 불러올 수 없습니다.');
      });
  };

  useEffect(() => {
    fetchPosts();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewPost(prev => ({ ...prev, [name]: value }));
  };

  const handlePostSubmit = () => {
    const { boardTitle, boardContent, insertId } = newPost;
    if (!boardTitle.trim() || !boardContent.trim() || !insertId.trim()) return;

    axios.post('http://localhost:8080/api/boards', newPost)
      .then(() => {
        setNewPost({ boardTitle: '', boardContent: '', insertId: '' });
        fetchPosts();
        setViewMode('list');
      })
      .catch(err => console.error('게시글 등록 실패:', err));
  };

  const handleDelete = (id) => {
    if (!window.confirm('정말 삭제하시겠습니까?')) return;
    axios.delete(`http://localhost:8080/api/boards/${id}`)
      .then(() => {
        fetchPosts();
        setViewMode('list');
      })
      .catch(err => console.error('삭제 실패:', err));
  };

  const handleUpdate = () => {
    axios.put(`http://localhost:8080/api/boards/${selectedPost.boardId}`, selectedPost)
      .then(() => {
        setEditMode(false);
        fetchPosts();
        setViewMode('list');
      })
      .catch(err => console.error('수정 실패:', err));
  };

  return (
    <div className="p-6 max-w-4xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-800">📋 화학물질 게시판</h1>
        <button
          onClick={() => setViewMode(viewMode === 'list' ? 'write' : 'list')}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >{viewMode === 'list' ? '+ 새 글 작성' : '← 목록으로'}</button>
      </div>

      {viewMode === 'write' && (
        <div className="border p-4 mb-10 rounded shadow-sm bg-white">
          <h2 className="text-lg font-semibold mb-4">📝 새 글 작성</h2>
          <input
            type="text"
            name="boardTitle"
            placeholder="제목"
            value={newPost.boardTitle}
            onChange={handleInputChange}
            className="border p-2 w-full mb-2 rounded text-sm"
          />
          <textarea
            name="boardContent"
            placeholder="내용"
            value={newPost.boardContent}
            onChange={handleInputChange}
            className="border p-2 w-full mb-2 rounded text-sm"
            rows="4"
          />
          <input
            type="text"
            name="insertId"
            placeholder="작성자"
            value={newPost.insertId}
            onChange={handleInputChange}
            className="border p-2 w-full mb-4 rounded text-sm"
          />
          <button
            onClick={handlePostSubmit}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >등록</button>
        </div>
      )}

      {viewMode === 'detail' && selectedPost && (
        <div className="border p-4 rounded shadow-sm bg-white">
          {editMode ? (
            <>
              <input
                type="text"
                value={selectedPost.boardTitle}
                onChange={(e) => setSelectedPost(prev => ({ ...prev, boardTitle: e.target.value }))}
                className="border p-2 w-full mb-2 rounded text-sm"
              />
              <textarea
                value={selectedPost.boardContent}
                onChange={(e) => setSelectedPost(prev => ({ ...prev, boardContent: e.target.value }))}
                className="border p-2 w-full mb-2 rounded text-sm"
                rows="4"
              />
              <button onClick={handleUpdate} className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 mr-2">수정 완료</button>
            </>
          ) : (
            <>
              <h2 className="text-xl font-bold mb-2">{selectedPost.boardTitle}</h2>
              <div className="text-sm text-gray-500 mb-3">{selectedPost.insertDate} • {selectedPost.insertId}</div>
              <p className="text-gray-700 whitespace-pre-wrap mb-4">{selectedPost.boardContent}</p>
              <button onClick={() => setEditMode(true)} className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600 mr-2">수정</button>
              <button onClick={() => handleDelete(selectedPost.boardId)} className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700">삭제</button>
            </>
          )}
        </div>
      )}

      {viewMode === 'list' && (
        posts.length > 0 ? posts.map(post => (
          <div key={post.boardId} onClick={() => fetchPostById(post.boardId)} className="cursor-pointer border border-gray-200 rounded-md p-4 mb-6 shadow-sm bg-white">
            <div className="text-xl font-semibold text-gray-900 mb-1">{post.boardTitle}</div>
            <div className="text-sm text-gray-500 mb-3">{post.insertDate} • {post.insertId}</div>
            <p className="text-gray-700 mb-3 whitespace-pre-wrap">{post.boardContent}</p>
          </div>
        )) : (
          <p className="text-gray-500 text-sm">{message || '게시글이 없습니다.'}</p>
        )
      )}
    </div>
  );
};

export default BoardPage;
