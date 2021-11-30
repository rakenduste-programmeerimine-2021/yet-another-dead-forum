import { USER_CREATE, USER_LOGIN, USER_LOGOUT } from './actions';

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

export { authReducer };