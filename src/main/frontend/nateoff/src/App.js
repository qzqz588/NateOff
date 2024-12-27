import React from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Main from "./Main"
import Layout from "./component/common/Layout";
import Login from "./component/user/Login";
import SignUp from "./component/user/SignUp";


function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Layout />}>
                    <Route path="main" element={<Main />} />
                    <Route path="login" element={<Login />} />
                    <Route path="signup" element={<SignUp />} />
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
