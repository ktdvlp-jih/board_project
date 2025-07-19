import React, { useEffect, useState } from 'react';
import { getBoards } from '../api/boardApi';

function BoardPage() {
  const [boards, setBoards] = useState([]);

  useEffect(() => {
    getBoards()
      .then((res) => setBoards(res.data))
      .catch((err) => console.error(err));
  }, []);

  return (
    <div>
      <h1>게시판 목록</h1>
      <ul>
        {boards.map((board) => (
          <li key={board.boardId}>{board.title}</li>
        ))}
      </ul>
    </div>
  );
}

export default BoardPage;
