import wretch from "wretch";

const api = wretch()
  .url("http://localhost:8080")
  .options({credentials: "include"})
  .catcher(401, e => {
    history.pushState({message: e.message}, "", `/login?error`);
    return Promise.reject(e.message);
  });

export {api}
