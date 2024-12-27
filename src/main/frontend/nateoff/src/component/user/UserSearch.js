import React, { useState, useEffect } from "react";
import axios from "axios";

const UserSearch = () => {
    const [query, setQuery] = useState(""); // 검색 입력값
    const [users, setUsers] = useState([]); // 검색 결과 유저 목록

    // 입력값 변경 시 호출
    const handleInputChange = (e) => {
        setQuery(e.target.value);
    };

    // 검색 API 호출
    useEffect(() => {
        if (query.trim() === "") {
            setUsers([]); // 입력값이 비었을 때 결과 초기화
            return;
        }

        const fetchUsers = async () => {
            try {
                const response = await axios.get(`/api/users/search`, {
                    params: { query },
                });
                setUsers(response.data); // 결과 업데이트
            } catch (error) {
                console.error("Error fetching users:", error);
            }
        };

        fetchUsers();
    }, [query]); // query가 변경될 때마다 호출

    return (
        <div>
            <h2>Search Users</h2>
            <input
                type="text"
                value={query}
                onChange={handleInputChange}
                placeholder="Search for users..."
            />
            {users.length > 0 && (
                <ul>
                    {users.map((user) => (
                        <li key={user.id}>
                            {user.username} ({user.email})
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default UserSearch;
