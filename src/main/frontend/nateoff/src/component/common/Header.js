import React, {useEffect, useState} from "react";
import "../../css/HeaderStyle.css";
import axios from "axios";

const Header = () => {
    const [isLoggedIn,setIsLoggedIn] = useState(false);

    const checkLoginStatus = async () => {
        try {
            const response = await fetch("http://localhost:8090/api/auth/status", {
                credentials: "include", // 쿠키 포함 요청
            });
            console.log("Response Status:", response.status); // 상태 코드 출력
            const text = await response.text(); // 텍스트로 응답 확인
            console.log(text);
            try {
                // JSON 파싱 시도
                const data = JSON.parse(text);
                console.log("Response Data:", data);

                // loggedIn 상태 업데이트
                setIsLoggedIn(data.loggedIn || false);
            } catch (e) {
                // JSON 파싱 실패 시 HTML 처리
                console.error("Response is not valid JSON. Received:", text);
                setIsLoggedIn(false);
            }
        } catch (error) {
            console.error("Error checking login status:", error);
            setIsLoggedIn(false); // 기본 상태 설정
        }
    };

    useEffect(() => {
        checkLoginStatus();
    }, []);

    const handleLogout = async () => {
        try {
            const response = await axios.post('http://localhost:8090/api/auth/logout', null, {
                withCredentials: true,
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log("response: " + response);
            if (response.data.success) {
                setIsLoggedIn(false);
                window.location.href = '/login';
            } else {
                alert('로그아웃 처리에 실패했습니다.');
            }
        } catch (error) {
            console.error('로그아웃 실패:', error);
            alert('로그아웃 처리 중 오류가 발생했습니다.');
        }
    };

    return (
        <header className="header">
            <div className="header-logo">
                <img className="logo" src="/images/logo.png" alt="Example"/>
                <a href="/main">NateOff</a>
            </div>
            <nav className="header-nav">
            {isLoggedIn ? (<a href="/logout" onClick= {handleLogout}>로그아웃</a>) :  <a href="/login">로그인</a>}
                <a href="/signup">회원가입</a>
                <a href="/myinfo">내 정보</a>
            </nav>
        </header>
    );
};

export default Header;
