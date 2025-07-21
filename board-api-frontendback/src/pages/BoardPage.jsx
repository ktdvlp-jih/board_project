import React, { useState } from 'react';

const BoardPage = () => {
  const [posts, setPosts] = useState([
    {
      id: 1,
      title: 'Acme_Corp_MSDS_v4.pdf',
      date: '2024-04-22',
      author: 'John Doe',
      comments: ['감사합니다.', '확인했습니다.'],
      attachment: true
    },
    {
      id: 2,
      title: 'Hazardous incident and corrective actions taken',
      date: '2024-04-19',
      author: 'Jane Smith',
      comments: [],
      attachment: false
    }
  ]);

  const [newComment, setNewComment] = useState('');
  const [activePostId, setActivePostId] = useState(null);

  const handleCommentSubmit = (postId) => {
    if (!newComment.trim()) return;
    setPosts(posts.map(post => {
      if (post.id === postId) {
        return {
          ...post,
          comments: [...post.comments, newComment]
        };
      }
      return post;
    }));
    setNewComment('');
  };

  return (
    <div className="p-6 max-w-3xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Chemical Materials Board</h1>
        <button className="bg-black text-white px-4 py-2 rounded">+ Add New</button>
      </div>
      <div className="flex space-x-4 mb-4 border-b">
        <button className="pb-2 border-b-2 border-transparent hover:border-black">Announcements</button>
        <button className="pb-2 border-b-2 border-transparent hover:border-black">Regulations</button>
        <button className="pb-2 border-b-2 border-black font-bold">MSDSs</button>
      </div>

      {posts.map(post => (
        <div key={post.id} className="border p-4 mb-4 rounded shadow-sm bg-white">
          <div className="text-lg font-semibold mb-1">{post.title}</div>
          <div className="text-sm text-gray-500 mb-2">{post.date}  {post.author}</div>
          {post.attachment && (
            <div className="text-sm text-blue-600">📎 첨부파일 있음</div>
          )}

          <div className="mt-4">
            <button
              className="text-sm text-blue-500"
              onClick={() => setActivePostId(post.id === activePostId ? null : post.id)}
            >
              💬 댓글 {post.comments.length}개 보기
            </button>
            {activePostId === post.id && (
              <div className="mt-2">
                <ul className="space-y-1 text-sm text-gray-700">
                  {post.comments.map((cmt, idx) => (
                    <li key={idx}>- {cmt}</li>
                  ))}
                </ul>
                <div className="flex items-center mt-2 space-x-2">
                  <input
                    type="text"
                    placeholder="댓글 입력"
                    value={newComment}
                    onChange={e => setNewComment(e.target.value)}
                    className="border p-1 flex-1 rounded text-sm"
                  />
                  <button
                    onClick={() => handleCommentSubmit(post.id)}
                    className="text-sm px-3 py-1 bg-blue-500 text-white rounded"
                  >등록</button>
                </div>
              </div>
            )}
          </div>
        </div>
      ))}
    </div>
  );
};

export default BoardPage;
