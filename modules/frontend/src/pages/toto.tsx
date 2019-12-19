import * as React from "react";
import useSWR from "swr";
import wretch from "wretch";

const fetcher = (url: string) => wretch(url).get().json();

const Toto = () => {
  const { data, error } = useSWR('http://localhost:8080/api/toto', fetcher);

  if (error) return <div>failed to load</div>;
  if (!data) return <div>loading...</div>;
  return <div>{data.message}!</div>;
};

export default Toto;
