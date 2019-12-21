import * as React from "react";
import {api} from "../api";
import {useAsync} from "react-async-hook";

const MainPage = () => {
  const state = useAsync<{ message: string }>(async () => {
    return api.url("/api/whatever").get().json();
  }, []);

  if (state.loading) return <div>loading...</div>;
  if (state.error) return <div>failed to load</div>;
  return <div>Message from server: {state.result?.message} !!</div>;
};

export default MainPage;
