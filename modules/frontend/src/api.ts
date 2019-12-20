import wretch from "wretch";

const api = wretch()
  .url("http://localhost:8080/api")
  .options({credentials: "include"})
  .catcher(401, e => {
    console.log(e);
    history.pushState(null, "",`/login?error=${e.message}`)
  })
;

export {api}
