import * as React from 'react';
import {useForm} from "react-hook-form";
import {api} from "../api";
import {useLocation} from "wouter";
import classNames from "classnames";

interface Props {
}

interface FormData {
  userName: string
  password: string
}

const LoginForm: React.FC<Props> = (props) => {
  const {register, handleSubmit, errors} = useForm<FormData>();
  const [_, setLocation] = useLocation();

  const onSubmit = handleSubmit(({userName, password}) => {
    api.url("/api/auth")
      .post({userName, password}).text()
      .then(() => setLocation("/"))
  });

  const hasFieldErrors = !!Object.keys(errors).length;
  const urlParams = new URLSearchParams(window.location.search);
  const hasGlobalError = urlParams.has("error");
  const globalError = !hasFieldErrors && hasGlobalError && "Invalid credentials";

  return (
    <div className="form-container">
      <h2>Please login</h2>

      <form onSubmit={onSubmit} className={classNames({"error": hasGlobalError})}>
        <input tabIndex={0} type="text" autoComplete="off" name="userName" ref={register({required: true})}/>
        <div className="error">{errors.userName && "Username is required"}</div>

        <input type="password" autoComplete="off" name="password" ref={register({required: true})}/>
        <div className="error">{errors.password && "Password is required"}</div>

        <div className="error">{globalError}</div>
        <button type="submit">Login</button>
      </form>
    </div>
  );
};

export default LoginForm;
