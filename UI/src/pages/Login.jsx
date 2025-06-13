import axios from 'axios';
import {ROUTES} from '../constants/api';
import {useState} from "react";

export default function Login() {
    return (<div className="min-h-screen flex items-center justify-center">
        <LoginBox/>
    </div>);
}

export function LoginBox() {

    const [errMessage, setErrMessage] = useState({shown:false, message:''});

    const [credentials, setCredentials] = useState({
        name: "", email: "", username: "", password: "",
    });

    const [loggingIn, setLoggingIn] = useState(true)

    const login = (e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append("username", credentials.username);
        formData.append("password", credentials.password);

        axios.post(ROUTES.LOGIN, formData, {headers: {"Content-Type": "multipart/form-data"}})
            .then(res => {
                sessionStorage.setItem("JWTToken", res.data.token);
                window.location.href = "http://localhost:5173";
            }).catch(err => {
            setErrMessage({shown: true, message: "Invalid Credentials"});
        })
    }

    const onLogInCredentials = (e) => {
        const {name, value} = e.target;
        setCredentials({...credentials, [name]: value});
    }

    const signup = (e) => {
        e.preventDefault();

        if (credentials.username==="" || credentials.password==="" || credentials.name === "" ) {
            setErrMessage({shown: true, message:'Name, Username, Password must be filled'});
            return;
        }

        axios.post(ROUTES.REGISTER, credentials)
            .then(res => {
                setLoggingIn(true);
                console.log("Signed Up Successfully");
            }).catch(err => {
            console.log("Failed signup");
        })
    }

    return (<form onSubmit={loggingIn ? login : signup}>
        <div
            className={`bg-[#9DB2BF] shadow-lg w-md hover:scale-105 transition-all delay-300 duration-500 ease-in-out grid ${loggingIn ? `grid-rows-[1fr${errMessage.shown&&'_1fr'}_3fr_3fr_1fr]` : `grid-rows-[1fr${errMessage.shown&&'_1fr'}_3fr_3fr_3fr_3fr_1fr]`} gap-2 p-4 items-center justify-items-center rounded-md`}>
            <div><h2 className="text-3xl">Hear</h2></div>
            {errMessage.shown && <p className="text-[#B04A4A] justify-self-start">{errMessage.message}</p>}
            {!loggingIn && <>
                <div className="flex flex-col w-full">
                    <div className="grid grid-cols-[1fr_100px]">
                        <label className="mb-2">Name</label>
                        <a onClick={() => {
                            setLoggingIn(prevState => !prevState);
                            setErrMessage({shown: false, message: ''})
                        }}
                           className="justify-self-end underline hover:text-[#27374D]">Log In ?</a>
                    </div>
                    <input name="name" className="bg-[#DDE6ED] rounded-md text-[#27374D] h-16 p-2 shadow-md"
                           value={credentials.name} onChange={onLogInCredentials} type="text"/>
                </div>
                <div className="flex flex-col w-full">
                    <label className="mb-2">Email</label>
                    <input name="email" className="bg-[#DDE6ED] rounded-md text-[#27374D] h-16 p-2 shadow-md"
                           value={credentials.email} onChange={onLogInCredentials} type="text"/>
                </div>
            </>}
            <div className="flex flex-col w-full">
                <div className="grid grid-cols-[1fr_100px]">
                    <label className="mb-2">Username</label>
                    {loggingIn && <a onClick={() => {
                        setLoggingIn(prevState => !prevState);
                        setCredentials({name: "", email: "", username: "", password: "",})
                        setErrMessage({shown: false, message: ''})
                        }
                    }
                                     className="justify-self-end underline hover:text-[#27374D]">Sign Up
                        ?</a>}
                </div>
                <input name="username" className="bg-[#DDE6ED] rounded-md text-[#27374D] h-16 p-2 shadow-md"
                       value={credentials.username} onChange={onLogInCredentials} type="text"/>
            </div>
            <div className="flex flex-col w-full">
                <label className="mb-2">Password</label>
                <input name="password" className="bg-[#DDE6ED] rounded-md text-[#27374D] h-16 p-2 shadow-md"
                       value={credentials.password} onChange={onLogInCredentials} type="password"/>
            </div>
            <button type="submit"
                    className="bg-[#526D82]/75 rounded-md h-8 w-sm shadow-md hover:bg-[#27374D]">{loggingIn ? "Log In" : "Sign Up"}
            </button>
        </div>
    </form>);
}

