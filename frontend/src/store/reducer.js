import { USER_CREATE, USER_LOGIN, USER_LOGOUT, THREADS_UPDATE, POSTS_UPDATE } from './actions';

const authReducer = (state, action) => {
  switch(action.type) {
    case USER_CREATE:
      return {
        ...state,
        token: action.payload.token,
        user: action.payload.user,
      }
    case USER_LOGIN:
      localStorage.setItem('user', JSON.stringify(action.payload));
      return {
        ...state,
        token: action.payload.token,
        user: action.payload
      }
    case USER_LOGOUT:
      localStorage.removeItem('user');
      return {
        ...state,
        token: null,
        user: null
      }
    default:
      return state
  }
};

const threadReducer = (state, action) => {
  switch(action.type) {
    case THREADS_UPDATE:
      return {
        ...state,
        data: action.payload
      }
    default:
      return state
  }
}

const postReducer = (state, action) => {
  switch(action.type) {
    case POSTS_UPDATE:
      return {
        ...state,
        data: action.payload
      }
    default:
      return state
  }
}

export { authReducer, threadReducer, postReducer };