import React from "react";
import "../../css/FooterStyle.css";

const Footer = () => {
    return (
        <footer className="footer">
            <div className="footer-content">
                <p>© 2024 MyApp. All rights reserved.</p>
                <nav>
                    <a href="/terms">이용약관</a>
                    <a href="/privacy">개인정보 처리방침</a>
                    <a href="/contact">문의하기</a>
                </nav>
            </div>
        </footer>
    );
};

export default Footer;
