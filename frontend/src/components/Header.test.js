import { useReducer } from 'react';
import { render, screen } from '@testing-library/react';
import { Context } from '../store'
import { authReducer } from '../store/reducer';
import Header from './Header';

describe('<Header>', () => {
  const initialAuth = {
    auth: {
      'token': 'token',
      'user': 'Some User',
    },
  }

  const dispatch = jest.fn()

  render(<Context.Provider value={[initialAuth, dispatch]}><Header /></Context.Provider>)

  it('renders when user logged out', () => {
    given('props', () => ({
      token: null,
      user: null,
    }))

    const sitename = screen.getAllByText('Yet Another Dead Forum')
    expect(sitename).toBeInTheDocument()
  })
});