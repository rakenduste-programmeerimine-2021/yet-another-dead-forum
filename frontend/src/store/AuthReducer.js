import {
  USER_CREATE,
  USER_LOGIN,
  USER_LOGOUT,
} from './actions';

const AuthReducer = (state, action) => {
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

export default AuthReducer