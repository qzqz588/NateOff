import React from "react";
import Header from "./Header";
import Footer from "./Footer";
import { Outlet } from "react-router-dom";
import "../../css/LayOut.css"; // 추가할 CSS 파일

const Layout = () => {
    return (
        <div className="layout">
            <Header />
            <main className="content">
                <Outlet /> {/* 자식 라우트 렌더링 */}
            </main>
            <Footer />
        </div>
    );
};

export default Layout;