import { createContext, useReducer } from 'react';
import combineReducers from 'react-combine-reducers';
import AuthReducer from './AuthReducer';
import ThreadReducer from './ThreadReducer';
import PostReducer from './PostReducer';

const initialAuth = {
  token: JSON.parse(localStorage.getItem('user'))?.token,
  user: JSON.parse(localStorage.getItem('user')),
};

const initialThreads = {
  data: [],
  singleThread: {}
}

const initialPosts = {
  data: []
}

const [combinedReducer, initialState] = combineReducers({
  auth: [AuthReducer, initialAuth],
  threads: [ThreadReducer, initialThreads],
  posts: [PostReducer, initialPosts]
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