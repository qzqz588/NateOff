import React, { useState } from "react";
import axios from "axios";

const Login = () => {
    const [formData, setFormData] = useState({
        username: "",
        password: "",
        rememberMe: false,
    });
    const [message, setMessage] = useState("");

    // 일반 로그인 요청 처리
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8090/api/auth/login", formData, {
                headers: {
                    "Content-Type": "application/json",
                },
                withCredentials: true, // 쿠키 전송을 허용
            });
            setMessage("로그인 성공!");
            console.log("JWT Token:", response.data);
            window.location.replace('/')
        } catch (error) {
            setMessage("로그인 실패: " + (error.response?.data || "알 수 없는 오류"));
            console.error("Error:", error);
        }
    };

    // 소셜 로그인 처리 (Google, Kakao, Naver)
    const handleSocialLogin = (provider) => {

        const popup = window.open(
            `http://localhost:8090/oauth2/authorization/${provider}`,
            'OAuth2 Login',
            'width=500,height=600'
        );

        const handleMessage = (event) => {

            if (event.data.type === 'LOGIN_SUCCESS') {
                window.removeEventListener('message', handleMessage);

                if (window.opener) {
                    window.opener.location.href = '/';
                    window.close();
                } else {
                    window.location.replace('/main');
                }
            }
        };

        window.addEventListener('message', handleMessage);
    };

    // 입력 필드 변경 처리
    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData({
            ...formData,
            [name]: type === "checkbox" ? checked : value,
        });
    };

    return (
        <div>
            <h1>로그인</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>아이디:</label>
                    <input
                        type="text"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>비밀번호:</label>
                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>
                        <input
                            type="checkbox"
                            name="rememberMe"
                            checked={formData.rememberMe}
                            onChange={handleChange}
                        />
                        로그인 상태 유지
                    </label>
                </div>
                <button type="submit">로그인</button>
            </form>

            <p>{message}</p>

            <h2>소셜 로그인</h2>
            <button onClick={() => handleSocialLogin("google")}>Google로 로그인</button>
            <button onClick={() => handleSocialLogin("kakao")}>Kakao로 로그인</button>
            <button onClick={() => handleSocialLogin("naver")}>Naver로 로그인</button>
        </div>
    );
};

export default Login;
