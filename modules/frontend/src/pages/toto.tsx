import * as React from "react";
import useSWR from "swr";
import wretch from "wretch";

const api = wretch()
  .url("http://localhost:8080/api")
  .options({credentials: "include"});

const reAuthOn401 = api
  .catcher(401, async (error, request) => {
    const token = await api.url("/auth").post({
      userName: "admin",
      password: "admin"
    }).text();
    return request.replay().unauthorized(err => {
      throw err
    }).json()
  });

const fetcher = (url: string) => api.url(url)
  .get()
  .json();

const Toto = () => {
  const {data, error} = useSWR('/whatever', fetcher, {revalidateOnFocus: false, onErrorRetry: () => {}});

  if (error) return <div>failed to load</div>;
  if (!data) return <div>loading...</div>;
  return <div>{data.message}!</div>;
};

export default Toto;
