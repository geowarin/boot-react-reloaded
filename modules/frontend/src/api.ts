import wretch, {Wretcher} from "wretch";
import {useAsync} from "react-async-hook";

const baseUrl = wretch()
  .url(process.env.BACKEND_URL ?? "");

const api = baseUrl
  .options({credentials: "include"})
  .catcher(401, e => {
    history.pushState({message: e.message}, "", `/login?error`);
    return Promise.reject(e.message);
  });

function apiRequest<T>(p: (api: Wretcher) => Promise<T>) {
  return useAsync<T>(async () => {
    return p(api);
  }, []);
}

export {api, apiRequest}
