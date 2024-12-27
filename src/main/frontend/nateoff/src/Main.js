import React, { useState, useEffect } from "react";
import "./css/main.css";
import UserSearch from "./component/user/UserSearch";

const Main = () => {
    const [userInfo, setUserInfo] = useState({
        name: "",
        email: "",
    });
    const [friends, setFriends] = useState([]);
    const [chatRooms, setChatRooms] = useState([]);

    return (
        <div className="main-container">
            {/* 사용자 정보 */}
            <div className="user-info">
                <h3>내 정보</h3>
                <p>이름: {userInfo.name || ""}</p>
                <p>이메일: {userInfo.email|| ""}</p>
            </div>

            {/* 친구 목록과 채팅방 */}
            <div className="content-container">
                <div className="friends-list">
                    <h3>친구 목록</h3>
                    <UserSearch />
                    <ul>
                        {friends.map((friend, index) => (
                            <li key={index}>{friend}</li>
                        ))}
                    </ul>
                </div>

                <div className="chat-rooms">
                    <h3>채팅방</h3>
                    <ul>
                        {chatRooms.map((room, index) => (
                            <li key={index}>{room}</li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default Main;
