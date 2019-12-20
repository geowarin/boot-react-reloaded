import * as React from "react";
import {api} from "../api";
import {useAsync} from "react-use";

const MainPage = () => {
  const state = useAsync<{ message: string }>(async () => api.url("/api/whatever").get().json());

  if (state.loading) return <div>loading...</div>;
  if (state.error) return <div>failed to load</div>;
  return <div>Message from server: {state.value?.message} !!</div>;
};

export default MainPage;
