import * as React from "react";
import {apiRequest} from "../api";

const MainPage = () => {
  const state = apiRequest<{ message: string }>(api => api.url("/api/whatever").get().json());

  if (state.loading) return <div>loading...</div>;
  if (state.error) return <div>failed to load</div>;
  return <div>Message from server: {state.result?.message} !!</div>;
};

export default MainPage;
