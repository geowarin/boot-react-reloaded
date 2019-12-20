import * as React from 'react';
import {useForm} from "react-hook-form";
import * as yup from "yup";
import {api} from "../api";
import {useLocation} from "wouter";

interface Props {
}

const validationSchema = yup.object({
  userName: yup.string().required(),
  password: yup.string().required()
});

type FormData = yup.InferType<typeof validationSchema>;

const LoginForm: React.FC<Props> = (props) => {
  const {register, handleSubmit, errors} = useForm<FormData>({
    validationSchema
  });
  const [location, setLocation] = useLocation();
  const onSubmit = handleSubmit(({userName, password}) => {
    api.url("/auth")
      .post({userName, password}).text()
      .then(() => setLocation("/"))
  });

  return (
    <form onSubmit={onSubmit}>
      <input name="userName" ref={register}/>
      {errors.userName?.message}
      <input name="password" ref={register}/>
      {errors.password?.message}
      <input type="submit"/>
    </form>
  );
};

export default LoginForm;
