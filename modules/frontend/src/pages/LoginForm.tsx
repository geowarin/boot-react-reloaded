import * as React from 'react';
import {useForm} from "react-hook-form";
import * as yup from "yup";
import {api} from "../api";
import {useLocation} from "wouter";
import classNames from "classnames";

interface Props {
}

const validationSchema = yup.object({
  userName: yup.string().required(),
  password: yup.string().required()
});

type FormData = yup.InferType<typeof validationSchema>;

const LoginForm: React.FC<Props> = (props) => {
  const {register, handleSubmit, errors} = useForm<FormData>({validationSchema});
  const [location, setLocation] = useLocation();

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
        <input tabIndex={0} type="text" autoComplete="off" name="userName" ref={register}/>
        <div className="error">{errors.userName?.message}</div>

        <input type="password" autoComplete="off" name="password" ref={register}/>
        <div className="error">{errors.password?.message}</div>

        <div className="error">{globalError}</div>
        <input type="submit"/>
      </form>
    </div>
  );
};

export default LoginForm;
