import React, { useState } from "react";
import axios from "axios";
import "../../css/SignUpStyle.css"

const SignUp = () => {
    const [formData, setFormData] = useState({
        username: "",
        password: "",
        email: "",
        phone: ""
    });

    const [message, setMessage] = useState("");

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8090/api/users/signup", formData);
            if (response.data) {
                console.log("response.data", response.data);
                setMessage(response.data); // 서버 응답 메시지 설정
            } else {
                setMessage("회원가입 성공, 그러나 응답 데이터가 없습니다.");
            }
        } catch (error) {
            setMessage("회원가입 실패: " + (error.response?.data?.message || "알 수 없는 오류"));
        }
    };

    return (
        <div>
            <h2>회원가입</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>아이디:</label>
                    <input
                        type="id"
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
                    <label>이름:</label>
                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>전화번호:</label>
                    <input
                        type="text"
                        name="phone"
                        value={formData.phone}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>이메일:</label>
                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                </div>
                <button type="submit">회원가입</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default SignUp;