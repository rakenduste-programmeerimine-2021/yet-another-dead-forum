import { USER_CREATE, USER_LOGIN, USER_LOGOUT, THREADS_UPDATE } from './actions';

const authReducer = (state, action) => {
  switch(action.type) {
    case USER_CREATE:
      return {
        ...state,
        token: action.payload.token,
        user: action.payload.user,
      }
    case USER_LOGIN:
      localStorage.setItem('token', action.payload.token);
      localStorage.setItem('user', action.payload.user);
      return {
        ...state,
        token: action.payload.token,
        user: action.payload.user
      }
    case USER_LOGOUT:
      localStorage.removeItem('token');
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

export { authReducer, threadReducer };