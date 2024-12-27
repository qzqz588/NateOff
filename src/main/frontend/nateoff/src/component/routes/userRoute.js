import React from "react";
import { Routes, Route } from "react-router-dom";
import Login from "../../component/user/Login"
import Logout from "../../component/user/Logout";

import SignUp from "../user/SignUp";

const UserRoute = () => {
    return (
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/logout" element={<Logout />} />
                <Route path="/signup" element={<SignUp />} />
            </Routes>
    );
};

export default UserRoute;