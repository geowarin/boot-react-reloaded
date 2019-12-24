import * as React from 'react';
import LoginForm from '../src/pages/LoginForm';
import {render, userEvent, waitForExpect} from "./test-lib/test-utils";

describe("LoginForm", () => {

  test("user is redirected after a successful login", async () => {
    global.fetch.mockResponseOnce("OK");
    const pushStateSpy = jest.spyOn(window.history, 'pushState');

    const {getByName, getByRole} = render(<LoginForm/>);

    await userEvent.type(getByName(/username/i), "chuck");
    await userEvent.type(getByName(/password/i), "norris");

    userEvent.click(getByRole("button"));

    await waitForExpect(() => {
      expect(pushStateSpy).toHaveBeenCalledWith(expect.anything(), expect.anything(), "/");
    });
  });

  test("displays error message if login is unsucessful", async () => {
    global.fetch.mockResponseOnce("Unauthorized", {status: 401});
    const pushStateSpy = jest.spyOn(window.history, 'pushState');

    const {getByName, getByRole} = render(<LoginForm/>);

    await userEvent.type(getByName(/username/i), "chuck");
    await userEvent.type(getByName(/password/i), "norris");

    userEvent.click(getByRole("button"));

    await waitForExpect(() => {
      expect(pushStateSpy).toHaveBeenCalledWith(expect.anything(), expect.anything(), "/login?error");
    });
  });
});
