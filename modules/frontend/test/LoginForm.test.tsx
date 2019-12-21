// again, these first two imports are something you'd normally handle in
// your testing framework configuration rather than importing them in every file.
import '@testing-library/jest-dom/extend-expect';
import 'jest-fetch-mock';

import * as React from 'react';
import {queryByAttribute, render} from '@testing-library/react';
import LoginForm from '../src/pages/LoginForm';
import {Matcher} from "@testing-library/dom/matches";
import userEvent from "@testing-library/user-event";

const waitForExpect = require("wait-for-expect");

function queryByName(container: HTMLElement, id: Matcher) {
  const element = queryByAttribute("name", container, id);
  if (element == null) {
    throw new Error(`Could not find element with name ${id}`)
  }
  return element;
}

function queryByType(container: HTMLElement, id: Matcher) {
  const element = queryByAttribute("type", container, id);
  if (element == null) {
    throw new Error(`Could not find element with type ${id}`)
  }
  return element;
}

test('allows the user to login successfully', async () => {

  global.fetch.mockResponseOnce("OK");
  const pushStateSpy = jest.spyOn(history, 'pushState');

  const {container} = render(<LoginForm/>);

  await userEvent.type(queryByName(container, /username/i), "chuck");
  await userEvent.type(queryByName(container, /password/i), "norris");

  userEvent.click(queryByType(container, /submit/i));

  await waitForExpect(() => {
    expect(pushStateSpy).toHaveBeenCalledWith(expect.anything(), expect.anything(), "/");
  });
});
