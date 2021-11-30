import { createContext, useReducer } from 'react';
import combineReducers from 'react-combine-reducers';
import { authReducer } from './reducer';

const initialAuth = {
  token: localStorage.getItem('token'),
  user: null,
};

const [combinedReducer, initialState] = combineReducers({
  auth: [authReducer, initialAuth]
});

export const Context = createContext(initialState);

const Store = ({ children }) => {
  const [state, dispatch] = useReducer(combinedReducer, initialState);

  return (
    <Context.Provider value={[ state, dispatch ]}>
      { children }
    </Context.Provider>
  );
};

export default Store;