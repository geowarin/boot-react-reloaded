import wretch from "wretch";

const api = wretch()
// @ts-ignore
  .url(process.env.BACKEND_URL)
  .options({credentials: "include"})
  .catcher(401, e => {
    history.pushState({message: e.message}, "", `/login?error`);
    return Promise.reject(e.message);
  });

export {api}
